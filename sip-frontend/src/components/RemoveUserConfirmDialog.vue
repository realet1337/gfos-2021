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

//@vuese
//@group COMPONENTS
//Bittet den Nutzer um Bestätigung, dass er einen Nutzer aus einer Gruppe/Rolle entfernen will.
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
        //@vuese
        //Zeigt den Dialog. Wird dies als show(user) aufgerufen, fragt die Komponente, ob der Nutzer aus einer Gruppe entfernt werden soll.
        //Wird dies als show(user, role) aufgerufen, fragt die Komponente, ob der Nutzer aus einer Rolle entfernt werden soll.
        //@arg user
        //@arg role
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
        //Entfernt den Nutzer, abhängig davon, wie "show" aufgerufen wurde, beim Server entweder aus einer Gruppe oder aus einer Rolle.
        remove: function(){
            if(this.role){
                window.axios.delete(Vue.prototype.$getApiUrl('http') + '/roles/' + this.role.id + '/users/' + this.user.id,  {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    //@vuese
                    //Wenn der Nutzer erfolgreich entfernt wurde
                    //@arg user
                    this.$emit('removed', this.user);
                    this.close();
                }, () => {
                    this.$router.push('/home/groups');
                });
            }
            else{
                window.axios.delete(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/users/' + this.user.id,  {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    //@vuese
                    //Wenn der Nutzer erfolgreich entfernt wurde
                    //@arg user
                    this.$emit('removed', this.user);
                    this.close();
                }, () => {
                    this.$router.push('/home/groups');
                });
            }
        }
    }
}
</script>
