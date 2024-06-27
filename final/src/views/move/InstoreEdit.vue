<template>
    <div>
      <el-form :model="form" label-width="120px">
        <el-form-item label="入库单号">
          <el-input v-model="form.orderNumber" disabled></el-input>
        </el-form-item>
        <el-form-item label="入库类型">
          <el-radio-group v-model="form.entryType">
            <el-radio-button label="采购入库"></el-radio-button>
            <el-radio-button label="外协入库"></el-radio-button>
            <el-radio-button label="退货入库"></el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="供应商" required class="short-input">
          <el-input v-model="form.supplier" disabled>
          </el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.note" maxlength="100" show-word-limit></el-input>
        </el-form-item>
        <el-divider></el-divider>
        
        <div class="header">
          <el-text size="large" class="header-text">零件明细</el-text>
          <el-button type="success" plain class="header-button">
            <el-icon color="green"></el-icon>
            批量设置仓库/库区
          </el-button>
          <el-button type="primary" @click="addPart" class="header-button">添加零件</el-button>
          <AddPartView v-if="Visiable" ref="dialog" @confirmParts="handleConfirmParts" @close="Visiable = false"></AddPartView>
        </div>
        
        <el-table :data="form.parts">
          <el-table-column prop="partNumber" label="零件号" style="text-align: center;"></el-table-column>
          <el-table-column label="入库数量" style="text-align: center;">
            <template #default="scope">
              <el-input-number v-model="scope.row.expInNum" :min="1" class="box" @change="handleQuantityChange(scope.row)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="箱数" class="titile" style="text-align: center;">
            <template #default="scope">
              <el-input-number v-model="scope.row.boxes" :min="1" class="box" @change="handleBoxCountChange(scope.row)"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column prop="waitingPackage" label="待转包" style="text-align: center;">
            <template v-slot="scope">
              <el-select v-model="scope.row.waitingPackage" placeholder="请选择" class="box">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="wrapId" label="器具编码" width="180" style="text-align: center;">
            <template v-slot="scope">
            <el-select v-model="scope.row.wrapId" placeholder="请选择器具">
              <el-option v-for="item in Appliances"
                :key="item.wrapId"
                :label="item.wrapId"
                :value="item.wrapId">
                <!--Null-->
              </el-option>
            </el-select>
          </template>
          </el-table-column>
          <el-table-column label="包装容量" class="titile" >
            <template #default="scope">
              <el-input-number v-model="scope.row.wrapSize" 
              :min="1" 
              >
              </el-input-number>          
            </template>
          </el-table-column>
          <el-table-column label="操作" class="titile" style="text-align: center;">
            <template #default="scope">
              <el-button type="danger" icon="el-icon-delete" size="small" @click="removePart(scope.$index)"
              style="text-align:center;height: 40px;width: 139px;">删除</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="warehouseArea" label="仓库/库区" class="titile"></el-table-column>
        </el-table>
        
        <el-form-item class="savebutton" style="text-align: center;">
          <el-button type="primary" @click="save" style="margin-left: auto; margin-right: 20px;">保存</el-button>
          <el-button @click="cancel" style="margin-left: 0px; margin-right: auto;">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script>
  import AddPartView from './components/AddPartView.vue';
  import {get_enstore_by_id} from '../../request/request.js';
  import axios from 'axios';
  export default {
    components: {
      AddPartView
    },
    name: 'InventoryForm',
    data() {
      return {
        Visiable: false,
        form: {
          orderNumber: '',
          entryType: '采购入库',
          supplier: '',
          orderID: '',
          note: '',
          parts: [],
        },
        res_data1:null,
        res_data2:null,
        options: [
          { value: 'true', label: '是' },
          { value: 'false', label: '否' }
        ],
        Appliances:[],
        
      };
    },

    created() {
        this.res_data1 = this.$route.query.id;
        this.res_data2 = this.$route.query.supplier;
        this.form.orderNumber = this.res_data1;
        this.form.supplier = this.res_data2;
        this.fetchData();
        this.fetchAppliances();

    },
    
    methods: {
    
        async fetchData() {
      try {
        const response = await get_enstore_by_id({
        "listNum": this.form.orderNumber
        });
        if (response ) {
          this.form.supplier = response.supplier || '';
          console.log(this.form.supplier);
          this.form.orderNumber = response.listNum || '';
          console.log(this.form.orderNumber);
          this.form.parts = (response.wrapInfoDetails || []).map(detail => ({
            partNumber: detail.supId,
            expInNum: detail.expInNum,
            boxes: detail.boxes,
            wrapId: detail.wrapId,
            wrapSize: detail.wrapSize,
            waitingPackage: detail.status === 'OPEN' ? 'true' : 'false',
          }));
          console.log(this.form.parts);
        } else {
        
          console.error("Invalid response data:", response);
            console.log()
        }
      } catch (error) {
        console.error("Error fetching data:", error);
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
    getMaxPackageSize(wrapId) {
      console.log(wrapId);
      console.log(this.Appliances);
      const selectedAppliance = this.Appliances.find(item => item.wrapId === wrapId);
      return selectedAppliance ? selectedAppliance.wrapSize : Infinity;
    },
    handlePackageSizeChange(row) {
      
      const selectedAppliance = this.Appliances.find(item => item.wrapId === row.wrapId);
      if (selectedAppliance && row.packageSize > selectedAppliance.wrapSize) {
        row.packageSize = 1;
      }
    },
    handleQuantityChange(row){
      row.wrapSize = Math.ceil(row.expInNum/row.boxes);
    },
    handleBoxCountChange(row){
      row.wrapSize = Math.ceil(row.expInNum/row.boxes);
    },
    removePart(index) {
      this.form.parts.splice(index, 1);
    },
    async save(){
        console.log('Form data:', this.form.parts)

        let object = {
        "sup_id":"",
        "enstore_num":"",
        "wrap_id":"",
        "wrap_size":"",
        "boxes":""
      };
      let Array = [];
      console.log('parts:',this.form.parts)
      for(let i = 0;i<this.form.parts.length;i++){
        object.sup_id = this.form.parts[i].partNumber;
        object.enstore_num=this.form.parts[i].expInNum;
        object.wrap_id=this.form.parts[i].wrapId;
        console.log("warpId",this.form.parts[i].wrapId);
        object.wrap_size= this.form.parts[i].wrapSize;
        console.log("this.getMaxPackageSize(this.form.parts[i].wrapId)",this.getMaxPackageSize(this.form.parts[i].wrapId));
        if (this.form.parts[i].wrapSize > this.getMaxPackageSize(this.form.parts[i].wrapId)) {
        alert('超过容量上限 ' + this.getMaxPackageSize(this.form.parts[i].wrapId));
        this.removePart(i)
        return; // Stop further execution
    }
        object.boxes=this.form.parts[i].boxes;
        Array.push(Object.assign({}, object));
      }
      let message = {
        "listNum": this.form.orderNumber,
        "listStatus": "OPEN",
        "supplier":this.form.supplier,
        "wrapInfoDetails" :Array
      }
      console.log(message);
      console.log("message:",message);
      try {
        const response = await axios.post('http://97.64.20.181:8000/modify_enstore_list/update_supplement', 
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
      this.$router.replace('/move/enter/try/enter');
    },
    addPart() {
      this.Visiable = true;
    },
    handleConfirmParts(selectedParts) {
      selectedParts.forEach(part => {
        this.form.parts.push({
          partNumber: part.supId,
          quantity: 1,
          boxCount: '',
          waitingPackage: '',
          toolCode: '',
          packageSize: 1,
          warehouseArea: '',
        });
      });
      this.Visiable = false;
    },
    cancel() {
      console.log('取消');
      this.$router.replace('/move/enter/try/enter');

    }
    }
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
  .box {
    height: 40px;
    width: 139px;
  }
  
  </style>
  