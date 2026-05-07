import { useState } from 'react';
import spamService from '../services/spam.service';
import { ShieldCheck, ShieldAlert, AlertTriangle, Send, Loader2 } from 'lucide-react';
import toast from 'react-hot-toast';

const SpamChecker = () => {
  const [message, setMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [result, setResult] = useState(null);

  const handleCheck = async (e) => {
    e.preventDefault();
    if (!message.trim()) {
      toast.error('Please enter a message to check');
      return;
    }

    setIsLoading(true);
    try {
      const response = await spamService.checkSpam(message);
      setResult(response.data);
      toast.success('Analysis complete');
    } catch (error) {
      toast.error('Failed to check message');
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      <div>
        <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Spam Checker</h1>
        <p className="text-slate-500 dark:text-slate-400">Analyze any text to determine if it is spam or not.</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card p-6">
          <form onSubmit={handleCheck} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
                Message Content
              </label>
              <textarea
                className="input-field min-h-[200px] resize-y"
                placeholder="Paste the message you want to check here..."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
              ></textarea>
            </div>
            <button
              type="submit"
              disabled={isLoading || !message.trim()}
              className="btn-primary w-full flex justify-center items-center gap-2"
            >
              {isLoading ? (
                <>
                  <Loader2 className="animate-spin" size={20} />
                  Analyzing...
                </>
              ) : (
                <>
                  <Send size={20} />
                  Check Message
                </>
              )}
            </button>
          </form>
        </div>

        <div className="card p-6 flex flex-col">
          <h3 className="text-lg font-semibold text-slate-900 dark:text-white mb-4">Analysis Result</h3>
          
          {result ? (
            <div className="flex-1 flex flex-col space-y-6">
              <div className={`p-6 rounded-2xl flex flex-col items-center justify-center text-center transition-all ${
                result.prediction === 'SPAM' 
                  ? 'bg-red-50 dark:bg-red-900/20 text-red-600 dark:text-red-400 border border-red-100 dark:border-red-800' 
                  : 'bg-green-50 dark:bg-green-900/20 text-green-600 dark:text-green-400 border border-green-100 dark:border-green-800'
              }`}>
                {result.prediction === 'SPAM' ? <AlertTriangle size={64} className="mb-4" /> : <ShieldCheck size={64} className="mb-4" />}
                <h2 className="text-3xl font-black tracking-tight">{result.prediction}</h2>
                <p className="mt-2 font-medium">Confidence: {result.confidence.toFixed(1)}%</p>
              </div>
              
              <div className="bg-slate-50 dark:bg-slate-800/50 p-4 rounded-xl border border-slate-100 dark:border-slate-700">
                <h4 className="text-sm font-semibold text-slate-700 dark:text-slate-300 mb-2 flex items-center gap-2">
                  <ShieldAlert size={16} /> Reasoning
                </h4>
                <p className="text-slate-600 dark:text-slate-400 text-sm leading-relaxed">
                  {result.reason}
                </p>
              </div>
            </div>
          ) : (
            <div className="flex-1 flex flex-col items-center justify-center text-slate-400 dark:text-slate-500 min-h-[300px]">
              <ShieldAlert size={64} className="mb-4 opacity-50" />
              <p className="text-center max-w-[250px]">
                Submit a message to see the spam analysis results here.
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SpamChecker;
