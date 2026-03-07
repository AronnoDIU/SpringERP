/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        slate: {
          850: '#151b2b',
          950: '#020617',
        },
        indigo: {
          400: '#818cf8',
          500: '#6366f1',
          600: '#4f46e5',
          950: '#1e1b4b',
        },
        brand: {
          gold: '#fbbf24',
          emerald: '#10b981',
          rose: '#f43f5e'
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        'Plus_Jakarta_Sans': ['Plus Jakarta Sans', 'sans-serif'],
        'mono': ['JetBrains Mono', 'monospace'],
      },
      backgroundImage: {
        'glass-gradient': 'linear-gradient(135deg, rgba(255, 255, 255, 0.05), rgba(255, 255, 255, 0.01))',
        'mesh-pattern': "url('https://www.transparenttextures.com/patterns/cubes.png')",
        'data-stream': "linear-gradient(0deg, transparent 0%, rgba(99, 102, 241, 0.05) 50%, transparent 100%)",
      },
      animation: {
        'pulse-slow': 'pulse 12s cubic-bezier(0.4, 0, 0.6, 1) infinite',
        'float': 'float 8s ease-in-out infinite',
        'spin-slow': 'spin 45s linear infinite',
        'scan': 'scan 3s linear infinite',
        'shimmer': 'shimmer 2.5s infinite linear',
        'fade-up': 'fadeUp 0.8s cubic-bezier(0.16, 1, 0.3, 1) forwards',
        'stagger-1': 'fadeUp 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.1s forwards',
        'stagger-2': 'fadeUp 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.2s forwards',
        'stagger-3': 'fadeUp 0.8s cubic-bezier(0.16, 1, 0.3, 1) 0.3s forwards',
        'text-reveal': 'textReveal 2s ease-out forwards',
        'stream': 'stream 5s linear infinite',
        'shake': 'shake 0.5s cubic-bezier(.36,.07,.19,.97) both',
      },
      keyframes: {
        float: {
          '0%, 100%': { transform: 'translateY(0) rotate(0deg)' },
          '50%': { transform: 'translateY(-25px) rotate(1deg)' },
        },
        scan: {
          '0%': { top: '-100%' },
          '100%': { top: '300%' },
        },
        shimmer: {
          '0%': { transform: 'translateX(-100%)' },
          '100%': { transform: 'translateX(100%)' },
        },
        fadeUp: {
          '0%': { opacity: 0, transform: 'translateY(20px) scale(0.98)' },
          '100%': { opacity: 1, transform: 'translateY(0) scale(1)' },
        },
        textReveal: {
          '0%': { backgroundPosition: '-200% 0' },
          '100%': { backgroundPosition: '200% 0' }
        },
        stream: {
          '0%': { backgroundPosition: '0% 0px' },
          '100%': { backgroundPosition: '0% 80px' },
        },
        shake: {
          '10%, 90%': { transform: 'translate3d(-1px, 0, 0)' },
          '20%, 80%': { transform: 'translate3d(2px, 0, 0)' },
          '30%, 50%, 70%': { transform: 'translate3d(-4px, 0, 0)' },
          '40%, 60%': { transform: 'translate3d(4px, 0, 0)' }
        },
      }
    }
  },
  plugins: [],
};
