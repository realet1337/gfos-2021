<template>
     <v-card class="mx-auto rounded-lg" width=50%>
        <v-container>
            <v-form class="ma-3">
                <v-row justify="center">
                    <h1 class="header mt-2 mb-n1">Hello there</h1>
                </v-row>
                <v-row justify="center">
                    <h4 class="subheader mb-7">{{ greeting }}</h4>
                </v-row>
                <p class="label-text mb-1 ml-3"><b>E-MAIL</b></p>
                <div class="input-field">
                    <v-text-field
                        v-model="email"
                        single-line
                        outlined
                        class="input-field"
                        @keyup.enter="onSubmit"
                    ></v-text-field>
                </div>
                <p class="label-text mb-1 ml-3"><b>PASSWORD</b></p>
                <div class="input-field">
                    <v-text-field
                        v-model="password"
                        single-line
                        outlined
                        type="password"
                        @keyup.enter="onSubmit"
                        ></v-text-field>
                </div>
                <p v-if="error" class="error-message"><i>Oops! That didn't work. Did you type your E-Mail and password correctly?</i></p>
                <v-btn @click="onSubmit" block depressed large color="primary">Login</v-btn>
            </v-form>
        </v-container>
    </v-card>
</template>

<script>
import Vue from 'vue'

export default {
    data: function() {
        return {
            email: '',
            password: '',
            greeting: ['This is sip... or is it?','Welcome back!','Happy to see you!','Isn\'t life just great?','We missed you!'][Math.floor(Math.random()*5)],
            error: false
        }
    },
    methods: {

        onSubmit: function() {

            this.$data.error = false

            window.axios.post(Vue.prototype.$apiBaseUrl + '/api/auth/login', 
            new URLSearchParams({
                    'email': this.$data.email,
                    'password': this.$data.password
                }).toString()).then( (response) => {

                    var nextDay = new Date();
                    nextDay.setDate(nextDay.getDate() + 1);

                    document.cookie = "token=" + response.data.token + ", expires=" + nextDay.toUTCString();
                    this.$router.push('/home');

                }, () => {

                    this.$data.error = true;

                });

        }

    },
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

    name: 'LoginCard'
}
</script>

<style>
    .label-text{

        font-size: 70%;
        color: var(--v-secondary-lighten2);

    }
    .header{

        color: var(--v-secondary-lighten4)

    }
    .subheader{

        color: var(--v-secondary-lighten3)

    }
    .input-field input[type="text"]{

        color: var(--v-secondary-lighten4)!important;

    }
    .error-message{

        color: var(--v-error-base);
        font-size: 90%;

    }
</style>