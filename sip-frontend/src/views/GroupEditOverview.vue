<template>
    <v-container>
        <v-form v-if="group" v-model="isValid">
            <v-row no-gutters class="mb-4 ml-3">
                <v-col cols="24">
                    <h3>Edit group details: </h3>
                    <v-divider></v-divider>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col class="mx-2">
                    <v-text-field :rules="[v => !!v || 'The group needs a name', v => /\S/.test(v) || 'The group needs a name']" outlined v-model="group.name" label="name"></v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col class="mx-2">
                    <v-textarea outlined rows="4" no-resize v-model="group.description" label="description"></v-textarea>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col cols="auto">
                    <v-avatar v-if="imgFile">
                        <img :src="avatarUrl">
                    </v-avatar>
                    <v-avatar v-else-if="group.picture">
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
            </v-row>
            <v-row no-gutters>
                <v-col cols="auto" class="mx-auto">
                    <v-btn :disabled="!isValid" width="150" large class="ml-auto mt-2" color="primary" depressed @click="submit">UPDATE</v-btn>
                </v-col>
            </v-row>
            <template v-if="group.owner.id == $store.state.userId">
                <v-row no-gutters class="mt-4 mb-n2">
                    <v-divider></v-divider>
                </v-row>
                <v-row>
                    <v-col cols="24" class="mx-auto">
                        <v-btn @click="openDeleteDialog" block large text color="red">DELETE GROUP</v-btn>
                    </v-col>
                </v-row>
            </template>
        </v-form>
        <v-dialog v-if="group" v-model="showDeleteDialog" width="500">
            <v-card>
                <v-card-title>Delete group?</v-card-title>
                <v-card-text>Are you sure you want to delete the group <b>{{group.name}}</b>?</v-card-text>
                <v-card-actions class="secondary darken-4">
                    <v-btn class="ml-auto" text x-large @click="showDeleteDialog = false">CANCEL</v-btn>
                    <v-btn color="red" x-large @click="deleteGroup">DELETE</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>
<script>
import Vue from 'vue'

//@vuese
//Erlaubt das Bearbeiten grundlegender Eigenschaften einer Gruppe wie z.B. ihren Namen und das Löschen einer Gruppe.
export default {
    name: 'GroupEditOverview',
    data: function(){
        return {
            isValid: false,
            cancelTokenSource: undefined,
            imgFile: undefined,
            group: undefined,
            showDeleteDialog: false,
        }
    },
    methods: {
        //@vuese
        //ändert das imgFile und sendet ein post-request an "/api/images/groups/pictures" um einen neuen Picture-Code zu erhalten.
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
                window.axios.post(Vue.prototype.$getApiUrl('http') + '/images/groups/pictures', formData, {
                    headers: {
                        'content-type': 'multipart/form-data'
                    },
                    cancelToken: cancelTokenSource.token
                }).then((response) => {
                    this.$data.group.picture = response.data.picture;
                })
            }
        },
        //@vuese
        //Aktualisiert die Gruppe beim Server
        submit: function(){
            window.axios.put(Vue.prototype.$getApiUrl('http') + '/groups', this.group, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                //pass
            }, () => {
                this.$router.push('/');
            })
        },
        //@vuese
        //Öffnet einen Dialog, der das Löschen der Gruppe bestätigen lässt.
        openDeleteDialog: function(){
            this.showDeleteDialog = true;
        },
        //@vuese
        //Löscht die Gruppe beim Server
        deleteGroup: function(){
            window.axios.delete(Vue.prototype.$getApiUrl('http') + '/groups/' + this.group.id, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.$router.push('/home/groups');
            }, () => {
                this.$router.push('/home/groups');
            })
        }
    },
    created: function(){
        window.axios.get(Vue.prototype.$getApiUrl('http') + '/groups/' + this.$route.params.groupId, {
            headers:{
                'Authorization': 'Bearer ' + this.$store.state.token,
            }
        }).then((response) => {
            this.group = response.data;
        }, () => {
            this.$router.push('/home/groups');
        })
    },
    computed: {
        //@vuese
        //Generiert eine Object-Url anhand des imgFile.
        avatarUrl: function(){
            return URL.createObjectURL(this.$data.imgFile);
        }
    },
}
</script>