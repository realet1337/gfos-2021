<template>
    <v-app v-if="$store.state.initialized">
        <v-app-bar app hide-on-scroll height="62">
            <v-row no-gutters :class="$vuetify.breakpoint.xs ? 'mx-4' : ''">
                <!-- spacer col so the log is centered -->
                <v-col cols="1">
                </v-col>
                <v-col cols="auto" class="mx-auto">
                    <img src="@/assets/sip-banner.png" width="200">
                </v-col>
                <v-col v-if="user" cols="1" align-self="center">
                    <v-row no-gutters>
                        <v-menu
                            :close-on-content-click="false"
                            :nudge-width="200"
                            offset-x
                            offset-y
                        >
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn 
                                    fab 
                                    icon
                                    v-bind="attrs"
                                    v-on="on"
                                    class="ml-auto"
                                >
                                    <v-avatar class="clickable" color="primary">
                                        <img v-if="user.profilePicture" :src="$getAvatarUrl('user', user)">
                                        <span v-else>{{user.username.substring(0,1)}}</span>
                                    </v-avatar>
                                </v-btn>
                            </template>
                            <v-list>
                                <v-list-item class="mb-2">
                                    <v-avatar color="primary" size="40" class="mr-2">
                                        <img v-if="user.profilePicture" :src="$getAvatarUrl('user', user)">
                                        <span style="font-size: 20px;" v-else>{{user.username.substring(0,1)}}</span>
                                    </v-avatar>
                                    <v-list-item-content>
                                        <v-list-item-title>{{user.username}}</v-list-item-title>
                                        <v-list-item-subtitle><b>ID:</b> {{user.id}}</v-list-item-subtitle>
                                    </v-list-item-content>
                                </v-list-item>
                                <v-divider></v-divider>
                                <v-list-item to="/edit/profile" class="mt-2">
                                    <v-list-item-icon>
                                        <v-icon>
                                            mdi-pencil
                                        </v-icon>
                                    </v-list-item-icon>
                                    <v-list-item-content>
                                        <v-list-item-title>Edit profile</v-list-item-title>
                                    </v-list-item-content>
                                </v-list-item>
                                <v-list-item to="/edit/settings" class="mb-2">
                                    <v-list-item-icon>
                                        <v-icon>
                                            mdi-cog
                                        </v-icon>
                                    </v-list-item-icon>
                                    <v-list-item-content>
                                        <v-list-item-title>Settings</v-list-item-title>
                                    </v-list-item-content>
                                </v-list-item>
                                <v-divider></v-divider>
                                <v-list-item @click="logout" class="mt-2">
                                    <v-list-item-icon>
                                        <v-icon color="red">
                                            mdi-logout
                                        </v-icon>
                                    </v-list-item-icon>
                                    <v-list-item-content>
                                        <v-list-item-title class="red--text">Logout</v-list-item-title>
                                    </v-list-item-content>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-row>
                </v-col>
            </v-row>
            <template v-slot:extension>
                <v-tabs centered>
                    <v-tab to="/home/direct-chats">DIRECT CHATS</v-tab>
                    <v-tab to="/home/groups">GROUPS</v-tab>
                </v-tabs>
            </template>
        </v-app-bar>
        <v-main>
            <MessageAlerts style="position: fixed;" @open-chat="openChat"></MessageAlerts>
            <router-view></router-view>
        </v-main>
    </v-app>
    <v-app v-else>
        <LoadingScreen></LoadingScreen>
    </v-app>
</template>

<script>
import LoadingScreen from '@/components/LoadingScreen'
import MessageAlerts from '@/components/MessageAlerts'
import Vue from 'vue'

export default {
    name: 'Home',
    components: {
        LoadingScreen,
        MessageAlerts
    },
    data: function(){
        return {
            user: undefined,
        }
    },
    methods:{
        openChat: function(chat){
            if(chat.name){
                this.$router.push('/group/' + chat.group.id + '/chat/' + chat.id);
            }
            else{
                this.$router.push('/chat/' + chat.id);
            }
        },
        fetchUser: function(){
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$store.state.userId, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.user = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        logout: function(){
            window.axios.post(Vue.prototype.$apiHttpUrl + '/api/auth/logout', 
                new URLSearchParams({
                    'token': this.$store.state.token,
                }).toString()
            ).then( () => {

                this.$router.push('/');
				document.cookie = 'token=; Max-Age=-99999999;';

            }, () => {

                //probably invalid session
                this.$router.push('/');

            });
        }
    },
    created: function(){
        this.fetchUser();
    }
}
</script>