<template>
    <v-container fluid>
        <v-row no-gutters>
            <v-col>
                <v-select v-model="chatId" :items="chats" item-text="name" item-value="id">
                </v-select>
            </v-col>
        </v-row>
        <template v-if="rule">
            <v-row no-gutters>
                <v-col cols="24">
                    <v-list nav class="rounded-lg">
                        <v-list-item>
                            <v-list-item-title>
                                Rule:
                            </v-list-item-title>
                            <v-list-item-action>
                                <v-checkbox v-model="rule.canRead"></v-checkbox>
                            </v-list-item-action>
                            <v-list-item-action-text class="ml-2">Can Read</v-list-item-action-text>
                            <v-list-item-action class="ml-3">
                                <v-checkbox v-model="rule.canWrite"></v-checkbox>
                            </v-list-item-action>
                            <v-list-item-action-text class="ml-2">Can Write</v-list-item-action-text>
                            <v-list-item-action class="mx-4">
                                <v-tooltip top>
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-btn 
                                            icon
                                            v-bind="attrs"
                                            v-on="on"
                                            @click="updatePermission(rule)"
                                        >
                                            <v-icon color="green">mdi-check</v-icon>
                                        </v-btn>
                                    </template>
                                    <span>Save changes to rule</span>
                                </v-tooltip>
                            </v-list-item-action>
                        </v-list-item>
                        <v-row no-gutters>
                            <v-divider></v-divider>
                        </v-row>
                        <v-row no-gutters>
                            <h3 class="mt-4 ml-2 mb-2">Exceptions:</h3>
                        </v-row>
                        <v-row v-if="exceptions.length===0" class="ml-2 mb-2" no-gutters>
                            <span class="secondary--text">No exceptions</span>
                        </v-row>
                        <v-list-item v-for="permission in exceptions" :key="permission.id">
                            <v-list-item-title :style="'color: ' + permission.role.color">
                                {{permission.role.name}}
                            </v-list-item-title>
                            <v-list-item-action>
                                <v-checkbox v-model="permission.canRead"></v-checkbox>
                            </v-list-item-action>
                            <v-list-item-action-text class="ml-2">Can Read</v-list-item-action-text>
                            <v-list-item-action class="ml-3">
                                <v-checkbox v-model="permission.canWrite"></v-checkbox>
                            </v-list-item-action>
                            <v-list-item-action-text class="ml-2">Can Write</v-list-item-action-text>
                            <v-list-item-action class="ml-4">
                                <v-tooltip top>
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-btn 
                                            icon
                                            v-bind="attrs"
                                            v-on="on"
                                            @click="updatePermission(permission)"
                                        >
                                            <v-icon color="green">mdi-check</v-icon>
                                        </v-btn>
                                    </template>
                                    <span>Save changes to exception</span>
                                </v-tooltip>
                            </v-list-item-action>
                            <v-list-item-action class="mx-4">
                                <v-tooltip top>
                                    <template v-slot:activator="{ on, attrs }">
                                        <v-btn 
                                            icon
                                            v-bind="attrs"
                                            v-on="on"
                                            @click="deleteException(permission)"
                                        >
                                            <v-icon color="red">mdi-window-close</v-icon>
                                        </v-btn>
                                    </template>
                                    <span>Remove exception</span>
                                </v-tooltip>
                            </v-list-item-action>
                        </v-list-item>
                        <v-row no-gutters>
                            <v-divider></v-divider>
                        </v-row>
                        <v-list-item link class="mt-2" @click="showChatFinder">
                            <v-list-item-icon>
                                <v-icon>mdi-plus</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Add exception</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </v-col>
            </v-row>
        </template>
        <v-dialog v-model="roleFinderDialogIsOpen" width="500">
            <v-card>
                <v-card-title class="primary white--text">
                    Add exceptions for chats
                </v-card-title>
                <v-card-text>
                    <div style="height: 500px;" class="overflow-y-auto">
                        <v-list nav>
                            <v-list-item link v-for="role in rolesWithoutExceptions" :key="role.id">
                                <v-list-item-icon class="ml-2">
                                    <v-icon :color="role.color">mdi-account-group</v-icon>
                                </v-list-item-icon>
                                <v-list-item-title :style="'color: ' + role.color">{{role.name}}</v-list-item-title>
                                <v-list-item-action class="mr-4">
                                    <v-btn icon @click="addException(role)">
                                        <v-icon>mdi-plus</v-icon>
                                    </v-btn>
                                </v-list-item-action>
                            </v-list-item>
                        </v-list>
                        <span v-if="rolesWithoutExceptions.length === 0" class="secondary--text">All roles have an exception.</span>
                    </div>
                </v-card-text>
            </v-card>
        </v-dialog>
    </v-container>
</template>
<script>
import Vue from 'vue'

export default {
    name: 'GroupEditPermissionsView',
    data: function(){
        return {
            roles: [],
            exceptions: [],
            rule: undefined,
            chats: [],
            chatId: undefined,
            chat: undefined,
            roleFinderDialogIsOpen: false,
        }
    },
    methods: {
        fetchRoles: function(){
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
        fetchChats: function(){
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId + '/chats', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.chats = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        fetchPermissions: function(){
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/chats/' + this.chat.id + '/permissions', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                const ruleIdx = response.data.findIndex(p => p.role === undefined);
                this.rule = response.data[ruleIdx];
                response.data.splice(ruleIdx, 1);
                this.exceptions = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        showChatFinder: function(){
            this.roleFinderDialogIsOpen = true;
        },
        addException: function(role){
            const permission = {
                canRead: false,
                canWrite: false,
                chat: this.chat,
                role: role,
            }
            window.axios.post(Vue.prototype.$apiHttpUrl + '/api/chats/' + this.chat.id + '/permissions', permission, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.fetchPermissions();
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        updatePermission: function(permission){
            window.axios.put(Vue.prototype.$apiHttpUrl + '/api/permissions', permission, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.fetchPermissions();
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        deleteException: function(permission){
            window.axios.delete(Vue.prototype.$apiHttpUrl + '/api/permissions/' + permission.id, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.fetchPermissions();
            }, () => {
                this.$router.push('/home/groups');
            });
        }
    },
    created: function(){
        this.fetchRoles();
        this.fetchChats();
    },
    watch: {
        chatId: function(chatId){
            if(chatId !== -1 && chatId !== undefined){
                this.chat = this.chats[this.chats.findIndex(chat => chat.id === this.chatId)];
                this.fetchPermissions();
            }
        }
    },
    computed: {
        rolesWithoutExceptions: function(){
            if(this.chat){
                return this.roles.filter( r => !this.exceptions.some(p => r.id === p.role.id));
            }
            else{
                return this.roles;
            }
        }
    }
}
</script>