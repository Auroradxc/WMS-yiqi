<template>
    <el-dialog v-model="dialogVisible" title="打印出库单" width="60%">
    <div class="app-container">
      <h1 align="center">打印预览</h1>
      <div id="printForm">
        <el-form :ref="formRef" :model="form" :rules="validateRules">
          <div style="padding: 20px;" align="left">
            <span style="margin: 50px 10px">客户：{{ tableHeader.Customer }}</span>
            <span style="margin: 50px 10px">订单号：{{ tableHeader.ListNum }}</span>
            <span style="margin: 50px 10px">出库类型：{{ tableHeader.ExstoreType }}</span>
            <span style="margin: 50px 10px">日期：{{ tableHeader.Date }}</span>
          </div>
          <div align="center">
            <el-table :data="tableData" style="width: 95%" border>
              <el-table-column prop="SupplementID" label="物料ID" />
              <el-table-column prop="SupplementName" label="物料名" />
              <el-table-column prop="WrapSize" label="包装容量" />
              <el-table-column prop="EnstoreNum" label="数量" />
              <el-table-column prop="Boxes" label="箱数" />
              <el-table-column prop="UnloadPlace" label="卸货地" />
            </el-table>
          </div>
        </el-form>
      </div>
      <div align="center" style="margin-top: 50px">
        <el-button type="info" @click="handlePrint(printData)">打印</el-button>
      </div>
    </div>
    </el-dialog>
  </template>
  
  <script>
  import printJS from 'print-js';
  import { get_print_detail_exstore, get_supplement_via_id_exstore, get_customer_list } from '../../../request/request.js';
  
  export default {
    props: {
      dialogVisible1: {
        type: Boolean,
        required: true,
      },
      listnum: {
        type: String,
        required: true,
      },
    },
    data() {
      return {
        dialogVisible:this.dialogVisible1,
        tableData: [],
        tableHeader: {
          Customer: null,
          ListNum: this.listnum,
          ExstoreType: "",
          Date: null,
        },
        formRef: 'form',
        validateRules: [],
        printData: {
          printable: 'printForm',
          header: 'application',
          ignore: ['no-print'],
        },
      };
    },
    watch: {
      dialogVisible1(newVal) {
        this.dialogVisible = newVal;
        if (newVal) {
          this.get_full_print_detail();
        }
      },
      listnum() {
        if (this.dialogVisible) {
          this.get_full_print_detail();
        }
      },
    },
    // mounted() {
    // this.tableHeader.ListNum =this.listnum;

    // this.get_full_print_detail();
    // },
  
    methods: {
      handlePrint(params) {
        printJS({
          printable: params.printable,
          type: params.type || 'html',
          header: params.header,
          targetStyles: ['*'],
          style: '@page {margin: 0 10mm};',
          ignoreElements: params.ignore || [],
          properties: params.properties || null,
        });
      },

      async get_full_print_detail() {
        this.tableHeader.ListNum = this.listnum;
        let info = { listNum: this.tableHeader.ListNum };
        console.log(info);
        try {
          let r_info = await get_print_detail_exstore(info);
          let sr_info = await get_customer_list();
          const statusMapping = {
            "OPEN": "未发货",
            "CLOSED": "已发货",
            "PENDING": "部分发货",
            "DISCARDED": "作废",
          };
          this.tableHeader.ExstoreType = statusMapping[r_info.listStatus];
          let CurrentCustomer = sr_info.find((Customer) => Customer.customerName == r_info.customer);
          if (!CurrentCustomer) {
            console.error(`customer not found for customer name: ${r_info.customer}`);
            alert("客户信息未找到！");
            return;
          }
  
          this.printData.header = "出库单--" + r_info.customer + " [" + CurrentCustomer.customerId + "]";
          this.tableHeader.Customer = r_info.customer + " [" + CurrentCustomer.customerId + "]";
          this.tableHeader.Date = new Date().toLocaleDateString();
  
          this.tableData = r_info.wrapInfoDetails.map(detail => ({
            SupplementID: detail.supId,
            SupplementName: null,
            WrapSize: detail.wrapSize,
            EnstoreNum: detail.expInNum,
            Boxes: detail.boxes,
            UnloadPlace: detail.unloadPlace,
          }));
  
          for (let supplement of this.tableData) {
            let s_message = { supId: supplement.SupplementID };
            let s_info = await get_supplement_via_id_exstore(s_message);
            if (s_info.length === 1) {
              supplement.SupplementName = s_info[0].supName;
            } else {
              alert("物料信息请求失败！");
            }
          }
        } catch (error) {
          console.error("Error fetching print details: ", error);
        }
      },
    },
  };
  </script>
  