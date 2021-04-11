import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: false,
    userId: false,
    ws: undefined,
    initialized: false,
    blockedUsers: [],
    blockedBy: [],
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    },
    setUserId(state, userId) {
      state.userId = userId;
    },
    setWs(state, ws){
      state.ws = ws;
    },
    setInitialized(state, initialized){
      state.initialized = initialized;
    },
    setBlockedUsers(state, blockedUsers){
      state.blockedUsers = blockedUsers
    },
    setBlockedBy(state, blockedBy){
      state.blockedBy = blockedBy
    },
    reset(state){
      state.ws.close();
      Object.assign(state, {
        token: false,
        userId: false,
        ws: undefined,
        initialized: false,
      });
    }
  }
});
