import React, { Suspense, lazy } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { AppLayout } from './components/layout/AppLayout';
import { ProtectedRoute } from './components/common/ProtectedRoute';
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

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          {/* Public */}
          <Route path="/login" element={<LoginPage />} />

          {/* Protected */}
          <Route element={<ProtectedRoute />}>
            <Route element={<AppLayout />}>
              <Route index element={<Navigate to="/dashboard" replace />} />
              <Route
                path="/dashboard"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <DashboardPage />
                  </Suspense>
                }
              />
              <Route
                path="/ceo"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <CEODashboardPage />
                  </Suspense>
                }
              />
              <Route
                path="/customers"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <CustomersPage />
                  </Suspense>
                }
              />
              <Route
                path="/suppliers"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <SuppliersPage />
                  </Suspense>
                }
              />
              <Route
                path="/products"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <ProductsPage />
                  </Suspense>
                }
              />
              <Route
                path="/invoices"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <InvoicesPage />
                  </Suspense>
                }
              />
              <Route
                path="/employees"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <EmployeesPage />
                  </Suspense>
                }
              />
              <Route
                path="/accounting"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <AccountingPage />
                  </Suspense>
                }
              />
              <Route
                path="/inventory"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <InventoryPage />
                  </Suspense>
                }
              />
              <Route
                path="/sales"
                element={
                  <Suspense fallback={<PageLoader />}>
                    <SalesPage />
                  </Suspense>
                }
              />
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
        theme="light"
      />

      {import.meta.env.DEV && <ReactQueryDevtools initialIsOpen={false} />}
    </QueryClientProvider>
  );
};

export default App;

