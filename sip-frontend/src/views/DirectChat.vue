<template>
    <v-app v-if="$store.state.userId">
        <v-app-bar app clipped-left height="62">
            <v-row no-gutters>
                <v-col v-ripple style="width: 256px;" cols="auto" @click="$router.push('/home/direct-chats')" class="ml-n4 clickable">
                    <v-row justify="center" no-gutters>
                        <v-col class="mx-auto" cols="auto">
                            <img src="@/assets/sip.png" width="100">
                        </v-col>
                    </v-row>
                </v-col>
                <v-col cols="auto">
                    <v-divider vertical></v-divider>
                </v-col>
                <v-col cols="auto" v-if="$data.chat" align-self="center">
                    <v-avatar class="mx-2" color="primary">
                        <img v-if="$data.chat.notSelf.profilePicture" :src="$getAvatarUrl('user', $data.chat.notSelf)">
                        <span v-else class="headline">{{$data.chat.notSelf.username.substring(0,1)}}</span>
                    </v-avatar>
                </v-col>
            </v-row>
        </v-app-bar>
        <v-navigation-drawer app clipped floating permanent color="secondary darken-4">
            <v-list nav>
                <v-list-item v-for="chat in chats" :key="chat.id" @click="openChat(chat)">
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
                <ChatWindow @showUser="showUser" :key="$route.params.chatId" style="height: 89vh;"/>
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
            chat: undefined,
        }
    },
    methods: {
        showUser: function(user){
            this.$refs.userDialog.show(user);
        },
        openChat: function(chat){
            this.$data.chat = chat
            this.$router.push('/chat/' + chat.id);
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
            this.$data.find = response.data.find(chat => {
                if(chat.id == this.$route.params.chatId){
                    this.$data.chat = chat;
                }
            });
            if(!this.$data.chat){

                //can't open this chat
                this.$router.push('/home')
            }
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