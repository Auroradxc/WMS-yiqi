<template>
    <div >
      <p>
        <el-text style="font-weight: bold; margin-left: 20px;">编号 </el-text>
        <el-input v-model="filters.id" placeholder="请输入编号" style="width: 200px; margin-right: 50px;"></el-input>
        <el-text style="font-weight: bold;">名称</el-text>
        <el-input v-model="filters.name" placeholder="请输入名称" style="width: 200px; margin-left: 5px;"></el-input>
   
        <el-button type="primary" @click="search" style="margin-left: 10px;">搜索</el-button>
        <el-button @click="resetFilters" style="margin-left: 10px;">重置</el-button>
          </p>
  
      <el-button type="primary" @click="handleAdd" style="margin-left: 10px;margin-bottom: 10px;" plain>新增</el-button>
      <Add v-if="Visible"  @close="Visible = false" :dialogVisible="Visible"></Add>
      <el-button type="success" @click="handleExport" style="margin-left: 10px; margin-bottom: 10px;" plain>导出</el-button>
    </div>
      <div >
      <el-table
        v-if="paginatedData.length" :data="paginatedData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        border
        stripe
      >
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column prop="supplierId" label="编号" width="600"></el-table-column>
        <el-table-column prop="supplierName" label="名称"></el-table-column>
        <el-table-column label="操作" width="300">
          <template v-slot="{ row }">
            <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :small="small"
        :disabled="disabled"
        :background="background"
        layout="total, prev, pager, next, jumper"
        :total="filteredData.length"
        @current-change="handleCurrentChange"
      />
    </div>
  
  </template>
  
  <script>
  import { ElTable, ElTableColumn, ElButton } from 'element-plus';
  import{get_supplier_list} from '../../request/request.js'
  import Http from 'axios';
  import Add from'./component/Add.vue'
  export default {
    name:'SupplierControl',
    components: {
      ElTable,
      ElTableColumn,
      ElButton,
      Add
    },
    data(){
      return{
        SupplierList:[],
        filters:{
          id:'',
          name:''
        },
        filteredData:[],
        item_total:0,
        currentPage:1,
        pageSize:10,
        activeTab: 'all',
        Visible:false,
      }
    },
    computed: {
      paginatedData() {
        const start = (this.currentPage - 1) * this.pageSize;
        const end = start + this.pageSize;
        return this.filteredData.slice(start, end);
      }
    },
  
    methods: {
      async fetchSupplierList(){
        let data =  await get_supplier_list();
        console.log(data);
        this.SupplierList = data;
        this.filteredData = data;
      },
      search() {
          console.log('搜索条件:', this.filters);
          let filtered = this.SupplierList;
    
          if (this.filters.id) {
            filtered = filtered.filter(item => item.supplierId.includes(this.filters.id));
          }
    
          // 根据供应商筛选
          if (this.filters.name) {
            filtered = filtered.filter(item => item.supplierName.includes(this.filters.name));
          }
  
          this.filteredData = filtered;
        },
      async handleDelete(row){
        this.filteredData = this.filteredData.filter(item=>item!==row)
        await Http.post("http://97.64.20.181:8000/delete_supplier",{"supplierId":row.supplierId}).then((res) => {
          console.log(res)
        })
    },
    handleAdd(){
      this.Visible =true;
    },
    resetFilters(){
      location.reload();
    }
    
  },
    mounted() {
      this.fetchSupplierList()
    },
  }
  </script>
  