<template>
    <div>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane label="未发货" name="not-shipped"></el-tab-pane>
        <el-tab-pane label="在途" name="in-transit"></el-tab-pane>
        <el-tab-pane label="部分入库" name="partial-storage"></el-tab-pane>
        <el-tab-pane label="作废" name="invalid"></el-tab-pane>
        <el-tab-pane label="入库完成" name="completed"></el-tab-pane>
      </el-tabs>
  
      <el-row :gutter="20" class="filter-section">
        <el-col :span="6">
          <el-input v-model="filters.supplier" placeholder="请输入供应商"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input v-model="filters.storageNumber" placeholder="请输入入库单号"></el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-col>
      </el-row>
  
      <el-button type="primary" @click="createStorageOrder">创建入库单</el-button>
  
      <el-table v-if="paginatedData.length" :data="paginatedData" style="width: 100%" class="table-section">
        <el-table-column prop="storageNumber" label="入库单号"></el-table-column>
        <el-table-column prop="supplier" label="供应商"></el-table-column>
        <el-table-column label="入库状态">
          <template #default="scope">
            <span :class="getStatusClass(scope.row.storageStatus)">{{ scope.row.storageStatus }}</span>
          </template>
        </el-table-column>
        <el-table-column>
          <template #default="scope">
            <div class="action-buttons">
              <el-button  type="primary" v-if="scope.row.storageStatus === '未发货'"  size="small" @click="edit(scope.row)">
                <i class="el-icon-edit"></i> 修改
              </el-button>
              <el-button type="primary"  size="small" @click="store(scope.row)">
                <i class="el-icon-download"></i> 入库
              </el-button>
              <el-button type="primary" size="small" @click="openPrintDialog(scope.row.storageNumber)">
                <i class="el-icon-printer"></i> 打印入库单
              </el-button>
              <PrintTemp  v-model="Visible" @close="Visible = false"
               :dialogVisible1="Visible" :listnum = "listNum"></PrintTemp>
               <!-- <el-dialog v-model="Visible" >
                <span>This is a message</span>
                <template #footer>
                  <div class="dialog-footer">
                    <el-button @click="Visible = false">Cancel</el-button>
                    <el-button type="primary" @click="Visible = false">
                      Confirm
                    </el-button>
                  </div>
                </template>            

               </el-dialog> -->
              <el-button type="primary"  size="small" @click="printKanban(scope.row)">
                <i class="el-icon-document"></i> 打印看板
              </el-button>
              <el-button  type="primary"  v-if="scope.row.storageStatus === '未发货'||'入库完成'"  size="small" @click="deleteRow(scope.row)">
                <i class="el-icon-delete"></i> 删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :small="small"
        :disabled="disabled"
        :background="background"
        layout="total,  prev, pager, next, jumper"
        :total="filteredData.length"
        @current-change="handleCurrentChange"
      />
    </div>
    </div>
  
  </template>
  
  <script>
  // import { tr } from 'element-plus/es/locale/index.js';
import { enstore_list } from '../../request/request.js'
  import { delete_enstore_list_entry } from '../../request/request.js'
  import PrintTemp from'./components/PrintTemp.vue'
  export default {
    name: 'InventoryItem',
    components:{
      PrintTemp
    },
  
    data() {
      return {
        listNum:null,
        Visible: false,
        item_total:0,
        currentPage:1,
        pageSize:5,
        activeTab: 'all',
        filters: {
          orderNumber: '',
          supplier: '',
          storageNumber: ''
        },
        tableData: [],
        filteredData: []
      }
    },
    computed: {
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredData.slice(start, end);
    }
  },
  watch: {
    activeTab() {
      this.search();
    }
  },
    mounted() {
  
      this.fetchDataAndSearch();
  
  
      // 禁用 ResizeObserver loop limit exceeded 错误
      const resizeObserverLoopErr = /ResizeObserver loop limit exceeded/;
      window.addEventListener('error', e => {
        if (resizeObserverLoopErr.test(e.message)) {
          e.stopImmediatePropagation();
        }
      });
    },
    methods: {
      async fetchDataAndSearch() {
        try {
          // 等待 get_enstore_list() 异步函数执行完毕
          await this.get_enstore_list();
  
          // 在这里执行 search() 方法
          this.search();
        } catch (error) {
          console.error('Error fetching or searching:', error);
        }
      },
      async get_enstore_list() {
        try {
          let info = {
            listNum: null,
            listStatus: null,
            supplier: null,
          };
          let data = await enstore_list(info);
          console.log(data);
          // 使用 map 方法对数组中的每个对象进行属性名的修改
          data = data.map(item => ({
            id: item.id,
            storageNumber: item.listNum,
            storageStatus: item.listStatus,
            supplier: item.supplier,
            creator: item.creator,
            updator: item.updator,
            createTime: item.createTime,
            updateTime: item.updateTime
          }));
          this.item_total = data.length;
          // 映射关系
          const statusMapping = {
            "OPEN": "未发货",
            "ONLOAD": "在途",
            "PENDING": "部分入库",
            "DISCARDED": "作废",
            "CLOSED": "入库完成"
          };
  
          // 遍历数组，修改 storageStatus 属性对应的值
          data.forEach(item => {
            if (item.storageStatus in statusMapping) {
              item.storageStatus = statusMapping[item.storageStatus];
            }
          });
          console.log(data);
          this.tableData = data;
          console.log('this.tableData', this.tableData);
          // 处理登录成功后的逻辑，比如跳转到其他页面或者显示欢迎信息
        } catch (error) {
          console.error(error);
          // 处理登录失败的情况，比如显示错误提示信息
        }
      },
      openPrintDialog(storageNumber) {
      this.listNum = storageNumber;
      this.Visible = true;
      console.log(this.listNum);
    },
      getStatusClass(status) {
        switch (status) {
          case '未发货':
            return 'not-shipped';
          case '在途':
            return 'in-transit';
          case '部分入库':
            return 'partial-storage';
          case '作废':
            return 'invalid';
          case '入库完成':
            return 'completed';
          default:
            return '';
        }
      },
      search() {
        console.log('搜索条件:', this.filters);
        console.log('this.tableData', this.tableData);
        let filtered = this.tableData;
  
        // 根据顶部选项卡筛选
        if (this.activeTab !== 'all') {
          filtered = filtered.filter(item => {
            switch (this.activeTab) {
              case 'not-shipped':
                return item.storageStatus === '未发货';
              case 'in-transit':
                return item.storageStatus === '在途';
              case 'partial-storage':
                return item.storageStatus === '部分入库';
              case 'invalid':
                return item.storageStatus === '作废';
              case 'completed':
                return item.storageStatus === '入库完成';
              default:
                return true;
            }
          });
        }
  
        // 根据订单号筛选
        if (this.filters.orderNumber) {
          filtered = filtered.filter(item => item.orderNumber.includes(this.filters.orderNumber));
        }
  
        // 根据供应商筛选
        if (this.filters.supplier) {
          filtered = filtered.filter(item => item.supplier.includes(this.filters.supplier));
        }
  
        // 根据入库单号筛选
        if (this.filters.storageNumber) {
          filtered = filtered.filter(item => item.storageNumber.includes(this.filters.storageNumber));
        }
  
        this.filteredData = filtered;
      },
      resetFilters() {
        this.filters.orderNumber = '';
        this.filters.supplier = '';
        this.filters.storageNumber = '';
        this.search();
      },
      createStorageOrder() {
        console.log('创建入库单');
        // 在这里添加创建入库单逻辑
        this.$router.push('/move/enter/try/InventoryForm');
      },
      edit(row) {
        if (row.storageStatus !== '未发货') {
          console.warn('只有未发货状态的入库单可以编辑');
          return;
        }
        // console.log('编辑:', row);
        // 在这里添加编辑逻辑
        this.$router.push({name:'edit',query:{id:row.storageNumber,supplier:row.supplier}});
        // console.log(this.$router.query.id);
      },
      store(row) {
        console.log('入库:', row);
        // 在这里添加入库逻辑
        this.$router.push({name:'real_enter',query:{id:row.storageNumber,supplier:row.supplier}});
      },
      printOrder(row) {
        console.log('打印入库单:',row);
        this.Visiable = true;
        console.log(this.Visiable);
      },
      printKanban(row) {
        console.log('打印看板:', row);
        // 在这里添加打印看板逻辑
        console.log(this.Visiable);
      },
      // async delete_enstore_list(listnum) {
      //   try {
      //     let info={
      //       listNum: listnum,
      //     };
      //     let data = await delete_enstore_list_entry(info);
      //     console.log(data);
      //   } catch (error) {
      //     console.error(error);
      //     // 处理登录失败的情况，比如显示错误提示信息
      //   }
      // },
      async deleteRow(row) {
        this.tableData = this.tableData.filter(item => item !== row);
        console.log("row.storageNumber", row.storageNumber)
        //await this.delete_enstore_list(row.storageNumber);
        try {
          let info = {
            listNum: row.storageNumber,
          };
          let data = await delete_enstore_list_entry(info);
          console.log(data);
        } catch (error) {
          console.error(error);
          // 处理登录失败的情况，比如显示错误提示信息
        }
        console.log(1111111111)
        this.search();
        console.log('删除:', row);
      },
      

    }
  }
  </script>
  
  <style scoped>
  .filter-section {
    margin-bottom: 20px;
  }
  
  .table-section {
    margin-top: 20px;
  }
  
  .el-table {
    width: 100%;
  }
  
  .el-table-column {
    text-align: center;
  }
  
  .not-shipped {
    border: 1px solid gray;
    color: gray;
    padding: 2px 5px;
    border-radius: 5px;
  }
  
  .in-transit {
    border: 1px solid rgb(255, 179, 0);
    color: rgb(255, 179, 0);
    padding: 2px 5px;
    border-radius: 5px;
  }
  
  .partial-storage {
    border: 1px solid blue;
    color: blue;
    padding: 2px 5px;
    border-radius: 5px;
  }
  
  .invalid {
    border: 1px solid red;
    color: red;
    padding: 2px 5px;
    border-radius: 5px;
  }
  
  .completed {
    border: 1px solid green;
    color: green;
    padding: 2px 5px;
    border-radius: 5px;
  }
  
  span.not-shipped,
  span.in-transit,
  span.partial-storage,
  span.invalid,
  span.completed {
    display: inline-block;
  }
  
  .el-button {
    margin-right: 10px;
    margin-bottom: 5px;
    /* 添加底部边距，使按钮在多行时更整齐 */
  }
  
  .action-buttons {
    display: flex;
    flex-wrap: wrap;
    /* 允许按钮换行 */
    justify-content: flex-end;
    /* 左对齐按钮 */
  }

  .action-buttons .el-button {
    flex: 0 1 calc(50% - 10px);
    /* 每行两个按钮 */
    margin-right: 10px;
    margin-bottom: 10px;
  }
  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
  </style>
  