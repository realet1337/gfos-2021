<template>
    <v-app>
        <v-app-bar>
            <p>ogchamp</p>
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

export default {
    name: 'Home',
        components: {
    },
    created: function(){

        //FIXME: UPON PAGE RELOAD, the vuex store is reset.
        //Implement before-enter guard to check if token/userid are false and do validation using cookie if they are.
        //Reject routing if no cookie and no store.
        if(this.$store.state.token === false){

            var cookie = (document.cookie.match('(^|;)\\s*' + "token" + '\\s*=\\s*([^;]+)')?.pop() || '').split(',')[0];

            if(cookie !== ''){

                window.axios.post(Vue.prototype.$apiBaseUrl + '/api/auth/validate',
                new URLSearchParams({
                    'token': cookie
                })
                ).then((response) => {

                    this.$store.commit('setToken', cookie);
                    this.$store.commit('setUserId', response.data.userId);

                }, () => {

                    document.cookie = 'token=; Max-Age=-99999999;';
                    this.$router.push('/');

                })
           }else{
               this.$router.push('/');
           }

        }

    }
}
</script>
