<template>
    <v-main>
        <v-container fluid v-if="$store.state.userId" class="ma-0">
            <v-row no-gutters>
                <v-col cols="auto">
                    <div style="max-width:250px; min-width:250px">
                        <v-list dense>
                            <v-list-item v-for="chat in $data.chats" :key = "chat.id">
                                <v-list-item-avatar color="primary">
                                    <img v-if="chat.notSelf.profilePicture" :src="$getAvatarUrl('user', chat.notSelf)">
                                    <span v-else>{{chat.notSelf.username.substring(0,1)}}</span>
                                </v-list-item-avatar>
                                <v-list-item-title>
                                    {{chat.notSelf.username}}
                                </v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </div>
                </v-col>
                <v-col>
                    <div style="height: 96vh;" class="ml-2">
                        <ChatWindow @showUser="showUser"/>
                    </div>
                </v-col>
            </v-row>
            <UserProfileDialog ref="userDialog" @showUser="showUser"></UserProfileDialog>
        </v-container>
    </v-main>
</template>

<script>
import ChatWindow from '@/components/ChatWindow'
import Vue from'vue'
import store from '@/store/'
import UserProfileDialog from '@/components/UserProfileDialog'

export default {
    name: 'DirectChat',
    components: {
        ChatWindow,
        UserProfileDialog,
    },
    data: function(){
        return {
            chats: []
        }
    },
    methods: {
        showUser: function(user){
            this.$refs.userDialog.show(user);
        }
    },
    beforeRouteEnter: function(to, from, next){
        if(store.state.token === false){

            var cookie = (document.cookie.match('(^|;)\\s*' + "token" + '\\s*=\\s*([^;]+)')?.pop() || '').split(',')[0];

            if(cookie !== ''){

                window.axios.post(Vue.prototype.$apiHttpUrl + '/api/auth/validate',
                new URLSearchParams({
                    'token': cookie
                })
                ).then((response) => {

                    store.commit('setToken', cookie);
                    store.commit('setUserId', response.data.userId);
                    next();

                }, () => {

                    document.cookie = 'token=; Max-Age=-99999999;';
                    next('/');

                })
           }else{
               next('/');
           }

        }
        next();
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