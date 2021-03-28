<template>
    <v-dialog width="60%" v-model="open">
        <v-card>
            <v-container>
                <v-row no-gutters class="ma-n3 highlighted-portion">
                    <v-row no-gutters>
                        <v-col cols="auto" justify="center" align="center">
                            <v-avatar class="mx-4 my-4" size="100">
                                <img :src="imageUrl">
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
                            <v-btn depressed large color="white" class="primary--text">message</v-btn>
                        </v-col>
                        <v-col cols="auto" class="mr-3 ml-2" align-self="center">
                            <v-icon size="150%">mdi-dots-vertical</v-icon>
                        </v-col>
                    </v-row>
                </v-row>
                <!-- everything up until this point took 3 hours alone -->
                <!-- css is terrible -->
                <v-row no-gutters class="mb-n3 mt-6">
                    <p class="label-text"><b>USER INFO</b></p>
                </v-row>                
                <v-row no-gutters>
                    <p v-if="$props.user.info">{{$props.user.info}}</p>
                    <p v-else>This user doesn't have any information about themselves.</p>
                </v-row>
                <v-row no-gutters>
                    <v-col cols="auto">
                        <p class="label-text"><b>SHARED GROUPS</b></p>
                    </v-col>
                </v-row>
                <GroupChip v-for="group in sharedGroups" :key="group.id" :group="group"></GroupChip>
            </v-container> 
        </v-card>
    </v-dialog>
</template>

<script>
import Vue from 'vue'
import GroupChip from './GroupChip.vue'

export default {
    name: 'UserProfileDialog',
    props:[
        'user',
    ],
    components:{
        GroupChip
    },
    computed:{
        imageUrl: function(){

            return Vue.prototype.$apiBaseUrl + "/upload/images/" + this.$props.user.profilePicture + ".jpg";
            
        }
    },
    data: function(){
        return{
            open: false,
            sharedGroups: []
        }
    },methods: {
        show: function(){
            this.$data.open = true;
        }
    },watch: {
        user: function(){

            //reload shared groups on prop change
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
        }
    }

}
</script>