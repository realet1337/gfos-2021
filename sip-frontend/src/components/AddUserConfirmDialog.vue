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

//@vuese
//Bittet den Nutzer um Bestätigung, dass er einen Nutzer zu einer Gruppe/Rolle hinzufügen will.
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
        //@vuese
        //Zeigt den Dialog. Wird dies als show(user) aufgerufen, fragt die Komponente, ob der Nutzer zu einer Gruppe hinzugefügt werden soll.
        //Wird dies als show(user, role) aufgerufen, fragt die Komponente, ob der Nutzer zu einer Rolle hinzugefügt werden soll.
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
        //@vuese
        //Schließt den Dialog.
        close: function(){
            this.isOpen = false;
        },
        //@vuese
        //Fügt den Nutzer, abhängig davon, wie "show" aufgerufen wurde, beim Server entweder zu einer Gruppe oder einer Rolle hinzu.
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