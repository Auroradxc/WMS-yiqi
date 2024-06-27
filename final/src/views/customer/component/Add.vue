<template>
    <el-dialog v-model="dialogVisible" title="添加客户" width="60%">
      <el-form label-position="top" label-width="100px" :model="form">
        <el-form-item label="客户编号">
          <el-input v-model="form.customerId" placeholder="请输入客户编号"></el-input>
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="form.customerName" placeholder="请输入客户名称"></el-input>
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
          customerId: '',
          customerName: '',
        },
      };
    },
    methods: {
      
      async handleConfirm() {
        try {
          const response = await axios.post('http://97.64.20.181:8000/add_customer', {
            "customerId":this.form.customerId,
            "customerName":this.form.customerName,
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
  