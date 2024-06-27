<template>
  <el-container class="container_box">
    <!-- Header -->
    <el-header class="header">
      <Header></Header>
    </el-header>
    <!-- Content below the header -->
    <el-container class="content-container">
      <!-- Sidebar navigation -->
      <el-aside width="202px" class="sider_box">
        <div class="sider-content">
          <Sliber></Sliber>
        </div>
      </el-aside>
      <!-- Main content -->
      <el-main class="main">
        <Tabs ref="tabs"></Tabs>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Sliber from './components/Sliber.vue';
import Header from './components/Header.vue';
import Tabs from './components/Tabs.vue';

export default {
  components: {
    Sliber,
    Header,
    Tabs
  },
  methods: {
    handleAddTab(menuItem) {
      this.$refs.tabs.addTab(menuItem);
    }
  }
}
</script>

<style lang="scss" scoped>
html, body, .container_box {
  height: 100%;
  margin: 0;
  overflow-y: hidden;
}

.el-header {
  position: fixed;
  width: 100%;
  z-index: 1000;
}
.sider_box{
  height: 100%;
}
.sider-content{
  height: 100%;
}
.content-container {
  margin-top: 64px; /* Adjust based on header height */
  display: block flex;
}


.main {
  overflow-y: auto;
  height: (1223-60)px;
  }
</style>
