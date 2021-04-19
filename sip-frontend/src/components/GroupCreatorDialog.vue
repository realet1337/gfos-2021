<template>
    <v-dialog width="700" v-model="$data.isOpen">
        <v-card>
            <v-container fluid>
                <v-form v-model="$data.isValid">
                    <v-row align="center" no-gutters class="mb-6">
                        <v-avatar class="mx-auto" size="120">
                            <img v-if="$data.imgFile" :src="avatarUrl">
                            <span v-else>No picture</span>
                        </v-avatar>
                    </v-row>
                    <v-row no-gutters>
                        <v-file-input
                            @change="updateFile"
                            outlined
                            accept="image/png, image/jpeg, image/bmp"
                            placeholder="Pick an avatar"
                            append-icon="mdi-camera"
                            prepend-icon=""
                            label="Avatar"
                        ></v-file-input>
                    </v-row>
                    <p class="secondary--text text--lighten-2"><b>Pick a name:</b></p>
                    <v-text-field class="mt-5 mb-n3" filled v-model="name" :rules="[v => !!v || 'The group needs a name', v => /\S/.test(v) || 'The chat needs a name']"></v-text-field>
                    <p class="secondary--text text--lighten-2"><b>Describe the group:</b></p>
                    <v-textarea class="mt-5 mb-n3" filled v-model="description" no-resize rows="4"></v-textarea>
                    <v-btn block depressed large color="primary" @click="create" :disabled="!$data.isValid">CREATE</v-btn>
                </v-form>
            </v-container>  
        </v-card>
    </v-dialog>
</template>
<script>
import Vue from 'vue';
export default {
    name: 'GroupCreatorDialog',
    data: function(){
        return {
            isOpen: false,
            name: '',
            description: '',
            isValid: false,
            cancelTokenSource: undefined,
            imgFile: undefined,
            picture: undefined,
        }
    },
    methods: {
        updateFile: async function(file){
            if(this.$data.cancelTokenSource){
                this.$data.cancelTokenSource.cancel();
            }
            if(!file){
                this.$data.picture = undefined;
                this.$data.imgFile = undefined;
            }
            else{
                this.$data.imgFile = file;
                const formData = new FormData();
                formData.append('file', file);
                const cancelTokenSource = window.axios.CancelToken.source();
                this.$data.cancelTokenSource = cancelTokenSource;
                window.axios.post(Vue.prototype.$apiHttpUrl + '/api/images/groups/pictures', formData, {
                    headers: {
                        'content-type': 'multipart/form-data'
                    },
                    cancelToken: cancelTokenSource.token
                }).then((response) => {
                    this.$data.picture = response.data.picture;
                })
            }
        },
        show: function(){
            this.$data.name = '';
            this.$data.description = '';
            this.$data.cancelTokenSource = undefined;
            this.$data.imgFile = undefined;
            this.$data.picture = undefined;
            this.$data.isOpen = true;
        },
        create: function(){
            const group = {
                name: this.$data.name,
            }
            if(this.description){
                group.description = this.description.match(/\S(.*\S)?/s)[0];
            }
            if(this.picture){
                group.picture = this.picture;
            }
            window.axios.post(Vue.prototype.$apiHttpUrl + '/api/groups/', group, {
                headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then((response) => {
                this.close();
                this.$emit('open-group-id', response.data.id);
                // this.$emit('show-group', response.data);
            }, () => {
                //no errors possible if request is correct
            })
        },
        close: function(){
            this.$data.isOpen = false;
        }
    },
    computed: {
        avatarUrl: function(){
            return URL.createObjectURL(this.$data.imgFile);
        }
    },
}
</script>