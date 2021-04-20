<template>
    <v-dialog v-model="isOpen" width="500">
        <v-card>
            <v-card-title>Remove user</v-card-title>
            <v-card-text v-if="user">
                <span>Remove user </span>
                <span><b>{{user.username}}</b></span>
                <template v-if="role">
                    <span> from role </span>
                    <span :style="'color: ' + role.color"><b>{{role.name}}</b></span>
                </template>
                <span>?</span>
            </v-card-text>
            <v-card-actions>
                <v-btn class="ml-auto" text large @click="close">Cancel</v-btn>
                <v-btn depressed large color="error" @click="remove">Remove</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<script>
import Vue from 'vue'

export default {
    name: 'RemoveUserConfirmDialog',
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
        remove: function(){
            window.axios.delete(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId + '/users/' + this.user.id,  {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.$emit('removed');
                this.close();
            }, () => {
                this.$router.push('/home/groups');
            });
        }
    }
}
</script>
