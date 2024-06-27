<template>
  <div class="container">
    <!-- 添加订单号和供应商信息 -->
    <div class="flex-container">
      <div class="info">
        <el-input v-model="orderNum" style="max-width: 400px" placeholder="" disabled>
          <template #prepend>订单号</template>
        </el-input>
      </div>
      <div class="info">
        <el-input v-model="supplier" style="max-width: 400px" placeholder="" disabled>
          <template #prepend>供应商</template>
        </el-input>
      </div>
    </div>

    <div style="margin-bottom: 20px">
      
    </div>

    <div class="material-details">
      <el-col :span="24">
        <div class="content">
          <span class="title">物料明细</span>
        </div>
      </el-col>
    </div>

    <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange"  header-align="center" border>
      <el-table-column type="selection" width="55" ></el-table-column>
      <el-table-column prop="supId" label="零件号" width="180" align="center" ></el-table-column>
      <el-table-column prop="materialType" label="物料类型" width="180" align="center"></el-table-column>
      <el-table-column prop="expInNum" label="计划数量" width="180" align="center"></el-table-column>
      <el-table-column label="实际数量" width="180" align="center">
        <template #default="row">
          <!--<el-input-number v-model="scope.row.actInName" size="small" :min="0" :max="scope.row.expInNum" />-->
          <el-input-number v-model="tableData[row.$index].actInNum" :min="data_unchanged[row.$index]" :max="tableData[row.$index].expInNum"/>
        </template>
      </el-table-column>
      <el-table-column label="入库状态" width="180" align="center">
        <template #default="scope">
          <el-select v-model="scope.row.status">
            <el-option v-for="status in storageStatuses" :key="status" :label="status" :value="status"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="note" label="备注" width="180" align="center">
        <template #default="scope">
          <el-input v-model="scope.row.note"></el-input>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-bottom: 20px">
      
    </div>

    <div align="center">
      <el-button-group class="button-group">
      <el-button type="primary" @click="save">保存</el-button>
      <el-button @click="cancel">取消</el-button>
    </el-button-group>
    </div>
    
  </div>
</template>

<script>
import { request_enstore_list, update_detail_list, update_stock_accordingly } from '../../request/request.js';

export default {
  data() {
    return {
      tableData: [],
      selection: [],
      data_unchanged: [],
      warehouses: ['吉耀', '丹灶'],
      storageStatuses: ['未发货', '已入库', '部分入库'],
      orderNum: "06132024-1449Biff",
      supplier: "Wizzy_Frostech"
    };
  },

  created() {
        this.res_data1 = this.$route.query.id;
        this.res_data2 = this.$route.query.supplier;
        this.orderNum = this.res_data1;
        this.supplier = this.res_data2;
        this.get_enstore_list();

    },
  methods: {
    increaseQuantity(index) {
      this.items[index].actualQuantity++;
    },
    async get_enstore_list() {
      try {
        let info = {
          "listNum": this.orderNum,
        };
        let data = await request_enstore_list(info);
        data = data.wrapInfoDetails.map(item => ({
          orderNum: data.listNum,
          storageStatus: data.listStatus,
          supplier: data.supplier,
          supId: item.supId,
          expInNum: item.expInNum,
          actInNum: item.actInNum,
          boxes: item.boxes,
          wrapId: item.wrapId,
          wrapSize: item.wrapSize,
          status: item.status
        }));

        data = data.map(item => ({
          ...item,
          actInNum: item.actInNum === null ? 0 : item.actInNum
        }));
        for (let i = 0; i < data.length; i++) {
          this.data_unchanged.push(data[i].actInNum);
        }
        const statusMapping = {
          "OPEN": "未发货",
          "PENDING": "部分入库",
          "CLOSED": "已入库",
        };
        data.forEach(item => {
          if (item.status in statusMapping) {
            item.status = statusMapping[item.status];
          }
        });
        this.tableData = data;
        console.log("TableData");
      console.log(this.tableData);
      } catch (error) {
        console.error(error);
      }
    },
    async save_detail_list(id, number) {
      try {
        let info = {
          listNum: this.orderNum,
          supID: id,
          actInNum: number
        };
        await update_detail_list(info);
      } catch (error) {
        console.error(error);
      }
    },
    async save_stock_accordingly() {
      try {
        let info = {
          listNum: this.orderNum,
          originalActInNum: this.data_unchanged
        };
        await update_stock_accordingly(info);
      } catch (error) {
        console.error(error);
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
      for (let i = 0; i < this.selection.length; i++) {
        console.log("this.Selection");
        console.log(this.selection);
        await this.save_detail_list(this.selection[i].supId, this.selection[i].actInNum);
      }
       await this.save_stock_accordingly();
       this.$router.replace('/move/enter/try/enter');

    },
    cancel() {
      console.log('取消');
      this.$router.replace('/move/enter/try/enter');

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
  margin-right: 20px;
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
.title {
  font-size: 18px;
  font-weight: bold;
}
.button-group {
  margin-left: 10px;
}
.container {
  padding: 20px;
}
</style>
