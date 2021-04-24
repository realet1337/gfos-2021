<template>
    <v-container fluid>
        <v-row no-gutters>
            <h3>Select role:</h3>
        </v-row>
        <v-row>
            <v-col>
                <v-select v-model="roleId" :items="roles" item-text="name" item-value="id">
                </v-select>
            </v-col>
                <v-col cols="auto" align-self="center">
                    <v-btn large depressed color="primary" @click="createRole">ADD ROLE</v-btn>
                </v-col>
            <template v-if="!$vuetify.breakpoint.xs">
                <v-col cols="auto" align-self="center">
                    <v-btn text large depressed color="primary" @click="showPriorityEditor">EDIT ROLE PRIORITIES</v-btn>
                </v-col>
            </template>
        </v-row>
        <v-row v-if="$vuetify.breakpoint.xs" class="mb-2" justify="center">
            <v-col cols="auto" align-self="center">
                <v-btn text large depressed color="primary" @click="showPriorityEditor">EDIT ROLE PRIORITIES</v-btn>
            </v-col>
        </v-row>
        <v-divider class="mb-5"></v-divider>
        <v-form v-if="role" v-model="isValid">
            <h3 class="mb-3" v-if="role.id">{{!!role.id ? 'Role details:' : 'Create role:'}}</h3>
            <v-row no-gutters>
                <span class="overline secondary--text text--lighten-3">NAME</span>
            </v-row>
            <v-row no-gutters>
                <v-text-field :rules="[v => !!v || 'The role needs a name', v => /\S/.test(v) || 'The role needs a name']" outlined v-model="role.name"></v-text-field>
            </v-row>
            <v-row no-gutters class="mt-5">
                <span class="overline secondary--text text--lighten-3">COLOR</span>
            </v-row>
            <v-row no-gutters>
                <v-color-picker hide-inputs flat v-model="color"></v-color-picker>
            </v-row>
            <v-row no-gutters class="mt-5">
                <span class="overline secondary--text text--lighten-3 mt-6 mb-n4">ALLOW ADMINISTRATIVE PRIVILEGES</span>
            </v-row>
            <v-row no-gutters>
                <v-checkbox v-model="role.admin" label="Administrator"></v-checkbox>
            </v-row>
            <v-row no-gutters class="mt-5" justify="center">
                <span class="overline secondary--text text--lighten-3" @click="submit">{{!!role.id ? 'SAVE CHANGES' : 'CREATE ROLE'}}</span>
            </v-row>
            <v-row no-gutters justify="center">
                <v-btn :disabled="!isValid" @click="submit" depressed large color="primary" width="120">{{!!role.id ? 'SAVE' : 'CREATE'}}</v-btn>
            </v-row>
            <template v-if="role.id">
                <v-divider class="mb-5 mt-5"></v-divider>
                <v-row no-gutters>
                    <v-col cols="24">
                        <v-list nav class="rounded-lg">
                            <v-list-item v-for="user in role.users" :key="user.id" link>
                                <v-avatar color="primary" size="30" class=" mr-2">
                                    <img v-if="user.profilePicture" :src="$getAvatarUrl('user', user)">
                                    <span v-else class="text-body-2">{{user.username.substring(0,1)}}</span>
                                </v-avatar>
                                <v-list-item-title class="ml-5">{{user.username}}</v-list-item-title>
                                <v-list-item-action class="ml-auto">
                                    <v-btn icon width="30" height="30" @click="removeUser(user)">
                                        <v-icon size="20" color="grey lighten-1">mdi-window-close</v-icon>
                                    </v-btn>
                                </v-list-item-action>
                            </v-list-item>
                            <v-divider v-if="role.users.length > 0" class="mb-2"></v-divider>
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
                <v-divider class="mb-5 mt-5"></v-divider>
                <!-- <v-row no-gutters class="mt-2" justify="center">
                    <span class="overline secondary--text text--lighten-3 mt-2">DELETE ROLE</span>
                </v-row> -->
                <v-row no-gutters justify="center">
                    <v-btn block text color="red" @click="deleteRole" large width="120">Delete role</v-btn>
                </v-row>
            </template>
        </v-form>
        <RemoveUserConfirmDialog ref="removeUserConfirmDialog" @removed="localRemoveUserFromRole"></RemoveUserConfirmDialog>
        <AddUserConfirmDialog ref="addUserConfirmDialog" @added="localAddUserToRole"></AddUserConfirmDialog>
        <v-dialog v-model="userFinderDialogIsOpen" width="500">
            <v-card>
                <v-card-title class="primary white--text">
                    Add users to role
                </v-card-title>
                <v-card-text>
                    <div style="height: 500px;" class="overflow-y-auto">
                        <v-list>
                            <v-list-item link v-for="user in notMembers" :key="user.id">
                                <v-avatar color="primary" size="45" class=" mr-4">
                                    <img v-if="user.profilePicture" :src="$getAvatarUrl('user', user)">
                                    <span v-else class="text-body-2">{{user.username.substring(0,1)}}</span>
                                </v-avatar>
                                <v-list-item-title>{{user.username}}</v-list-item-title>
                                <v-list-item-action>
                                    <v-btn icon @click="addUser(user)">
                                        <v-icon>mdi-plus</v-icon>
                                    </v-btn>
                                </v-list-item-action>
                            </v-list-item>
                        </v-list>
                        <span v-if="notMembers.length === 0" class="secondary--text">All group members have this role.</span>
                    </div>
                </v-card-text>
            </v-card>
        </v-dialog>
        <v-dialog v-model="priorityEditorIsOpen" width="500">
            <v-card>
                <v-card-title class="secondary white--text">
                    Edit role priority
                </v-card-title>
                <v-card-text>
                    <div style="height: 500px;" class="overflow-y-auto">
                        <v-list>
                            <v-list-item link v-for="(role, idx) in priorityEditorArray" :key="role.id">
                                <v-list-item-title :style="'color: ' + role.color">{{role.name}}</v-list-item-title>
                                <v-list-item-action>
                                    <v-btn icon v-if="idx !== 0" @click="swapRolesUpwards(idx)">
                                        <v-icon>mdi-chevron-up</v-icon>
                                    </v-btn>
                                </v-list-item-action>
                                <v-list-item-action>
                                    <v-btn icon v-if="idx !== roles.length -1" @click="swapRolesDownwards(idx)">
                                        <v-icon>mdi-chevron-down</v-icon>
                                    </v-btn>
                                </v-list-item-action>
                            </v-list-item>
                        </v-list>
                        <span v-if="roles.length === 0" class="secondary--text">All group members have this role.</span>
                    </div>
                </v-card-text>
                <v-card-actions>
                    <v-btn text color="primary" block @click="updateRolePriorities">update priorities</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>
<script>
import Vue from 'vue';

import AddUserConfirmDialog from '@/components/AddUserConfirmDialog'
import RemoveUserConfirmDialog from '@/components/RemoveUserConfirmDialog'

export default {
    name: 'GroupEditRolesView',
    data: function(){
        return {
            roles: [],
            roleId: undefined,
            role: undefined,
            color: '#FFFFFF',
            userFinderDialogIsOpen: false,
            priorityEditorIsOpen: false,
            groupUsers: [],
            selectedGroupUser: undefined,
            isValid: false,
            priorityEditorArray: [],
        }
    },
    components: {
        RemoveUserConfirmDialog,
        AddUserConfirmDialog,
    },
    methods: {
        fetchRoles: function(){
            window.axios.get(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/roles', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.roles = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        submit: function(){
            this.role.color = this.color;

            if(this.role.id){
                window.axios.put(Vue.prototype.$getApiUrl('http') + '/roles', this.role, {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    this.role = undefined;
                    this.roleId = undefined;
                    this.color = '#FFFFFF';
                    this.fetchRoles();
                }, () => {
                    this.$router.push('/home/groups');
                });
            }
            else{
                window.axios.post(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/roles', this.role, {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    this.role = undefined;
                    this.roleId = undefined;
                    this.color = '#FFFFFF';
                    this.fetchRoles();
                }, () => {
                    this.$router.push('/home/groups');
                });
            }
        },
        createRole: function(){
            this.roleId = -1;
            this.role = {
                name:'',
                color: '#FFFFFF',
                admin: false,
            };
            this.color = '#FFFFFF';
        },
        deleteRole: function(){
            window.axios.delete(Vue.prototype.$getApiUrl('http') + '/roles/' + this.role.id, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.role = undefined;
                this.roleId = undefined;
                this.color = '#FFFFFF';
                this.fetchRoles();
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        removeUser: function(user){
            this.$refs.removeUserConfirmDialog.show(user, this.roles[this.roles.findIndex(role => role.id === this.roleId)]);
        },
        addUser: function(user){
            if(!this.role.users.some(tmpUser => user.id == tmpUser.id)){
                this.$refs.addUserConfirmDialog.show(user, this.role);
            }
        },
        showUserFinder: function(){
            this.userFinderDialogIsOpen = true;
        },
        localRemoveUserFromRole: function(user){
            const roleIndex = this.role.users.findIndex(tmpUser => user.id === tmpUser.id);
            if(roleIndex !== -1){
                this.role.users.splice(roleIndex,1);
            }
        },
        localAddUserToRole: function(user){
            if(!this.role.users.some(tmpUser => user.id === tmpUser.id)){
                this.role.users.push(user);
            }
        },
        fetchUsers: function(){
            window.axios.get(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/users', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.groupUsers = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        swapRolesUpwards: function(idx){
            const buf = this.priorityEditorArray[idx];
            Vue.set(this.priorityEditorArray, idx, this.priorityEditorArray[idx - 1]);
            Vue.set(this.priorityEditorArray, idx - 1, buf);
        },
        swapRolesDownwards: function(idx){
            const buf = this.priorityEditorArray[idx];
            Vue.set(this.priorityEditorArray, idx, this.priorityEditorArray[idx + 1]);
            Vue.set(this.priorityEditorArray, idx + 1, buf);
        },
        showPriorityEditor: function(){
            this.priorityEditorArray = this.roles.slice();
            this.priorityEditorIsOpen = true;
        },
        updateRolePriorities: function(){
            const ids = [];
            for(var i = 0; i < this.priorityEditorArray.length; i++){
                ids.push(this.priorityEditorArray[i].id);
            }
            window.axios.put(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/roles/priorities', ids, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.fetchRoles();
                this.priorityEditorIsOpen = false;
            }, () => {
                this.$router.push('/home/groups');
            });
        }
    },
    created: function(){
        this.fetchRoles();
        this.fetchUsers();
    },
    watch: {
        roleId: function(roleId){
            if(roleId !== -1 && roleId !== undefined){
                this.role = Object.assign({}, this.roles[this.roles.findIndex(role => role.id === this.roleId)]);
                this.color = this.role.color;
            }
        }
    },
    computed: {
        notMembers: function(){
            if(this.role && this.role.id){
                const roleUsers = this.role.users;
                return this.groupUsers.filter(tmpUser => !roleUsers.some(tmpTmpUser => tmpTmpUser.id === tmpUser.id));
            }
            else{
                return this.groupUsers;
            }
        }
    }
}
</script>