import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css'

const app = createApp(App)

app.use(router)
app.use(ElementPlus)

app.mount('#app')

// app.vue写在script里面  main.js写在app挂在完之后
const debounce = (fn, delay) => {
    let timer
     return (...args) => {
       if (timer) {
         clearTimeout(timer)
       }
       timer = setTimeout(() => {
         fn(...args)
       }, delay)
     }
  }
    
  const _ResizeObserver = window.ResizeObserver;
  window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
     constructor(callback) {
       callback = debounce(callback, 200);
       super(callback);
     }
  }

  