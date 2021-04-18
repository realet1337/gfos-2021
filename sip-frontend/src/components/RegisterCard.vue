<template>
     <v-card class="rounded-lg" width="700">
        <v-container>
            <v-row justify="center">
                <h1 class="header mt-2 mb-n1">Hi!</h1>
            </v-row>
            <v-row justify="center">
                <h4 class="subheader mb-7">Register an account</h4>
            </v-row>
            <v-stepper v-model="step">
                <v-stepper-header>
                    <v-stepper-step step="1">Login Information</v-stepper-step>
                    <v-divider></v-divider>
                    <v-stepper-step step="2">Customize</v-stepper-step>
                    <v-divider></v-divider>
                    <v-stepper-step step="3">Configure</v-stepper-step>
                </v-stepper-header>
                <v-stepper-items>
                    <v-stepper-content step="1">
                        <v-form v-model="$data.isValid[0]">
                            <v-row>
                                <v-col>
                                    <p class="label-text mb-1 ml-3"><b>USERNAME</b></p>
                                    <div class="input-field">
                                        <v-text-field
                                            v-model="username"
                                            single-line
                                            outlined
                                            class="input-field"
                                            :rules="[v => !!v || 'Username is required' , v=> !/\s/g.test(v) || 'Name cannot contain whitespaces.']"
                                        ></v-text-field>
                                    </div>
                                </v-col>
                                <v-col>
                                    <p class="label-text mb-1 ml-3"><b>EMAIL</b></p>
                                    <div class="input-field">
                                        <v-text-field
                                            v-model="email"
                                            single-line
                                            outlined
                                            class="input-field"
                                            :rules="[v => !!v || 'Email is required'], v => new Reg(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/.test(v) || 'This email is invalid'"
                                        ></v-text-field>
                                    </div>
                                </v-col>
                            </v-row>
                            <v-row>
                                <v-col>
                                    <p class="label-text mb-1 ml-3"><b>PASSWORD</b></p>
                                    <div class="input-field">
                                        <v-text-field
                                            v-model="password"
                                            single-line
                                            outlined
                                            class="input-field"
                                            :rules="[v => !!v|| 'Password is required']"
                                            :append-icon="showPassword1 ? 'mdi-eye' : 'mdi-eye-off'"
                                            :type="showPassword1 ? 'text' : 'password'"
                                            @click:append="showPassword1 = !showPassword1"
                                        ></v-text-field>
                                    </div>
                                </v-col>
                                <v-col>
                                    <p class="label-text mb-1 ml-3"><b>REPEAT PASSWORD</b></p>
                                    <div class="input-field">
                                        <v-text-field
                                            v-model="password2"
                                            single-line
                                            outlined
                                            class="input-field"
                                            :rules="[v => $data.password == $data.password2|| 'Passwords must match']"
                                            :append-icon="showPassword2 ? 'mdi-eye' : 'mdi-eye-off'"
                                            :type="showPassword2 ? 'text' : 'password'"
                                            @click:append="showPassword2 = !showPassword2"
                                        ></v-text-field>
                                    </div>
                                </v-col>
                            </v-row>
                            <span v-if="$data.fail.emailInUse" class="red--text">This email is already in use.</span>
                            <v-row no-gutters>
                                <v-btn
                                    class="ml-auto"
                                    color="primary" 
                                    @click="step++"
                                    :disabled="!$data.isValid[0]"
                                >next</v-btn>
                            </v-row>
                        </v-form>
                    </v-stepper-content>
                    <v-stepper-content step="2">
                        <v-form v-model="$data.isValid[1]">
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
                            <v-row>
                                <v-col>
                                    <p class="label-text mb-1 ml-3"><b>INFO</b></p>
                                    <div class="input-field">
                                        <v-textarea
                                            v-model="userInfo"
                                            outlined
                                            rows="4"
                                            no-resize
                                            placeholder="Tell us something about yourself."
                                        ></v-textarea>
                                    </div>
                                </v-col>
                            </v-row>
                            <v-row no-gutters>
                                <v-btn
                                    text
                                    class="ml-auto"
                                    color="primary" 
                                    @click="step--"
                                >previous</v-btn>
                                <v-btn
                                    class="ml-1"
                                    color="primary" 
                                    @click="step++"
                                    :disabled="!$data.isValid[1]"
                                >next</v-btn>
                            </v-row>
                        </v-form>
                    </v-stepper-content>
                    <v-stepper-content step="3">
                        <v-form class="ml-3 mt-2" v-model="$data.isValid[2]">
                            <v-row no-gutters>
                                <p class="label-text">At most how many messages should the application have loaded simultaneously?</p>
                            </v-row>
                            <v-slider 
                            v-model="config.maxLoadedMessages" 
                            class="ml-3 mr-6" 
                            thumb-label
                            step="50"
                            max="500"
                            min="50"
                            ></v-slider>
                            <v-row no-gutters>
                                <p class="label-text">How many new messages should the application load at once?</p>
                            </v-row>
                            <v-slider 
                            v-model="config.messageChunkSize" 
                            class="ml-3 mr-6" 
                            thumb-label
                            step="10"
                            max="500"
                            min="10"
                            :rules="[v => $data.config.maxLoadedMessages >= $data.config.messageChunkSize || 'This should be smaller than the maximum loaded messages']"
                            ></v-slider>
                            <v-checkbox v-model="config.reverseBlocking"  label="Hide messages from Users that have blocked you"></v-checkbox>
                            <v-row no-gutters>
                                <v-btn
                                    text
                                    class="ml-auto"
                                    color="primary" 
                                    @click="step--"
                                >previous</v-btn>
                                <v-btn
                                    class="ml-18"
                                    color="primary" 
                                    @click="onSubmit"
                                    :disabled="!$data.isValid[2]"
                                >Register</v-btn>
                            </v-row>
                        </v-form>
                    </v-stepper-content>
                </v-stepper-items>
            </v-stepper>
            <v-row class="mt-2 mb-0 ml-3">
                <span class="secondary--text text--lighten-2" style="font-size: 11px;">Already have an account? <router-link to="/login">Login!</router-link></span>
            </v-row>
        </v-container>
    </v-card>
</template>

<script>
import Vue from 'vue'

export default {
    data: function() {
        return {
            username: '',
            email: '',
            password: '',
            password2: '',
            error: false,
            step: 1,
            picture: undefined,
            userInfo: '',
            isValid: [false, true, true],
            config: {
                reverseBlocking: false,
                messageChunkSize: 50,
                maxLoadedMessages: 100,
            },

            //woulda loved to use an array but vue
            showPassword1: false,
            showPassword2: false,
            processing: false,
            fail: {
                emailInUse: false
            },
            cancelTokenSource: undefined,
            imgFile: undefined,
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
                window.axios.post(Vue.prototype.$apiHttpUrl + '/api/images/users/pictures', formData, {
                    headers: {
                        'content-type': 'multipart/form-data'
                    },
                    cancelToken: cancelTokenSource.token
                }).then((response) => {
                    this.$data.picture = response.data.picture;
                })
            }
        },
        onSubmit: async function() {
            var userPayload = {
                username: this.$data.username,
                email: this.$data.email,
                pass: this.$data.password,
                userProfiles: [this.$data.config]
            };
            if(this.$data.picture){
                userPayload.profilePicture = await this.$data.picture;
            }
            if(this.$data.userInfo){
                userPayload.info = this.$data.userInfo;
            }
            window.axios.post(Vue.prototype.$apiHttpUrl + '/api/users', userPayload).then(() => {
                this.$data.processing = false;
                this.$router.push('/login');
            }, () => {
                //so, basically, the only reason this should ever fail is when the email is wrong so we just display an error for that and set step back to 1
                this.$data.step = 1;
                this.$data.fail.emailInUse = true;
            });
            this.$data.processing = true;
        }

    },
    computed: {
        avatarUrl: function(){
            return URL.createObjectURL(this.$data.imgFile);
        }
    },
    name: 'RegisterCard'
}
</script>