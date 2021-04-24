<template>
    <v-dialog v-model="isOpen" width="500">
        <v-card>
            <v-card-title>Add user</v-card-title>
            <v-card-text v-if="user">
                <span>Add user </span>
                <span><b>{{user.username}}</b></span>
                <template v-if="role">
                    <span> to role </span>
                    <span :style="'color: ' + role.color"><b>{{role.name}}</b></span>
                </template>
                <span>?</span>
            </v-card-text>
            <v-card-actions>
                <v-btn class="ml-auto" text large @click="close">Cancel</v-btn>
                <v-btn depressed large color="primary" @click="add">Add</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<script>
import Vue from 'vue'

export default {
    name: 'AddUserConfirmDialog',
    data: function(){
        return {
            isOpen: false,
            role: undefined,
            user: undefined
        }
    },
    methods: {
        show: function(user, role){
            this.user = user;

            if(role){
                this.role = role;
            }
            else{
                this.role = undefined;
            }

            this.isOpen = true;
        },
        close: function(){
            this.isOpen = false;
        },
        add: function(){
            if(!this.role){
                window.axios.post(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/users', this.user, {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    this.$emit('added');
                    this.close();
                }, () => {
                    this.$router.push('/home/groups');
                });
            }
            else{
                window.axios.post(Vue.prototype.$getApiUrl('http') + '/roles/' + this.role.id + '/users', this.user, {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    this.$emit('added', this.user);
                    this.close();
                }, () => {
                    this.$router.push('/home/groups');
                });
            }
        }
    }
}
</script>