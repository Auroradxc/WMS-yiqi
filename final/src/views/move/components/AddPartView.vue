<template>
  <el-dialog v-model="dialogVisible" title="添加零件" width="60%">
    <div class="inputArea">
      <el-text class="inputTitle" tag="b">供应商</el-text>
      <el-input v-model="query.supplier" placeholder="代码" class="filter-input" clearable autosize type="textarea"
        @clear="handleClear" @input="handleInput"></el-input>
      <el-text class="inputTitle" tag="b">零件号</el-text>
      <el-input v-model="query.supId" placeholder="批量查询" class="filter-input" clearable autosize type="textarea"
        @clear="handleClear" @input="handleInput"></el-input>
      <el-button type="primary" @click="handleQuery">查询</el-button>
    </div>
    <div style="display: flex; justify-content: center; ">
      <el-table :data="filteredData" border style="width: 70%; margin-bottom: 20px;"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="70"></el-table-column>
        <el-table-column prop="supName" label="零件名称"></el-table-column>
        <el-table-column prop="supId" label="零件编号"></el-table-column>
        <el-table-column prop="supplier" label="供应商"></el-table-column> 
      </el-table>
    </div>
    <div style="display: flex; justify-content: center; margin-bottom: 20px;">
      <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize"
        @current-change="handlePageChange"></el-pagination>
    </div>
    <span class="dialog-footer">
      <div>
        <slot name="footer"></slot>
      </div>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确认</el-button>
    </span>
  </el-dialog>
</template>

<script>
import axios from 'axios';
import { get_supplement_list } from '../../../request/request.js';

export default {
  name: 'AddPartView',
  data() {
    return {
      dialogVisible: true,
      supplier: null,
      query: {
        supplier:'',
        supId:''
      },
      selectedParts: [],
      parts: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
    };
  },
  created() {
    this.fetchSupplementList();
  },
  computed: {
    filteredData() {
      const partsArray = Array.isArray(this.parts) ? this.parts : [];
      if (this.query) {
        return partsArray.filter(part => (part.supId && part.supId.includes(this.query.supId)&&(part.supplier && part.supplier.includes(this.query.supplier))));
      }
      return partsArray.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
  },
  watch: {
    parts(newValue) {
      this.total = Array.isArray(newValue) ? newValue.length : 0;
    },
  },
  methods: {
    handleClear() {
      this.query = '';
      this.currentPage = 1;
    },
    handleInput() {
      this.currentPage = 1;
    },
    handleQuery() {
      this.currentPage = 1;
      this.get_supplement_list_filtered();
    },
    handleSelectionChange(val) {
      this.selectedParts = val;
    },
    handlePageChange(val) {
      this.currentPage = val;
    },
    handleConfirm() {
      this.$emit('confirmParts', this.selectedParts);
      this.dialogVisible = false;
      console.log(this.selectedParts)
    },

    async get_supplement_list_filtered() {
      let info = {
        supId: this.query.supId || null,
        supplier: this.query.supplier || null,
      };
      try {
        const response = await get_supplement_list(info);
        if (Array.isArray(response)) {
          this.parts = response;
        } else {
          console.error('Unexpected response data:', response);
        }
      } catch (error) {
        console.error('Error fetching filtered list:', error);
      }
    },
    async fetchSupplementList() {
      try {
        const response = await axios.post('http://97.64.20.181:8000/request_supplement_list', {
          "supId": null,
          "supplier": null
        });
        if (response.data && Array.isArray(response.data)) {
          this.parts = response.data;
        } else {
          console.error('Unexpected response data:', response.data);
        }
      } catch (error) {
        console.error('Error fetching supplier list:', error);
      }
    },
  },
  mounted() {
    this.total = Array.isArray(this.parts) ? this.parts.length : 0;
    this.get_supplement_list_filtered();
  },
};
</script>

<style scoped>
.filter-input {
  margin-bottom: 20px;
  width: 150px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-right: 120px;
}

.inputArea {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  margin-top: 10px;
}

.inputTitle {
  margin: 0 10px;
  margin-bottom: 20px;
}

.filter-input {
  width: 150px;
}

.el-button {
  margin-left: 10px;
  margin-bottom: 20px;
}
</style>
