<template>
    <v-card width="350" min-width="350" class="rounded-xl" elevation="0">
        <div v-ripple class="clickable" @click="$router.push('/chat/' + $props.chat.id)">
            <v-row>
                <v-avatar size="130" class="mx-auto mt-6 mb-0" color="primary">
                    <img v-if="$data.user.profilePicture" :src="imageUrl">
                    <span v-else class="headline">{{$data.user.username.substring(0,1)}}</span>
                </v-avatar>
            </v-row>
            <v-row>
                <v-card-title class="mx-auto">{{ this.$data.user.username}}</v-card-title>
            </v-row>
        </div>
        <v-divider class="mt-3 mb-9"></v-divider>
        <v-row>
            <v-sheet class="mx-auto mb-4 rounded-lg" width="85%" color="secondary darken-3" elevation="7">
                <div class="mx-3 mt-1">
                    <v-row v-if="this.$data.message" class="primary rounded-lg mb-3">
                        <h3 v-if="this.$data.message.authorId == this.$store.state.userId" class="ml-3 header">You:</h3>
                        <h3 v-else @click="$emit('show-user', $data.user)" class="ml-3 header clickable">Them:</h3>
                    </v-row>
                    <p v-if="this.$data.message" class="max-2-lines">{{message.content}}</p>
                    <p v-else-if="this.$data.message === undefined" class="ml-6 my-9 secondary--text">You have yet to chat with this user</p>
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
            return Vue.prototype.$apiBaseUrl + "/upload/pic/user/" + this.$data.user.profilePicture + ".jpg";
        }
    },
    data: function(){
        return {
            user: this.$props.chat.user1.id == this.$store.state.userId ? this.$props.chat.user2 : this.$props.chat.user1 ,
            message: false, //false: loading, undefined: no messages, message: message
        }
    },
    created: function(){
        window.axios.get(Vue.prototype.$apiBaseUrl + '/api/chat-messages', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            },
            params: {
                chat: this.$props.chat.id,
                count: 1,
            }
        }).then((response) => {
            if(response.data){
                this.$data.message = response.data[0];
            }
            else{
                this.$data.message = undefined;
            }
        }, (error) => {

            if(error.response.status == 403){
                this.$router.push('/login');
            }

        });
    }
    
}
</script>