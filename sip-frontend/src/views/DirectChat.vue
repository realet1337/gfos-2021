<template>
    <v-app v-if="$store.state.userId">
        <v-navigation-drawer app floating permanent color="secondary darken-4">
            <v-list>
                <v-list-item>
                    <v-list-item-title class="ml-1">
                        <h1>Chats:</h1>
                    </v-list-item-title>
                </v-list-item>
            </v-list>
            <v-divider class="mx-2"></v-divider>
            <v-list nav>
                <v-list-item v-for="chat in chats" :key="chat.id" @click="$router.push('/chat/' + chat.id)">
                    <v-list-item-avatar color="primary">
                        <img v-if="chat.notSelf.profilePicture" :src="$getAvatarUrl('user', chat.notSelf)">
                        <span v-else>{{chat.notSelf.username.substring(0,1)}}</span>
                    </v-list-item-avatar>
                    <v-list-item-title>
                        {{chat.notSelf.username}}
                    </v-list-item-title>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>
        <v-main>
            <v-container fluid>
                <ChatWindow @showUser="showUser" :key="$route.params.chatId" style="height: 96vh;"/>
                <UserProfileDialog ref="userDialog" @showUser="showUser"></UserProfileDialog>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>
import ChatWindow from '@/components/ChatWindow'
import Vue from'vue'
import UserProfileDialog from '@/components/UserProfileDialog'

export default {
    name: 'DirectChat',
    components: {
        ChatWindow,
        UserProfileDialog,
    },
    data: function(){
        return {
            chats: [],
            console: console
        }
    },
    methods: {
        showUser: function(user){
            this.$refs.userDialog.show(user);
        }
    },
    created: function(){
        window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$store.state.userId + '/direct-chats/', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            response.data.forEach(chat => {
                if(chat.user1.id == this.$store.state.userId){
                    chat.notSelf = chat.user2;
                }
                else{
                    chat.notSelf = chat.user1;
                }

                chat.user1 = undefined;
                chat.user2 = undefined;
            });
            this.$data.chats = response.data;
        },  
        (error) => {
            if(error.response.status === 403){
                if(error.response.data == "Unauthenticated"){
                    this.$router.push('/');
                }
                else if(error.response.data == "Unauthorized"){
                    this.$router.push('/home')
                }
            }
            else if(error.response.status === 404){
                this.$router.push('/home')
            }
        })
    }
}
</script>