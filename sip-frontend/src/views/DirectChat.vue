<template>
    <v-main>
        <v-container fluid v-if="$store.state.userId">
            <v-row no-gutters>
                <v-col cols="auto">
                    <div style="max-width:250px; min-width:250px">
                        <p>ogchamp</p>
                    </div>
                </v-col>
                <v-col>
                    <div style="height: 96vh;">
                        <ChatWindow @showUser="showUser"/>
                    </div>
                </v-col>
            </v-row>
            <v-dialog>
            </v-dialog>
        </v-container>
    </v-main>
</template>

<script>
import ChatWindow from '@/components/ChatWindow'
import Vue from'vue'
import store from '@/store/'

export default {
    name: 'DirectChat',
    components: {
        ChatWindow,
    },
    data: function(){
        return {}
    },
    methods: {
        showUser: function(user){
            console.log(user);
        }
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