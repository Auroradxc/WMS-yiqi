<template>
  <div class="app-container">
    <div class="center-content">
      <el-card class="image-card">
        <h2>欢迎登录 WMS 物流系统！</h2>
      </el-card>
      </div>

    <footer class="footer">
      <p>制作人： 冯扬</p>
      <p> 张雨洋 董现冲</p>
      <p>黄洵彬 陈凯晨 侯学毅 陈镛宇</p>
      <p>制作团队：华南理工大学第五小组</p>
    </footer>
    
  </div>
</template>

<script>
export default {
  name: 'home',
};
</script>

<style scoped>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: Arial, sans-serif;
}

.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f9f9f9;
  height: 90%;
}

.main-content {
  display: flex;
  justify-content: center;
  align-items: stretch;
  flex-grow: 1;
  width: 100%;
  padding: 20px;
}
 .center-content{
    display: flex;
  justify-content: center;
  align-items: center;
 }
.left-content,.right-content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 10px;
  height: 300px;
}

.el-card {
  width: 100%;
  padding: 30px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: white;
}

.image-card img {
  max-width: 100%;
  height: auto;
  max-height: 300px; /* Adjust height as necessary */
  border-radius: 8px;
  margin-top: 20px;
}

.footer {
  background-color: white;
  padding: 20px 0;
  text-align: center;
  color:black;
}

h2 {
  color: #409EFF;
  margin-bottom: 20px;
}

p {
  margin: 5px 0;
}

/* Responsive design */
@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }
  
  .left-content, .center-content, .right-content {
    margin: 10px 0;
  }
}
</style>
