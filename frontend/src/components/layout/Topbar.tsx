import React from 'react';
import { useLogout } from '../../hooks/useAuth';
import { useAuthStore } from '../../store/auth.store';
import { useThemeStore } from '../../store/theme.store';

interface TopbarProps {
  title: string;
}

export const Topbar: React.FC<TopbarProps> = ({ title }) => {
  const user = useAuthStore((s) => s.user);
  const logout = useLogout();
  const isDark = useThemeStore((s) => s.isDark);
  const toggle = useThemeStore((s) => s.toggle);

  return (
    <header className="h-16 bg-white dark:bg-slate-900 border-b border-gray-100 dark:border-slate-800 flex items-center justify-between px-6 shadow-sm dark:shadow-slate-900/50 sticky top-0 z-20 transition-colors duration-300">
      <h1 className="text-xl font-semibold text-gray-800 dark:text-slate-100">{title}</h1>

      <div className="flex items-center gap-3">
        {/* Theme toggle */}
        <button
          onClick={toggle}
          title={isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
          className="w-9 h-9 flex items-center justify-center rounded-xl text-gray-500 dark:text-slate-400 hover:bg-gray-100 dark:hover:bg-slate-800 transition-colors"
        >
          {isDark ? (
            /* Sun icon */
            <svg className="w-5 h-5 text-amber-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364-6.364l-.707.707M6.343 17.657l-.707.707M17.657 17.657l-.707-.707M6.343 6.343l-.707-.707M12 8a4 4 0 100 8 4 4 0 000-8z" />
            </svg>
          ) : (
            /* Moon icon */
            <svg className="w-5 h-5 text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
            </svg>
          )}
        </button>

        {/* Divider */}
        <div className="w-px h-6 bg-gray-200 dark:bg-slate-700" />

        {/* User info */}
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-blue-100 dark:bg-indigo-900/60 rounded-full flex items-center justify-center">
            <span className="text-sm font-semibold text-blue-600 dark:text-indigo-400">
              {user?.firstName?.charAt(0).toUpperCase() ?? 'U'}
            </span>
          </div>
          <span className="text-sm font-medium text-gray-700 dark:text-slate-300 hidden sm:inline">
            {user ? `${user.firstName} ${user.lastName}` : 'User'}
          </span>
        </div>

        {/* Logout */}
        <button
          onClick={logout}
          className="flex items-center gap-1.5 px-3 py-1.5 text-sm text-red-500 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-colors"
        >
          <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
          </svg>
          <span className="hidden sm:inline">Logout</span>
        </button>
      </div>
    </header>
  );
};
