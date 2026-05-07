import api from './api';

const getStats = () => {
  return api.get('/dashboard/stats');
};

const dashboardService = {
  getStats,
};

export default dashboardService;
