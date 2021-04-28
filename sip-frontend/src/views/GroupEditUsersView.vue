<template>
    <v-container>
        <v-row justify="center">
            <v-col cols="24">
                <h3>
                    Add/remove users:
                </h3>
                <v-divider></v-divider>
            </v-col>
        </v-row>
        <v-row>
            <v-col cols="24">
                <v-list nav v-if="group" class="rounded-lg">
                    <v-list-item v-for="user in users" :key="user.id" link>
                        <v-avatar color="primary" size="30" class=" mr-2">
                            <img v-if="user.profilePicture" :src="$getAvatarUrl('user', user)">
                            <span v-else class="text-body-2">{{user.username.substring(0,1)}}</span>
                        </v-avatar>
                        <v-list-item-title class="ml-5">{{user.username}}</v-list-item-title>
                        <v-list-item-action class="ml-auto" v-if="user.id !== group.owner.id">
                            <v-btn icon width="30" height="30" @click="removeUser(user)">
                                <v-icon size="20" color="grey lighten-1">mdi-window-close</v-icon>
                            </v-btn>
                        </v-list-item-action>
                    </v-list-item>
                    <v-divider class="mb-2"></v-divider>
                    <v-list-item link @click="showUserFinder">
                        <v-list-item-icon class="ml-1">
                            <v-icon>
                                mdi-plus
                            </v-icon>
                        </v-list-item-icon>
                        <v-list-item-title class="ml-n1">Add User</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-col>
        </v-row>
        <RemoveUserConfirmDialog ref="removeUserConfirmDialog" @removed="fetchUsers"></RemoveUserConfirmDialog>
        <AddUserConfirmDialog ref="addUserConfirmDialog" @added="fetchUsers"></AddUserConfirmDialog>
        <UserFinderDialog ref="userFinderDialog" @selected-user="addUser"></UserFinderDialog>
    </v-container>
</template>
<script>
import Vue from'vue'

import AddUserConfirmDialog from '@/components/AddUserConfirmDialog'
import RemoveUserConfirmDialog from '@/components/RemoveUserConfirmDialog'
import UserFinderDialog from '@/components/UserFinderDialog'

//@vuese
//Erlaubt, Nutzer zu Gruppen hinzuzufügen und sie zu entfernen.
export default {
    name: 'GroupEditUsersView',
    components: {
        RemoveUserConfirmDialog,
        AddUserConfirmDialog,
        UserFinderDialog
    },
    data: function(){
        return {
            users: [],
            group: undefined,
        }
    },
    methods: {
        //@vuese
        //Zeigt den RemoveUserConfirmDialog.
        removeUser: function(user){
            this.$refs.removeUserConfirmDialog.show(user);
        },
        //@vuese
        //Zeigt den AddUserConfirmDialog.
        addUser: function(user){
            if(!this.users.some(tmpUser => user.id == tmpUser.id)){
                this.$refs.addUserConfirmDialog.show(user);
            }
        },
        //@vuese
        //Lädt alle Nutzer.
        fetchUsers: function(){
            window.axios.get(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/users', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.users = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        //@vuese
        //Lädt die aktuelle Gruppe.
        fetchGroup: function(){
            window.axios.get(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.group = response.data;
            }, () => {
                this.$router.push('/home/groups');
            })
        },
        //@vuese
        //Zeigt den UserFinderDialog
        showUserFinder: function(){
            this.$refs.userFinderDialog.show();
        }
    },
    created: function(){
        this.fetchUsers();
        this.fetchGroup();
    }
}
</script>