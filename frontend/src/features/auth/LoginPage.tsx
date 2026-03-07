import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLogin } from '../../hooks/useAuth';
import { Icons } from '../../components/Icons';

export const LoginPage: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [systemTime, setSystemTime] = useState(new Date().toLocaleTimeString());
  const [mousePos, setMousePos] = useState({ x: 0, y: 0 });
  
  // Theme state
  const [isDarkMode, setIsDarkMode] = useState(
    window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
  );

  const { mutate: login, isLoading, error: authError } = useLogin();

  useEffect(() => {
    const timer = setInterval(() => setSystemTime(new Date().toLocaleTimeString()), 1000);
    const handleMouseMove = (e: MouseEvent) => setMousePos({ x: e.clientX, y: e.clientY });
    window.addEventListener('mousemove', handleMouseMove);
    return () => {
      clearInterval(timer);
      window.removeEventListener('mousemove', handleMouseMove);
    };
  }, []);

  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [isDarkMode]);

  const toggleTheme = () => setIsDarkMode(!isDarkMode);

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    // Connect user's username/email with our actual auth hook hook
    login({ email: username, password });
  };

  return (
    <div className="h-screen w-screen relative flex overflow-hidden bg-[#fafbfc] dark:bg-[#020308] transition-colors duration-1000 font-['Plus_Jakarta_Sans']">
      
      {/* Cinematic Mouse Interaction Flare */}
      <div 
        className="absolute w-[600px] h-[600px] rounded-full pointer-events-none opacity-[0.05] dark:opacity-[0.08] blur-[150px] transition-transform duration-300 z-0"
        style={{ 
          transform: `translate(${mousePos.x - 300}px, ${mousePos.y - 300}px)`,
          background: 'radial-gradient(circle, #6366f1 0%, transparent 70%)' 
        }}
      ></div>

      {/* LEFT PANE: BRAND ARCHITECTURE (62%) */}
      <div className="hidden lg:flex flex-col justify-between w-[62%] p-24 md:p-32 relative z-10 border-r border-slate-200 dark:border-white/[0.03] overflow-hidden">
        {/* Animated Data Mesh Overlay */}
        <div className="absolute inset-0 bg-data-stream bg-[length:100%_80px] opacity-[0.3] dark:opacity-[0.15] animate-stream pointer-events-none"></div>
        
        <div className="relative">
          <div className="flex items-center gap-8 mb-32 animate-fade-up">
             <div className="w-20 h-20 bg-indigo-600 rounded-[2.2rem] flex items-center justify-center shadow-2xl rotate-3 hover:rotate-0 transition-transform duration-1000 relative group">
                <div className="absolute -inset-2 bg-indigo-500/20 blur-xl opacity-0 group-hover:opacity-100 transition-opacity"></div>
                <span className="text-white font-black text-5xl italic tracking-tighter relative z-10">S</span>
             </div>
             <div>
                <span className="text-3xl font-black tracking-tighter text-slate-950 dark:text-white block leading-none">SPRING ERP</span>
                <span className="text-[10px] font-black text-indigo-600 dark:text-indigo-500 uppercase tracking-[0.7em] mt-2 block opacity-70">Sovereign Intelligence Architecture</span>
             </div>
          </div>

          <div className="space-y-12 max-w-5xl">
            <h1 className="text-[12rem] font-black tracking-tighter text-slate-950 dark:text-white leading-[0.7] opacity-95 animate-fade-up" style={{ animationDelay: '0.2s' }}>
              Strategic <br/>
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-indigo-700 via-indigo-500 to-slate-400 dark:from-indigo-300 dark:via-white dark:to-indigo-700 text-glow-indigo">Nexus.</span>
            </h1>
            <p className="text-3xl text-slate-500 dark:text-slate-400 font-light leading-snug max-w-2xl animate-fade-up" style={{ animationDelay: '0.4s' }}>
              Orchestrate the global functional stack with zero-latency autonomous loops. <br/>
              <span className="text-slate-800 dark:text-slate-200 font-bold uppercase tracking-[0.3em] text-sm mt-6 block">Unified Operations OS v5.0</span>
            </p>
          </div>
        </div>

        {/* Dynamic Telemetry HUD Foot */}
        <div className="flex items-end gap-20 animate-fade-up relative z-10" style={{ animationDelay: '0.6s' }}>
           <div className="space-y-3">
              <p className="text-[10px] font-black text-indigo-600 dark:text-indigo-500/60 uppercase tracking-[0.5em]">System Epoch</p>
              <p className="text-slate-950 dark:text-white font-bold text-lg font-mono tracking-tighter">{systemTime}</p>
           </div>
           <div className="space-y-3">
              <p className="text-[10px] font-black text-indigo-600 dark:text-indigo-500/60 uppercase tracking-[0.5em]">Grid Latency</p>
              <p className="text-slate-950 dark:text-white font-bold text-lg flex items-center gap-3">
                 <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse shadow-[0_0_12px_rgba(16,185,129,0.8)]"></span> 1.4ms <span className="text-[10px] opacity-40">OPTIMAL</span>
              </p>
           </div>
           <div className="space-y-3">
              <p className="text-[10px] font-black text-indigo-600 dark:text-indigo-500/60 uppercase tracking-[0.5em]">Registry ID</p>
              <p className="text-slate-950 dark:text-white font-bold text-lg">NX-US-01</p>
           </div>
        </div>
      </div>

      {/* RIGHT PANE: ACCESS TERMINAL (38%) */}
      <div className="flex-1 flex items-center justify-center lg:justify-start lg:pl-16 relative z-20 bg-white/40 dark:bg-slate-950/60 backdrop-blur-[100px]">
        
        {/* Minimalist Spectral Switcher */}
        <div className="absolute top-12 right-12 animate-fade-up">
           <button 
             onClick={toggleTheme} 
             className="glass px-6 py-3 rounded-2xl flex items-center gap-4 border-slate-300 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-indigo-600 dark:hover:text-indigo-400 transition-all shadow-xl active:scale-95 group"
           >
              <span className="text-[10px] font-black uppercase tracking-[0.4em] opacity-40 group-hover:opacity-100 transition-opacity">Spectral Mode</span>
              {isDarkMode ? <Icons.Sun size={18} /> : <Icons.Moon size={18} />}
           </button>
        </div>

        <div className="w-full max-w-md px-10">
          <div className="glass p-16 rounded-[5rem] border-slate-300 dark:border-white/10 shadow-[0_50px_100px_-20px_rgba(0,0,0,0.1)] dark:shadow-[0_80px_150px_-30px_rgba(0,0,0,1)] relative overflow-hidden group animate-fade-up" style={{ animationDelay: '0.3s' }}>
            
            {/* Holographic Security Laser */}
            <div className="absolute left-0 w-full h-[35%] bg-gradient-to-b from-transparent via-indigo-600/10 dark:via-indigo-500/5 to-transparent animate-scan pointer-events-none"></div>
            
            {/* Terminal Corner Brackets */}
            <div className="absolute top-8 left-8 w-8 h-8 border-t-2 border-l-2 border-slate-300 dark:border-white/10 rounded-tl-xl opacity-50"></div>
            <div className="absolute bottom-8 right-8 w-8 h-8 border-b-2 border-r-2 border-slate-300 dark:border-white/10 rounded-br-xl opacity-50"></div>

            <div className="mb-16 relative">
              <div className="inline-flex items-center gap-3 px-6 py-3 rounded-full bg-indigo-500/5 border border-indigo-500/20 text-indigo-600 dark:text-indigo-400 text-[10px] font-black uppercase tracking-[0.5em] mb-12 shadow-sm">
                 <Icons.Shield size={14} className="animate-pulse" /> Identity Check
              </div>
              <h2 className="text-7xl font-black tracking-tighter text-slate-950 dark:text-white mb-3 leading-none">Login.</h2>
              <p className="text-slate-500 dark:text-slate-500 text-lg font-medium">Identify to the core matrix.</p>
            </div>

            <form onSubmit={handleLogin} className="space-y-10">
              <div className="space-y-4">
                 <label className="text-[11px] font-black text-slate-600 dark:text-slate-400 uppercase tracking-[0.5em] ml-1">Access Identity</label>
                 <div className="relative group/input">
                    <div className="absolute inset-y-0 left-0 pl-8 flex items-center pointer-events-none text-slate-400 dark:text-slate-600 group-focus-within/input:text-indigo-600 transition-colors">
                       <Icons.HR size={24} />
                    </div>
                    <input 
                      required
                      type="text"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                      placeholder="Enter Email"
                      className="w-full bg-white dark:bg-white/[0.01] border border-slate-200 dark:border-white/5 rounded-[2.8rem] py-7 pl-20 pr-10 text-base font-bold text-slate-950 dark:text-white outline-none focus:border-indigo-600 dark:focus:border-indigo-500 transition-all placeholder:text-slate-300 dark:placeholder:text-slate-800 shadow-sm"
                    />
                 </div>
              </div>

              <div className="space-y-4">
                 <div className="flex justify-between items-end mb-1 px-1">
                    <label className="text-[11px] font-black text-slate-600 dark:text-slate-400 uppercase tracking-[0.5em]">Auth Cipher</label>
                    <button type="button" className="text-[11px] font-black text-indigo-600 dark:text-indigo-500/50 uppercase tracking-widest hover:text-indigo-800 transition-colors">Recover</button>
                 </div>
                 <div className="relative group/input">
                    <div className="absolute inset-y-0 left-0 pl-8 flex items-center pointer-events-none text-slate-400 dark:text-slate-600 group-focus-within/input:text-indigo-600 transition-colors">
                       <Icons.Shield size={24} />
                    </div>
                    <input 
                      required
                      type="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      placeholder="••••••••"
                      className="w-full bg-white dark:bg-white/[0.01] border border-slate-200 dark:border-white/5 rounded-[2.8rem] py-7 pl-20 pr-10 text-base font-bold text-slate-950 dark:text-white outline-none focus:border-indigo-600 dark:focus:border-indigo-500 transition-all placeholder:text-slate-300 dark:placeholder:text-slate-800 shadow-sm"
                    />
                 </div>
              </div>

              {!!authError && (
                <div className="flex items-center gap-5 text-rose-600 dark:text-rose-400 text-[10px] font-black uppercase tracking-widest px-10 py-7 bg-rose-500/5 border border-rose-500/20 rounded-[3rem] animate-shake">
                   <Icons.Alert size={22} />
                   {(authError as any)?.response?.data?.message || 'AUTH_FAILED: SYSTEM_ACCESS_DENIED'}
                </div>
              )}

              <button 
                type="submit"
                disabled={isLoading}
                className="w-full bg-slate-950 dark:bg-indigo-600 hover:bg-slate-800 dark:hover:bg-indigo-500 disabled:opacity-50 text-white font-black text-lg py-8 rounded-[3rem] transition-all shadow-3xl shadow-indigo-600/20 active:scale-[0.97] mt-6 relative overflow-hidden group/btn"
              >
                {isLoading ? (
                  <div className="flex items-center justify-center gap-6">
                    <div className="w-6 h-6 border-[3px] border-white/20 border-t-white rounded-full animate-spin"></div>
                    <span className="uppercase tracking-[0.6em] text-sm">Synchronizing</span>
                  </div>
                ) : (
                  <div className="flex items-center justify-center gap-5">
                    <span className="uppercase tracking-[0.6em] text-sm">Establish Uplink</span>
                    <Icons.ArrowRight size={24} className="group-hover/btn:translate-x-3 transition-transform" />
                  </div>
                )}
                {/* Dual Shimmer Effect */}
                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent -translate-x-full group-hover/btn:animate-shimmer pointer-events-none"></div>
              </button>
            </form>
          </div>
          
          <div className="mt-20 text-center">
            <p className="text-[11px] text-slate-400 dark:text-slate-800 font-black uppercase tracking-[1.2em]">Authorized Terminal Access Only</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
