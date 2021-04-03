<template>
    <v-container fluid>
        <v-row v-for="(chatMessage, index) in chatMessages" :key="index" no-gutters>
            <!-- beeg -->
            <v-col v-if="index === 0 || chatMessages[index - 1].author.id !== chatMessage.author.id" cols="auto">
                <v-avatar color="primary" class="my-2">
                    <img v-if="chatMessage.author.profilePicture" :src="getImageUrl(chatMessage)">
                    <span v-else>{{chatMessage.author.username.substring(0,1)}}</span>
                </v-avatar>
            </v-col>
            <v-col v-if="index === 0 || chatMessages[index - 1].author.id !== chatMessage.author.id" cols="auto" align-self="center">
                <p class="ml-6 my-auto">
                    <b>{{chatMessage.author.username}}</b>
                </p>
                <p class="ml-6 my-auto">
                    {{chatMessage.content}}
                </p>
            </v-col>
            <!-- smol -->
            <v-col v-else>
                <p class="ml-16 pl-2 my-auto">
                    {{chatMessage.content}}
                </p>
            </v-col>
        </v-row>
    </v-container>
</template>
<script>
import Vue from 'vue'

export default {
    name: 'ChatWindow',
    data: function(){
        return {
            chatMessages: [],
            hasOldest: false,
            hasNewest: true,
        }
    },
    methods: {
        updateMessages: function(){
            window.axios.get(Vue.prototype.$apiBaseUrl + '/api/chat-messages', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                },
                params: {
                    chat: this.$route.params.chatId,
                    count: Vue.prototype.$messageChunkSize,
                }
            }).then((response) => {
                this.$data.chatMessages = response.data.reverse();
                if(response.data.length < Vue.prototype.$messageChunkSize){
                    this.hasOldest == true;
                }
            })
        },
        getImageUrl: function(chatMessage){
            return Vue.prototype.$apiBaseUrl + "/upload/pic/user/" + chatMessage.author.profilePicture + ".jpg";
        }
    },
    watch: {
        $route: function () {
            this.updateMessages();
        }
    },
    created: function(){
        this.updateMessages();
    },
}
</script>