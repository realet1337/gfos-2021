<template>
    <v-dialog width="700" v-model="$data.isOpen">
        <v-card>
            <v-container fluid>
                <v-form v-model="isValid">
                    <h2>{{isEditing ? 'Edit chat details:' : 'Create chat:'}}</h2>
                    <v-divider class="mb-3"></v-divider>
                    <p class="secondary--text text--lighten-2"><b>Name:</b></p>
                    <v-text-field class="mt-5 mb-n1" filled v-model="chat.name" :rules="[v => !!v || 'The chat needs a name', v => /\S/.test(v) || 'The chat needs a name']" @keydown.enter.prevent="submit"></v-text-field>
                    <v-btn block depressed large color="primary" @click="submit" :disabled="!$data.isValid">{{isEditing ? 'SAVE' : 'CREATE'}}</v-btn>
                </v-form>
            </v-container>
        </v-card>
    </v-dialog>
</template>
<script>
import Vue from 'vue';

//@vuese
//Erlaubt das Bearbeiten von Gruppenchats.
export default {
    name: 'ChatEditorDialog',
    data: function(){
        return {
            chat: {
                name: ''
            },
            isOpen: false,
            isValid: false,
            isEditing: false,
        };
    },
    methods: {
        //@vuese
        //Öffnet den Dialog. Falls ein "chat"-Paramter angegeben ist, wird der Dialog im Bearbeitungsmodus geöffnet, andernfalls wird er der Erstellungsmodus gewählt.
        show: function(chat){
            if(chat){
                Object.assign(this.chat, chat);
                this.isEditing = true;
            }
            else{
                this.chat = {
                    name: ''
                };                
                this.isEditing = false;
            }
            this.isOpen = true;
        },
        //@vuese
        //Aktualisiert/erstellt, abhängig vom Modus, beim Server einen Chat.
        submit: function(){
            if(this.isEditing){
                window.axios.put(Vue.prototype.$getApiUrl('http') + '/chats', this.chat, {
                    headers:{
                            'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    
                    //@vuese
                    //Emittiert den aktualisierten Chat
                    //@arg chat
                    this.$emit('chat-updated', this.chat);
                    this.close();
                }, () => {
                    //shouldn't fail
                })
            }
            else{
                window.axios.post(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId + '/chats', this.chat, {
                    headers:{
                            'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    //@vuese
                    //Emittiert den erstellten Chat
                    //@arg chat
                    this.$emit('chat-created', this.chat);
                    this.close();
                }, () => {
                    //shouldn't fail
                })
            }
        },
        //@vuese
        //Schließt den Chat
        close: function(){
            this.isOpen = false;
        }
    }
}
</script>