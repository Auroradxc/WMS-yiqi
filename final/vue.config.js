const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
 transpileDependencies: true,
 publicPath: './',
 outputDir: 'dist',
 assetsDir: 'static'
 }
)

// module.exports={
//   publicPath: process.env.NODE_ENV==='production'?'./':'./',
//   outputDir: 'dist',
//   assetsDir: 'static',
//   transpileDependencies: true
// }