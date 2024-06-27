<template>
    <el-dialog v-model="dialogVisible" title="添加零件" width="60%">
      <el-form label-position="top" label-width="100px" :model="form">
        <el-form-item label="零件编号">
          <el-input v-model="form.supId" placeholder="请输入零件编号"></el-input>
        </el-form-item>
        <el-form-item label="零件名称">
          <el-input v-model="form.supName" placeholder="请输入零件名称"></el-input>
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="form.supplier" placeholder="请选择供应商">
            <el-option
              v-for="item in SupplierList"
              :key="item.supplierId"
              :label="item.supplierName"
              :value="item.supplierName"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确认</el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import axios from 'axios';
  export default {
    name: 'Add',
    data() {
      return {
        dialogVisible: true,
        form: {
          supId: '',
          supName: '',
          supplier: '',
        },
        SupplierList: [],
      };
    },
    methods: {
      async fetchSupplierList() {
        try {
          const response = await axios.post('http://97.64.20.181:8000/get_supplier_list', {
            // 如果请求需要携带数据，请在这里添加
          });
          if (response.data && Array.isArray(response.data)) {
            this.SupplierList = response.data;
          } else {
            console.error('Unexpected response data:', response.data);
          }
        } catch (error) {
          console.error('Error fetching supplier list:', error);
        }
      },
      async handleConfirm() {
        try {
          const response = await axios.post('http://97.64.20.181:8000/add_supplement', {
            "supId":this.form.supId,
            "supName":this.form.supName,
            "supplier":this.form.supplier
          });
          if (response.data && Array.isArray(response.data)) {
            this.SupplierList = response.data;
          } else {
            console.error('Unexpected response data:', response.data);
          }
        } catch (error) {
          console.error('Error fetching supplier list:', error);
        }
        this.dialogVisible = false;
        location.reload();

      },
    },
    mounted() {
      this.fetchSupplierList();
    },
  };
  </script>
  
  <style scoped>
  .dialog-footer {
    text-align: right;
    margin: 0;
  }
  </style>
  