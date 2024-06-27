<template>
  <div class="asideNav">
    <el-menu :default-active="$route.path"
             class="el-menu-vertical"
             router
             unique-opened
             :collapse="iscollapse"
             :collapse-transition="false"
             active-text-color='#FFC000'
             background-color="#604A7B"
             text-color="#fff">
      <div v-for="(item, i) in lastMeun"
           :key="i">
        <el-sub-menu :index="item.prouter" v-if="item.children">
          <template v-slot:title>
            <div class="title-container">
              <i :class="item.icon"></i>
              <span class="tohide">{{ item.name }}</span>
            </div>
          </template>
          <el-menu-item
            :index="val.crouter"
            v-for="val in item.children"
            :key="val.id"
            @click="addTab(val)"
          >
            <i class="el-icon-s-help"></i>
            <span>{{ val.name }}</span>
          </el-menu-item>
        </el-sub-menu>

        <el-menu-item :index="item.prouter"
                      v-else
                      @click="addTab(item)">
          <template v-slot:title>
            <div class="title-container">
              <i :class="item.icon"></i>
              <span class="tohide">{{ item.name }}</span>
            </div>
          </template>
        </el-menu-item>
      </div>
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      defaultActive: '/home',
      iscollapse: false,
      logourl: require('../../assets/logo.png'),
      username: null,
      show: false,
      lastMeun: [],
      pMeun: [],
      cMeun: [],
      meunList: [
        { id: '1', pid: '', name: '首页', router: '/home', icon: 'el-icon-eleme' },
        { id: '2', pid: '', name: '入/出库', router: '/move', icon: 'el-icon-s-tools' },
        { id: '3', pid: '2', name: '入库', router: '/move/enter', icon: 'el-icon-s-tools' },
        { id: '4', pid: '2', name: '出库', router: '/move/shipment', icon: 'el-icon-star-on' },
        { id: '6', pid: '5', name: '零件信息', router: '/Inventory', icon: 'el-icon-edit' },
        { id: '8', pid: '', name: '库存盘点', router: '/stock', icon: 'el-icon-edit' },
        { id: '5', pid: '', name: '基本信息管理', router: '/control', icon: 'el-icon-edit' },
        { id: '10', pid: '5', name: '供应商', router: '/supplier', icon: 'el-icon-bell' },
        { id: '11', pid: '5', name: '客户', router: '/customer', icon: 'el-icon-bell' },
      ]
    }
  },
  mounted() {
    this.info()
  },
  methods: {
    info() {
      var pMeun = [] //父级菜单
      var cMeun = [] //子菜单
      if (this.meunList.length > 0) {
        this.meunList.forEach((item) => {
          if (!item.pid) {
            pMeun.push({
              id: item.id,
              name: item.name,
              icon: item.icon,
              prouter: item.router,
              children: []
            })
          } else {
            cMeun.push(item)
          }
        })
        pMeun.forEach((pitem) => {
          cMeun.forEach((citem) => {
            if (citem.pid == pitem.id) {
              pitem.children.push({
                id: citem.id,
                name: citem.name,
                crouter: citem.router
              })
            }
          })
        })
        pMeun.forEach((item, index) => {
          if (!item.children.length) {
            delete pMeun[index].children
          }
        })
        this.lastMeun = pMeun //赋值给菜单数据源
      } else {
        return
      }
    },
    addTab(menuItem) {
      this.$emit('handleAddTab', menuItem)
    },
    setcollapse() {
      this.iscollapse = !this.iscollapse
      this.$nextTick(() => {
        const logo = document.getElementById('logo')
        const aside = document.querySelector('.el-aside')
        const tohideElements = document.querySelectorAll('.tohide, .el-submenu__title .el-icon-arrow-right')

        if (this.iscollapse) {
          if (logo) logo.style.width = '64px'
          if (aside) aside.style.width = 'auto'
          tohideElements.forEach(el => el.style.display = 'none')
        } else {
          if (logo) logo.style.width = 'auto'
          if (aside) aside.style.width = '152px'
          tohideElements.forEach(el => el.style.display = 'inline')
        }
      })
    }
  }
}
</script>

<style lang='scss' scoped>
.asideNav {
  text-align: left;
}
.title-container {
  display: flex;
  justify-content: left; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  width: 100%;
  height: 100%;
}

.title-container i {
  margin-right: 8px; /* 图标与文本之间的间距 */
}

.tohide {
  display: flex;
  align-items: center; /* 确保文本垂直居中 */
}
</style>
