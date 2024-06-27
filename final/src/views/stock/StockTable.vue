<template>
  <div>
  <el-row :gutter="20" class="row">

    <el-col :span="2">   
      <p style="text-align: center;">物料</p>  
    </el-col>

    <el-col :span="6"> 
    <el-input v-model="filters.supId"  placeholder="请选择物料"></el-input>  
    </el-col>

    <el-col :span="2" text-align="center">   
      <p style="text-align: center;">仓库</p>  
    </el-col>

    <el-col :span="6">  
    <el-select v-model="selectedsupplier" placeholder="请选择仓库">  
    <el-option value='仓库1'
    label='仓库1'></el-option>
    </el-select>  
    </el-col>

    <el-col :span="6">
      <el-button type="primary" @click="search">搜索</el-button>
      <el-button @click="reset">重置</el-button>
    </el-col>

  </el-row>

  <el-table v-if="paginatedData.length" :data="paginatedData" style="margin-top: 30px;" border :span-method="mergeCells">
    <el-table-column label="仓库">
      <template v-slot="{ $index }">
        <span v-if="$index === 0">仓库</span>
      </template>
    </el-table-column>
    <el-table-column prop="supId" label="编号" width="600"></el-table-column>
    <el-table-column prop="supName" label="名称"></el-table-column>
    <el-table-column prop="amount" label="库存数量"></el-table-column>
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
import{fetchStock} from '../../request/request.js';

export default {


  data() {
    return {
      stockList:[],
      item_total:0,
      currentPage:1,
      pageSize:5,
      activeTab: 'all',
      filters:{
        supId:'',
      },
      filteredData:[],
    }
  },
  computed: {
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredData.slice(start, end);
    }
  },
  mounted() { 
    this.get_stock();  
  }, 
  methods: {
    async get_stock(){
      let info = {
        "supId":null,
        "supplier":null
      };
      let req = await fetchStock(info);
      this.filteredData = req;
      this.stockList=req;
      console.log("filteredData",this.filteredData);
    },
    search() {
        console.log('搜索条件:', this.filters);
        let filtered = this.stockList;
  
        if (this.filters.supId) {
          filtered = filtered.filter(item => item.supId.includes(this.filters.supId));
        }
  
        this.filteredData = filtered;
        console.log("this.filteredData",this.filteredData)
      },
   
    reset() {
      location.reload();
    },
    mergeCells({ rowIndex, columnIndex }) {
      if (columnIndex === 0 && rowIndex === 0) {
        return {
          rowspan: this.paginatedData.length,
          colspan: 1
        };
      } else if (columnIndex === 0 && rowIndex > 0) {
        return {
          rowspan: 0,
          colspan: 0
        };
      }
      return { rowspan: 1, colspan: 1 };
    }
}
}
</script>

<style scoped>

.table-section {
  margin-top: 20px;
}


.el-table-column {
  text-align: center;
}
.el-table .el-table__row td {  
  padding: 20px 0; /* 根据需要调整 padding 值 */  
  line-height: 1.8; /* 根据需要调整 line-height 值 */  
}  


.pagination-right {
  flex:1;  
  bottom: 20px; /* 距离底部的距离 */  
  right: 20px; /* 距离右边的距离 */  
}  

.el-table .el-table__row td {  
  padding: 20px 0; /* 根据需要调整 padding 值 */  
  line-height: 30px; /* 根据需要调整 line-height 值 */  
}  
  
/* 使表格自适应屏幕宽度 */  
.el-table {  
  width: 100%; /* 表格宽度设为 100% */  
  min-width: 100%; /* 防止表格内容溢出 */  
  overflow-x: auto; /* 当内容超出容器宽度时，显示水平滚动条 */  
}  

.el-input-group__prepend span  {  
  margin-right: 10px;
}  

.row{
  margin-top: 30px;  
}
</style>
