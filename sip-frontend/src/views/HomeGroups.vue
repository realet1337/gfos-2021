<template>
    <v-container>
        <v-row justify="center" >
            <v-col v-for="group in groups" :key="group.id" cols="auto">
                <GroupCard :group="group" class="my-4"></GroupCard>
            </v-col>
            <v-col cols="auto">
                <v-card width="350" min-width="350" class="rounded-xl my-4" elevation="0" color="secondary darken-4">
                    <div v-ripple class="clickable" @click="showGroupCreator">
                        <v-row>
                            <v-avatar size="130" class="mx-auto mt-6 mb-0" color="secondary darken-3">
                                <span class="headline">+</span>
                            </v-avatar>
                        </v-row>
                        <v-row>
                            <v-card-title class="mx-auto">Create a group</v-card-title>
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
        <GroupCreatorDialog ref="creatorDialog" @open-group-id="openGroupId"/>
    </v-container>
</template>
<script>
import GroupCard from '@/components/GroupCard.vue'
import Vue from 'vue'
import GroupCreatorDialog from '@/components/GroupCreatorDialog'

export default {
    name: 'HomeGroups',
    components:{
        GroupCard,
        GroupCreatorDialog,
    },
    data: function(){
        return {
            groups: [],
        }
    },
    created: function(){

        window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$store.state.userId + '/groups', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.groups = response.data;
        })
    },
    methods: {
        showGroupCreator: function(){
            this.$refs.creatorDialog.show();
        },
        openGroupId: function(id){
            this.$router.push('/group/' + id);
        }
    },
    computed: {
        voidMessage: function(){
            return Vue.prototype.$void[Math.floor(Math.random()*Vue.prototype.$void.length)];
        },
    }

}
</script>