<template>
    <v-app v-if="$store.state.initialized">
        <v-app-bar app clipped-left clipped-right height="62">
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
                    <v-btn v-if="isAdmin" width="35" height="35" icon class="my-auto" @click="$router.push('/group/' + $route.params.groupId + '/edit/overview')">
                        <v-icon color="secondary lighten-2">mdi-pencil</v-icon>
                    </v-btn>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-btn width="35" height="35" icon class="my-auto" @click="toggleUserDrawer">
                        <v-icon color="secondary lighten-2">mdi-account-multiple</v-icon>
                    </v-btn>
                </v-col>
            </v-row>
        </v-app-bar>
        <v-navigation-drawer app clipped floating permanent color="secondary darken-4">
            <v-row
                class="fill-height"
                no-gutters
            >
                <v-navigation-drawer
                    mini-variant
                    mini-variant-width="56"
                    permanent
                    color="secondary darken-3"
                    floating
                >
                    <v-list style="max-height: 100%;" class="hide-scrollbar overflow-x-hidden">
                        <v-list-item v-for="group in groups" :key="group.id" @click="openGroup(group)" :input-value="$data.group.id === group.id">
                            <v-tooltip right>
                                <template v-slot:activator="{ on, attrs }">
                                    <v-avatar 
                                        size="40" 
                                        class="ml-n2"
                                        v-bind="attrs"
                                        v-on="on"
                                        color="primary"
                                    >
                                        <img v-if="group.picture" :src="$getAvatarUrl('group', group)">
                                        <span v-else>{{group.name.substring(0,1)}}</span>
                                    </v-avatar>
                                </template>
                                {{group.name}}
                            </v-tooltip>
                        </v-list-item>
                        <v-divider class="my-2"></v-divider>
                        <v-list-item @click="showGroupCreator">
                            <v-avatar
                                size="40" 
                                class="ml-n2"
                                color="secondary darken-4"
                            >
                                <span class="primary--text text-h6">+</span>
                            </v-avatar>
                        </v-list-item>
                    </v-list>
                </v-navigation-drawer>
                <v-list nav dense style="max-height: 100%;" width="100" class="overflow-y-auto overflow-x-hidden grow">
                    <v-row class="mt-1 mb-2 ml-2">
                        <span class="secondary--text text--lighten-4" style="font-size: 14px;"><b>CHATS</b></span>
                    </v-row>
                    <v-list-item v-for="chat in chats" :key="chat.id" @click="openChat(chat)" :input-value="$data.chat.id === chat.id">
                        <v-list-item-icon>
                            <v-icon class="ml-2" color="secondary">
                                mdi-message-text
                            </v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>
                            {{chat.name}}
                        </v-list-item-title>
                        <v-list-item-action>
                            <v-menu
                                rounded="lg"
                                offset-y
                                offset-x
                                v-if="isAdmin && chats.length > 1"
                            >
                                <template v-slot:activator="{ on }">
                                    <v-btn icon width="20" height="20" v-on="on" >
                                        <v-icon size="20" color="grey lighten-1">mdi-dots-vertical</v-icon>
                                    </v-btn>
                                </template>
                                <v-list
                                nav
                                dense
                                >
                                    <v-list-item
                                        link
                                    >
                                        <v-list-item-content
                                            style="min-width: 100px;"
                                            @click="editChat(chat)"
                                        >Edit</v-list-item-content>
                                    </v-list-item>
                                    <v-list-item
                                        link
                                    >
                                        <v-list-item-content
                                            class="red--text text--lighten-2"
                                            style="min-width: 100px;"
                                            @click="deleteChat(chat)"
                                        >Delete</v-list-item-content>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </v-list-item-action>
                    </v-list-item>
                    <template v-if="isAdmin">
                        <v-divider class="my-2"></v-divider>
                        <v-list-item @click="createChat">
                            <v-icon class="ml-2 mr-6">mdi-plus</v-icon>
                            <v-list-item-title>
                                Add chat
                            </v-list-item-title>
                        </v-list-item>
                    </template>
                </v-list>
            </v-row>
        </v-navigation-drawer>
        <v-navigation-drawer app clipped floating  right color="grey darken-4" :value="$data.showUserDrawer">
            <v-list nav dense v-if="group">
                <span class="label-text"><b>OWNER</b></span>
                <v-list-item @click="showUser(group.owner)">
                    <v-avatar color="primary" size="30" class=" mr-2">
                        <img v-if="group.owner.profilePicture" :src="$getAvatarUrl('user', group.owner)">
                        <span v-else class="text-body-2">{{group.owner.username.substring(0,1)}}</span>
                    </v-avatar>
                    <v-list-item-title>{{group.owner.username}}</v-list-item-title>
                </v-list-item>
            </v-list>
            <v-divider></v-divider>
            <v-list nav dense v-for="role in rolesWithMembers" :key="role.id">
                <span class="label-text"><b>{{role.name.toUpperCase()}}</b></span>
                <v-list-item v-for="user in role.users" :key="user.id"
                @click="showUser(user)">
                    <v-avatar color="primary" size="30" class=" mr-2">
                        <img v-if="user.profilePicture" :src="$getAvatarUrl('user', user)">
                        <span v-else class="text-body-2">{{user.username.substring(0,1)}}</span>
                    </v-avatar>
                    <v-list-item-title 
                    :style="'color: ' + role.color"
                    >{{user.username}}</v-list-item-title>
                </v-list-item>
            </v-list>
            <v-list v-if="basicUsers != false" nav dense>
                <span class="label-text"><b>NO ROLE</b></span>
                <v-list-item v-for="user in basicUsers" :key="user.id" @click="showUser(user)">
                    <v-avatar color="primary" size="30" class=" mr-2">
                        <img v-if="user.profilePicture" :src="$getAvatarUrl('user', user)">
                        <span v-else class="text-body-2">{{user.username.substring(0,1)}}</span>
                    </v-avatar>
                    <v-list-item-title 
                    >{{user.username}}</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>
        <v-main>
            <MessageAlerts style="position: fixed;" @open-chat="openChat"></MessageAlerts>
            <v-container fluid>
                <!-- the reason we are not checking for blockedBy here is that we dont want other users trolling us by blocking/unblocking us, reloading our Chatwindow every time -->
                <ChatWindow v-if="chat" @show-user="showUser" :key="$route.params.chatId + $store.state.blockedUsers" style="height: 89vh;"/>
                <UserProfileDialog ref="userDialog" @open-direct-chat="openDirectChat" @open-group="openGroup"></UserProfileDialog>
                <GroupCreatorDialog ref="creatorDialog" @open-group-id="openGroupId"/>
                <!-- maybe change to manual adding later so we don't reload every time -->
                <ChatEditorDialog ref="chatEditorDialog" @chat-updated="this.initGroup" @chat-created="this.initGroup"></ChatEditorDialog>
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
import GroupCreatorDialog from '@/components/GroupCreatorDialog'
import ChatEditorDialog from '@/components/ChatEditorDialog'
import Vue from 'vue'

export default {
    name: 'Group',
    components: {
        ChatWindow,
        UserProfileDialog,
        MessageAlerts,
        LoadingScreen,
        GroupCreatorDialog,
        ChatEditorDialog
    },
    data: function(){
        return {
            chats: [],
            roles: [],
            basicUsers: [],
            chat: undefined,
            group: undefined,
            groups: [],
            showUserDrawer: false,
            dummy: 0
        }
    },
    methods: {
        toggleUserDrawer: function(){
            this.$data.showUserDrawer = !this.$data.showUserDrawer;
        },
        showUser: function(user){
            this.$refs.userDialog.show(user);
        },
        openDirectChat: function(chat){
            this.$router.push('/chat/' + chat.id);
        },
        openGroup: function(group){
            if(group.id !== this.$data.group.id){
                this.$router.push('/group/' + group.id);
                this.group = group;
                this.resetView();
                this.initGroup();
                if(!this.groups.some(tmpGroup => tmpGroup.id === group.id)){
                    this.getGroups();
                }
            }
        },
        openChat: function(chat){

            if(chat.id !== this.$data.chat.id){

                if(!chat.name){
                    this.openDirectChat(chat);
                }
                else{
                    var chatIndex = this.$data.chats.findIndex(listChat => listChat.id == chat.id);
                    if(chatIndex === -1){
                        this.$router.push('/group/' + chat.group.id + '/chat/' + chat.id);
                        this.resetView();
                        this.initGroup();
                    }
                    else{
                        this.$data.chatIndex = chatIndex;
                        this.$data.chat = this.$data.chats[chatIndex];
                    }
                    this.$router.push('/group/' + chat.group.id + '/chat/' + chat.id);
                }
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
        initGroup: function(){
            //get chats
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId + '/chats', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                var chatIndex = response.data.findIndex(chat => chat.id == this.$route.params.chatId);
                if(chatIndex == -1){

                    //invalid / no chat
                    chatIndex = 0;
                    if(!this.$route.params.chatId){
                        this.$router.push(this.$route.path + '/chat/' + response.data[0].id);
                    }
                    else{
                        this.$router.push('/group/' + this.$route.params.groupId + '/chat/' + response.data[0].id);
                    }
                }
                this.$data.chat = response.data[chatIndex];
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
            });

            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId + '/basic-users', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.basicUsers = response.data;
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
            });

            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId + '/roles', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                //lots of useless traffic here https://youtu.be/B4wUsDozedE
                // 4 nested loops lmao
                for(var i = 0; i < response.data.length; i++){
                    response.data[i].users.forEach(user => {
                        for(var j = i +1; j < response.data.length; j++){
                            var index = response.data[j].users.findIndex(subUser => user.id == subUser.id);
                            if(index !== -1){
                                response.data[j].users.splice(index, 1);
                            } 
                        }
                    });
                }
                this.$data.roles = response.data;
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
            });
        },
        resetView: function(){
            this.$data.chats = [];
            this.$data.chat = undefined;
            this.$data.roles = [];
            this.$data.showUserDrawer = false;
        },
        getGroups: function(){
            //get groups
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$store.state.userId + '/groups', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                var groupIndex = response.data.findIndex(group => group.id == this.$route.params.groupId);
                if(groupIndex == -1){

                    //invalid / no chat
                    groupIndex = 0;
                    this.$router.push('/home/groups');
                }
                this.$data.group = response.data[groupIndex];
                this.$data.groups = response.data;
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
            });
        },
        openGroupId: function(id){
            var groupIndex = this.groups.findIndex(group => group.id === id);
            if(groupIndex === -1){
                this.resetView();
                this.$router.push('/group/' + id);
                this.getGroups();
                this.initGroup();
                
            }
            else{
                //this is useless but its nice having some safety
                this.group = this.groups[groupIndex];
                this.$router.push('/group/' + id);
                this.resetView();
                this.initGroup();
            }
        },
        showGroupCreator: function(){
            this.$refs.creatorDialog.show();
        },
        createChat: function(){
            this.$refs.chatEditorDialog.show();
        },
        editChat: function(chat){
            this.$refs.chatEditorDialog.show(chat);
        },
        deleteChat: function(chat){
            window.axios.delete(Vue.prototype.$apiHttpUrl + '/api/chats/' + chat.id, {
                headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.initGroup();
            }, () => {
                //shouldn't fail
            });
        },
    },
    created: function(){

        this.getGroups();
        this.initGroup();

    },
    computed: {
        isAdmin: function(){
            if(this.group === undefined){
                return false;
            }
            var tmpGroup = this.group;
            var tmpRoles = this.roles;
            if(tmpGroup.owner.id === this.$store.state.userId){
                return true;
            }
            for(var i = 0; i < tmpRoles.length; i++){
                if(tmpRoles[i].admin){
                    for(var j = 0; j < tmpRoles[i].users.length; j++){
                        if(tmpRoles[i].users[j].id === this.$store.state.userId){
                            return true;
                        }
                    }
                }
            }
            return false;
        },
        rolesWithMembers: function(){
            return this.roles.filter(role => role.users.length > 0);
        }
    }
}
</script>