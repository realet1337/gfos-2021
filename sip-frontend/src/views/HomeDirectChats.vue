<template>
    <v-container>
        <v-row justify="center" >
            <v-col v-for="chat in chats" :key="chat.id" cols="auto">
                <ChatCard :chat="chat" @show-user="showUserDialog" class="my-4"></ChatCard>
            </v-col>
            <v-col cols="auto">
                <v-card width="350" min-width="300" class="rounded-xl my-4" elevation="0" color="secondary darken-4">
                    <div v-ripple class="clickable" @click="showUserFinder">
                        <v-row>
                            <v-avatar size="130" class="mx-auto mt-6 mb-0" color="secondary darken-3">
                                <span class="headline">+</span>
                            </v-avatar>
                        </v-row>
                        <v-row>
                            <v-card-title class="mx-auto">Start a conversation</v-card-title>
                        </v-row>
                    </div>
                    <v-divider class="mt-3 mb-6"></v-divider>
                    <v-row>
                        <v-sheet class="mx-auto mb-4 rounded-lg reveal-on-hover-container" width="85%" color="secondary darken-3" elevation="0">
                            <v-row no-gutters style="height: 100px;" class="fill-height" align="center">
                                <v-col align-self="center">
                                    <p class="ml-4 mx-auto my-auto secondary--text reveal-on-hover">{{voidMessage}}</p>
                                </v-col>
                            </v-row>
                        </v-sheet>
                    </v-row>
                </v-card>
            </v-col>
        </v-row>
        <UserFinderDialog ref="finderDialog" @selected-user="showUserDialog"></UserFinderDialog>
        <UserProfileDialog ref="userDialog" @open-direct-chat="openDirectChat" @open-group="openGroup"></UserProfileDialog>
    </v-container>
</template>
<script>
import ChatCard from '@/components/ChatCard.vue'
import UserProfileDialog from '@/components/UserProfileDialog'
import Vue from 'vue'
import UserFinderDialog from '@/components/UserFinderDialog'

//@vuese
//@group VIEWS
//Zeigt alle Direct-Chats des eigenen Users in Form von Karten
export default {
    name: 'HomeDirectChats',
    components:{
        ChatCard,
        UserProfileDialog,
        UserFinderDialog,
    },
    data: function(){
        return {
            chats: [],
            selectedUser: false,
            userDialogIsOpen: false,
        }
    },
    created: function(){
        window.axios.get(Vue.prototype.$getApiUrl('http') + '/users/' + this.$store.state.userId + '/direct-chats/', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.chats = response.data;
        })
    },
    methods: {
        //@vuese
        //Zeigt den UserProfileDialog.
        //@arg user
        showUserDialog: function(user){

            this.$refs.userDialog.show(user);

        },
        //@vuese
        //Routet die Anwendung zu einem Chat.
        //@arg chat
        openDirectChat: function(chat){
            this.$router.push('/chat/' + chat.id);
        },
        //@vuese
        //Routet die Anwendung zu einer Group.
        //@arg group
        openGroup: function(group){
            this.$router.push('/group/' + group.id);
        },
        //@vuese
        //Zeigt den UserFinderDialog.
        showUserFinder: function(){
            this.$refs.finderDialog.show();
        }
    },
    computed: {
        voidMessage: function(){
            return Vue.prototype.$void[Math.floor(Math.random()*Vue.prototype.$void.length)];
        },
    }

}
</script>