<template>
    <v-snackbar v-if="$data.chatMessage" app :value="$data.show"
    right 
    top
    transition="scroll-y-transition"
    color="secondary darken-4"
    timeout="-1"
    >
        <v-row>
            <v-col cols="auto">
                <v-avatar color="primary" size="40">
                    <img v-if="chatMessage.author.profilePicture" :src="$getAvatarUrl('user', chatMessage.author)">
                    <span v-else>{{chatMessage.author.username.substring(0,1)}}</span>
                </v-avatar>
            </v-col>
            <v-col>
                <v-row no-gutters>
                    <h3>{{$data.chatMessage.author.username}}</h3>
                </v-row>
                <v-row no-gutters>
                    <p class="mb-n4" style="max-width: 250px; overflow:hidden; text-overflow: ellipsis;">{{$data.chatMessage.content}}</p>
                </v-row>
            </v-col>
        </v-row>
        <template v-slot:action="{ attrs }">
            <v-btn
                color="primary"
                text
                v-bind="attrs"
                @click="openChat"
            >
                Show
            </v-btn>
        </template>
    </v-snackbar>
</template>

<script>
import Vue from 'vue'

export default{
    name: 'MessageAlerts',
    data: function(){
        return {
            chatMessage: undefined,
            timeout: 0,
            show: false,
        }
    },
    created: function(){
        this.$eventHub.$on('new-message', this.onNewMessage);
    },
    beforeDestroy: function(){
        this.$eventHub.$off('new-message', this.onNewMessage);
    },
    methods: {
        onNewMessage: function(chatMessage){
            if(chatMessage.author.id != this.$store.state.userId && chatMessage.chat.id != this.$route.params.chatId){
                this.$data.chatMessage = chatMessage;
                this.$data.show = true;
                this.$data.timeout = new Date().getTime() + Vue.prototype.$notificationTimeout;
                var _this = this;
                setTimeout(function(){
                    if(_this.$data.timeout <= new Date().getTime()){
                        _this.$data.show = false; 
                    }
                }, Vue.prototype.$notificationTimeout);
            }
        },
        openChat: function(){            
            this.$emit('open-chat', this.$data.chatMessage.chat);
            this.$data.show = false;
        }
    }
}
</script>