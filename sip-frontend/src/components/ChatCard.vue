<template>
    <v-card width="30%" class="rounded-xl" elevation="0">
        <div v-ripple class="clickable" @click="$router.push('/chat/' + $props.chat.id)">
            <v-row>
                <v-avatar size="60%" class="mx-auto mt-6 mb-0">
                    <img :src="imageUrl">
                </v-avatar>
            </v-row>
            <v-row>
                <v-card-title class="mx-auto">{{ this.$data.user.username}}</v-card-title>
            </v-row>
        </div>
        <v-divider class="mt-3 mb-9"></v-divider>
        <v-row v-if="this.$data.message">
            <v-sheet class="mx-auto mb-4 rounded-lg" width="85%" color="secondary darken-3">
                <div class="mx-3 mt-1">
                    <v-row class="secondary darken-2 rounded-lg mb-3">
                        <h3 v-if="this.$data.message.author.id == this.$store.state.userId" class="ml-3 colored-header">You:</h3>
                        <h3 v-else @click="$emit('show-user', $data.user)" class="ml-3 colored-header clickable">Them:</h3>
                    </v-row>
                    <p class="max-2-lines">{{message.content}}</p>
                </div>
            </v-sheet>
        </v-row>
    </v-card>
</template>
<script>
import Vue from 'vue'

export default {
    name: 'ChatCard',
    props: ['chat'],
    computed:{
        imageUrl: function(){
            return Vue.prototype.$apiBaseUrl + "/upload/images/" + this.$data.user.profilePicture + ".jpg";
        }
    },
    data: function(){
        return {
            user: this.$props.chat.user1.id == this.$store.state.userId ? this.$props.chat.user2 : this.$props.chat.user1 ,
            message: ''
        }
    },
    created: function(){
        window.axios.get(Vue.prototype.$apiBaseUrl + '/api/chat-messages/most-recent-by-chat/'+this.$props.chat.id, {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.message = response.data;
        }, (error) => {

            if(error.response.status == 403){
                this.$router.push('/login');
            }

        })
    }
    
}
</script>