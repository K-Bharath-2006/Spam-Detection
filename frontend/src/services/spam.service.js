import api from './api';

const checkSpam = (message) => {
  return api.post('/spam/check', { message });
};

const getHistory = () => {
  return api.get('/spam/history');
};

const spamService = {
  checkSpam,
  getHistory,
};

export default spamService;
