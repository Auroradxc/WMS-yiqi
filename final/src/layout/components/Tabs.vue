<template>
  <div>
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick" @tab-remove="removeTab" closable>
      <el-tab-pane
        v-for="tab in tabs"
        :key="tab.name"
        :label="tab.title"
        :name="tab.name"
      >
        <router-view :key="tab.name"></router-view>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tabs: [
        { title: '首页', name: '/home' },
      ],
      activeTab: '/home'
    }
  },
  watch: {
    $route(to) {
      this.activeTab = to.path;
      this.addTabByRoute(to);
    }
  },
  methods: {
    addTab(menuItem) {
      const tabExists = this.tabs.find(tab => tab.name === menuItem.router);
      if (!tabExists) {
        this.tabs.push({ title: menuItem.name, name: menuItem.router });
      }
      this.activeTab = menuItem.router;
      if (this.$router) {
        this.$router.push(menuItem.router).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error(err);
          }
        });
      } else {
        console.error('Router instance is not available.');
      }
    },
    addTabByRoute(route) {
      const tabExists = this.tabs.find(tab => tab.name === route.path);
      if (!tabExists && route.meta.title) {
        this.tabs.push({ title: route.meta.title, name: route.path });
      }
    },
    handleTabClick(tab) {
        this.$router.push(tab.props.name).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error(err);
          }
        });
    },
    removeTab(targetName) {
      let tabs = this.tabs;
      let activeTab = this.activeTab;
      if (activeTab === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            const nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeTab = nextTab.name;
            }
          }
        });
      }
      this.activeTab = activeTab;
      this.tabs = tabs.filter(tab => tab.name !== targetName);
      if (this.$router && this.activeTab) {
        this.$router.push(this.activeTab).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error(err);
          }
        });
      } else {
        console.error('Router instance is not available.');
      }
    }
  },
  mounted() {
    this.addTabByRoute(this.$route);
  }
}
</script>
