<template>
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
            <v-row class="mt-2 ml-3">
                <span class="secondary--text text--lighten-2" style="font-size: 11px;">Don't have an account? <router-link to="/register">Register!</router-link></span>
            </v-row>
        </v-form>
    </v-container>
</template>

<script>
import Vue from 'vue'

//@vuese
//Hier kann sich der Nutzer anmelden
export default {
    data: function() {
        return {
            email: '',
            password: '',
            greeting: [
            'This is sip... or is it?',
            'Welcome back!',
            'Happy to see you!',
            'Just because something is a discord clone doesn\'t mean it\'s bad...',
            'We missed you!'
            ][Math.floor(Math.random()*5)],
            error: false
        }
    },
    methods: {

        //@vuese
        //Führt Anmeldung durch. Falls dies fehlschlägt wird eine Fehlermeldung gezeigt.
        onSubmit: function() {

            this.$data.error = false

            window.axios.post(Vue.prototype.$getApiUrl('http') + '/auth/login', 
            new URLSearchParams({
                    'email': this.$data.email,
                    'password': this.$data.password
                }).toString()).then( (response) => {

                    var nextDay = new Date();
                    nextDay.setDate(nextDay.getDate() + 1);

                    document.cookie = "token=" + response.data.token + ", expires=" + nextDay.toUTCString();
                    this.$store.commit('setToken',response.data.token);
                    this.$store.commit('setUserId',response.data.userId);
                    this.$router.push('/home');

                }, () => {

                    this.$data.error = true;

                });

        }

    },
    name: 'LoginForm'
}
</script>