import { Menu, Bell } from 'lucide-react';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const Navbar = ({ setIsOpen }) => {
  const { user } = useContext(AuthContext);

  return (
    <header className="bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 h-16 flex items-center justify-between px-4 lg:px-8 sticky top-0 z-30">
      <div className="flex items-center gap-4">
        <button 
          className="lg:hidden p-2 text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 rounded-lg"
          onClick={() => setIsOpen(true)}
        >
          <Menu size={24} />
        </button>
        <h2 className="text-xl font-bold text-slate-800 dark:text-white hidden sm:block">
          Welcome back, {user?.name?.split(' ')[0]}! 👋
        </h2>
      </div>

      <div className="flex items-center gap-4">
        <button className="p-2 text-slate-500 hover:text-blue-600 dark:text-slate-400 dark:hover:text-blue-400 transition-colors relative">
          <Bell size={20} />
          <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full border-2 border-white dark:border-slate-900"></span>
        </button>
      </div>
    </header>
  );
};

export default Navbar;
