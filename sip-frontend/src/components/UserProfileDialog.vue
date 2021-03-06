<template>
    <!-- avoid errors -->
    <v-dialog v-if="$data.user.id" 
        :min-width="$vuetify.breakpoint.xs ? undefined : '550'" 
        :width="$vuetify.breakpoint.xs ? undefined : '850'" 
        v-model="isOpen" 
        :fullscreen="$vuetify.breakpoint.xs"
    >
        <v-card 
            :min-width="$vuetify.breakpoint.xs ? undefined : '550'" 
            :width="$vuetify.breakpoint.xs ? undefined : '850'" 
        >
            <v-container>
                <v-row class="highlighted-portion" v-if="$vuetify.breakpoint.xs">
                    <v-col cols="auto" class="ml-auto">
                        <v-btn
                            icon
                            dark
                            @click="isOpen = false"
                        >
                            <v-icon>
                                mdi-window-close
                            </v-icon>
                        </v-btn>
                    </v-col>
                </v-row>
                <v-row no-gutters class="ma-n3 highlighted-portion">
                    <v-col cols="auto" justify="center" align="center">
                        <v-badge bottom bordered offset-x="45" offset-y="40" :color="$data.user.isOnline ? 'green' : 'red'">
                            <v-avatar class="mx-4 my-4" size="100" color="secondary darken-4">
                                <img v-if="$data.user.profilePicture" :src="$getAvatarUrl('user', $data.user)">
                                <span v-else>{{$data.user.username.substring(0,1)}}</span>
                            </v-avatar>
                        </v-badge>
                    </v-col>
                    <v-col cols="auto" align-self="center">
                        <v-card-title><h2>{{ $data.user.username }}</h2>
                            <span class="ml-2 mt-1" v-if="$data.user.id == $store.state.userId">(You)</span>
                        </v-card-title>
                        <v-card-text><b>ID:</b> {{ $data.user.id }}<template v-if="!user.isOnline && user.lastSeen"><br><b>last seen:</b> {{ new Date(user.lastSeen).toLocaleString() }}</template> </v-card-text>
                    </v-col>
                    <template v-if="!$vuetify.breakpoint.xs">
                        <v-spacer></v-spacer>
                        <v-col v-if="$data.user.id != $store.state.userId" cols="auto" align-self="center">
                            <v-btn v-if="!userIsBlocked" depressed large color="error darken-2" class="white--text" @click="blockUser">block</v-btn>
                            <v-btn v-else depressed large color="error darken-2" class="white--text" @click="unblockUser">unblock</v-btn>
                        </v-col>
                        <v-col v-if="$data.user.id != $store.state.userId" cols="auto" class="ml-2 mr-3" align-self="center">
                            <v-btn @click="openDirectChat" depressed large color="white" class="primary--text">message</v-btn>
                        </v-col>
                    </template>
                </v-row>
                <v-row v-if="$vuetify.breakpoint.xs" class="highlighted-portion" justify="center">
                    <v-col v-if="$data.user.id != $store.state.userId" align-self="center" cols="auto">
                        <v-btn v-if="!userIsBlocked" depressed large color="error darken-2" class="white--text" @click="blockUser">block</v-btn>
                        <v-btn v-else depressed large color="error darken-2" class="white--text" @click="unblockUser">unblock</v-btn>
                    </v-col>
                    <v-col v-if="$data.user.id != $store.state.userId" cols="auto" class="ml-2 mr-3" align-self="center">
                        <v-btn @click="openDirectChat" depressed large color="white" class="primary--text">message</v-btn>
                    </v-col>
                </v-row>
                <v-row class="highlighted-portion">
                    <v-divider></v-divider>
                </v-row>
                <v-row class="highlighted-portion">
                    <v-tabs
                    v-model="tab"
                    centered
                    background-color="primary"
                    slider-color="white"
                        >
                        <v-tab>
                            INFO
                        </v-tab>
                        <v-tab v-if="$data.user.id != $store.state.userId">
                            SHARED GROUPS
                        </v-tab>
                    </v-tabs>
                </v-row>
                <!-- everything up until this point took 3 hours alone -->
                <!-- css is terrible -->
                <v-tabs-items v-model="tab" class="mt-3">
                    <v-tab-item>
                        <v-row no-gutters v-if="$route.params.groupId && roles != false">
                            <p class="label-text mt-3"><b>ROLES</b></p>
                        </v-row>                
                        <v-row no-gutters v-if="$route.params.groupId && roles != false">
                            <v-col v-for="role in roles" :key="role.id" cols="auto">
                                <v-chip
                                outlined
                                :color="role.color"
                                class="ma-1"
                                >{{role.name}}</v-chip>
                            </v-col>
                        </v-row>
                        <template v-if="user.status">
                            <v-row no-gutters>
                                <p class="label-text mt-3"><b>STATUS</b></p>
                            </v-row>
                            <v-row class="mt-n2" no-gutters>
                                <p>{{user.status}}</p>
                            </v-row>
                        </template>
                        <v-row no-gutters>
                            <p class="label-text mt-3"><b>USER INFO</b></p>
                        </v-row>                
                        <v-row no-gutters class="mt-n2">
                            <p v-if="$data.user.info">{{$data.user.info}}</p>
                            <p v-else>This user doesn't have any information about themselves.</p>
                        </v-row>
                        </v-tab-item>
                    <v-tab-item v-if="$data.user.id != $store.state.userId">
                        <v-row no-gutters>
                            <p class="label-text mt-3"><b>SHARED GROUPS</b></p>
                        </v-row>
                        <v-virtual-scroll 
                        :items="sharedGroups"
                        height="290"
                        item-height="64">
                            <template v-slot:default="{ item }">
                                <v-list nav :key="item.id">
                                    <v-list-item @click="openGroup(item)">
                                        <v-avatar color="secondary darken-4" size="40" class="mr-3">
                                            <img v-if="item.picture" :src="$getAvatarUrl('group',item)">
                                            <span v-else>{{item.name.substring(0,1)}}</span>
                                        </v-avatar>
                                        <v-list-item-content>
                                            <v-list-item-title>{{item.name}}</v-list-item-title>
                                        </v-list-item-content>
                                    </v-list-item>
                                </v-list>
                            </template>
                        </v-virtual-scroll>
                    </v-tab-item>
                </v-tabs-items>
            </v-container> 
        </v-card>
    </v-dialog>
</template>

<script>
import Vue from 'vue'

//@vuese
//@group COMPONENTS
//Zeigt das Profil eines Nutzers in einem Dialog an. Erlaubt, den Nutzer zu blockieren und einen Chat mit ihm zu öffnen.
export default {
    name: 'UserProfileDialog',
    components:{
    },
    data: function(){
        return{
            sharedGroups: [],
            roles: [],
            tab: 0,
            isOpen: false,
            user: {}
        }
    },
    methods: {
        //@vuese
        //Lädt einen Direct-Chat zwischen dem gezeigten Nutzer  und dem eingeloggten Nutzer vom Server und emmitiert 'open-direct-chat' mit ihm.
        openDirectChat: function(){
            window.axios.get(Vue.prototype.$getApiUrl('http') + '/chats', {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token
                    },
                    params:{
                        user1: this.$data.user.id,
                        user2: this.$store.state.userId
                    }
                }).then((response) => {
                    //@vuese
                    //Wenn der Nutzer auf "MESSAGE" klickt um den Direct-Chat zu öffnen.
                    //@arg chat
                    this.$emit('open-direct-chat', response.data);
                    this.$data.isOpen = false;
                },(error) => {
                    if(error.response.status == 403){
                        this.$router.push('/login');
                    }
                })
        },
        //@vuese
        //Emittiert 'open-group' mit einem Parameter group und schließt den Dialog.
        //@arg group
        openGroup: function(group){
            //@vuese
            //Wenn der Nutzer eine Gruppe anklickt.
            //@arg group
            this.$emit('open-group', group);
            this.$data.isOpen = false;
        },
        //@vuese
        //Setzt die Komponente zurück und zeigt den Dialog.
        show: function(user){

            this.sharedGroups = [];
            this.roles = [];
            this.tab = 0;

            this.$data.user = user;
            this.$data.isOpen = true;

            if(user.id != this.$store.state.userId){
                //load shared groups (performance?)
                window.axios.get(Vue.prototype.$getApiUrl('http') + '/users/' + this.$store.state.userId +
                '/shared-groups/' + this.$data.user.id, {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                }).then((response) => {
                    this.$data.sharedGroups = response.data;
                },(error) => {

                    if(error.response.status == 403){
                        this.$router.push('/login');
                    }
                })

            }

            if(this.$route.params.groupId){
                window.axios.get(Vue.prototype.$getApiUrl('http') + '/users/' + this.$data.user.id +
                '/roles', {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token
                    },
                    params:{
                        group: this.$route.params.groupId
                    }
                }).then((response) => {
                    this.$data.roles = response.data;
                },(error) => {
                    if(error.response.status == 403){
                        this.$router.push('/login');
                    }
                })
            }
        },
        //@vuese
        //Blockiert einen Nutzer beim Server.
        blockUser: function(){
            window.axios.post(Vue.prototype.$getApiUrl('http') + '/users/' + this.$store.state.userId + '/blocked-users', this.$data.user, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            }).then(() => {
                //pass
            });
        },
        //@vuese
        //Hebt eine Blockierung beim Server auf.
        unblockUser: function(){
            window.axios.delete(Vue.prototype.$getApiUrl('http') + '/users/' + this.$store.state.userId + '/blocked-users/' + this.$data.user.id, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            }).then(() => {
                //pass
            });
        }
    },
    computed: {
        //@vuese
        //Ob der gezeigt Nutzer blockiert ist.
        userIsBlocked: function(){
            return this.$store.state.blockedUsers.some(user => user.id == this.$data.user.id);
        }
    }

}
</script>