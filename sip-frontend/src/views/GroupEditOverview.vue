<template>
    <v-container>
        <v-form v-if="group">
            <v-row no-gutters class="mb-2 ml-3">
                <h3>Edit group details: </h3>
            </v-row>
            <v-row no-gutters>
                <v-col class="mx-2">
                    <v-text-field outlined v-model="group.name" label="name"></v-text-field>
                </v-col>
                <v-col class="mx-2">
                    <v-text-field outlined v-model="group.description" label="description"></v-text-field>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col cols="auto">
                    <v-avatar v-if="group.picture">
                        <img :src="$getAvatarUrl('group', group)">
                    </v-avatar>
                    <v-row v-else class="mt-4" no-gutters>
                        <span>No picture</span>
                    </v-row>
                </v-col>
                <v-col class="mx-2">
                    <v-file-input
                        @change="updateFile"
                        outlined
                        accept="image/png, image/jpeg, image/bmp"
                        placeholder="Pick an avatar"
                        append-icon="mdi-camera"
                        prepend-icon=""
                        label="Avatar"
                    ></v-file-input>
                </v-col>
                <v-col cols="auto" class="mx-2">
                    <v-btn class="ml-auto mt-2" color="primary" depressed>UPDATE</v-btn>
                </v-col>
            </v-row>
        </v-form>
    </v-container>
</template>
<script>
import Vue from 'vue'

export default {
    name: 'GroupEditOverview',
    data: function(){
        return {
            isValid: false,
            cancelTokenSource: undefined,
            imgFile: undefined,
            group: undefined,
        }
    },
    methods: {
        updateFile: async function(file){
            if(this.$data.cancelTokenSource){
                this.$data.cancelTokenSource.cancel();
            }
            if(!file){
                this.$data.group.picture = undefined;
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
                    console.log(response.data);
                    this.$data.group.picture = response.data.picture;
                })
            }
        },
    },
    created: function(){
        window.axios.get(Vue.prototype.$apiHttpUrl + '/api/groups/' + this.$route.params.groupId, {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.group = response.data;
        }, () => {
            this.$router.push('/home/groups');
        })
    }
}
</script>