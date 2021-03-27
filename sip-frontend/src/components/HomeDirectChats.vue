<template>
    <v-container>
        <ChatCard v-for="chat in chats" :key="chat.id" :chat="chat"></ChatCard>
    </v-container>
</template>
<script>
import ChatCard from './ChatCard.vue'
import Vue from 'vue'

export default {
    name: 'HomeDirectChats',
    components:{
        ChatCard,
    },
    data: function(){
        return {
            chats: [],
        }
    },
    created: function(){

        window.axios.get(Vue.prototype.$apiBaseUrl + '/api/chats/directchats-by-user/' + this.$store.state.userId, {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.chats = response.data;
        })
    }

}
</script>