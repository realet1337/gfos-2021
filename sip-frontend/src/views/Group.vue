<template>
    <v-app v-if="$store.state.initialized">
        <v-app-bar app clipped-left height="62">
            <v-row no-gutters>
                <v-col v-ripple style="width: 256px;" cols="auto" @click="$router.push('/home/groups')" class="ml-n4 clickable">
                    <v-row justify="center" no-gutters>
                        <v-col class="mx-auto" cols="auto">
                            <img src="@/assets/sip.png" width="100">
                        </v-col>
                    </v-row>
                </v-col>
                <v-col cols="auto">
                    <v-divider vertical></v-divider>
                </v-col>
                <v-col v-if="$data.chat" cols="auto" align-self="center" class="ml-3">
                    <v-row no-gutters class="my-auto">
                        <v-icon class="mx-2" color="secondary">
                            mdi-message-text
                        </v-icon>
                        <h3>
                        {{$data.chat.name}}
                        </h3>
                    </v-row>
                </v-col>
                <v-col class="ml-auto" cols="auto" align-self="center">
                    <v-icon class="my-auto" color="secondary lighten-2" @click="toggleUserDrawer">mdi-account-multiple</v-icon>
                </v-col>
            </v-row>
        </v-app-bar>
        <v-navigation-drawer app clipped floating permanent color="secondary darken-4">
            <v-list nav dense>
                <v-list-item-group v-model="$data.chatIndex" mandatory>
                    <v-list-item v-for="chat in chats" :key="chat.id" @click="openChat(chat)">
                        <v-list-item-icon>
                            <v-icon class="ml-2" color="secondary">
                                mdi-message-text
                            </v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>
                            {{chat.name}}
                        </v-list-item-title>
                    </v-list-item>
                </v-list-item-group>
            </v-list>
        </v-navigation-drawer>
        <v-main>
            <MessageAlerts style="position: fixed;" @open-chat="openChat"></MessageAlerts>
            <v-container fluid>
                <!-- the reason we are not checking for blockedBy here is that we dont want other users trolling us by blocking/unblocking us, reloading our Chatwindow every time -->
                <ChatWindow v-if="$route.params.chatId" @showUser="showUser" :key="$route.params.chatId + $store.state.blockedUsers" style="height: 89vh;"/>
                <UserProfileDialog ref="userDialog" @open-direct-chat="openDirectChat" @open-group="openGroup"></UserProfileDialog>
            </v-container>
        </v-main>
    </v-app>
    <v-app v-else>
        <LoadingScreen></LoadingScreen>
    </v-app>
</template>

<script>
import ChatWindow from '@/components/ChatWindow'
import UserProfileDialog from '@/components/UserProfileDialog'
import MessageAlerts from '@/components/MessageAlerts'
import LoadingScreen from '@/components/LoadingScreen'
import Vue from 'vue'

export default {
    name: 'DirectChat',
    components: {
        ChatWindow,
        UserProfileDialog,
        MessageAlerts,
        LoadingScreen
    },
    data: function(){
        return {
            chats: [],
            chat: undefined,
            chatIndex: 0,
            groupIndex: 0,
            group: undefined,
        }
    },
    methods: {
        toggleUserDrawer: function(){
            console.log("Toggling user drawer: IMPLEMENT!");
        },
        showUser: function(user){
            this.$refs.userDialog.show(user);
        },
        openDirectChat: function(chat){
            this.$router.push('/chat/' + chat.id);
        },
        openGroup: function(group){
            
            //FIXME: doesn't work, just a placeholder
            this.$router.push('/group/' + group.id);
        },
        openChat: function(chat){

            //FIXME: doesn't work, just a placeholder
            if(!chat.name){
                this.openDirectChat(chat);
            }
            else{
                this.$router.push('/group/' + chat.group.id + '/chat/' + chat.id);
            }
        },
        findNotSelf: function(chat){
            if(chat.user1.id == this.$store.state.userId){
                chat.notSelf = chat.user2;
            }
            else{
                chat.notSelf = chat.user1;
            }
            return chat;
        },
    },
    created: function(){
        window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId + '/chats/', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            var chatIndex = response.data.findIndex(chat => chat.id == this.$route.params.chatId);
            if(chatIndex == -1){

                //invalid / no chat
                chatIndex = 0;
                this.$router.push(this.$route.path + '/chat/' + response.data[0].id);
            }
            this.$data.chatIndex = chatIndex;
            this.$data.chat = response.data[this.$data.chatIndex];
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
    },
    beforeDestroy: function(){
    }
}
</script>