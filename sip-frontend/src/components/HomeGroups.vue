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

        window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$store.state.userId + '/groups', {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.$data.groups = response.data;
            //testing (left here because will get removed at build)
            /*
            var noDescGroup = Object.assign({},this.$data.groups[0]);
            noDescGroup.description = undefined;
            this.$data.groups.push(noDescGroup);
            console.log(this.$data.groups)*/
        })
    },

}
</script>