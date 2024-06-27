<template>
    <el-dialog v-model="dialogVisible" title="添加供应商" width="60%">
      <el-form label-position="top" label-width="100px" :model="form">
        <el-form-item label="供应商编号">
          <el-input v-model="form.supplierId" placeholder="请输入供应商编号"></el-input>
        </el-form-item>
        <el-form-item label="供应商名称">
          <el-input v-model="form.supplierName" placeholder="请输入供应商名称"></el-input>
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
            supplierId: '',
            supplierName: '',
        },
      };
    },
    methods: {
      
      async handleConfirm() {
        try {
          const response = await axios.post('http://97.64.20.181:8000/add_supplier', {
            "supplierId":this.form.supplierId,
            "supplierName":this.form.supplierName,
          });
          if (response.data && Array.isArray(response.data)) {
            this.SupplierList = response.data;
          } else {
            console.error('Unexpected response data:', response.data);
          }
        } catch (error) {
          console.error('Error:', error);
        }
        this.dialogVisible = false;
        location.reload();
      },
    },

  };
  </script>
  
  <style scoped>
  .dialog-footer {
    text-align: right;
    margin: 0;
  }
  </style>
  