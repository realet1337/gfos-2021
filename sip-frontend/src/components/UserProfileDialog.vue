<template>
    <v-card>
        <v-container>
            <v-row no-gutters class="ma-n3 highlighted-portion">
                <v-col cols="auto" justify="center" align="center">
                    <v-avatar class="mx-4 my-4" size="100">
                        <img :src="getAvatarUrl('user',$props.user)">
                    </v-avatar>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-card-title><h2>{{ $props.user.username }}</h2></v-card-title>
                    <v-card-text><b>ID:</b> {{ $props.user.id }}</v-card-text>
                </v-col>
                <v-col cols="auto" align-self="center">
                </v-col>
                <v-col cols="auto" class="ml-auto" align-self="center">
                    <v-btn depressed large color="error darken-2" class="white--text">block</v-btn>
                </v-col>
                <v-col cols="auto" class="ml-2" align-self="center">
                    <v-btn @click="openDirectChat" depressed large color="white" class="primary--text">message</v-btn>
                </v-col>
                <v-col cols="auto" class="mr-3 ml-2" align-self="center">
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
                    <v-tab>
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
                        <p v-if="$props.user.info">{{$props.user.info}}</p>
                        <p v-else>This user doesn't have any information about themselves.</p>
                    </v-row>
                    </v-tab-item>
                <v-tab-item>
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
                                    <v-list-item-avatar>
                                        <img :src="getAvatarUrl('group',item)">
                                    </v-list-item-avatar>
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
</template>

<script>
import Vue from 'vue'

export default {
    name: 'UserProfileDialog',
    props:[
        'user',
    ],
    components:{
    },
    data: function(){
        return{
            open: false,
            sharedGroups: [],
            tab: 0,
            directChat: {},
        }
    },methods: {
        show: function(){
            this.$data.open = true;
        },
        
        getAvatarUrl: function(type, obj){
            if(type === "user"){
                return Vue.prototype.$apiBaseUrl + "/upload/pic/user/" + obj.profilePicture + ".jpg";
            }else if(type==="group") {
                return Vue.prototype.$apiBaseUrl + "/upload/pic/group/" + obj.picture + ".jpg";
            }
            return;
            
        },

        openDirectChat: function(){
            this.$router.push('/chat/' + this.$data.directChat.id)
        }
    },created: function(){

        //load shared groups (performance?)
        window.axios.get(Vue.prototype.$apiBaseUrl + '/api/groups/shared-groups/' + this.$store.state.userId +
        '/' + this.$props.user.id, {
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
        window.axios.get(Vue.prototype.$apiBaseUrl + '/api/chats/directchat-by-users/' + this.$store.state.userId +
        '/' + this.$props.user.id, {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token
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
</script>