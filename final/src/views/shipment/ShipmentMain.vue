<template>
    <div>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="全部" name="all"></el-tab-pane>
        <el-tab-pane label="未发货" name="not-shipped"></el-tab-pane>
        <el-tab-pane label="已发货" name="in-transit"></el-tab-pane>
        <el-tab-pane label="部分发货" name="partial-storage"></el-tab-pane>
        <el-tab-pane label="作废" name="invalid"></el-tab-pane>
      </el-tabs>
  
      <el-row :gutter="20" class="filter-section">
        <el-col :span="6">
          <el-input v-model="filters.customer" placeholder="请输入客户"></el-input>
        </el-col>
        <el-col :span="6" style="margin-left:40px">
          <el-input v-model="filters.deliveryNumber" placeholder="请输入出库单号"></el-input>
        </el-col>
        <el-col :span="6" style="margin-left:40px">
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-col>
      </el-row>
  
      <el-button type="primary" @click="createDeliverOrder">创建出库单</el-button>
  
      <el-table v-if="paginatedData.length" :data="paginatedData" style="width: 100%" class="table-section">
        <el-table-column prop="deliveryNumber" label="出库单号"></el-table-column>
        <!--      <el-table-column prop="storageType" label="入库类型"></el-table-column>-->
        <el-table-column prop="customer" label="客户"></el-table-column>
        <!--   <el-table-column prop="orderNumber" label="订单号"></el-table-column>-->
        <el-table-column label="出库状态">
          <template #default="scope">
            <span :class="getStatusClass(scope.row.deliveryStatus)">{{ scope.row.deliveryStatus }}</span>
          </template>
        </el-table-column>
        <el-table-column  label="备注"></el-table-column>
        <el-table-column >
          <template #default="scope">
            <div class="action-buttons">
              <el-button v-if="scope.row.deliveryStatus === '未发货'||'部分发货'" type="primary" size="small" @click="edit(scope.row)">
                <i class="el-icon-edit"></i> 修改
              </el-button>
              <el-button v-if="scope.row.deliveryStatus === '未发货'" type="primary" size="small" @click="deleteRow(scope.row)">
                <i class="el-icon-delete"></i> 删除
              </el-button>
              <el-button v-if="scope.row.deliveryStatus === '未发货'||'部分发货'" type="primary" size="small" @click="store(scope.row)">
                <i class="el-icon-download"></i> 发货/出库
              </el-button>
              <el-button  type="primary" size="small" @click="printOrder(scope.row)">
                <i class="el-icon-printer"></i> 打印
              </el-button>
              <PrintItem  v-model="Visible" @close="Visible = false"
              :dialogVisible1="Visible" :listnum = "listNum"></PrintItem>
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
          layout="total, prev, pager, next, jumper"
          :total="filteredData.length"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </template>
  
  <script>
  import { exstore_list } from '../../request/request.js'
  import { delete_exstore_list_entry } from '../../request/request.js'
  import PrintItem from './components/PrintItem.vue'
  export default {
    name: 'ShipmentMain',
    components:{
      PrintItem
    },
    data() {
      return {
        listNum:'',
        activeTab: 'all',
        customer:'',
        Visible: false,
        item_total:0,
        currentPage:1,
        pageSize:5,
        filters: {
          customer: '',
          deliveryNumber: ''
        },
        tableData: [],
        filteredData: []
      }
    },
    watch: {
      activeTab() {
        this.search();
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
          await this.get_exstore_list();
  
          // 在这里执行 search() 方法
          this.search();
        } catch (error) {
          console.error('Error fetching or searching:', error);
        }
      },
      async get_exstore_list() {
        try {
          let info = {
            listNum: null,
            listStatus: null,
            customer: null,
          };
          let data = await exstore_list(info);
          console.log("exstore_list",data);
          // 使用 map 方法对数组中的每个对象进行属性名的修改
          data = data.map(item => ({
            id: item.id,
            deliveryNumber: item.listNum,
            deliveryStatus: item.listStatus,
            customer: item.customer,
            creator: item.creator,
            updator: item.updator,
            createTime: item.createTime,
            updateTime: item.updateTime
          }));
  
          // 映射关系
          const statusMapping = {
            "OPEN": "未发货",
            "CLOSED": "已发货",
            "PENDING": "部分发货",
            "DISCARDED": "作废",
          };
  
          // 遍历数组，修改 storageStatus 属性对应的值
          data.forEach(item => {
            if (item.deliveryStatus in statusMapping) {
              item.deliveryStatus = statusMapping[item.deliveryStatus];
            }
          });
          console.log("exstore_list_xiu",data);
          this.tableData = data;
          console.log('this.tableData', this.tableData);
          // 处理登录成功后的逻辑，比如跳转到其他页面或者显示欢迎信息
        } catch (error) {
          console.error(error);
          // 处理登录失败的情况，比如显示错误提示信息
        }
      },
  
      getStatusClass(status) {
        switch (status) {
          case '未发货':
            return 'not-shipped';
          case '已发货':
            return 'in-transit';
          case '部分发货':
            return 'partial-storage';
          case '作废':
            return 'invalid';
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
                return item.deliveryStatus === '未发货';
              case 'in-transit':
                return item.deliveryStatus === '已发货';
              case 'partial-storage':
                return item.deliveryStatus === '部分发货';
              case 'invalid':
                return item.deliveryStatus === '作废';
              default:
                return true;
            }
          });
        }
  
      
  
        // 根据供应商筛选
        if (this.filters.customer) {
          filtered = filtered.filter(item => item.customer.includes(this.filters.customer));
        }
  
        // 根据入库单号筛选
        if (this.filters.deliveryNumber) {
          filtered = filtered.filter(item => item.deliveryNumber.includes(this.filters.deliveryNumber));
        }
  
        this.filteredData = filtered;
      },
      resetFilters() {
        this.filters.customer = '';
        this.filters.deliveryNumber = '';
        this.search();
      },
      createDeliverOrder() {
        console.log('创建出库单');
        // 在这里添加创建入库单逻辑
        this.$router.push('/move/shipment/ShipmentCreate')
      },
      edit(row) {
        if (row.deliveryStatus !== '未发货') {
          console.warn('只有未发货状态的入库单可以编辑');
        
        }
        this.$router.push({name:'ShipmentEdit',
          query:{DeliveryNum: row.deliveryNumber,
            customer:row.customer
          }});
        console.log('编辑:', row);
        // 在这里添加编辑逻辑
      },
      store(row) {
        console.log('出库:', row);
        // 在这里添加入库逻辑
        this.$router.push({name:'ShipmentDo',
                          query:{DeliveryNum: row.deliveryNumber}});
      },
      printOrder(row) {
        console.log('打印出库单:', row);
        this.Visible =  true;
        this.listNum = row.deliveryNumber;
        console.log(this.listNum);
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
        console.log("row.deliveryNumber", row.deliveryNumber)
        //await this.delete_enstore_list(row.storageNumber);
        try {
          let info = {
            listNum: row.deliveryNumber,
          };
          let data = await delete_exstore_list_entry(info);
          console.log("delete_data",data);
        } catch (error) {
          console.error(error);
          // 处理登录失败的情况，比如显示错误提示信息
        }
        console.log(1111111111)
        this.search();
        console.log('删除:', row);
      }
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
    border: 1px solid rgb(255, 0, 234);
    color: rgb(255, 0, 234);
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
  </style>
  