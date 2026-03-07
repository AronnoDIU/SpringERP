import React, { useState } from 'react';
import { Icons } from '../../components/Icons';
import { Customer } from '../../types';
import { generateCustomerEmail } from '../../services/geminiService';

const mockCustomers: Customer[] = [
  { id: 1, firstName: 'Alice', lastName: 'Freeman', companyName: 'Global Tech', email: 'alice@globaltech.com', stage: 'Negotiation', lastContact: '2 days ago', value: 45000 },
  { id: 2, firstName: 'Bob', lastName: 'Smith', companyName: 'Smith Logistics', email: 'bob@smithlog.com', stage: 'Lead', lastContact: '1 week ago', value: 12000 },
  { id: 3, firstName: 'Charlie', lastName: 'Davis', companyName: 'Davis Retail', email: 'charlie@davisretail.com', stage: 'Closed', lastContact: 'Yesterday', value: 8500 },
  { id: 4, firstName: 'Diana', lastName: 'Prince', companyName: 'Themyscira Inc', email: 'diana@amazon.com', stage: 'Lead', lastContact: '3 days ago', value: 95000 },
  { id: 5, firstName: 'Evan', lastName: 'Wright', companyName: 'WriteSolutions', email: 'evan@write.com', stage: 'Negotiation', lastContact: '5 hours ago', value: 2400 },
];

const CRM: React.FC = () => {
  const [viewMode, setViewMode] = useState<'list' | 'kanban'>('kanban');
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [emailContext, setEmailContext] = useState('');
  const [generatedDraft, setGeneratedDraft] = useState('');
  const [loading, setLoading] = useState(false);

  const handleGenerateEmail = async () => {
    if (!selectedCustomer) return;
    setLoading(true);
    const draft = await generateCustomerEmail(selectedCustomer, emailContext || 'Follow up on previous discussion');
    setGeneratedDraft(draft);
    setLoading(false);
  };

  const getStageColor = (stage: string) => {
    switch(stage) {
      case 'Lead': return 'bg-blue-50 dark:bg-blue-900/30 border-blue-200 dark:border-blue-800 text-blue-700 dark:text-blue-300';
      case 'Negotiation': return 'bg-amber-50 dark:bg-amber-900/30 border-amber-200 dark:border-amber-800 text-amber-700 dark:text-amber-300';
      case 'Closed': return 'bg-emerald-50 dark:bg-emerald-900/30 border-emerald-200 dark:border-emerald-800 text-emerald-700 dark:text-emerald-300';
      default: return 'bg-slate-50 dark:bg-slate-800 border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-300';
    }
  };

  return (
    <div className="space-y-6 h-[calc(100vh-140px)] flex flex-col">
      <div className="flex justify-between items-center shrink-0">
        <div>
          <h2 className="text-2xl font-bold text-slate-800 dark:text-white">Customer Pipeline</h2>
          <p className="text-slate-500 dark:text-slate-400">Manage deals and track progress.</p>
        </div>
        <div className="flex bg-white dark:bg-slate-800 rounded-lg border border-slate-200 dark:border-slate-700 p-1">
           <button 
             onClick={() => setViewMode('kanban')} 
             className={`flex items-center gap-2 px-3 py-1.5 rounded text-sm font-medium transition-colors ${viewMode === 'kanban' ? 'bg-indigo-50 dark:bg-indigo-900/50 text-indigo-600 dark:text-indigo-400' : 'text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700'}`}
           >
             <Icons.Kanban size={16} /> Board
           </button>
           <button 
             onClick={() => setViewMode('list')} 
             className={`flex items-center gap-2 px-3 py-1.5 rounded text-sm font-medium transition-colors ${viewMode === 'list' ? 'bg-indigo-50 dark:bg-indigo-900/50 text-indigo-600 dark:text-indigo-400' : 'text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-700'}`}
           >
             <Icons.List size={16} /> List
           </button>
         </div>
      </div>

      <div className="flex-1 min-h-0 grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Main Content Area (Kanban or List) */}
        <div className="lg:col-span-2 flex flex-col min-h-0">
          {viewMode === 'list' ? (
            <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-100 dark:border-slate-700 overflow-hidden flex-1 overflow-auto">
              <table className="w-full text-left text-sm">
                <thead className="bg-slate-50 dark:bg-slate-900/50 border-b border-slate-100 dark:border-slate-700 sticky top-0 z-10">
                  <tr>
                    <th className="px-6 py-4 font-semibold text-slate-600 dark:text-slate-400">Contact</th>
                    <th className="px-6 py-4 font-semibold text-slate-600 dark:text-slate-400">Company</th>
                    <th className="px-6 py-4 font-semibold text-slate-600 dark:text-slate-400">Value</th>
                    <th className="px-6 py-4 font-semibold text-slate-600 dark:text-slate-400">Stage</th>
                    <th className="px-6 py-4 font-semibold text-slate-600 dark:text-slate-400">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 dark:divide-slate-700">
                  {mockCustomers.map((c) => (
                    <tr key={c.id} className="hover:bg-slate-50 dark:hover:bg-slate-700/50 transition-colors">
                      <td className="px-6 py-4">
                        <div className="font-medium text-slate-900 dark:text-white">{c.firstName} {c.lastName}</div>
                        <div className="text-xs text-slate-500 dark:text-slate-500">{c.email}</div>
                      </td>
                      <td className="px-6 py-4 text-slate-600 dark:text-slate-400">{c.companyName}</td>
                      <td className="px-6 py-4 font-medium text-slate-900 dark:text-white">${(c.value ?? 0).toLocaleString()}</td>
                      <td className="px-6 py-4">
                        <span className={`px-2 py-1 rounded text-xs font-medium border ${getStageColor(c.stage ?? '')}`}>
                          {c.stage}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <button 
                          onClick={() => { setSelectedCustomer(c); setGeneratedDraft(''); setEmailContext(''); }}
                          className="text-indigo-600 dark:text-indigo-400 hover:text-indigo-800 dark:hover:text-indigo-300 font-medium text-xs flex items-center gap-1"
                        >
                          <Icons.Email size={14} /> Draft
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="flex gap-4 h-full overflow-x-auto pb-4">
              {['Lead', 'Negotiation', 'Closed'].map((stage) => (
                <div key={stage} className="flex-1 min-w-[280px] bg-slate-100 dark:bg-slate-900/50 rounded-xl p-3 flex flex-col">
                  <div className="flex justify-between items-center mb-3 px-1">
                    <h3 className="font-semibold text-slate-700 dark:text-slate-300 text-sm uppercase tracking-wide">{stage}</h3>
                    <span className="bg-slate-200 dark:bg-slate-700 text-slate-600 dark:text-slate-300 text-xs font-bold px-2 py-0.5 rounded-full">
                      {mockCustomers.filter(c => c.stage === stage).length}
                    </span>
                  </div>
                  <div className="flex-1 overflow-y-auto space-y-3 pr-1 custom-scrollbar">
                    {mockCustomers.filter(c => c.stage === stage).map(c => (
                        <div
                        key={c.id}
                        onClick={() => { setSelectedCustomer(c); setGeneratedDraft(''); setEmailContext(''); }}
                        className={`bg-white dark:bg-slate-800 p-4 rounded-lg shadow-sm border border-slate-200 dark:border-slate-700 cursor-pointer transition-all hover:shadow-md ${selectedCustomer?.id === c.id ? 'ring-2 ring-indigo-500 ring-offset-1 dark:ring-offset-slate-900' : ''}`}
                      >
                        <div className="flex justify-between items-start mb-2">
                          <h4 className="font-bold text-slate-800 dark:text-slate-200 text-sm">{c.companyName}</h4>
                          <button className="text-slate-300 dark:text-slate-500 hover:text-slate-500 dark:hover:text-slate-300"><Icons.Menu size={14} /></button>
                        </div>
                        <p className="text-xs text-slate-500 dark:text-slate-400 mb-3">{c.firstName} {c.lastName}</p>
                        <div className="flex justify-between items-center">
                          <span className="text-sm font-bold text-slate-900 dark:text-white">${(c.value ?? 0).toLocaleString()}</span>
                          <div className="w-6 h-6 rounded-full bg-indigo-100 dark:bg-indigo-900 text-indigo-600 dark:text-indigo-400 flex items-center justify-center text-xs font-bold">
                            {c.firstName.charAt(0)}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* AI Email Writer Panel - Sticky */}
        <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-100 dark:border-slate-700 p-6 flex flex-col h-full overflow-hidden">
          <div className="flex items-center gap-2 mb-4 text-indigo-700 dark:text-indigo-400 shrink-0">
            <Icons.AI size={20} />
            <h3 className="font-bold">AI Communications</h3>
          </div>
          
          {selectedCustomer ? (
            <div className="flex-1 flex flex-col space-y-4 overflow-hidden">
              <div className="text-sm text-slate-600 dark:text-slate-400 shrink-0">
                Drafting for: <span className="font-semibold text-slate-900 dark:text-white">{selectedCustomer.firstName} {selectedCustomer.lastName}</span>
                <div className="text-xs text-slate-400 dark:text-slate-500">{selectedCustomer.companyName} • {selectedCustomer.stage}</div>
              </div>
              
              <div className="shrink-0">
                <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 mb-1">Context / Topic</label>
                <textarea 
                  value={emailContext}
                  onChange={(e) => setEmailContext(e.target.value)}
                  placeholder="E.g., Checking in on the contract..."
                  className="w-full border border-slate-200 dark:border-slate-600 rounded-lg p-3 text-sm focus:ring-2 focus:ring-indigo-500 focus:outline-none bg-slate-50 dark:bg-slate-900 text-slate-900 dark:text-slate-200 placeholder:text-slate-400"
                  rows={3}
                />
              </div>

              <button 
                onClick={handleGenerateEmail}
                disabled={loading}
                className="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-2 rounded-lg text-sm font-medium transition-colors disabled:opacity-50 shrink-0 shadow-sm"
              >
                {loading ? (
                  <span className="flex items-center justify-center gap-2"><span className="animate-spin w-4 h-4 border-2 border-white border-t-transparent rounded-full"></span> Writing...</span>
                ) : 'Generate Draft'}
              </button>

              {generatedDraft && (
                <div className="mt-4 flex-1 flex flex-col min-h-0">
                  <label className="block text-xs font-semibold text-slate-500 dark:text-slate-400 mb-1">Generated Content</label>
                  <div className="w-full bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-600 rounded-lg p-4 text-sm text-slate-800 dark:text-slate-200 flex-1 overflow-y-auto custom-scrollbar leading-relaxed whitespace-pre-wrap">
                    {generatedDraft}
                  </div>
                  <div className="mt-3 flex gap-2 shrink-0">
                    <button className="flex-1 bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-600 hover:bg-slate-50 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 py-1.5 rounded text-xs font-medium">Copy</button>
                    <button className="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white py-1.5 rounded text-xs font-medium">Send</button>
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="flex-1 flex flex-col items-center justify-center text-slate-400 dark:text-slate-500 text-sm text-center">
              <div className="w-16 h-16 bg-slate-50 dark:bg-slate-900 rounded-full flex items-center justify-center mb-3">
                 <Icons.Email size={24} className="text-slate-300 dark:text-slate-600" />
              </div>
              <p>Select a deal from the pipeline to draft a personalized email.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CRM;
