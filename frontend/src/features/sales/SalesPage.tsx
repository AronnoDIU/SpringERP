import React from 'react';
import CRM from './CRM';

const SalesPage: React.FC<{ subModule: string }> = ({ subModule }) => {
  if (subModule === 'CRM') return <CRM />;
  return (
    <div className="p-8 animate-fade-in glass rounded-[2rem] border border-white/5">
      <h1 className="text-3xl font-black text-slate-900 dark:text-white">Sales & Markets Module</h1>
      <p className="text-slate-500 dark:text-slate-400 mt-2">Active view: {subModule}</p>
      <div className="mt-8 opacity-50">
         Data stream processing for {subModule}...
      </div>
    </div>
  );
};

export default SalesPage;
