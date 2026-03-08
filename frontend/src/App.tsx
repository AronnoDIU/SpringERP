import React, { Suspense, lazy } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { AppLayout } from './components/layout/AppLayout';
import { ProtectedRoute } from './components/common/ProtectedRoute';
import { ErrorBoundary } from './components/common/ErrorBoundary';
import { ThemeProvider } from './components/common/ThemeProvider';
import { LoginPage } from './features/auth/LoginPage';

// Lazy-loaded feature pages for code splitting
const DashboardPage = lazy(() =>
  import('./features/dashboard/DashboardPage').then((m) => ({ default: m.DashboardPage }))
);
const CEODashboardPage = lazy(() =>
  import('./features/dashboard/CEODashboardPage').then((m) => ({ default: m.CEODashboardPage }))
);
const CustomersPage = lazy(() =>
  import('./features/customers/CustomersPage').then((m) => ({ default: m.CustomersPage }))
);
const SuppliersPage = lazy(() =>
  import('./features/suppliers/SuppliersPage').then((m) => ({ default: m.SuppliersPage }))
);
const ProductsPage = lazy(() =>
  import('./features/products/ProductsPage').then((m) => ({ default: m.ProductsPage }))
);
const InvoicesPage = lazy(() =>
  import('./features/invoices/InvoicesPage').then((m) => ({ default: m.InvoicesPage }))
);
const EmployeesPage = lazy(() =>
  import('./features/employees/EmployeesPage').then((m) => ({ default: m.EmployeesPage }))
);
const AccountingPage = lazy(() =>
  import('./features/accounting/AccountingPage').then((m) => ({ default: m.AccountingPage }))
);
const InventoryPage = lazy(() =>
  import('./features/inventory/InventoryPage').then((m) => ({ default: m.InventoryPage }))
);
const SalesPage = lazy(() =>
  import('./features/sales/CRM')
);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 2, // 2 minutes
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

const PageLoader: React.FC = () => (
  <div className="flex items-center justify-center h-64">
    <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-blue-600" />
  </div>
);

const SafeSuspense: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <ErrorBoundary>
    <Suspense fallback={<PageLoader />}>{children}</Suspense>
  </ErrorBoundary>
);

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider>
        <BrowserRouter>
          <Routes>
            {/* Public */}
            <Route path="/login" element={<LoginPage />} />

            {/* Protected */}
            <Route element={<ProtectedRoute />}>
              <Route element={<AppLayout />}>
                <Route index element={<Navigate to="/dashboard" replace />} />
                <Route path="/dashboard" element={<SafeSuspense><DashboardPage /></SafeSuspense>} />
                <Route path="/ceo" element={<SafeSuspense><CEODashboardPage /></SafeSuspense>} />
                <Route path="/customers" element={<SafeSuspense><CustomersPage /></SafeSuspense>} />
                <Route path="/suppliers" element={<SafeSuspense><SuppliersPage /></SafeSuspense>} />
                <Route path="/products" element={<SafeSuspense><ProductsPage /></SafeSuspense>} />
                <Route path="/invoices" element={<SafeSuspense><InvoicesPage /></SafeSuspense>} />
                <Route path="/employees" element={<SafeSuspense><EmployeesPage /></SafeSuspense>} />
                <Route path="/accounting" element={<SafeSuspense><AccountingPage /></SafeSuspense>} />
                <Route path="/inventory" element={<SafeSuspense><InventoryPage /></SafeSuspense>} />
                <Route path="/sales" element={<SafeSuspense><SalesPage /></SafeSuspense>} />
              </Route>
            </Route>

            {/* Fallback */}
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </BrowserRouter>

        <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          closeOnClick
          pauseOnHover
          draggable
          theme="colored"
        />

        {import.meta.env.DEV && <ReactQueryDevtools initialIsOpen={false} />}
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App;

