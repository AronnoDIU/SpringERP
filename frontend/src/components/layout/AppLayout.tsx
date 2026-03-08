import React from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { Topbar } from './Topbar';

const pageTitles: Record<string, string> = {
  '/dashboard': 'Dashboard',
  '/ceo': 'CEO Dashboard',
  '/customers': 'Customers',
  '/suppliers': 'Suppliers',
  '/products': 'Products',
  '/invoices': 'Invoices',
  '/employees': 'Employees',
  '/accounting': 'Accounting',
  '/inventory': 'Inventory',
  '/sales': 'Sales / CRM',
};

export const AppLayout: React.FC = () => {
  const { pathname } = useLocation();
  const title = pageTitles[pathname] ?? 'SpringERP';

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-slate-950 overflow-hidden transition-colors duration-300">
      <Sidebar />
      <div className="flex-1 ml-64 flex flex-col overflow-hidden">
        <Topbar title={title} />
        <main className="flex-1 overflow-y-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

