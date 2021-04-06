<template>
    <v-app>
        <v-app-bar app hide-on-scroll>
            <v-row no-gutters justify="center">
                <v-col cols="auto">
                    <img src="@/assets/sip-banner.png" width="200">
                </v-col>
            </v-row>
            <template v-slot:extension>
                <v-tabs centered>
                    <v-tab to="/home/direct-chats">DIRECT CHATS</v-tab>
                    <v-tab to="/home/groups">GROUPS</v-tab>
                </v-tabs>
            </template>
        </v-app-bar>
        <v-main>
            <!-- only load view if user is certainly logged in -->
            <div v-if="this.$store.state.userId">
                <router-view></router-view>
            </div>
        </v-main>
    </v-app>
</template>

<script>
import Vue from 'vue'
import store from '@/store/'

export default {
    name: 'Home',
        components: {
    },
    beforeRouteEnter: function(to, from, next){
        if(store.state.token === false){

            var cookie = (document.cookie.match('(^|;)\\s*' + "token" + '\\s*=\\s*([^;]+)')?.pop() || '').split(',')[0];

            if(cookie !== ''){

                window.axios.post(Vue.prototype.$apiBaseUrl + '/api/auth/validate',
                new URLSearchParams({
                    'token': cookie
                })
                ).then((response) => {

                    store.commit('setToken', cookie);
                    store.commit('setUserId', response.data.userId);
                    next();

                }, () => {

                    document.cookie = 'token=; Max-Age=-99999999;';
                    next('/');

                })
           }else{
               next('/');
           }

        }
        next();
    }
}
</script>
