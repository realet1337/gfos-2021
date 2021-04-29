<template>
    <v-container>
        <v-form v-if="user" v-model="isValid[0]">
            <v-row no-gutters class="mb-4 ml-3">
                <v-col cols="24">
                    <h3>Edit details: </h3>
                    <v-divider></v-divider>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col class="mx-2">
                    <v-text-field :rules="[v => !!v || 'Username is required' , v=> !/\s/g.test(v) || 'Name cannot contain whitespaces.']" outlined v-model="user.username" label="username"></v-text-field>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col class="mx-2">
                    <v-text-field outlined v-model="user.status" label="status"></v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col class="mx-2">
                    <v-textarea outlined rows="4" no-resize v-model="user.info" label="info"></v-textarea>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col cols="auto">
                    <v-avatar v-if="imgFile">
                        <img :src="avatarUrl">
                    </v-avatar>
                    <v-avatar v-else-if="user.profilePicture">
                        <img :src="$getAvatarUrl('user', user)">
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
            <v-row v-if="imageError" no-gutters>
                <span class="red">Can't use this image.</span>
            </v-row>
            <v-row no-gutters>
                <v-col cols="auto" class="mx-auto">
                    <v-btn :disabled="!isValid[0]" width="150" large class="ml-auto mt-2" color="primary" depressed @click="updateUser">UPDATE</v-btn>
                </v-col>
            </v-row>
        </v-form>
        <v-form v-if="user" v-model="isValid[1]" class="mt-4">
            <v-row no-gutters class="mb-4 ml-3">
                <v-col cols="24">
                    <h3>Change password: </h3>
                    <v-divider></v-divider>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col class="mx-2">
                    <v-text-field type="password" outlined v-model="password1" label="Password"></v-text-field>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col class="mx-2">
                    <v-text-field type="password" outlined v-model="password2" label="Repeat password"></v-text-field>
                </v-col>
            </v-row>
            <v-row no-gutters>
                <v-col cols="auto" class="mx-auto">
                    <v-btn :disabled="!isValid[1]" width="250" large class="ml-auto mt-2" color="primary" depressed @click="changePass">CHANGE PASSWORD</v-btn>
                </v-col>
            </v-row>
        </v-form>
        <v-row no-gutters class="mt-4 mb-n2">
            <v-divider></v-divider>
        </v-row>
        <v-row>
            <v-col cols="24" class="mx-auto">
                <v-btn @click="openDeleteDialog" block large text color="red">DELETE ACCOUNT</v-btn>
            </v-col>
        </v-row>
        <v-dialog v-if="user" v-model="showDeleteDialog" width="500">
            <v-card>
                <v-card-title>Delete account?</v-card-title>
                <v-card-text>If you delete your account all your <b>messages and groups</b> will disappear.
                <br>
                <b>This action is not reversible.</b>
                <br>
                Are you sure you want to delete your account?</v-card-text>
                <v-card-actions class="secondary darken-4">
                    <v-btn class="ml-auto" text x-large @click="showDeleteDialog = false">CANCEL</v-btn>
                    <v-btn color="red" x-large @click="deleteUser">DELETE</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>
<script>
import Vue from 'vue'

//@vuese
//@group VIEWS
//Erlaubt, das Profil des eigenen Nutzers zu bearbeiten, das Passwort zu ändern oder den Account zu löschen.
export default {
    name: 'EditProfile',
    data: function(){
        return {
            //Ob die Forms gültig sind. Index 0 bezieht sich auf das Profil-Formular, Index 1 auf das Passwort Formular.
            isValid: [false, false],
            //Ein Cancel-Token für das Axios-Request zu "/api/images/users/pictures". Notwending, falls sich der User umentscheidet, bevor das erste Request fertig ist.
            cancelTokenSource: undefined,
            //Blob für das ausgewählte Bild
            imgFile: undefined,
            //Eigener Nutzer
            user: undefined,
            //Ob der Dialog zur Kontolöschung angezeigt wird.
            showDeleteDialog: false,
            password1: '',
            password2: '',
            imageError: false,
        }
    },
    methods: {
        //@vuese
        //Ändert das imgFile und sendet ein post-request an "/api/images/users/pictures" um einen neuen Picture-Code zu erhalten.
        //@arg file
        updateFile: async function(file){
            if(this.$data.cancelTokenSource){
                this.$data.cancelTokenSource.cancel();
            }
            if(!file){
                this.$data.user.profilePicture = undefined;
                this.$data.imgFile = undefined;
            }
            else{
                this.imageError = false;
                this.$data.imgFile = file;
                const formData = new FormData();
                formData.append('file', file);
                const cancelTokenSource = window.axios.CancelToken.source();
                this.$data.cancelTokenSource = cancelTokenSource;
                window.axios.post(Vue.prototype.$getApiUrl('http') + '/images/users/pictures', formData, {
                    headers: {
                        'content-type': 'multipart/form-data'
                    },
                    cancelToken: cancelTokenSource.token
                }).then((response) => {
                    this.$data.user.profilePicture = response.data.picture;
                }, () => {
                    this.imageError = true;
                    this.imgFile = undefined;
                    this.picture = undefined; 
                });
            }
        },
        //@vuese
        //Aktualisiert den User beim Server.
        updateUser: function(){
            window.axios.put(Vue.prototype.$getApiUrl('http') + '/users', this.user, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.$store.commit('setUser', this.user);
            }, () => {
                this.$router.push('/');
            });
        },
        //@vuese
        //Aktualisiert das Passwort beim Server.
        changePass: function(){
            //prepare object

            const tmpUser = Object.assign({}, this.$store.state.user);
            tmpUser.pass = this.password1;

            window.axios.put(Vue.prototype.$getApiUrl('http') + '/users', tmpUser, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.password1 = '';
                this.password2 = '';
            }, () => {
                this.$router.push('/');
            })
        },
        //@vuese
        //Öffnet einen Dialog, der das Löschen des Nutzers bestätigen lässt.
        openDeleteDialog: function(){
            this.showDeleteDialog = true;
        },
        //@vuese
        //Löscht den User beim Server
        deleteUser: function(){
            window.axios.delete(Vue.prototype.$getApiUrl('http') + '/users/' + this.user.id, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {
                this.$router.push('/');
            }, () => {
                this.$router.push('/');
            })
        }
    },
    created: function(){
        this.user = Object.assign({}, this.$store.state.user)
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