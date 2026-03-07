import React, { useState, useEffect } from 'react';
import { Icons } from '../../components/Icons';
import { Company } from '../../types';

interface CompanySelectProps {
  companies: Company[];
  onSelect: (company: Company) => void;
  isDarkMode: boolean;
  toggleTheme: () => void;
}

const CompanySelect: React.FC<CompanySelectProps> = ({ companies, onSelect, isDarkMode, toggleTheme }) => {
  const [loadingText, setLoadingText] = useState('AUTHENTICATING NODES');
  const [hoveredNode, setHoveredNode] = useState<string | null>(null);

  useEffect(() => {
    const texts = [
      'MAPPING GLOBAL ASSETS',
      'SYNCHRONIZING QUANTUM CORES',
      'ESTABLISHING TACTICAL UPLINK',
      'VERIFYING NODE INTEGRITY',
      'INITIALIZING NEXUS PROTOCOLS'
    ];
    let i = 0;
    const interval = setInterval(() => {
      setLoadingText(texts[i % texts.length]);
      i++;
    }, 2500);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="h-screen w-screen relative flex flex-col overflow-hidden bg-slate-50 dark:bg-[#02030a] font-['Plus_Jakarta_Sans'] transition-colors duration-1000 p-8 lg:p-16 perspective-container">
      
      {/* Background Cinematic Aura */}
      <div className="absolute top-[-30%] left-[-10%] w-[80%] h-[80%] bg-indigo-500/10 dark:bg-indigo-600/5 rounded-full blur-[200px] animate-pulse-slow"></div>
      <div className="absolute bottom-[-20%] right-[-10%] w-[70%] h-[70%] bg-purple-500/10 dark:bg-purple-600/5 rounded-full blur-[180px]"></div>
      
      <header className="flex justify-between items-center relative z-10 mb-12 animate-fade-up">
         <div className="flex items-center gap-6">
            <div className="w-14 h-14 bg-indigo-600 rounded-2xl flex items-center justify-center shadow-2xl rotate-3 hover:rotate-0 transition-all duration-1000 relative group">
               <div className="absolute -inset-1 bg-indigo-500/30 blur-lg opacity-0 group-hover:opacity-100 transition-opacity"></div>
               <span className="text-white font-black text-3xl italic tracking-tighter relative z-10">N</span>
            </div>
            <div>
               <h1 className="text-2xl font-black tracking-tighter text-slate-950 dark:text-white leading-none">Enterprise Registry</h1>
               <p className="text-[10px] font-black text-indigo-600 dark:text-indigo-500 uppercase tracking-[0.5em] mt-1.5 block opacity-70">Unified Multi-Tenant Core</p>
            </div>
         </div>
         <div className="flex items-center gap-4">
            <button 
              onClick={toggleTheme} 
              className="glass px-6 py-2.5 rounded-2xl flex items-center gap-4 border-slate-300 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 transition-all shadow-xl active:scale-95 group"
            >
               <span className="text-[9px] font-black uppercase tracking-[0.4em] opacity-40 group-hover:opacity-100 transition-opacity">Spectral State</span>
               {isDarkMode ? <Icons.Sun size={18} /> : <Icons.Moon size={18} />}
            </button>
         </div>
      </header>

      <main className="relative z-10 flex-1 overflow-auto custom-scrollbar">
         <div className="max-w-[1800px] mx-auto space-y-20 pb-12">
            <div className="text-center space-y-6 animate-fade-up" style={{ animationDelay: '0.2s' }}>
               <div className="relative inline-block">
                  <h2 className="text-7xl lg:text-[8rem] font-black tracking-tighter text-slate-950 dark:text-white leading-tight">
                    Initialize <span className="quantum-shimmer text-glow-indigo">Enterprise Uplink.</span>
                  </h2>
               </div>
               <div className="flex items-center justify-center gap-4">
                  <span className="h-[1px] w-12 bg-indigo-500/20"></span>
                  <p className="text-sm font-black text-indigo-600/60 dark:text-indigo-400/50 uppercase tracking-[0.8em] animate-pulse">
                    {loadingText}
                  </p>
                  <span className="h-[1px] w-12 bg-indigo-500/20"></span>
               </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-10">
               {companies.map((company, i) => (
                  <div 
                     key={company.id}
                     onClick={() => onSelect(company)}
                     onMouseEnter={() => setHoveredNode(company.id)}
                     onMouseLeave={() => setHoveredNode(null)}
                     className={`tilt-card glass p-12 rounded-[5rem] border-slate-300 dark:border-white/5 hover:border-indigo-600/50 dark:hover:border-indigo-500/50 transition-all duration-700 cursor-pointer group hover:shadow-[0_40px_100px_-20px_rgba(99,102,241,0.2)] dark:hover:shadow-[0_40px_120px_-30px_rgba(0,0,0,1)] relative overflow-hidden animate-stagger-1`}
                     style={{ animationDelay: `${i * 0.08 + 0.5}s` }}
                  >
                     {/* Dynamic Corner HUD Accents */}
                     <div className="absolute top-8 left-8 w-6 h-6 border-t-2 border-l-2 border-indigo-500/20 rounded-tl-xl opacity-0 group-hover:opacity-100 transition-all duration-700"></div>
                     <div className="absolute bottom-8 right-8 w-6 h-6 border-b-2 border-r-2 border-indigo-500/20 rounded-br-xl opacity-0 group-hover:opacity-100 transition-all duration-700"></div>
                     
                     {/* Internal Data Mesh */}
                     <div className="absolute inset-0 bg-data-stream bg-[length:100%_40px] opacity-[0.02] dark:opacity-[0.05] group-hover:opacity-10 transition-opacity"></div>

                     {/* Company Identification Ring */}
                     <div className="w-24 h-24 rounded-[3.2rem] bg-slate-950 dark:bg-white/[0.03] border border-slate-200 dark:border-white/10 flex items-center justify-center text-white dark:text-indigo-400 mb-12 transition-all duration-700 group-hover:scale-110 group-hover:shadow-2xl shadow-indigo-500/20">
                        <Icons.Dashboard size={40} />
                     </div>

                     <div className="space-y-4 relative z-10">
                        <div className="flex items-center gap-3">
                           <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                           <p className="text-[10px] font-black text-indigo-600 dark:text-indigo-500/80 uppercase tracking-[0.4em]">{company.industry}</p>
                        </div>
                        <h3 className="text-3xl font-black text-slate-950 dark:text-white leading-none group-hover:text-indigo-600 transition-colors">{company.name}</h3>
                        
                        {/* Interactive Telemetry Data */}
                        <div className="pt-6 space-y-2.5 opacity-40 group-hover:opacity-100 transition-opacity">
                           <div className="flex justify-between items-center text-[10px] font-bold text-slate-500 dark:text-slate-400 uppercase tracking-widest">
                              <span>Sync Latency</span>
                              <span className="font-mono text-indigo-600 dark:text-indigo-400">0.{Math.floor(Math.random() * 9)}ms</span>
                           </div>
                           <div className="flex justify-between items-center text-[10px] font-bold text-slate-500 dark:text-slate-400 uppercase tracking-widest">
                              <span>Node Health</span>
                              <span className="font-mono text-emerald-500">99.9%</span>
                           </div>
                           <div className="w-full bg-slate-200 dark:bg-white/5 h-1 rounded-full overflow-hidden mt-3">
                              <div className="bg-indigo-600 dark:bg-indigo-500 h-full w-[85%] group-hover:w-full transition-all duration-1000"></div>
                           </div>
                        </div>
                     </div>

                     <div className="mt-14 flex items-center gap-4 text-sm font-black uppercase tracking-[0.4em] text-indigo-700 dark:text-indigo-400 translate-y-4 opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all duration-700">
                        Establish Connection <Icons.ArrowRight size={22} className="group-hover:translate-x-2 transition-transform" />
                     </div>

                     {/* Subtle Geometric Background */}
                     <div className="absolute -right-12 -bottom-12 opacity-[0.03] group-hover:opacity-[0.08] transition-all duration-1000 pointer-events-none scale-150 group-hover:scale-100">
                        <Icons.Network size={350} className="text-slate-900 dark:text-white" />
                     </div>
                  </div>
               ))}
            </div>
         </div>
      </main>

      <footer className="relative z-10 pt-10 border-t border-slate-200 dark:border-white/[0.05] flex flex-col md:flex-row justify-between items-center gap-6 animate-fade-up">
         <div className="flex items-center gap-10">
            <p className="text-[10px] text-slate-400 dark:text-slate-700 font-black uppercase tracking-[1.5em]">Global Hub Switch v6.02</p>
            <div className="hidden md:flex gap-10">
               <div className="space-y-1">
                  <p className="text-[8px] font-black text-indigo-600/40 uppercase tracking-widest">Encryption</p>
                  <p className="text-[10px] font-bold text-slate-600 dark:text-slate-400">RSA-4096-ECC</p>
               </div>
               <div className="space-y-1">
                  <p className="text-[8px] font-black text-indigo-600/40 uppercase tracking-widest">Handshake</p>
                  <p className="text-[10px] font-bold text-slate-600 dark:text-slate-400">TLS 1.3 / UDP-Q</p>
               </div>
            </div>
         </div>
         <div className="flex items-center gap-6">
            <div className="glass px-5 py-2 rounded-xl text-[10px] font-black text-slate-500 dark:text-slate-500 uppercase tracking-widest flex items-center gap-3">
               <span className="w-1.5 h-1.5 rounded-full bg-emerald-500"></span> Node Server: Frankfurt-DE
            </div>
            <button className="text-[10px] font-black text-indigo-600 dark:text-indigo-400 uppercase tracking-widest hover:underline">Support Portal</button>
         </div>
      </footer>
    </div>
  );
};

export default CompanySelect;
