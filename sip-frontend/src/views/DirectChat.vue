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
                <!-- why do we use :key here? We want to force a re-render on array change. See vuetify bug. Dirty workaround! -->
                <!-- https://github.com/vuetifyjs/vuetify/issues/11405 -->
                <v-list-item-group v-model="$data.chatIndex" :key="$data.chats.map(chat => chat.id).join()" mandatory>
                    <v-list-item v-for="chat in chats" :key="chat.id" @click="openChat(chat)">
                        <v-badge bottom dot bordered offset-x="10" offset-y="10" :color="chat.notSelf.isOnline ? 'green' : 'red'">
                            <!-- i would have ****loved**** to use <v-list-item-avatar> instead of <v-avatar> here but for SOME REASON
                            that suddenly broke. The text is shifted to the very left of the avatar. Why? No idea. It worked before... -->
                            <v-avatar color="primary" class="ml-0" size="40">
                                <img v-if="chat.notSelf.profilePicture" :src="$getAvatarUrl('user', chat.notSelf)">
                                <span v-else >{{chat.notSelf.username.substring(0,1)}}</span>
                            </v-avatar>
                        </v-badge>
                        <v-list-item-title class="ml-2">
                            {{chat.notSelf.username}}
                        </v-list-item-title>
                    </v-list-item>
                </v-list-item-group>
                <v-divider class="my-2"></v-divider>
                <v-list-item @click="showUserFinder">
                    <v-avatar color="secondary darken-4" class="ml-0" size="40">
                        <span>+</span>
                    </v-avatar>
                    <v-list-item-title class="ml-2">
                        Start a conversation
                    </v-list-item-title>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>
        <v-main>
            <MessageAlerts style="position: fixed;" @open-chat="openChat"></MessageAlerts>
            <v-container fluid>
                <!-- the reason we are not checking for blockedBy here is that we dont want other users trolling us by blocking/unblocking us, reloading our Chatwindow every time -->
                <ChatWindow @show-User="showUser" :key="$route.params.chatId + $store.state.blockedUsers" style="height: 89vh;"/>
                <UserProfileDialog ref="userDialog" @open-direct-chat="openDirectChat" @open-group="openGroup"></UserProfileDialog>
                <UserFinderDialog ref="finderDialog" @show-user="showUser"></UserFinderDialog>
            </v-container>
        </v-main>
    </v-app>
    <v-app v-else>
        <LoadingScreen></LoadingScreen>
    </v-app>
</template>

<script>
import ChatWindow from '@/components/ChatWindow'
import Vue from'vue'
import UserProfileDialog from '@/components/UserProfileDialog'
import MessageAlerts from '@/components/MessageAlerts'
import LoadingScreen from '@/components/LoadingScreen'
import UserFinderDialog from '@/components/UserFinderDialog'

export default {
    name: 'DirectChat',
    components: {
        ChatWindow,
        UserProfileDialog,
        MessageAlerts,
        LoadingScreen,
        UserFinderDialog
    },
    data: function(){
        return {
            chats: [],
            chat: undefined,
            chatIndex: 0,
        }
    },
    methods: {
        showUser: function(user){
            this.$refs.userDialog.show(user);
        },
        openChat: function(chat){
            if(!chat.name){
                this.openDirectChat(chat);
            }
            else{
                this.$router.push('/group/' + chat.group.id + '/chat/' + chat.id);
            }
        },
        openDirectChat: function(chat){
            if(chat.id != this.$data.chat.id){
                if(!chat.notSelf){
                    this.findNotSelf(chat);
                }
                this.$data.chat = chat;
                this.$data.chatIndex = this.$data.chats.findIndex(findChat => chat.id == findChat.id);
                this.$router.push('/chat/' + chat.id);
            }
        },
        openGroup: function(group){
            this.$router.push('/group/' + group.id);
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
        onNewMessage: function(message){
            //prepare for usage
            this.findNotSelf(message.chat);

            var chatIndex = this.$data.chats.findIndex(chat => chat.id == message.chat.id);
            if(chatIndex != -1){
                this.$data.chats.splice(chatIndex,1);
                this.$data.chats = [message.chat].concat(this.$data.chats);

            }

            //fix chatIndex
            this.$data.chatIndex = this.$data.chats.findIndex(chat => this.$data.chat.id == chat.id);
        },
        showUserFinder: function(){
            this.$refs.finderDialog.show();
        }
    },
    created: function(){
        window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$store.state.userId + '/direct-chats/', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            response.data.forEach(chat => {
                this.findNotSelf(chat);
            });
            var chatIndex = response.data.findIndex(chat => chat.id == this.$route.params.chatId);
            if(chatIndex == -1){

                //can't open this chat
                this.$router.push('/home');
                return;
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
        this.$eventHub.$on('new-message', this.onNewMessage);
    },
    beforeDestroy: function(){
        this.$eventHub.$off('new-message', this.onNewMessage);
    }
}
</script>