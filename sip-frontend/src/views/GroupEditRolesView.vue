<template>
    <v-container fluid>
        <v-row>
            <v-col>
                <v-select cache-items v-model="roleIndex" :items="roles" item-text="name" item-value="id">
                </v-select>
            </v-col>
            <v-col cols="auto" align-self="center">
                <v-btn large depressed color="primary">EDIT ROLE PRIORITY</v-btn>
            </v-col>
        </v-row>
        <v-divider></v-divider>
        <v-form v-if="role">
            <v-row no-gutters>
                <v-text-field v-model="role.name"></v-text-field>
            </v-row>
            <v-row>
                <v-col>
                    <v-color-picker flat v-model="role.color"></v-color-picker>
                </v-col>
                <v-col>

                </v-col>
            </v-row>
        </v-form>
    </v-container>
</template>
<script>
import Vue from 'vue';
export default {
    name: 'GroupEditRolesView',
    data: function(){
        return {
            roles: [],
            roleIndex: undefined,
        }
    },
    methods: {
        fetchUsers: function(){
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId + '/roles', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.roles = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
    },
    created: function(){
        this.fetchUsers();
    },
    computed: {
        role: function(){
            if(this.roleIndex !== undefined){
                //vuetify uses 1-index. IDK WHY EITHER
                return this.roles[this.roleIndex-1];
            }
            else{
                return undefined;
            }
        }
    }
}
</script>