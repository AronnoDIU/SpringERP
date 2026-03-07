import React from 'react';
import { Icons } from '../../components/Icons';
import { useAuthStore } from '../../store/auth.store';

// We inline the Company type for safety, or use existing from types if available
interface Company {
  id: string | number;
  name: string;
}

export const DashboardPage: React.FC = () => {
  const { user } = useAuthStore();
  
  // Use user's company information if available, otherwise fallback to default
  const company: Company = {
    id: 'CORE-SYN',
    name: 'Spring ERP',
    // You can swap this with real user.company data if it exists in your schema
  };

  return (
    <div className="space-y-14 pb-14">
      
      {/* Strategic Command Hero - $50M Standard */}
      <section className="relative h-[650px] w-full rounded-[4.5rem] overflow-hidden border border-slate-200 dark:border-white/5 shadow-[0_40px_100px_-30px_rgba(0,0,0,0.15)] dark:shadow-[0_40px_100px_-30px_rgba(0,0,0,0.8)] bg-slate-100 dark:bg-slate-950 group animate-fade-up">
        {/* Dynamic Multi-layered Environment */}
        <div className="absolute inset-0 bg-gradient-to-br from-indigo-50/50 via-slate-50 to-purple-50 dark:from-[#0a0f1e] dark:via-[#05070a] dark:to-[#120a1e]"></div>
        <div className="absolute top-[-25%] right-[-10%] w-[900px] h-[900px] bg-indigo-500/10 dark:bg-indigo-600/10 rounded-full blur-[180px] animate-pulse-slow"></div>
        <div className="absolute bottom-[-15%] left-[-10%] w-[700px] h-[700px] bg-purple-500/5 dark:bg-purple-600/10 rounded-full blur-[150px]"></div>
        <div className="absolute inset-0 bg-[repeating-linear-gradient(0deg,transparent,transparent_79px,rgba(99,102,241,0.05)_80px)] opacity-[0.4] pointer-events-none"></div>
        
        {/* Horizon Line Accent */}
        <div className="absolute top-0 left-0 w-full h-[1px] bg-gradient-to-r from-transparent via-indigo-600/30 dark:via-indigo-500/20 to-transparent"></div>
        
        <div className="relative z-10 grid lg:grid-cols-5 h-full items-center px-16 md:px-28">
          <div className="lg:col-span-3 space-y-12">
            <div className="inline-flex items-center gap-3.5 px-6 py-3 rounded-2xl glass border-slate-300 dark:border-indigo-500/10 text-indigo-700 dark:text-indigo-300 text-[11px] font-black uppercase tracking-[0.5em] shadow-sm">
              <span className="flex h-3 w-3 relative">
                <span className="animate-ping absolute h-full w-full rounded-full bg-indigo-500 opacity-75"></span>
                <span className="relative inline-flex rounded-full h-3 w-3 bg-indigo-600 dark:bg-indigo-500 shadow-[0_0_15px_rgba(99,102,241,1)]"></span>
              </span>
              ACTIVE NODE: {company.id} [VERIFIED]
            </div>
            
            <div className="space-y-8">
              <h1 className="text-8xl md:text-[9rem] font-black tracking-tighter text-slate-950 dark:text-white leading-[0.75] mb-4">
                {company.name.split(' ')[0]} <br/>
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-indigo-700 via-slate-500 to-indigo-950 dark:from-indigo-300 dark:via-white dark:to-indigo-700 text-glow-indigo">Nexus OS.</span>
              </h1>
              <p className="text-2xl text-slate-600 dark:text-slate-400 font-light max-w-2xl leading-relaxed">
                Autonomous orchestration of global strategic assets for <span className="font-bold text-slate-900 dark:text-slate-100">{company.name}</span>. <br/>
                <span className="text-slate-800 dark:text-slate-500 font-bold uppercase tracking-[0.3em] text-xs">Mission-Critical Intelligence at Scale.</span>
              </p>
            </div>

            <div className="flex gap-8">
               <button className="bg-slate-950 dark:bg-white text-white dark:text-slate-950 px-16 py-6 rounded-[2.5rem] font-black text-base hover:-translate-y-2 transition-all shadow-2xl active:scale-95 shadow-indigo-500/10">
                  Initialize Operations
               </button>
               <button className="glass border-slate-300 dark:border-white/10 px-12 py-6 rounded-[2.5rem] font-bold text-base text-slate-900 dark:text-white hover:bg-slate-200 dark:hover:bg-white/10 transition-all active:scale-95 shadow-sm">
                  Strategic Matrix
               </button>
            </div>
          </div>

          <div className="lg:col-span-2 hidden lg:flex justify-end relative items-center h-full">
             <div className="relative w-[500px] h-[500px] animate-[float_6s_ease-in-out_infinite]">
                <div className="absolute -inset-20 bg-indigo-600/10 dark:bg-indigo-500/10 blur-[120px] rounded-full"></div>
                
                {/* Tactical Parallax Panes */}
                <div className="glass absolute -top-14 right-0 p-12 rounded-[4rem] border-slate-300 dark:border-white/20 shadow-2xl z-20 w-80 rotate-3 group-hover:rotate-0 transition-all duration-1000">
                   <div className="flex justify-between items-center mb-12">
                      <div className="p-5 bg-indigo-600/10 dark:bg-indigo-500/10 rounded-3xl text-indigo-700 dark:text-indigo-300"><Icons.Finance size={32}/></div>
                      <span className="text-[11px] font-black text-emerald-600 dark:text-emerald-400 bg-emerald-500/10 px-3 py-1.5 rounded-xl">+18.4%</span>
                   </div>
                   <p className="text-[11px] font-black text-slate-400 dark:text-slate-600 uppercase tracking-[0.4em] mb-2">Portfolio Delta</p>
                   <h4 className="text-4xl font-black text-slate-950 dark:text-white">$24.8B</h4>
                </div>

                <div className="glass absolute -bottom-12 -left-12 p-12 rounded-[4rem] border-slate-300 dark:border-white/20 shadow-2xl z-10 w-80 -rotate-6 group-hover:rotate-0 transition-all duration-1000">
                   <div className="flex justify-between items-center mb-12">
                      <div className="p-5 bg-purple-600/10 dark:bg-purple-500/10 rounded-3xl text-purple-700 dark:text-purple-300"><Icons.HR size={32}/></div>
                   </div>
                   <p className="text-[11px] font-black text-slate-400 dark:text-slate-600 uppercase tracking-[0.4em] mb-2">Managed Nodes</p>
                   <h4 className="text-4xl font-black text-slate-950 dark:text-white">102.4k</h4>
                </div>
                
                <div className="absolute inset-0 border border-indigo-600/10 dark:border-indigo-500/10 rounded-full animate-[spin_15s_linear_infinite] opacity-30"></div>
                <div className="absolute inset-14 border border-dashed border-indigo-600/20 dark:border-indigo-500/20 rounded-full animate-[spin_20s_linear_infinite_reverse] opacity-30"></div>
             </div>
          </div>
        </div>
      </section>

      {/* Operations Grid - Staggered Entry */}
      <section className="space-y-12">
        <div className="flex items-end justify-between px-6 opacity-0 animate-[fadeUp_0.8s_ease-out_0.2s_forwards]">
          <div>
             <h2 className="text-5xl font-black tracking-tighter text-slate-950 dark:text-white">Ops Spheres.</h2>
             <p className="text-indigo-600 dark:text-indigo-500 font-black uppercase text-[11px] tracking-[0.6em] mt-4">Functional Module Interface</p>
          </div>
          <button className="glass border-slate-300 dark:border-white/5 px-8 py-3 rounded-2xl text-[11px] font-black uppercase tracking-[0.4em] text-slate-500 dark:text-slate-400 hover:text-indigo-700 dark:hover:text-white transition-all shadow-sm">Matrix View</button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-10">
           {modules.map((m, i) => (
             <div key={i} style={{ animationDelay: `${0.3 + (i * 0.1)}s` }} className="opacity-0 animate-[fadeUp_0.8s_ease-out_forwards] glass p-12 rounded-[4.5rem] border-slate-200 dark:border-white/5 hover:border-indigo-600/30 dark:hover:border-indigo-500/30 transition-all duration-1000 group/card cursor-pointer hover:shadow-2xl hover:-translate-y-5 relative overflow-hidden">
                <div className={`w-24 h-24 rounded-[2.5rem] flex items-center justify-center mb-12 transition-transform duration-1000 group-hover/card:scale-110 group-hover/card:rotate-6 ${m.bg}`}>
                   <m.icon size={40} className={m.color} />
                </div>
                <h3 className="text-3xl font-black mb-5 text-slate-950 dark:text-white group-hover/card:text-indigo-600 transition-colors">{m.title}</h3>
                <p className="text-base text-slate-500 dark:text-slate-500 leading-relaxed font-medium mb-12">{m.desc}</p>
                <div className="flex items-center gap-4 text-xs font-black uppercase tracking-[0.4em] text-indigo-700 translate-x-[-15px] opacity-0 group-hover/card:translate-x-0 group-hover/card:opacity-100 transition-all duration-700">
                  Enter Sphere <Icons.ArrowRight size={22} />
                </div>
                <div className="absolute -right-10 -bottom-10 opacity-[0.03] group-hover/card:opacity-[0.08] transition-all duration-1000 group-hover/card:scale-125">
                   <m.icon size={220} />
                </div>
             </div>
           ))}
        </div>
      </section>

      {/* Global Health Pulse Bar */}
      <section className="opacity-0 animate-[fadeUp_0.8s_ease-out_0.6s_forwards] glass rounded-[5rem] p-16 flex flex-col md:flex-row items-center justify-between border-slate-200 dark:border-indigo-500/10 gap-16 group shadow-lg">
         <div className="flex items-center gap-14">
            <div className="w-28 h-28 rounded-[3.5rem] bg-emerald-500/5 flex items-center justify-center text-emerald-600 dark:text-emerald-400 border border-emerald-500/10 shadow-inner group-hover:scale-110 transition-transform duration-1000">
               <Icons.Activity size={56} className="animate-pulse" />
            </div>
            <div className="space-y-2">
               <h4 className="text-4xl font-black text-slate-950 dark:text-white tracking-tight">System Pulse.</h4>
               <p className="text-slate-500 font-bold text-sm uppercase tracking-[0.4em]">Grid Integrity: <span className="text-emerald-500">100% Operational</span></p>
            </div>
         </div>
         <div className="flex -space-x-8">
            {[1,2,3,4,5,6,7].map(i => (
              <div key={i} className="w-20 h-20 rounded-full glass border-2 border-slate-100 dark:border-slate-900 overflow-hidden flex items-center justify-center text-[11px] font-black text-indigo-700 dark:text-indigo-400 shadow-xl hover:-translate-y-5 transition-transform duration-700 cursor-pointer bg-slate-50 dark:bg-slate-950/50">
                 GRID-{i}
              </div>
            ))}
         </div>
      </section>
    </div>
  );
};

const modules = [
  { title: "Finance", icon: Icons.Finance, color: "text-emerald-600 dark:text-emerald-400", bg: "bg-emerald-500/10", desc: "Predictive fiscal orchestration with synchronized quantum-ledgers." },
  { title: "Logistics", icon: Icons.Truck, color: "text-blue-600 dark:text-blue-400", bg: "bg-blue-500/10", desc: "Autonomous global SCM nodes with zero-latency routing protocols." },
  { title: "Personnel", icon: Icons.HR, color: "text-rose-600 dark:text-rose-400", bg: "bg-rose-500/10", desc: "Human capital orchestration with neural performance indexing." },
  { title: "Factory", icon: Icons.Factory, color: "text-amber-600 dark:text-amber-400", bg: "bg-amber-500/10", desc: "Precision shop-floor telemetry with automated resource loops." },
];

export default DashboardPage;
