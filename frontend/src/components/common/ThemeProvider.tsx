import React, { useEffect } from 'react';
import { useThemeStore } from '../../store/theme.store';

/** Syncs the persisted theme to the <html> class on every mount. */
export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const isDark = useThemeStore((s) => s.isDark);

  useEffect(() => {
    if (isDark) document.documentElement.classList.add('dark');
    else document.documentElement.classList.remove('dark');
  }, [isDark]);

  return <>{children}</>;
};

