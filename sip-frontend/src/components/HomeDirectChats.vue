<template>
    <v-container>
        <v-row justify="center" >
            <v-col v-for="chat in chats" :key="chat.id" cols="auto">
                <ChatCard :chat="chat" @show-user="showUserDialog" class="my-4"></ChatCard>
            </v-col>
        </v-row>
            <UserProfileDialog ref="userDialog"></UserProfileDialog>
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

        window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$store.state.userId + '/direct-chats/', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.chats = response.data;
        })
    },
    methods: {
        showUserDialog: function(user){

            this.$refs.userDialog.show(user);

        }
    }

}
</script>