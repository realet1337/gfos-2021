<template>
    <v-container>
        <ChatCard v-for="chat in chats" :key="chat.id" :chat="chat" @show-user="showUserDialog"></ChatCard>
        <UserProfileDialog :user="$data.selectedUser" ref="UserProfileDialog"></UserProfileDialog>
    </v-container>
</template>
<script>
import ChatCard from './ChatCard.vue'
import UserProfileDialog from './UserProfileDialog.vue'
import Vue from 'vue'

export default {
    name: 'HomeDirectChats',
    components:{
        ChatCard,
        UserProfileDialog,
    },
    data: function(){
        return {
            chats: [],
            selectedUser: false,
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
    },
    methods: {
        showUserDialog: function(user){

            this.$data.selectedUser = user;
            this.$refs.UserProfileDialog.show();

        }
    }

}
</script>