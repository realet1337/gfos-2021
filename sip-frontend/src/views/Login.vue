<template>
    <v-app>
        <v-main>
            <v-container fill-height>
                <v-row
                    align="center"
                    justify="center"
                >
                    <v-col>
                        <v-card v-if="!$vuetify.breakpoint.xs" width="500" class="mx-auto rounded-lg">
                            <LoginForm/>
                        </v-card>
                        <LoginForm v-else class="mx-auto"/>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
</template>

<style>

</style>

<script>
import Vue from 'vue'
import LoginForm from '@/components/LoginForm'
import store from '@/store/'

//@vuese
//Zeigt das LoginForm. Initialisiert die App, falls ein gültiges Token im Cookie gefunden werden kann.
export default {
    components: {
        LoginForm
    },
    
    beforeRouteEnter: function(to, from, next) {
        var cookie = document.cookie.match('(^|;)\\s*' + "token" + '\\s*=\\s*([^;]+)')?.pop() || '' //taken from internet 
        cookie = cookie.split(',')[0];

        if(cookie !== ''){

            window.axios.post(Vue.prototype.$getApiUrl('http') + '/auth/validate',
            new URLSearchParams({
                'token': cookie
            })
            ).then((response) => {

                store.commit('setToken', cookie);
                store.commit('setUserId', response.data.userId);
                next('/home');

            }, () => {

                document.cookie = 'token=; Max-Age=-99999999;';
                next();

            })
        }
        else{
            next();
        }
    },
    name: 'LoginPage'
}
</script>
