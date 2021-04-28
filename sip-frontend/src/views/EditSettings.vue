<template>
    <v-form class="ml-3 mt-2" v-model="isValid" v-if="userProfile">
        <v-row no-gutters class="mb-4 ml-3">
            <v-col cols="24">
                <h3>Edit settings: </h3>
                <v-divider></v-divider>
            </v-col>
        </v-row>
        <v-row no-gutters>
            <p class="label-text">At most how many messages should the application have loaded simultaneously?</p>
        </v-row>
        <v-slider 
        v-model="userProfile.maxLoadedMessages" 
        class="ml-3 mr-6" 
        thumb-label
        step="50"
        max="500"
        min="50"
        ></v-slider>
        <v-row no-gutters>
            <p class="label-text">How many new messages should the application load at once?</p>
        </v-row>
        <v-slider 
        v-model="userProfile.messageChunkSize" 
        class="ml-3 mr-6" 
        thumb-label
        step="10"
        max="500"
        min="10"
        :rules="[v => $data.userProfile.maxLoadedMessages >= $data.userProfile.messageChunkSize || 'This should be smaller than the maximum loaded messages']"
        ></v-slider>
        <v-checkbox v-model="userProfile.reverseBlocking"  label="Hide messages from Users that have blocked you"></v-checkbox>
        <v-row no-gutters>
            <v-btn
                class="ml-auto mr-4"
                color="primary" 
                depressed
                @click="onSubmit"
                :disabled="!isValid"
            >UPDATE</v-btn>
        </v-row>
    </v-form>
</template>

<script>
import Vue from 'vue';

//@vuese
//Erlaubt, die User-Einstellungen zu bearbeiten.
export default {
    name: 'EditSettings',
    data: function(){
        return {
            isValid: false,
            userProfile: undefined,
        }
    },
    methods: {
        //@vuese
        //Aktualisiert die Einstellungen in Form eines "UserProfile" beim Server.
        onSubmit: function(){
            window.axios.put(Vue.prototype.$getApiUrl('http') + '/user-profiles', this.userProfile, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.$store.commit('setUser', this.user);
            }, () => {
                this.$router.push('/');
            });
        }
    },
    created: function(){
        this.userProfile = Object.assign({}, this.$store.state.userProfile);
        this.userProfile.user = {
            id: this.$store.state.userId
        };
    }

}
</script>