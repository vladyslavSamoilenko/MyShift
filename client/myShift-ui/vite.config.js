import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/auth': { target: 'http://127.0.0.1:8087', changeOrigin: true },
      '/employees': { target: 'http://127.0.0.1:8087', changeOrigin: true },
      '/projects': { target: 'http://127.0.0.1:8087', changeOrigin: true },
      '/shifts': { target: 'http://127.0.0.1:8087', changeOrigin: true },
      '/users': { target: 'http://127.0.0.1:8087', changeOrigin: true },
    }
  }
})