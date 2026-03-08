import React from 'react';

interface BadgeProps {
  label?: string;
  children?: React.ReactNode;
  variant?: 'success' | 'warning' | 'danger' | 'info' | 'default';
}

const variantClasses: Record<string, string> = {
  success: 'bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-400',
  warning: 'bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-400',
  danger: 'bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400',
  info: 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400',
  default: 'bg-gray-100 dark:bg-slate-700 text-gray-700 dark:text-slate-300',
};

export const Badge: React.FC<BadgeProps> = ({ label, children, variant = 'default' }) => (
  <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${variantClasses[variant]}`}>
    {children ?? label}
  </span>
);
