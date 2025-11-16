import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0',
    proxy: {
      '/user': {
        target: 'http://192.168.0.134:8080',
        changeOrigin: true
      },
      '/group': {  // 必须加上这一条！
        target: 'http://192.168.0.134:8080',
        changeOrigin: true
      }
    }
  }
})
