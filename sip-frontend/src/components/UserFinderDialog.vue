<template>
    <v-dialog width="700" v-model="$data.isOpen">
        <v-card>
            <v-container fluid>
                <v-form>
                    <p class="headline secondary--text text--lighten-2"><b>Please enter a user ID:</b></p>
                    <v-divider></v-divider>
                    <v-text-field class="mt-5 mb-n3" height="80" filled v-model="id" append-icon="mdi-magnify" @keydown.enter.prevent="search"></v-text-field>
                    <p class="error--text">{{this.$data.errorMessage}}</p>
                </v-form>
            </v-container>  
        </v-card>
    </v-dialog>
</template>
<script>
import Vue from 'vue';
export default {
    name: 'UserFinderDialog',
    data: function(){
        return {
            isOpen: false,
            id: '',
            errorMessage: ''
        }
    },
    methods: {
        show: function(){
            this.$data.id = '';
            this.$data.errorMessage = '';
            this.$data.isOpen = true;
        },
        search: function(){
            if(!isNaN(this.$data.id)){
                window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$data.id,{
                    headers:{
                            'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then((response) => {
                    this.close();
                    this.$emit('show-user', response.data);
                }, () => {
                    this.$data.errorMessage = "We're sorry, that user doesn't exist."
                })
            }
            else{
                this.$data.errorMessage = "That's not a valid ID"
            }
        },
        close: function(){

        }
    }
}
</script>