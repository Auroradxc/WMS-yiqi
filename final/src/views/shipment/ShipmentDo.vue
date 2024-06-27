<template>
  <div class="container">
     <div class="flex-container">
  <div class="info">
    <el-input
      v-model="deliveryNum"
      style="max-width: 400px"
      placeholder=""
      disabled
    >
      <template #prepend>订单号</template>
    </el-input>
  </div>
  <div class="info">
    <el-input
      v-model="customer"
      style="max-width: 400px"
      placeholder=""
      disabled
    >
      <template #prepend>客户</template>
    </el-input>
  </div>
  <div class="info">
    <el-input
      v-model="deliveryStatus"
      style="max-width: 400px"
      placeholder=""
      disabled
    >
      <template #prepend>出库状态</template>
    </el-input>
  </div>
</div>

    <div class="material-details">
      <el-col :span="24">
        <div class="content">
          <span class="title">物料明细</span>
          <!-- <el-button-group class="button-group">
            <el-button type="primary">批量设置仓库/库区</el-button>
            <el-button type="primary">批量设置出库状态</el-button>
          </el-button-group> -->
        </div>
      </el-col>
    </div>

    <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange" header-align="center" border>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="supId" label="物料名" width="180"></el-table-column>
      <el-table-column prop="materialType" label="物料类型" width="180"></el-table-column>
      <el-table-column prop="expInNum" label="计划数量" width="180"></el-table-column>
      <el-table-column label="实际数量" width="180">
        <template #default="row">
          <el-input-number v-model="tableData[row.$index].actInNum" :min="data_unchanged[row.$index]" :max="tableData[row.$index].expInNum"/>
        </template>
        <!-- <template #default="scope">
          <el-button @click="decreaseQuantity(scope.$index)">-</el-button>
          <el-input v-model="scope.row.actInName" style="width: 60px; text-align: center;"></el-input>
          <el-button @click="increaseQuantity(scope.$index)">+</el-button>
        </template> -->
      </el-table-column>
      <el-table-column label="出库状态" width="180">
        <template #default="scope">
          <el-select v-model="scope.row.status">
            <el-option v-for="status in deliveryStatuses" :key="status" :label="status" :value="status"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="note" label="备注" width="180">
        <template #default="scope">
          <el-input v-model="scope.row.note"></el-input>
        </template>
      </el-table-column>
    </el-table>
    <el-button-group class="button-group">
      <el-button type="primary" @click="save">保存</el-button>
      <el-button @click="cancel">取消</el-button>
    </el-button-group>
  </div>
</template>

<script>
// import { exstore_list} from '../request/request.js'
import { request_exstorelist_detail} from '../../request/request.js'
import { account_detail_list } from '../../request/request.js'
import { relieve_stock_accordingly } from '../../request/request.js'
export default {
  data() {
    return {
      deliveryNum:"06192024-1459Biff",
      customer:"",
      deliveryStatus:"",
      tableData: [],
      data_unchanged: [],
      selection:[],
      deliveryStatuses: ['未发货', '已发货','部分发货','作废'],
      // orderNum:"06132024-1428Biff",
      // customer:"Sancti_Healer"
    };
  },
  
  
  mounted() {
    this.deliveryNum =this.$route.query.DeliveryNum;
    this.get_exstore_list();
    // this.get_exstore_list_detail();
  },
  methods: {
    increaseQuantity(index) {
      this.items[index].actualQuantity++;
    },
    // async get_exstore_list_detail() {
    //   try {
    //     let info = {
    //       listNum:  this.deliveryNum,
    //       listStatus:null,
    //       customer:null
    //     };
    //     let data = await exstore_list(info);
    //     console.log("exstore_list",data);
    //     // 使用 map 方法对数组中的每个对象进行属性名的修改
    //     data = data.map(item => ({
    //       id: item.id,
    //       deliveryNum: item.listNum,
    //       deliveryStatus: item.listStatus,
    //       customer: item.customer,
    //     }));
    //     console.log("exstore_list",data);
    //     this.customer=data[0].customer
    //     console.log("this.customer",this.customer)
        
    //     console.log("this.deliveryStatus",this.deliveryStatus)
    //     const statusMapping = {
    //       "OPEN": "未发货",
    //       "CLOSED": "已发货",
    //       "PENDING": "部分发货",
    //       "DISCARDED": "作废",
    //     };
        

    //     // 遍历数组，修改 storageStatus 属性对应的值
    //     data.forEach(item => {
    //       if (item.deliveryStatus in statusMapping) {
    //         item.deliveryStatus = statusMapping[item.deliveryStatus];
    //       }
    //     });
    //     console.log(data);
    //     this.deliveryStatus=data[0].deliveryStatus
    //     // 处理登录成功后的逻辑，比如跳转到其他页面或者显示欢迎信息
    //   } catch (error) {
    //     console.error(error);
    //     // 处理登录失败的情况，比如显示错误提示信息
    //   }
    // },
    async get_exstore_list() {
      try {
        let info = {
          "listNum": this.deliveryNum,
        };
        let data = await request_exstorelist_detail(info);
        data = data.wrapInfoDetails.map(item => ({
          deliveryNum: data.listNum,
          deliveryStatus: data.listStatus,
          customer: data.customer,
          supId: item.supId,
          expInNum: item.expInNum,
          actInNum: item.actInNum,
          boxes: item.boxes,
          wrapId: item.wrapId,
          wrapSize: item.wrapSize,
          status: item.status,
          unmodify_actInNum: item.actInNum
        }));
        
        data = data.map(item => ({
          ...item,
          actInNum: item.actInNum === null ? 0 : item.actInNum
        }));
        for (let i = 0; i < data.length; i++) {
          this.data_unchanged.push(data[i].actInNum);
        }
        console.log("this.data_unchanged",this.data_unchanged)
        const statusMapping = {
          "OPEN": "未发货",
          "CLOSED": "已发货",
          "PENDING": "部分发货",
          "DISCARDED": "作废",
        };
        console.log( data);
        data.forEach(item => {
          if (item.status in statusMapping) {
            item.status = statusMapping[item.status];
          }
        });
        this.customer=data[0].customer
        this.deliveryStatus=data[0].deliveryStatus
        console.log("this.deliveryStatus",this.deliveryStatus)
        console.log("this.customer",this.customer)
        this.tableData = data;
        console.log("TableData");
      console.log(this.tableData);
      } catch (error) {
        console.error(error);
      }
    },


    async save_detail_list(id,number) {
      try {
        let info = {
          listNum:  this.deliveryNum,
          supID:id,
          actInNum:number
        };
        await account_detail_list(info);
      
        // 处理登录成功后的逻辑，比如跳转到其他页面或者显示欢迎信息
      } catch (error) {
        console.error(error);
        // 处理登录失败的情况，比如显示错误提示信息
      }
    },
    async save_stock_accordingly(id,number) {
      try {
        let info = {
          listNum:  this.deliveryNum,
          supId:id,
          previousNum:number
        
        };
        console.log(info);
         await relieve_stock_accordingly(info);
      
        // 处理登录成功后的逻辑，比如跳转到其他页面或者显示欢迎信息
      } catch (error) {
        console.error(error);
        // 处理登录失败的情况，比如显示错误提示信息
      }
    },
    handleSelectionChange(selection) {
      this.selection = selection;
    },

    decreaseQuantity(index) {
      if (this.items[index].actualQuantity > 1) {
        this.items[index].actualQuantity--;
      }
    },
    async save() {
      for(let i=0;i<this.selection.length;i++)
      {
        await this.save_detail_list(this.selection[i].supId,this.selection[i].actInNum);
      }
      for(let i=0;i<this.selection.length;i++)
      {
        await this.save_stock_accordingly(this.selection[i].supId,this.selection[i].unmodify_actInNum==null?0:this.selection[i].unmodify_actInNum);
      }
    
      
      // 处理保存逻辑
      console.log('保存', this.tableData);
      this.$router.replace('/move/shipment/ShipmentMain');


    },
    cancel() {
      // 处理取消逻辑
      this.$router.replace('/move/shipment/ShipmentMain');
      console.log('取消');
    }
  }
};
</script>

<style scoped>
.material-details {
  display: flex;
  align-items: center;
}
.flex-container {
  display: flex;
}

.info {
  margin-right: 20px; /* 可以根据需要调整间距 */
}


.content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}
.order-status {
  width: 100%;
  margin: 20px 0;
  font-size: 18px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.row {
  display: flex;
  padding: 10px 0;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.row:last-child {
  border-bottom: none;
}
.label {
  flex: 1;
  font-weight: bold;
  color: #333;
}
.value {
  margin-right: 20px;
  flex: 2;
  background-color: #fff;
  padding: 8px 12px;
  border-radius: 4px;
  border: 1px solid transparent; /* 设置为透明 */
  color: #333;
  text-align: left;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1); /* 轻微的阴影效果 */
}
.title {
  font-size: 18px;
  font-weight: bold;
  margin-right: 20px; /* 调整标题和按钮组之间的距离 */
}

.button-group {
  margin-left: 10px;
  align-items: center;
  /* 调整按钮组件之间的距离 */
}

.container {
  padding: 20px;
}

.info {
  margin-bottom: 10px;
}

.info-item {
  font-size: 16px;
  line-height: 1.5;
  user-select: none; /* 禁止用户选择文本 */
}
</style>
