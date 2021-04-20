<template>
    <v-dialog width="700" v-model="$data.isOpen">
        <v-card>
            <v-container fluid>
                <v-form v-model="isValid">
                    <p class="headline secondary--text text--lighten-2"><b>Please enter a user ID:</b></p>
                    <v-divider></v-divider>
                    <v-text-field class="mt-5 mb-n3" 
                    height="80" 
                    filled 
                    v-model="id" 
                    append-icon="mdi-magnify"
                    @keydown.enter.prevent="search"
                    :rules="[v => (!isNaN(v) && v != '') || 'ID must be a number']"
                    ></v-text-field>
                    <p class="error--text">{{errorMessage}}</p>
                    <v-btn block color="primary" :disabled="!isValid" @click="search">SEARCH</v-btn>
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
            isValid: true,
            id: '',
            errorMessage: '',
        }
    },
    methods: {
        show: function(){
            this.$data.id = '';
            this.$data.errorMessage = '';
            this.$data.isOpen = true;
        },
        search: function(){
            if(this.isValid){
                this.errorMessage = '';

                window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + this.$data.id,{
                    headers:{
                            'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then((response) => {
                    this.close();
                    this.$emit('selected-user', response.data);
                }, () => {
                    this.errorMessage = "We're sorry, that user doesn't exist."
                });
            }
        },
        close: function(){
            this.$data.isOpen = false;
        }
    },
}
</script>