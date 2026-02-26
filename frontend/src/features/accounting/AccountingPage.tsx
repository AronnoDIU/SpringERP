import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { accountingApi } from '../../api/accounting.api';
import { Table } from '../../components/common/Table';
import { Badge } from '../../components/common/Badge';
import type { ChartOfAccount, AccountType } from '../../types';

const accountTypeBadge = (type: AccountType) => {
  const map: Record<AccountType, 'success' | 'info' | 'warning' | 'danger' | 'default'> = {
    ASSET: 'success',
    LIABILITY: 'danger',
    EQUITY: 'info',
    REVENUE: 'success',
    EXPENSE: 'warning',
  };
  return map[type];
};

export const AccountingPage: React.FC = () => {
  const { data: accounts, isLoading } = useQuery({
    queryKey: ['chart-of-accounts'],
    queryFn: accountingApi.getChartOfAccounts,
  });

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-lg font-semibold text-gray-800">Chart of Accounts</h2>
          <p className="text-sm text-gray-500">{accounts?.length ?? 0} accounts</p>
        </div>
      </div>

      {/* Account Type Summary */}
      <div className="grid grid-cols-2 sm:grid-cols-5 gap-3">
        {(['ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE'] as AccountType[]).map((type) => {
          const count = accounts?.filter((a) => a.accountType === type).length ?? 0;
          return (
            <div key={type} className="bg-white rounded-xl border border-gray-100 p-4 text-center shadow-sm">
              <p className="text-xs text-gray-500 font-medium mb-1">{type}</p>
              <p className="text-2xl font-bold text-gray-800">{count}</p>
            </div>
          );
        })}
      </div>

      <Table<ChartOfAccount>
        isLoading={isLoading}
        data={accounts ?? []}
        keyExtractor={(a) => a.id}
        columns={[
          { key: 'accountCode', header: 'Code', render: (a) => <span className="font-mono text-sm font-medium text-blue-600">{a.accountCode}</span> },
          { key: 'accountName', header: 'Account Name', render: (a) => <span className="font-medium text-gray-800">{a.accountName}</span> },
          { key: 'accountType', header: 'Type', render: (a) => <Badge label={a.accountType} variant={accountTypeBadge(a.accountType)} /> },
          { key: 'description', header: 'Description' },
          {
            key: 'balance', header: 'Balance',
            render: (a) => (
              <span className={`font-semibold ${(a.balance ?? 0) < 0 ? 'text-red-600' : 'text-gray-800'}`}>
                ${(a.balance ?? 0).toLocaleString()}
              </span>
            ),
          },
          {
            key: 'isActive', header: 'Status',
            render: (a) => <Badge label={a.isActive ? 'Active' : 'Inactive'} variant={a.isActive ? 'success' : 'default'} />,
          },
        ]}
      />
    </div>
  );
};

