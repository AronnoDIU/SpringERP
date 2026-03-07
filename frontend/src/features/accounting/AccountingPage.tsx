import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { accountingApi } from '../../api/accounting.api';
import { Table } from '../../components/common/Table';
import { Badge } from '../../components/common/Badge';
import { StatCard } from '../../components/common/StatCard';
import type { ChartOfAccount } from '../../types';

export const AccountingPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'accounts' | 'journal'>('accounts');

  const { data: accounts, isLoading } = useQuery({
    queryKey: ['chart-of-accounts'],
    queryFn: accountingApi.getChartOfAccounts,
  });

  const totalAssets = accounts
    ?.filter((a) => a.accountType === 'ASSET')
    .reduce((s, a) => s + (a.currentBalance ?? 0), 0) ?? 0;

  const totalLiabilities = accounts
    ?.filter((a) => a.accountType === 'LIABILITY')
    .reduce((s, a) => s + (a.currentBalance ?? 0), 0) ?? 0;

  const totalRevenue = accounts
    ?.filter((a) => a.accountType === 'REVENUE')
    .reduce((s, a) => s + (a.currentBalance ?? 0), 0) ?? 0;

  const accountTypeColor = (type: ChartOfAccount['accountType']) => {
    const map: Record<string, 'success' | 'warning' | 'danger' | 'info' | 'default'> = {
      ASSET: 'success',
      LIABILITY: 'danger',
      EQUITY: 'info',
      REVENUE: 'success',
      EXPENSE: 'warning',
    };
    return map[type] ?? 'default';
  };

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Accounting</h2>
          <p className="text-sm text-gray-500">Chart of Accounts & Journal Entries</p>
        </div>
      </div>

      {/* Summary Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <StatCard title="Total Assets" value={`$${totalAssets.toLocaleString()}`} icon="💰" color="green" />
        <StatCard title="Total Liabilities" value={`$${totalLiabilities.toLocaleString()}`} icon="📋" color="red" />
        <StatCard title="Total Revenue" value={`$${totalRevenue.toLocaleString()}`} icon="📈" color="blue" />
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="flex gap-6">
          {(['accounts', 'journal'] as const).map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`pb-2 text-sm font-medium border-b-2 transition-colors ${
                activeTab === tab
                  ? 'border-blue-600 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              {tab === 'accounts' ? 'Chart of Accounts' : 'Journal Entries'}
            </button>
          ))}
        </nav>
      </div>

      {activeTab === 'accounts' && (
        <Table<ChartOfAccount>
          isLoading={isLoading}
          data={accounts ?? []}
          keyExtractor={(a) => a.id}
          columns={[
            { key: 'accountCode', header: 'Code' },
            { key: 'accountName', header: 'Account Name', render: (a) => <span className="font-medium">{a.accountName}</span> },
            {
              key: 'accountType', header: 'Type',
              render: (a) => <Badge label={a.accountType} variant={accountTypeColor(a.accountType)} />,
            },
            { key: 'accountCategory', header: 'Category', render: (a) => a.accountCategory ?? '—' },
            {
              key: 'currentBalance', header: 'Balance',
              render: (a) => a.currentBalance != null ? `$${a.currentBalance.toLocaleString()}` : '—',
            },
            {
              key: 'isActive', header: 'Status',
              render: (a) => <Badge label={a.isActive ? 'Active' : 'Inactive'} variant={a.isActive ? 'success' : 'default'} />,
            },
          ]}
        />
      )}

      {activeTab === 'journal' && (
        <div className="bg-white rounded-xl p-8 text-center border border-gray-100">
          <div className="w-16 h-16 bg-blue-50 text-blue-600 rounded-full flex items-center justify-center mx-auto mb-4 text-3xl">
            📒
          </div>
          <h3 className="text-xl font-bold text-gray-800 mb-2">Journal Entries</h3>
          <p className="text-gray-500">Double-entry bookkeeping journal entries will appear here.</p>
        </div>
      )}
    </div>
  );
};

