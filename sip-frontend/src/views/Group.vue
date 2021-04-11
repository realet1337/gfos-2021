<template>
    <v-app v-if="$store.state.initialized">
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
                    <v-avatar @click="showUser($data.chat.notSelf)" class="clickable mx-2" color="primary">
                        <img v-if="$data.chat.notSelf.profilePicture" :src="$getAvatarUrl('user', $data.chat.notSelf)">
                        <span v-else class="headline">{{$data.chat.notSelf.username.substring(0,1)}}</span>
                    </v-avatar>
                </v-col>
                <v-col v-if="$data.chat" cols="auto" align-self="center" class="ml-3">
                    <v-row no-gutters class="my-auto">
                        <h3 @click="showUser($data.chat.notSelf)" class="clickable">
                        {{$data.chat.notSelf.username}}
                        </h3>
                    </v-row>
                </v-col>
                <v-col v-if="$data.chat" cols="auto" align-self="center" class="ml-5 mr-1">
                    <v-icon size="10" :color="$data.chat.notSelf.isOnline ? 'green' : 'red'">mdi-checkbox-blank-circle</v-icon>
                </v-col>
                <v-col v-if="$data.chat" cols="auto" align-self="center">
                    <p :class="'my-auto ' + ($data.chat.notSelf.isOnline ? 'green--text ' : 'red--text ')">{{$data.chat.notSelf.isOnline ? 'ONLINE' : 'OFFLINE'}}</p>
                </v-col>
            </v-row>
        </v-app-bar>
        <v-navigation-drawer app clipped floating permanent color="secondary darken-4">
            <v-list nav>
                <v-list-item-group v-model="$data.chatIndex">
                    <v-list-item v-for="chat in chats" :key="chat.id" @click="openChat(chat)">
                        <v-badge bottom dot bordered offset-x="25" offset-y="20" :color="chat.notSelf.isOnline ? 'green' : 'red'">
                            <v-list-item-avatar color="primary" class="ml-0" >
                                <img v-if="chat.notSelf.profilePicture" :src="$getAvatarUrl('user', chat.notSelf)">
                                <span v-else>{{chat.notSelf.username.substring(0,1)}}</span>
                            </v-list-item-avatar>
                        </v-badge>
                        <v-list-item-title>
                            {{chat.notSelf.username}}
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
                <UserProfileDialog ref="userDialog" @showUser="showUser"></UserProfileDialog>
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
        }
    },
    methods: {
        showUser: function(user){
            this.$refs.userDialog.show(user);
        },
        openChat: function(chat){
            if(!chat.notSelf){
                this.findNotSelf(chat);
            }
            this.$data.chat = chat;
            this.$data.chatIndex = this.$data.chats.findIndex(listChat => chat.id == listChat.id);
            this.$router.push('/chat/' + chat.id);
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
        if(this.$route.params.chatId){
            console.log("pong!");
        }
    },
    beforeDestroy: function(){
    }
}
</script>