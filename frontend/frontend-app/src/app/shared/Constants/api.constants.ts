export const API = {
  BASE_URL: 'http://localhost:8080',

  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth'
  },

  BOOKS: {
    BASE: '/books',
    CREATE: '/books',
    BY_ID: (id: number) => `/books/${id}`
  }
};
