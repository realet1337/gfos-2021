<template>
    <div class="d-flex flex-column fill-height">

        <!-- message section -->
        <div class="flex-grow-1 overflow-y-auto" id="messages">
            <div v-for="(chatMessage, index) in chatMessages" :key="chatMessage.id" no-gutters>
                <v-row v-if="index === 0 || new Date(chatMessages[index - 1].sent).getDate() < new Date(chatMessage.sent).getDate()" no-gutters>
                    <v-divider class="mt-3 mb-1"></v-divider>
                    <span class="date-divider-label mx-2">{{new Date(chatMessage.sent).toDateString()}}</span>
                    <v-divider inset class="mt-3 ml-0 mb-1"></v-divider>
                </v-row>
                <v-row v-if="index === 0 || chatMessages[index - 1].author.id !== chatMessage.author.id
                || new Date(chatMessages[index - 1].sent).getDate() < new Date(chatMessage.sent).getDate()" :class="'mb-4 ' + 'mt-5'" no-gutters>
                    <!-- beeg -->
                    <v-col cols="auto">
                        <v-avatar @click="$emit('showUser', chatMessage.author)" color="primary" class="clickable" size="48">
                            <img v-if="chatMessage.author.profilePicture" :src="getImageUrl(chatMessage)">
                            <span v-else>{{chatMessage.author.username.substring(0,1)}}</span>
                        </v-avatar>
                    </v-col>
                    <v-col cols="auto" align-self="center">
                        <p @click="$emit('showUser', chatMessage.author)" class="ml-4 my-auto clickable underline-on-hover">
                            <b>{{chatMessage.author.username}}</b>
                        </p>
                        <p class="ml-4 my-auto">
                            {{chatMessage.content}}
                        </p>
                    </v-col>
                </v-row>

                <!-- smol -->
                <v-row class="ml-16 mt-n3" v-else no-gutters>    
                    <v-col>
                        <p>
                            {{chatMessage.content}}
                        </p>
                    </v-col>
                </v-row>
            </div>
        </div>

        <div class="flex-shrink-1 mt-3 mb-n4">
            <v-row no-gutters>
                <v-col>
                    <v-text-field
                    @keyup.enter="sendMessage"
                    v-model="message"
                    outlined></v-text-field>
                </v-col>
            </v-row>
        </div>

    </div>
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
            showNewest: true,
            message: "",
        }
    },
    methods: {
        updateMessages: function(){
            window.axios.get(Vue.prototype.$apiBaseUrl + '/api/chats/' + this.$route.params.chatId + '/chat-messages', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                },
                params: {
                    count: Vue.prototype.$messageChunkSize,
                }
            }).then((response) => {
                this.$data.chatMessages = response.data.reverse();
                if(response.data.length < Vue.prototype.$messageChunkSize){
                    this.$data.hasOldest = true;
                }

                if(this.$data.showNewest){
                    //wait for div to update
                    this.scrollDownMessages();   
                }

            })
        },
        getImageUrl: function(chatMessage){
            return Vue.prototype.$apiBaseUrl + "/upload/pic/user/" + chatMessage.author.profilePicture + ".jpg";
        },
        sendMessage: function(){
            window.axios.post(Vue.prototype.$apiBaseUrl + '/api/chats/' + this.$route.params.chatId + '/chat-messages',
            {
                    content: this.$data.message,
                },
            {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.$data.message = "";
            })
        },
        scrollDownMessages: function(){
            this.$nextTick( function(){
                        var msgDiv = document.getElementById('messages');
                        msgDiv.scrollTop = msgDiv.scrollHeight;
            });
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