<template>
    <v-container fluid>
        <v-row no-gutters>
            <v-col>
                <v-select v-model="roleId" :items="roles" item-text="name" item-value="id">
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
                        <h3 class="mt-4 ml-2 mb-2">Exceptions:</h3>
                        <v-list-item v-for="permission in exceptions" :key="permission.id">
                            <v-list-item-title>
                                {{permission.chat.name}}
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
        <v-dialog v-model="chatFinderDialogIsOpen" width="500">
            <v-card>
                <v-card-title class="primary white--text">
                    Add exceptions for chats
                </v-card-title>
                <v-card-text>
                    <div style="height: 500px;" class="overflow-y-auto">
                        <v-list nav>
                            <v-list-item link v-for="chat in chatsWithoutExceptions" :key="chat.id">
                                <v-list-item-icon>
                                    <v-icon color="secondary lighten-2">mdi-message-text</v-icon>
                                </v-list-item-icon>
                                <v-list-item-title>{{chat.name}}</v-list-item-title>
                                <v-list-item-action class="mr-4">
                                    <v-btn icon @click="addException(chat)">
                                        <v-icon>mdi-plus</v-icon>
                                    </v-btn>
                                </v-list-item-action>
                            </v-list-item>
                        </v-list>
                        <span v-if="chatsWithoutExceptions.length === 0" class="secondary--text">All chats have an exception.</span>
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
            roleId: undefined,
            role: undefined,
            exceptions: [],
            rule: undefined,
            chats: [],
            chatFinderDialogIsOpen: false,
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
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/roles/' + this.role.id + '/permissions', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                const ruleIdx = response.data.findIndex(p => p.chat === undefined);
                this.rule = response.data[ruleIdx];
                response.data.splice(ruleIdx, 1);
                this.exceptions = response.data;
            }, () => {
                this.$router.push('/home/groups');
            });
        },
        showChatFinder: function(){
            this.chatFinderDialogIsOpen = true;
        },
        addException: function(chat){
            const permission = {
                canRead: true,
                canWrite: true,
                role: this.role,
                chat: chat,
            }
            window.axios.post(Vue.prototype.$apiHttpUrl + '/api/roles/' + this.role.id + '/permissions', permission, {
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
        roleId: function(roleId){
            if(roleId !== -1 && roleId !== undefined){
                this.role = this.roles[this.roles.findIndex(role => role.id === this.roleId)];
                this.fetchPermissions();
            }
        }
    },
    computed: {
        chatsWithoutExceptions: function(){
            if(this.role && this.role.id){
                return this.chats.filter( c => !this.exceptions.some(p => c.id === p.chat.id));
            }
            else{
                return this.chats;
            }
        }
    }
}
</script>