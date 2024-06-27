<template>
    <el-dialog v-model="dialogVisible" title="打印入库单" width="60%">
      <div class="app-container">
        <h1 align="center"> 打印预览 </h1>
        <div id="printForm">
          <el-form :ref="formRef" :model="form" :rules="validateRules">
            <div style="padding: 20px;" align="left">
              <span style="margin: 50px 20px">供应商：{{ tableHeader.Supplier }}</span>
              <span style="margin: 50px 20px">订单号：{{ tableHeader.ListNum }}</span>
              <span style="margin: 50px 20px">日期：{{ tableHeader.Date }}</span>
            </div>
            <div align="center">
              <el-table :data="tableData" style="width: 95%" border>
                <el-table-column prop="SupplementID" label="物料ID" />
                <el-table-column prop="SupplementName" label="物料名" />
                <el-table-column prop="WrapSize" label="包装容量" />
                <el-table-column prop="EnstoreNum" label="数量" />
                <el-table-column prop="Boxes" label="箱数" />
              </el-table>
            </div>
          </el-form>
        </div>
        <div align="center" style="margin-top: 50px">
          <el-button type="info" @click="handlePrint(printData)"> 打印 </el-button>
        </div>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import printJS from 'print-js';
  import { get_print_detail, get_supplement_via_id, get_supplier_list } from '../../../request/request.js';
  
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
        dialogVisible: this.dialogVisible1,
        tableData: [],
        tableHeader: {
          Supplier: null,
          ListNum: this.listnum,
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
        this.tableData = [];
        this.tableHeader.ListNum = this.listnum;
        let info = {
          listNum: this.tableHeader.ListNum,
        };
  
        let r_info = await get_print_detail(info);
        let sr_info = await get_supplier_list();
        let CurrentSupplier = sr_info.find((Supplier) => Supplier.supplierName == r_info.supplier);
  
        if (!CurrentSupplier) {
          console.error(`Supplier not found for supplier name: ${r_info.supplier}`);
          alert("供应商信息未找到！");
          return;
        }
  
        this.printData.header = "入库单--" + r_info.supplier + " [" + CurrentSupplier.supplierId + "]";
        this.tableHeader.Supplier = r_info.supplier + " [" + CurrentSupplier.supplierId + "]";
        const Day = new Date();
        this.tableHeader.Date = Day.toLocaleDateString();
  
        this.tableData = [];
        for (let Supplement in r_info.wrapInfoDetails) {
          let InsertedSupplement = {
            SupplementName: null,
            SupplementID: null,
            WrapSize: null,
            EnstoreNum: null,
            Boxes: null,
          };
          InsertedSupplement.SupplementID = r_info.wrapInfoDetails[Supplement].supId;
          InsertedSupplement.WrapSize = r_info.wrapInfoDetails[Supplement].wrapSize;
          InsertedSupplement.EnstoreNum = r_info.wrapInfoDetails[Supplement].expInNum;
          InsertedSupplement.Boxes = r_info.wrapInfoDetails[Supplement].boxes;
  
  
          let s_message = {
            supId: r_info.wrapInfoDetails[Supplement].supId,
          };
  
          let s_info = await get_supplement_via_id(s_message);
          if (s_info.length > 1) {
            alert("物料信息请求失败！");
          } else {
            InsertedSupplement.SupplementName = s_info[0].supName;
          }
          this.tableData.push(InsertedSupplement);
        }
      },
    },
  };
  </script>
  