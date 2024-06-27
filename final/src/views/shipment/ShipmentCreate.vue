<template>
  <div>
    <el-form :model="form" label-width="120px">
      <el-form-item label="出库单号">
        <el-input v-model="form.orderNumber" disabled></el-input>
      </el-form-item>

      <!-- <el-form-item label="供应商" required class="short-input">
        <el-select v-model="form.supplier" placeholder="请选择供应商">
          <el-option v-for="item in SupplierList"
            :key="item.supplierId"
            :label="item.supplierName"
            :value="item.supplierName">
          </el-option>
        </el-select>
      </el-form-item> -->
      
      <el-form-item label="顾客" required class="short-input">
        <el-select v-model="form.customer" placeholder="请选择客户">
          <el-option v-for="item in CustomerList"
            :key="item.customerId"
            :label="item.customerName"
            :value="item.customerName">
            <!--Null-->
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.note" maxlength="100" show-word-limit></el-input>
      </el-form-item>
      <el-divider></el-divider>
      
      <div class="header">
        <el-text size="large" class="header-text">物料明细</el-text>
        <el-button type="success" plain class="header-button">
          <el-icon color="green"></el-icon>
          批量设置仓库/库区
        </el-button>
        <el-button type="primary" @click="addPart" class="header-button">添加物料</el-button>
        <AddSup v-if="Visiable" ref="dialog" @confirmParts="handleConfirmParts" @close="Visiable = false"></AddSup>
      </div>
      
      <el-table :data="form.parts" border>
        <el-table-column prop="partNumber" label="物料号" width="180" style="text-align: center;"></el-table-column>
        <el-table-column prop="partName" label="物料名" width="180" style="text-align: center;"></el-table-column>
        <el-table-column label="计划数量" width="180" style="text-align: center;">
          <template #default="scope">
            <el-input-number  
            v-model="scope.row.quantity" 
            :min="1" 
            :max = scope.row.max_quantity
            class="box"
            @change="handleQuantityChange(scope.row)">
          </el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="箱数" class="titile" width="180" style="text-align: center;">
          <template #default="scope">
            <el-input-number 
            v-model="scope.row.boxCount" 
            :min="1" 
            class="box"
            @change="handleBoxCountChange(scope.row)">
          </el-input-number>
          </template>
        </el-table-column>

        <el-table-column prop="toolCode" label="器具编码" width="180" style="text-align: center;">
          <template v-slot="scope">
          <el-select v-model="scope.row.toolCode" placeholder="请选择器具"
          >
            <el-option v-for="item in Appliances"
              :key="item.wrapId"
              :label="item.wrapId"
              :value="item.wrapId">
              <!--Null-->
            </el-option>
          </el-select>
        </template>
        </el-table-column>
        <el-table-column label="包装容量" width="180" class="titile" >
          <template #default="scope">
            <el-input-number v-model="scope.row.packageSize" 
            :min="1" 
            >
            </el-input-number>          
          </template>
        </el-table-column>
        <el-table-column label="操作" class="titile" width="180" style="text-align: center;">
          <template #default="scope">
            <el-button type="danger" icon="el-icon-delete" size="small" @click="removePart(scope.$index)"
            style="text-align:center;height: 40px;width: 139px;">删除</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="warehouseArea" label="仓库/库区" width="180" class="titile"></el-table-column>
      </el-table>
      
      <el-form-item class="savebutton" style="text-align: center;">
        <el-button type="primary" @click="save" style="margin-left: auto; margin-right: 20px;">保存</el-button>
        <el-button @click="cancel" style="margin-left: 0px; margin-right: auto;">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios';
import AddSup from './components/AddSup.vue'
export default {
  components: {
    AddSup
  },
  name: 'ShipmentCreate',
  data() {
    return {
      Visiable: false,
      form: {
        orderNumber: '',
        customer:'',
        note: '',
        parts: [],
      },
      SupplierList: [],
      Appliances:[],
      CustomerList:[],
    };
  },
  created() {
    this.fetchSupplierList();
    this.fetchAppliances();
    this.fetchOrderNumber();
    this.get_enstorelist_num();
    this.fetchCustomer()
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
    async fetchCustomer() {
      try {
        const response = await axios.post('http://97.64.20.181:8000/get_customer_list', {
          // 如果请求需要携带数据，请在这里添加
        });
        if (response.data && Array.isArray(response.data)) {
          this.CustomerList = response.data;
        } else {
          console.error('Unexpected response data:', response.data);
        }
      } catch (error) {
        console.error('Error fetching supplier list:', error);
      }
    },
    async fetchAppliances() {
      try {
        const response = await axios.post('http://97.64.20.181:8000/request_all_appliances', {
           // 传递入库单号作为参数
          // 如果请求需要携带数据，请在这里添加
        });
        if (response.data && Array.isArray(response.data)) {
          this.Appliances = response.data;
        } else {
          console.error('Unexpected response data:', response.data);
        }
      } catch (error) {
        console.error('Error fetching supplier list:', error);
      }
      console.log(this.Appliances)
    },
    async fetchOrderNumber() {
        let info = await this.get_enstorelist_num();

        const orderNum = info.Num;console.log(info)
        const now = new Date();
        const year = now.getFullYear();
        const month = (now.getMonth() + 1).toString().padStart(2, '0');
        const date = now.getDate().toString().padStart(2, '0');
        const receiptOrderNo = `R-${year}-${month}-${date}-${orderNum}`;
        this.form.orderNumber = receiptOrderNo;
    },
    async get_enstorelist_num (){
    let info;
    await axios.post("http://97.64.20.181:8000/get_new_enstore_num_today").then(
        (Response)=>{
            info=Response.data;
        }
    )
    return info;
    },
    handleToolCodeChange(row) {
      const selectedAppliance = this.Appliances.find(item => item.wrapId === row.toolCode);
      if (selectedAppliance) {
        row.packageSize = Math.min(row.packageSize, selectedAppliance.wrapSize);
      }
    },
    getMaxPackageSize(toolCode) {
      const selectedAppliance = this.Appliances.find(item => item.wrapId === toolCode);
      return selectedAppliance ? selectedAppliance.wrapSize : Infinity;
    },
    


    handleQuantityChange(row){
      row.packageSize = Math.ceil(row.quantity/row.boxCount);
    },
    handleBoxCountChange(row){
      row.packageSize = Math.ceil(row.quantity/row.boxCount);
    },
    addPart() {
      this.Visiable = true;
    },
    handleConfirmParts(selectedParts) {
      selectedParts.forEach(part => {
        this.form.parts.push({
          partNumber: part.supId,
          partName:part.supName,
          quantity: 1,
          boxCount: '',
          waitingPackage: '',
          toolCode: '',
          packageSize: 1,
          warehouseArea: '',
          max_quantity:part.amount,
        });
      });
      this.Visiable = false;
    },
    removePart(index) {
      this.form.parts.splice(index, 1);
    },
    cancel(){
      this.$router.replace('/move/shipment/ShipmentMain');
    },
    async save() {
      let object = {
        "sup_id":"",
        "exstore_num":"",
        "wrap_id":"",
        "wrap_size":"",
        "boxes":""
      };
      let Array = [];
      console.log('Form data:', this.form);
      console.log('parts:',this.form.parts)
      for(let i = 0;i<this.form.parts.length;i++){
        object.sup_id = this.form.parts[i].partNumber;
        object.exstore_num=this.form.parts[i].quantity;
        object.wrap_id=this.form.parts[i].toolCode;
        object.wrap_size= this.form.parts[i].packageSize;
        if (this.form.parts[i].packageSize > this.getMaxPackageSize(this.form.parts[i].toolCode)) {
        alert('超过容量上限 ' + this.getMaxPackageSize(this.form.parts[i].toolCode));
        this.removePart(i)
        return; // Stop further execution
    }
        object.boxes=this.form.parts[i].boxCount;
        Array.push(Object.assign({}, object));
        console.log(Array)
      }
      let message = {
        "listNum": this.form.orderNumber,
        "listStatus": "OPEN",
        "customer":this.form.customer,
        "wrapInfoDetails" :Array
      }
      console.log(message);
      try {
        const response = await axios.post('http://97.64.20.181:8000/create_exstore_list', 
          message)// 传递入库单号作为参数
          // 如果请求需要携带数据，请在这里添加);
        if (response.data ) {
          console.log(response.data);
        } else {
          console.error('Unexpected response data:', response.data);
        }
      } catch (error) {
        console.error('Error fetching supplier list:', error);
      }
      this.$router.replace('/move/shipment/ShipmentMain');
    },
  },
};
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding-left: 60px; /* 调整内边距来移动内容 */
  margin-bottom: 60px;
}
.header-text {
  font-weight: bold;
}
.header-button {
  margin-left: 200px;
  margin-right: 200px;
}
.icon {
  width: 1em;
  height: 1em;
  vertical-align: -0.125em;
  fill: currentColor;
  overflow: hidden;
}
.savebutton {
  margin-top: 20px;
}


</style>

