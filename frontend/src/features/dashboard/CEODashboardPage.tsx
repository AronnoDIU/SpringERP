import React, { useState, useEffect } from 'react';
import { 
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer
} from 'recharts';
import { Icons } from '../../components/Icons';
import { analyzeFinancialHealth } from '../../services/geminiService';

const revenueData = [
  { name: 'Jan', value: 4.2 },
  { name: 'Feb', value: 3.8 },
  { name: 'Mar', value: 5.1 },
  { name: 'Apr', value: 4.9 },
  { name: 'May', value: 6.2 },
  { name: 'Jun', value: 7.5 },
];

export const CEODashboardPage: React.FC = () => {
  const [aiBriefing, setAiBriefing] = useState<string>('');
  const [isSyncing, setIsSyncing] = useState(false);

  const triggerAiBriefing = async () => {
    setIsSyncing(true);
    // Mocking high-level strategic data for the prompt
    const summary = await analyzeFinancialHealth([
      { id: '1', date: '2025', description: 'Annual Growth', amount: 7500000, type: 'Income', category: 'Global' }
    ]);
    setAiBriefing(summary);
    setIsSyncing(false);
  };

  useEffect(() => {
    triggerAiBriefing();
  }, []);

  return (
    <div className="space-y-8 animate-[fadeUp_0.8s_ease-out_forwards] pb-12">
      {/* Executive Header */}
      <div className="flex flex-col lg:flex-row justify-between items-start lg:items-end gap-6 border-b border-slate-200 dark:border-white/5 pb-8">
        <div>
          <div className="flex items-center gap-2 text-indigo-500 font-black text-[10px] uppercase tracking-[0.3em] mb-2">
            <Icons.Shield size={14} /> Level 5 Authorization
          </div>
          <h2 className="text-5xl font-black tracking-tighter text-slate-900 dark:text-white">Strategic Command</h2>
          <p className="text-slate-500 font-medium mt-1">Real-time valuation and global asset intelligence.</p>
        </div>
        <div className="flex gap-3">
           <button className="glass px-6 py-3 rounded-2xl font-bold text-sm flex items-center gap-2 hover:bg-white/10 transition-all text-slate-600 dark:text-slate-300">
              <Icons.Download size={18} /> Export LPs
           </button>
           <button onClick={triggerAiBriefing} className="bg-indigo-600 hover:bg-indigo-500 text-white font-black text-sm px-8 py-3 rounded-2xl transition-all shadow-xl shadow-indigo-500/20 active:scale-95">
              Refresh Intelligence
           </button>
        </div>
      </div>

      {/* Primary KPI Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {[
          { label: 'Market Valuation', value: '$128.4M', change: '+14.2%', icon: Icons.Analytics, trend: 'up' },
          { label: 'Net Liquidity', value: '$14.2M', change: '+2.1%', icon: Icons.Finance, trend: 'up' },
          { label: 'OpEx Efficiency', value: '92.4%', change: '-0.8%', icon: Icons.Activity, trend: 'neutral' },
          { label: 'Global Reach', value: '42 Nodes', change: '+4', icon: Icons.Network, trend: 'up' },
        ].map((kpi, i) => (
          <div key={i} className="glass bg-white dark:bg-transparent p-6 rounded-[2rem] border-slate-200 dark:border-white/5 relative overflow-hidden group hover:border-indigo-500/30 transition-all">
             <div className="relative z-10">
                <div className="flex justify-between items-center mb-6">
                   <div className="p-3 bg-indigo-500/10 rounded-xl text-indigo-600 dark:text-indigo-400">
                      <kpi.icon size={22} />
                   </div>
                   <span className={`text-[10px] font-black px-2 py-1 rounded-lg ${kpi.trend === 'up' ? 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400' : 'bg-slate-500/10 text-slate-500 dark:text-slate-400'}`}>
                      {kpi.change}
                   </span>
                </div>
                <p className="text-[10px] font-black text-slate-500 uppercase tracking-widest mb-1">{kpi.label}</p>
                <h3 className="text-3xl font-black tracking-tight text-slate-900 dark:text-white">{kpi.value}</h3>
             </div>
             <div className="absolute -right-4 -bottom-4 opacity-[0.02] group-hover:opacity-[0.05] transition-opacity">
                <kpi.icon size={120} />
             </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Global Growth Vector */}
        <div className="lg:col-span-2 glass bg-white dark:bg-transparent p-8 rounded-[2.5rem] border-slate-200 dark:border-white/5 relative overflow-hidden">
           <div className="flex justify-between items-center mb-10">
              <div>
                 <h3 className="text-xl font-black text-slate-900 dark:text-white">Revenue Velocity</h3>
                 <p className="text-xs text-slate-500 font-bold uppercase tracking-widest mt-1">Global Aggregate ($M)</p>
              </div>
              <div className="flex gap-2">
                 <div className="flex items-center gap-2 text-[10px] font-black text-slate-500 dark:text-slate-400">
                    <div className="w-2 h-2 rounded-full bg-indigo-500"></div> Revenue
                 </div>
              </div>
           </div>
           <div className="h-[350px]">
              <ResponsiveContainer width="100%" height="100%">
                 <AreaChart data={revenueData}>
                    <defs>
                       <linearGradient id="ceoRev" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor="#6366f1" stopOpacity={0.3}/>
                          <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
                       </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="transparent" />
                    <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 10, fontWeight: 800}} />
                    <YAxis axisLine={false} tickLine={false} tick={{fill: '#64748b', fontSize: 10, fontWeight: 800}} />
                    <Tooltip 
                       contentStyle={{backgroundColor: '#0f172a', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '1rem'}}
                       itemStyle={{color: '#fff', fontSize: '12px', fontWeight: 'bold'}}
                    />
                    <Area type="monotone" dataKey="value" stroke="#6366f1" strokeWidth={4} fillOpacity={1} fill="url(#ceoRev)" />
                 </AreaChart>
              </ResponsiveContainer>
           </div>
        </div>

        {/* Gemini Strategic Intelligence */}
        <div className="glass p-8 rounded-[2.5rem] border-indigo-500/20 bg-indigo-500/5 relative overflow-hidden">
           <div className="absolute top-0 right-0 p-6 opacity-10">
              <Icons.AI size={80} className="text-indigo-400" />
           </div>
           <div className="relative z-10 flex flex-col h-full">
              <div className="flex items-center gap-3 mb-8">
                 <div className="w-12 h-12 bg-indigo-600 rounded-2xl flex items-center justify-center text-white shadow-lg shadow-indigo-600/30">
                    <Icons.AI size={24} />
                 </div>
                 <div>
                    <h3 className="text-lg font-black leading-tight text-slate-900 dark:text-white">Gemini Briefing</h3>
                    <p className="text-[10px] text-indigo-600 dark:text-indigo-400 font-bold uppercase tracking-widest">AI Strategic Counsel</p>
                 </div>
              </div>

              {isSyncing ? (
                 <div className="flex-1 flex flex-col items-center justify-center space-y-4">
                    <div className="w-10 h-10 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin"></div>
                    <p className="text-xs font-black text-indigo-600 dark:text-indigo-300 uppercase tracking-widest animate-pulse">Analyzing Nodes...</p>
                 </div>
              ) : (
                 <div className="flex-1 space-y-6">
                    <div className="p-4 bg-white/50 dark:bg-white/5 rounded-2xl border border-slate-200 dark:border-white/5 shadow-sm">
                       <p className="text-xs font-bold text-slate-600 dark:text-slate-400 leading-relaxed italic">
                         "Synthesized current operational patterns. Financial health remains robust with a 12.4% uptick in service-sector margins."
                       </p>
                    </div>
                    <div className="space-y-4">
                       <h4 className="text-[10px] font-black text-indigo-600 dark:text-indigo-400 uppercase tracking-[0.2em]">Priority Action Items</h4>
                       {aiBriefing.split('\n').filter(l => l.includes('-') || l.includes('•')).slice(0, 3).map((item, idx) => (
                         <div key={idx} className="flex gap-3 items-start">
                            <div className="w-1.5 h-1.5 rounded-full bg-indigo-500 mt-1.5 shrink-0"></div>
                            <p className="text-xs font-bold text-slate-700 dark:text-slate-300 leading-tight">{item.replace(/[-•]/g, '').trim()}</p>
                         </div>
                       ))}
                    </div>
                 </div>
              )}

              <button className="mt-8 w-full glass py-3 rounded-xl text-[10px] font-black uppercase tracking-widest text-indigo-700 dark:text-indigo-300 hover:bg-indigo-500/10 dark:hover:bg-indigo-500/20 transition-all border-slate-200 dark:border-white/5">
                 Request Full Audit
              </button>
           </div>
        </div>
      </div>
    </div>
  );
};
