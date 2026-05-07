import { useState, useEffect } from 'react';
import dashboardService from '../services/dashboard.service';
import spamService from '../services/spam.service';
import { ShieldAlert, ShieldCheck, Activity, MessageSquare, Loader2 } from 'lucide-react';

const Dashboard = () => {
  const [stats, setStats] = useState({ totalChecks: 0, spamDetected: 0, nonSpamDetected: 0 });
  const [recentChecks, setRecentChecks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const statsRes = await dashboardService.getStats();
        setStats(statsRes.data);

        const historyRes = await spamService.getHistory();
        setRecentChecks(historyRes.data.slice(0, 5)); // Only show top 5 recent
      } catch (error) {
        console.error('Failed to load dashboard data', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (isLoading) {
    return (
      <div className="flex h-64 items-center justify-center">
        <Loader2 className="animate-spin text-blue-500" size={48} />
      </div>
    );
  }

  const statCards = [
    { title: 'Total Checks', value: stats.totalChecks, icon: <MessageSquare size={24} />, color: 'bg-blue-50 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400' },
    { title: 'Spam Detected', value: stats.spamDetected, icon: <ShieldAlert size={24} />, color: 'bg-red-50 text-red-600 dark:bg-red-900/20 dark:text-red-400' },
    { title: 'Safe Messages', value: stats.nonSpamDetected, icon: <ShieldCheck size={24} />, color: 'bg-green-50 text-green-600 dark:bg-green-900/20 dark:text-green-400' },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Dashboard Overview</h1>
        <p className="text-slate-500 dark:text-slate-400">View your spam detection statistics and recent activity.</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {statCards.map((card, idx) => (
          <div key={idx} className="card p-6 flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-slate-500 dark:text-slate-400">{card.title}</p>
              <h3 className="text-3xl font-bold text-slate-900 dark:text-white mt-1">{card.value}</h3>
            </div>
            <div className={`p-4 rounded-xl ${card.color}`}>
              {card.icon}
            </div>
          </div>
        ))}
      </div>

      {/* Recent Activity Table */}
      <div className="card">
        <div className="px-6 py-5 border-b border-slate-100 dark:border-slate-800 flex justify-between items-center">
          <h3 className="text-lg font-semibold text-slate-900 dark:text-white flex items-center gap-2">
            <Activity size={20} className="text-blue-500" /> Recent Activity
          </h3>
        </div>
        
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-50 dark:bg-slate-800/50 text-slate-500 dark:text-slate-400 text-sm uppercase tracking-wider">
                <th className="px-6 py-4 font-medium">Message Snippet</th>
                <th className="px-6 py-4 font-medium">Result</th>
                <th className="px-6 py-4 font-medium">Confidence</th>
                <th className="px-6 py-4 font-medium">Date</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 dark:divide-slate-800">
              {recentChecks.length === 0 ? (
                <tr>
                  <td colSpan="4" className="px-6 py-8 text-center text-slate-500 dark:text-slate-400">
                    No recent activity found.
                  </td>
                </tr>
              ) : (
                recentChecks.map((check) => (
                  <tr key={check.id} className="hover:bg-slate-50 dark:hover:bg-slate-800/20 transition-colors">
                    <td className="px-6 py-4">
                      <div className="text-sm text-slate-900 dark:text-white max-w-xs truncate">
                        {check.message}
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        check.prediction === 'SPAM' 
                          ? 'bg-red-100 text-red-800 dark:bg-red-900/30 dark:text-red-400' 
                          : 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400'
                      }`}>
                        {check.prediction}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <div className="w-full bg-slate-200 dark:bg-slate-700 rounded-full h-2 max-w-[4rem]">
                          <div 
                            className={`h-2 rounded-full ${check.prediction === 'SPAM' ? 'bg-red-500' : 'bg-green-500'}`}
                            style={{ width: `${check.confidence}%` }}
                          ></div>
                        </div>
                        <span className="text-sm text-slate-600 dark:text-slate-400">{check.confidence.toFixed(0)}%</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-sm text-slate-500 dark:text-slate-400">
                      {new Date(check.checkedAt).toLocaleDateString()}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
