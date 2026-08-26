import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],

  test: {
    environment: 'jsdom'
  },

  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },

  server: {
    host: '0.0.0.0',
    port: 5174,

    allowedHosts: [
      'quarry-bankroll-juicy.ngrok-free.dev'
    ],

    proxy: {
      // ==============================
      // AUTH
      // /api/auth/login
      //       ↓
      // backend /api/auth/login
      // ==============================
      '/api/auth': {
        target: 'http://localhost:8000',
        changeOrigin: true
      },

      // ==============================
      // CONTACTS
      // /api/contacts
      //       ↓
      // /contacts
      // ==============================
      '/api/contacts': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})