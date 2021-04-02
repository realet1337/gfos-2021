<template>
    <v-container>
        <v-row justify="center" >
            <v-col v-for="group in groups" :key="group.id" cols="auto">
                <GroupCard :group="group" class="my-4"></GroupCard>
            </v-col>
        </v-row>
    </v-container>
</template>
<script>
import GroupCard from './GroupCard.vue'
import Vue from 'vue'

export default {
    name: 'HomeGroups',
    components:{
        GroupCard,
    },
    data: function(){
        return {
            groups: [],
        }
    },
    created: function(){

        window.axios.get(Vue.prototype.$apiBaseUrl + '/api/groups/groups-by-user/' + this.$store.state.userId, {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.chats = response.data;
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
            this.$data.chats = this.$data.chats.concat(this.$data.chats);
        })
    },

}
</script>