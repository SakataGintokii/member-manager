module.exports = {
  devServer: {
    port: 8081,
    proxy: {
      '^/members': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}