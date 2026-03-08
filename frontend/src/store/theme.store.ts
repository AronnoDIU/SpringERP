import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface ThemeState {
  isDark: boolean;
  toggle: () => void;
  setDark: (v: boolean) => void;
}

export const useThemeStore = create<ThemeState>()(
  persist(
    (set) => ({
      isDark:
        typeof window !== 'undefined'
          ? window.matchMedia('(prefers-color-scheme: dark)').matches
          : false,
      toggle: () =>
        set((s) => {
          const next = !s.isDark;
          if (next) document.documentElement.classList.add('dark');
          else document.documentElement.classList.remove('dark');
          return { isDark: next };
        }),
      setDark: (v: boolean) =>
        set(() => {
          if (v) document.documentElement.classList.add('dark');
          else document.documentElement.classList.remove('dark');
          return { isDark: v };
        }),
    }),
    { name: 'erp-theme' }
  )
);

