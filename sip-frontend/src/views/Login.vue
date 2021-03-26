<template>
    <v-app>
        <v-main>
            <v-container fill-height>
                <v-row
                    align="center"
                    justify="center"
                >
                    <v-col>
                       <LoginCard class="mx-auto"/>
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
import LoginCard from '@/components/LoginCard.vue'

export default {
    components: {
        LoginCard
    },
    
    //TODO: THIS IS BAD! Implement this using vue-router guards to prevent loading of Login page entirely.
    created: function() {


        //check if token exists and validate. Redirect if valid. TODO: extend token/regenerate? Possibly implement refresh tokens?
        var cookie = document.cookie.match('(^|;)\\s*' + "token" + '\\s*=\\s*([^;]+)')?.pop() || '' //taken from internet 
        cookie = cookie.split(',')[0];

        if(cookie !== ''){

            window.axios.post(Vue.prototype.$apiBaseUrl + '/api/auth/validate',
            new URLSearchParams({
                'token': cookie
            })
            ).then(() => {

                this.$router.push('/home');

            })
        }
    },
    name: 'LoginPage'
}
</script>
