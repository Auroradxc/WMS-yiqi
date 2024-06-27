"use strict";(self["webpackChunkmenu"]=self["webpackChunkmenu"]||[]).push([[690],{8690:function(e,t,l){l.r(t),l.d(t,{default:function(){return k}});var a=l(6768);const i={class:"pagination-container"};function o(e,t,l,o,r,n){const d=(0,a.g2)("el-text"),s=(0,a.g2)("el-input"),u=(0,a.g2)("el-button"),c=(0,a.g2)("Add"),m=(0,a.g2)("el-table-column"),p=(0,a.g2)("el-table"),g=(0,a.g2)("el-pagination");return(0,a.uX)(),(0,a.CE)(a.FK,null,[(0,a.Lk)("div",null,[(0,a.Lk)("p",null,[(0,a.bF)(d,{style:{"font-weight":"bold","margin-left":"20px"}},{default:(0,a.k6)((()=>[(0,a.eW)("编号 ")])),_:1}),(0,a.bF)(s,{modelValue:r.filters.id,"onUpdate:modelValue":t[0]||(t[0]=e=>r.filters.id=e),placeholder:"请输入编号",style:{width:"200px","margin-right":"50px"}},null,8,["modelValue"]),(0,a.bF)(d,{style:{"font-weight":"bold"}},{default:(0,a.k6)((()=>[(0,a.eW)("名称")])),_:1}),(0,a.bF)(s,{modelValue:r.filters.name,"onUpdate:modelValue":t[1]||(t[1]=e=>r.filters.name=e),placeholder:"请输入名称",style:{width:"200px","margin-left":"5px"}},null,8,["modelValue"]),(0,a.bF)(u,{type:"primary",onClick:n.search,style:{"margin-left":"10px"}},{default:(0,a.k6)((()=>[(0,a.eW)("搜索")])),_:1},8,["onClick"]),(0,a.bF)(u,{onClick:n.resetFilters,style:{"margin-left":"10px"}},{default:(0,a.k6)((()=>[(0,a.eW)("重置")])),_:1},8,["onClick"])]),(0,a.bF)(u,{type:"primary",onClick:n.handleAdd,style:{"margin-left":"10px","margin-bottom":"10px"},plain:""},{default:(0,a.k6)((()=>[(0,a.eW)("新增")])),_:1},8,["onClick"]),r.Visible?((0,a.uX)(),(0,a.Wv)(c,{key:0,onClose:t[2]||(t[2]=e=>r.Visible=!1),dialogVisible:r.Visible},null,8,["dialogVisible"])):(0,a.Q3)("",!0),(0,a.bF)(u,{type:"success",onClick:e.handleExport,style:{"margin-left":"10px","margin-bottom":"10px"},plain:""},{default:(0,a.k6)((()=>[(0,a.eW)("导出")])),_:1},8,["onClick"])]),(0,a.Lk)("div",null,[n.paginatedData.length?((0,a.uX)(),(0,a.Wv)(p,{key:0,data:n.paginatedData,style:{width:"100%"},onSelectionChange:e.handleSelectionChange,border:"",stripe:""},{default:(0,a.k6)((()=>[(0,a.bF)(m,{type:"selection",width:"50"}),(0,a.bF)(m,{prop:"customerId",label:"编号",width:"600"}),(0,a.bF)(m,{prop:"customerName",label:"名称"}),(0,a.bF)(m,{label:"操作",width:"300"},{default:(0,a.k6)((({row:e})=>[(0,a.bF)(u,{size:"mini",type:"danger",onClick:t=>n.handleDelete(e)},{default:(0,a.k6)((()=>[(0,a.eW)("删除")])),_:2},1032,["onClick"])])),_:1})])),_:1},8,["data","onSelectionChange"])):(0,a.Q3)("",!0)]),(0,a.Lk)("div",i,[(0,a.bF)(g,{"current-page":r.currentPage,"onUpdate:currentPage":t[3]||(t[3]=e=>r.currentPage=e),"page-size":r.pageSize,"onUpdate:pageSize":t[4]||(t[4]=e=>r.pageSize=e),small:e.small,disabled:e.disabled,background:e.background,layout:"total, prev, pager, next, jumper",total:r.filteredData.length,onCurrentChange:e.handleCurrentChange},null,8,["current-page","page-size","small","disabled","background","total","onCurrentChange"])])],64)}var r=l(6023),n=l(47),d=l(1620),s=l(4373);const u={class:"dialog-footer"};function c(e,t,l,i,o,r){const n=(0,a.g2)("el-input"),d=(0,a.g2)("el-form-item"),s=(0,a.g2)("el-form"),c=(0,a.g2)("el-button"),m=(0,a.g2)("el-dialog");return(0,a.uX)(),(0,a.Wv)(m,{modelValue:o.dialogVisible,"onUpdate:modelValue":t[3]||(t[3]=e=>o.dialogVisible=e),title:"添加客户",width:"60%"},{default:(0,a.k6)((()=>[(0,a.bF)(s,{"label-position":"top","label-width":"100px",model:o.form},{default:(0,a.k6)((()=>[(0,a.bF)(d,{label:"客户编号"},{default:(0,a.k6)((()=>[(0,a.bF)(n,{modelValue:o.form.customerId,"onUpdate:modelValue":t[0]||(t[0]=e=>o.form.customerId=e),placeholder:"请输入客户编号"},null,8,["modelValue"])])),_:1}),(0,a.bF)(d,{label:"客户名称"},{default:(0,a.k6)((()=>[(0,a.bF)(n,{modelValue:o.form.customerName,"onUpdate:modelValue":t[1]||(t[1]=e=>o.form.customerName=e),placeholder:"请输入客户名称"},null,8,["modelValue"])])),_:1})])),_:1},8,["model"]),(0,a.Lk)("div",u,[(0,a.bF)(c,{onClick:t[2]||(t[2]=e=>o.dialogVisible=!1)},{default:(0,a.k6)((()=>[(0,a.eW)("取消")])),_:1}),(0,a.bF)(c,{type:"primary",onClick:r.handleConfirm},{default:(0,a.k6)((()=>[(0,a.eW)("确认")])),_:1},8,["onClick"])])])),_:1},8,["modelValue"])}var m={name:"Add",data(){return{dialogVisible:!0,form:{customerId:"",customerName:""}}},methods:{async handleConfirm(){try{const e=await s.A.post("http://97.64.20.181:8000/add_customer",{customerId:this.form.customerId,customerName:this.form.customerName});e.data&&Array.isArray(e.data)?this.SupplierList=e.data:console.error("Unexpected response data:",e.data)}catch(e){console.error("Error:",e)}this.dialogVisible=!1,location.reload()}}},p=l(1241);const g=(0,p.A)(m,[["render",c],["__scopeId","data-v-cf1c0c9e"]]);var f=g,h={name:"CustomerControl",components:{ElTable:r.Up,ElTableColumn:r.o8,ElButton:n.S2,Add:f},data(){return{CustomerList:[],filters:{id:"",name:""},filteredData:[],item_total:0,currentPage:1,pageSize:10,activeTab:"all",Visible:!1}},computed:{paginatedData(){const e=(this.currentPage-1)*this.pageSize,t=e+this.pageSize;return this.filteredData.slice(e,t)}},methods:{async fetchCustomer(){let e=await(0,d.Pt)();console.log(e),this.CustomerList=e,this.filteredData=e},search(){console.log("搜索条件:",this.filters);let e=this.CustomerList;this.filters.id&&(e=e.filter((e=>e.customerId.includes(this.filters.id)))),this.filters.name&&(e=e.filter((e=>e.customerName.includes(this.filters.name)))),this.filteredData=e},async handleDelete(e){this.filteredData=this.filteredData.filter((t=>t!==e)),await s.A.post("http://97.64.20.181:8000/delete_customer",{customerId:e.customerId}).then((e=>{console.log(e)}))},handleAdd(){this.Visible=!0},resetFilters(){location.reload()}},mounted(){this.fetchCustomer()}};const b=(0,p.A)(h,[["render",o]]);var k=b}}]);
//# sourceMappingURL=690.135b929b.js.map