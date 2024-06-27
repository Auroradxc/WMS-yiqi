import { createRouter, createWebHashHistory } from 'vue-router';
import Layout from '@/layout'; // 确保路径正确
import Home from '@/views/home.vue'; // 假设你的视图文件是.vue文件
// 其他视图组件导入...

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: Layout,
      redirect: '/home',
      children: [
        {
          path: 'home',
          name: 'home',
          component: Home,
          meta: { title: '首页', access: 0, affix: true },
        },
      ],
    },
    
    {
      path:'/control',
      component: Layout,
      redirect: '/Inventory',
      meta: { title: '基础信息管理' },
      children:[
        {
          path: '/Inventory',
          redirect: '/Inventory/Route',
          meta: { title: '物料管理' },
          children:[{
            path:'/Inventory/Route',
            name:'InventoryRoute',
            redirect:'/Inventory/InventortControl',
            component:()=>import('@/views/Inventory/InventoryRoute'),
            meta: { title: '物料管理页面', access: 0, affix: false },
            children:[{
              path: "/Inventory/InventortControl",
              name: 'InventortControl',
              component: () => import('@/views/Inventory/InventoryControl'),
              meta: { title: '物料管理页面', access: 0, affix: false },
            }]
          }]
        },
        {
          path: '/customer',
          redirect: '/customer/Route',
          meta: { title: '顾客管理' },
          children:[{
            path:'/customer/Route',
            name:'CustomerRoute',
            redirect:'/customer/CustomerControl',
            component:()=>import('@/views/customer/CustomerControl'),
            meta: { title: '顾客管理', access: 0, affix: false },
            children:[{
              path: "/customer/CustomerControl",
              name: 'CustomerControl',
              component: () => import('@/views/customer/CustomerControl'),
              meta: { title: '顾客管理', access: 0, affix: false },
            }]
          }]
        },
        {
          path: '/supplier',
          redirect: '/supplier/Route',
          meta: { title: '供应商管理' },
          children:[{
            path:'/supplier/Route',
            name:'SupplierRoute',
            redirect:'/supplier/SupplierControl',
            component:()=>import('@/views/supplier/SupplierControl'),
            meta: { title: '供应商管理', access: 0, affix: false },
            children:[{
              path: "/supplier/SupplierControl",
              name: 'SupplierControl',
              component: () => import('@/views/supplier/SupplierControl'),
              meta: { title: '供应商管理', access: 0, affix: false },
            }]
          }]
        },
      ]
    },
    {
      path: '/move',
      component: Layout,
      redirect: '/move/enter',
      meta: { title: '入/出库' },
      children: [
        {
          path: "/move/enter",
          name: 'try',
          redirect: '/move/enter/try/enter',
          component: () => import('@/views/move/InstoreRoute'),
          meta: { title: '入库', access: 0, affix: false },
          children: [
            {
              path: "/move/enter/try/enter",
              name: 'enter',
              component: () => import('@/views/move/InstoreMain'),
              meta: { title: '入库单浏览', access: 0, affix: false },
            },
            {
              path: "/move/enter/try/InventoryForm",
              name: 'InventoryForm',
              component: () => import('@/views/move/InstoreCreate'),
              meta: { title: '创建入库单', access: 0, affix: false }
            },
            {
              path: "/move/enter/try/edit",
              name: 'edit',
              component: () => import('@/views/move/InstoreEdit'),
              meta: { title: '修改入库单', access: 0, affix: false },
            },
            {
              path: "/move/enter/try/real_enter",
              name: 'real_enter',
              component: () => import('@/views/move/InstoreDo'),
              meta: { title: '入库操作', access: 0, affix: false },
            },
          ]
        },
        {
          path: "/move/shipment",
          name: 'shipment',
          redirect: '/move/shipment/ShipmentMain',
          component: () => import('@/views/shipment/ShipmentRoute'),
          meta: { title: '出库', access: 0, affix: false },
          children: [{
            path: "/move/shipment/ShipmentMain",
            name: 'ShipmentMain',
            component: () => import('@/views/shipment/ShipmentMain'),
            meta: { title: '出库主页', access: 0, affix: false },
          },
          {
            path: "/move/shipment/ShipmentCreate",
            name: 'ShipmentCreate',
            component: () => import('@/views/shipment/ShipmentCreate'),
            meta: { title: '添加出库单', access: 0, affix: false },
          },
          {
            path: "/move/shipment/ShipmentEdit",
            name: 'ShipmentEdit',
            component: () => import('@/views/shipment/ShipmentEdit'),
            meta: { title: '出库单修改', access: 0, affix: false },
          },
          {
            path:"/move/shipment/ShipmentDo",
            name:"ShipmentDo",
            component:()=>import('@/views/shipment/ShipmentDo'),
            meta:{ title :'出库', access: 0, affix: false},
          },
        ]
        }
      ]
    },
    {
      path:'/stock',
      component: Layout,
      redirect: '/stock/route',
      meta:{ title:'库存盘点'},
      children:[
        {
          path: "/stock/route",
          name: 'route',
          redirect: '/stock/table',
          component: () => import('@/views/stock/StockRoute'),
          meta: { title: '库存盘点', access: 0, affix: false },
          children: [{
            path: "/stock/table",
          name: 'table',
          component: () => import('@/views/stock/StockTable'),
          meta: { title: '库存盘点', access: 0, affix: false },
          }]
        }
      ]
    }
    // 可以继续添加其他路由配置
  ],
});

router.afterEach((to) => {
  document.title = to.meta.title //在全局后置守卫中获取路由元信息设置title
  
})
export default router;
