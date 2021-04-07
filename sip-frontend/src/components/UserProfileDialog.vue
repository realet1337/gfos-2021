<template>
                <!-- avoid errors -->
    <v-dialog v-if="$data.user.id" min-width="550" width="850" v-model="isOpen">
        <v-card min-width="550" width="850">
            <v-container>
                <v-row no-gutters class="ma-n3 highlighted-portion">
                    <v-col cols="auto" justify="center" align="center">
                        <v-avatar class="mx-4 my-4" size="100" color="secondary darken-4">
                            <img v-if="$data.user.profilePicture" :src="$getAvatarUrl('user', $data.user)">
                            <span v-else>{{$data.user.username.substring(0,1)}}</span>
                        </v-avatar>
                    </v-col>
                    <v-col cols="auto" align-self="center">
                        <v-card-title><h2>{{ $data.user.username }}</h2>
                            <span class="ml-2 mt-1" v-if="$data.user.id == $store.state.userId">(You)</span>
                        </v-card-title>
                        <v-card-text><b>ID:</b> {{ $data.user.id }}</v-card-text>
                    </v-col>
                    <v-spacer></v-spacer>
                    <v-col v-if="$data.user.id != $store.state.userId" cols="auto" align-self="center">
                        <v-btn depressed large color="error darken-2" class="white--text">block</v-btn>
                    </v-col>
                    <v-col v-if="$data.user.id != $store.state.userId" cols="auto" class="ml-2" align-self="center">
                        <v-btn @click="openDirectChat" depressed large color="white" class="primary--text">message</v-btn>
                    </v-col>
                    <v-col cols="auto" class="ml-2 mr-3" align-self="center">
                        <v-icon size="150%">mdi-dots-vertical</v-icon>
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
                        <v-row no-gutters>
                            <p class="label-text mt-3"><b>USER INFO</b></p>
                        </v-row>                
                        <v-row no-gutters>
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
                        height="300"
                        item-height="64">
                            <template v-slot:default="{ item }">
                                <v-list nav :key="item.id">
                                    <v-list-item @click="$router.push('/group/' + item.id)">
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

export default {
    name: 'UserProfileDialog',
    components:{
    },
    data: function(){
        return{
            sharedGroups: [],
            tab: 0,
            directChat: {},
            isOpen: false,
            user: {}
        }
    },methods: {
        openDirectChat: function(){
            this.$router.push('/chat/' + this.$data.directChat.id)
        },
        show: function(user){

            this.$data.user = user;
            this.$data.isOpen = true;

            if(user.id != this.$store.state.userId){
                //load shared groups (performance?)
                window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/shared-groups/' + this.$store.state.userId +
                '/' + this.$data.user.id, {
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


                //load chat. This is necessary to make the "block" button have the correct label
                window.axios.get(Vue.prototype.$apiHttpUrl + '/api/chats', {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token
                    },
                    params:{
                        user1: this.$data.user.id,
                        user2: this.$store.state.userId
                    }
                }).then((response) => {
                    this.$data.directChat = response.data;
                },(error) => {

                    if(error.response.status == 403){
                        this.$router.push('/login');
                    }
                })
            }
        }
    }

}
</script>