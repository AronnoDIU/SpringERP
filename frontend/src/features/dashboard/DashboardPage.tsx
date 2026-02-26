import React from 'react';
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from 'recharts';
import { StatCard } from '../../components/common/StatCard';
import { useCustomers } from '../../hooks/useCustomers';
import { useSuppliers } from '../../hooks/useSuppliers';
import { useProducts } from '../../hooks/useProducts';
import { useInvoices } from '../../hooks/useInvoices';
import { useEmployees } from '../../hooks/useEmployees';

const revenueData = [
  { month: 'Aug', revenue: 42000, expenses: 28000 },
  { month: 'Sep', revenue: 55000, expenses: 32000 },
  { month: 'Oct', revenue: 48000, expenses: 30000 },
  { month: 'Nov', revenue: 61000, expenses: 35000 },
  { month: 'Dec', revenue: 73000, expenses: 41000 },
  { month: 'Jan', revenue: 58000, expenses: 33000 },
  { month: 'Feb', revenue: 67000, expenses: 38000 },
];

export const DashboardPage: React.FC = () => {
  const { data: customers } = useCustomers();
  const { data: suppliers } = useSuppliers();
  const { data: products } = useProducts();
  const { data: invoices } = useInvoices(0, 100);
  const { data: employees } = useEmployees();

  const totalInvoices = invoices?.totalElements ?? 0;
  const pendingInvoices = invoices?.content?.filter((i) => i.status === 'PENDING').length ?? 0;
  const overdueInvoices = invoices?.content?.filter((i) => i.status === 'OVERDUE').length ?? 0;
  const totalRevenue = invoices?.content
    ?.filter((i) => i.status === 'PAID')
    .reduce((sum, i) => sum + (i.totalAmount ?? 0), 0) ?? 0;

  return (
    <div className="space-y-6">
      {/* Stats Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Total Revenue"
          value={`$${totalRevenue.toLocaleString()}`}
          color="blue"
          trend={{ value: 12.5, label: 'vs last month' }}
          icon={
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          }
        />
        <StatCard
          title="Total Customers"
          value={customers?.length ?? 0}
          color="green"
          trend={{ value: 8.2, label: 'vs last month' }}
          icon={
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
          }
        />
        <StatCard
          title="Total Invoices"
          value={totalInvoices}
          color="purple"
          icon={
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
            </svg>
          }
        />
        <StatCard
          title="Total Employees"
          value={employees?.length ?? 0}
          color="yellow"
          icon={
            <svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          }
        />
      </div>

      {/* Second row */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <StatCard
          title="Pending Invoices"
          value={pendingInvoices}
          color="yellow"
          icon={<svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 8v4l3 3" /><circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth={1.5} /></svg>}
        />
        <StatCard
          title="Overdue Invoices"
          value={overdueInvoices}
          color="red"
          icon={<svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" /></svg>}
        />
        <StatCard
          title="Total Products"
          value={products?.length ?? 0}
          color="blue"
          icon={<svg className="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10" /></svg>}
        />
      </div>

      {/* Revenue Chart */}
      <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">
        <h2 className="text-base font-semibold text-gray-800 mb-4">Revenue vs Expenses (Last 7 Months)</h2>
        <ResponsiveContainer width="100%" height={280}>
          <AreaChart data={revenueData}>
            <defs>
              <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.2} />
                <stop offset="95%" stopColor="#3b82f6" stopOpacity={0} />
              </linearGradient>
              <linearGradient id="colorExpenses" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#ef4444" stopOpacity={0.15} />
                <stop offset="95%" stopColor="#ef4444" stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
            <XAxis dataKey="month" tick={{ fontSize: 12 }} tickLine={false} />
            <YAxis tick={{ fontSize: 12 }} tickLine={false} axisLine={false}
              tickFormatter={(v) => `$${(v / 1000).toFixed(0)}k`} />
            <Tooltip formatter={(value: number) => [`$${value.toLocaleString()}`, '']} />
            <Area type="monotone" dataKey="revenue" name="Revenue" stroke="#3b82f6" strokeWidth={2}
              fill="url(#colorRevenue)" />
            <Area type="monotone" dataKey="expenses" name="Expenses" stroke="#ef4444" strokeWidth={2}
              fill="url(#colorExpenses)" />
          </AreaChart>
        </ResponsiveContainer>
      </div>

      {/* Bottom row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        {/* Recent Invoices */}
        <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">
          <h2 className="text-base font-semibold text-gray-800 mb-4">Recent Invoices</h2>
          <div className="space-y-3">
            {(invoices?.content ?? []).slice(0, 5).map((inv) => (
              <div key={inv.id} className="flex items-center justify-between py-2 border-b border-gray-50 last:border-0">
                <div>
                  <p className="text-sm font-medium text-gray-700">{inv.invoiceNumber ?? `INV-${inv.id}`}</p>
                  <p className="text-xs text-gray-400">{inv.customerName}</p>
                </div>
                <div className="text-right">
                  <p className="text-sm font-semibold text-gray-800">${(inv.totalAmount ?? 0).toLocaleString()}</p>
                  <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${
                    inv.status === 'PAID' ? 'bg-green-100 text-green-700' :
                    inv.status === 'OVERDUE' ? 'bg-red-100 text-red-700' :
                    'bg-yellow-100 text-yellow-700'
                  }`}>{inv.status}</span>
                </div>
              </div>
            ))}
            {!invoices?.content?.length && (
              <p className="text-sm text-gray-400 text-center py-4">No invoices yet</p>
            )}
          </div>
        </div>

        {/* Quick Stats */}
        <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6">
          <h2 className="text-base font-semibold text-gray-800 mb-4">Quick Overview</h2>
          <div className="space-y-4">
            {[
              { label: 'Customers', value: customers?.length ?? 0, max: 200, color: 'bg-blue-500' },
              { label: 'Suppliers', value: suppliers?.length ?? 0, max: 100, color: 'bg-green-500' },
              { label: 'Products', value: products?.length ?? 0, max: 500, color: 'bg-purple-500' },
              { label: 'Employees', value: employees?.length ?? 0, max: 300, color: 'bg-yellow-500' },
            ].map((item) => (
              <div key={item.label}>
                <div className="flex justify-between mb-1">
                  <span className="text-sm text-gray-600">{item.label}</span>
                  <span className="text-sm font-semibold text-gray-800">{item.value}</span>
                </div>
                <div className="w-full bg-gray-100 rounded-full h-2">
                  <div
                    className={`${item.color} h-2 rounded-full transition-all duration-500`}
                    style={{ width: `${Math.min((item.value / item.max) * 100, 100)}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

