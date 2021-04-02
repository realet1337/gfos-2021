<template>
    <v-container>
        <v-row justify="space-between">
            <v-col v-for="chat in chats" :key="chat.id" cols="auto">
                <ChatCard :chat="chat" @show-user="showUserDialog" class="my-4"></ChatCard>
            </v-col>
        </v-row>
        <v-dialog min-width="550" width="850" v-model="userDialogIsOpen">
            <UserProfileDialog v-if="userDialogIsOpen" :user="$data.selectedUser"></UserProfileDialog>
        </v-dialog>
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
            userDialogIsOpen: false,
        }
    },
    created: function(){

        window.axios.get(Vue.prototype.$apiBaseUrl + '/api/chats/directchats-by-user/' + this.$store.state.userId, {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.chats = response.data;
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
        })
    },
    methods: {
        showUserDialog: function(user){

            this.$data.selectedUser = user;
            this.$data.userDialogIsOpen = true;

        }
    }

}
</script>