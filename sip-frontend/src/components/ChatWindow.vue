<template>
    <div class="d-flex flex-column fill-height mt-n3">

        <!-- message section -->
        <div class="flex-grow-1 overflow-y-auto" id="messages">
            <v-row v-if="$data.isEmpty" class="fill-height" no-gutters justify="center">
                <v-col cols="auto" class="mx-auto" align-self="center">
                    <span class="my-auto secondary--text text--darken3">{{voidMessage}}</span>
                </v-col>
            </v-row>
            <v-row v-if="$data.canRead === false" class="fill-height" no-gutters justify="center">
                <v-col cols="auto" class="mx-auto" align-self="center">
                    <span class="my-auto secondary--text text--darken3">You don't have permission to read this chat.</span>
                </v-col>
            </v-row>
            <div id="loader-top" v-if="!$data.hasOldest">
                <v-row no-gutters class="mb-5">
                    <v-col cols="auto">
                        <v-skeleton-loader type="avatar"></v-skeleton-loader>
                    </v-col>
                    <v-col align-self="center" class="mx-4 mt-2">
                        <v-skeleton-loader type="text"></v-skeleton-loader>
                    </v-col>
                </v-row>
            </div>
            <div v-for="(chatMessage, index) in chatMessages" :key="chatMessage.id"
            no-gutters>
                <v-row class="mt-3 mb-1" v-if="index === 0 || new Date(chatMessages[index - 1].sent).getDate() < new Date(chatMessage.sent).getDate()" no-gutters>
                    <v-divider class="mt-3 mb-1"></v-divider>
                    <span class="date-divider-label mx-2">{{new Date(chatMessage.sent).toDateString()}}</span>
                    <v-divider inset class="mt-3 ml-0"></v-divider>
                </v-row>

                <!-- different user or 5 min apart -->
                <v-row v-if="index === 0 || chatMessages[index - 1].author.id !== chatMessage.author.id
                || new Date(chatMessages[index - 1].sent).getTime() + 300000 < new Date(chatMessage.sent).getTime()" class="text-message reveal-on-hover-container mt-5 rounded mr-2" no-gutters>
                    <!-- beeg -->
                    <v-col cols="auto" style="max-width: 63px;">
                        <v-avatar @click="$emit('show-user', chatMessage.author)" color="primary" class="clickable mt-1 ml-2" size="40">
                            <img v-if="chatMessage.author.profilePicture" :src="$getAvatarUrl('user', chatMessage.author)">
                            <span v-else>{{chatMessage.author.username.substring(0,1)}}</span>
                        </v-avatar>
                    </v-col>
                    <v-col align-self="center">
                        <v-row no-gutters>
                            <p @click="$emit('show-user', chatMessage.author)" class="ml-4 my-auto clickable underline-on-hover">
                                <b>{{chatMessage.author.username}}</b>
                            </p>
                            <span class="date ml-2 mt-1">{{new Date(chatMessage.sent).getHours().toString().padStart(2,'0') + ":"
                            + new Date(chatMessage.sent).getMinutes().toString().padStart(2,'0')}}</span>
                        </v-row>
                        <p class="ml-4 my-auto" style="word-break: break-all; white-space: break-spaces;">{{chatMessage.content}}<span v-if="chatMessage.edited" class="date ml-1" style="white-space: nowrap;">(edited: {{new Date(chatMessage.edited).toLocaleString()}})</span></p>
                    </v-col>
                    <v-col style="max-width: 20px; min-width: 20px;" class="ml-auto mr-1">
                        <MessageOptionsMenu 
                        :chatMessage="chatMessage"
                        @deleteMessage="deleteMessage(chatMessage)"
                        @editMessage="editMessage(chatMessage)"
                        @copyToClipboard="copyToClipboard(chatMessage.content)"/>
                    </v-col>
                </v-row>

                <!-- smol -->
                <v-row class="flat-text-message text-message reveal-on-hover-container my-1 rounded mr-2" v-else no-gutters>
                    <v-col style="max-width: 64px;" align-self="center">
                        <span class="date reveal-on-hover ml-3">{{new Date(chatMessage.sent).getHours().toString().padStart(2,'0') + ":"
                            + new Date(chatMessage.sent).getMinutes().toString().padStart(2,'0')}}</span>
                    </v-col>
                    <v-col>
                        <!-- yes, this needs to be one line because of the css white space tag -->
                        <p style="word-break: break-all; white-space: break-spaces;" class="mb-0">{{chatMessage.content}}<span v-if="chatMessage.edited" class="date ml-1" style="white-space: nowrap;">(edited: {{new Date(chatMessage.edited).toLocaleString()}})</span></p>
                    </v-col>
                    <v-col class="ml-auto mr-1" style="max-width: 20px; min-width: 20px;">
                        <MessageOptionsMenu
                        :chatMessage="chatMessage"
                        @deleteMessage="deleteMessage(chatMessage)"
                        @editMessage="editMessage(chatMessage)"
                        @copyToClipboard="copyToClipboard(chatMessage.content)"/>
                    </v-col>
                </v-row>
            </div>
            <div id="loader-bottom" v-if="!$data.hasNewest">
                <v-row no-gutters class="mb-5">
                    <v-col cols="auto">
                        <v-skeleton-loader type="avatar"></v-skeleton-loader>
                    </v-col>
                    <v-col align-self="center" class="mx-4 mt-2">
                        <v-skeleton-loader type="text"></v-skeleton-loader>
                    </v-col>
                </v-row>

            </div>
        </div>

        <div class="flex-shrink-1 mt-3 mb-n4">
            <v-row v-if="$data.editing.id" no-gutters class="mb-2">
                <v-col cols="auto" align-self="center">
                    <v-icon 
                    small 
                    color="secondary lighten-2" 
                    class="mb-1"
                    @click="$data.editing = {}; $data.message = ''"
                    >mdi-close-circle</v-icon>
                </v-col>
                <v-col align-self="center">
                    <p class="my-auto ml-2" style="font-size: 12px;">
                    <span 
                    class="secondary--text text--lighten-2 mr-1"
                    >Editing:</span>
                    "{{$data.editing.content.substring(0,20) + ($data.editing.content.length > 20 ? '...':'')}}"</p>
                </v-col>
            </v-row>
            <v-row v-if="$data.canWrite || editing.id" no-gutters>
                <v-col>
                    <v-textarea
                        v-if="!$vuetify.breakpoint.xs"
                        @keydown.enter.exact.prevent="sendMessage"
                        v-model="message"
                        outlined
                        rows="1"
                        auto-grow
                        no-resize
                        class="message-textarea"
                    ></v-textarea>
                    <v-text-field
                        v-else
                        @keydown.enter.exact.prevent="sendMessage"
                        v-model="message"
                        outlined
                    ></v-text-field>
                </v-col>
            </v-row>
            <v-row v-if="$data.canWrite === false && !editing.id" no-gutters justify="center" class="mb-3">
                <v-col cols="auto" align-self="center">
                    <span class="secondary--text text--darken3">You don't have permission to send any messages to this chat.</span>
                </v-col>
            </v-row>
        </div>

    </div>
</template>
<script>
import Vue from 'vue'
import MessageOptionsMenu from './MessageOptionsMenu'

export default {
    name: 'ChatWindow',
    components: {
        MessageOptionsMenu
    },
    data: function(){
        return {
            chatMessages: [],
            hasOldest: true,
            hasNewest: true,
            message: "",
            ignoreJSScroll: true,
            editing: {},
            isEmpty: false,
            ws: undefined,
            canRead: undefined,
            canWrite: undefined,
        }
    },
    methods: {
        updateMessages: function(before, after){

            var queryParams = {
                count: this.$store.state.userProfile.messageChunkSize,
                authorUnblockedBy: this.$store.state.userId,
                reverseBlocking: this.$store.state.userProfile.reverseBlocking ? 1:0,
            }

            if(before){
                queryParams.before = before.id;
            }

            if(after){
                queryParams.after = after.id;
            }
            
            window.axios.get(Vue.prototype.$apiHttpUrl + '/api/chats/' + this.$route.params.chatId + '/chat-messages', {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                },
                params: queryParams
            }).then((response) => {

                if(before){
                    if(response.data.length < this.$store.state.userProfile.messageChunkSize){
                        this.$data.hasOldest = true;
                    }
                }
                else if(after){
                    if(response.data.length < this.$store.state.userProfile.messageChunkSize){
                        this.$data.hasNewest = true;
                    }
                }
                else{
                    if(response.data.length === 0){
                        this.$data.isEmpty = true;
                    }
                    if(response.data.length < this.$store.state.userProfile.messageChunkSize){
                        this.$data.hasOldest = true;
                    }
                    else{
                        this.$data.hasOldest = false;
                    }
                }

                this.addMessages(response.data);

            }, (error) => {
                if(error.response.status === 403){
                    if(error.response.data == "Unauthenticated"){
                        this.$router.push('/');
                    }
                    else if(error.response.data == "Unauthorized"){
                        this.$router.push('/home')
                    }
                    else if(error.response.data == "Insufficient permissions"){
                        //pass
                    }
                }
                else if(error.response.status === 404){
                    this.$router.push('/home')
                }
            })
        },
        sendMessage: function(){
            if(/\S/.test(this.$data.message)){
                if(this.$data.editing.id){
                    window.axios.put(Vue.prototype.$apiHttpUrl + '/api/chat-messages',
                    {
                            //removing leading and trailing whitespaces
                            id: this.$data.editing.id,
                            content: this.$data.message.match(/\S(.*\S)?/s)[0],
                    },
                    {
                        headers:{
                            'Authorization': 'Bearer ' + this.$store.state.token,
                        }
                    }).then(() => {}, (error) => {
                        if(error.response.status === 403){
                            if(error.response.data == "Unauthenticated"){
                                this.$router.push('/');
                            }
                            else if(error.response.data == "Unauthorized"){
                                this.$router.push('/home')
                            }
                            else if(error.response.data == "Insufficient permissions"){
                                //pass
                            }
                        }
                        else if(error.response.status === 404){
                            this.$router.push('/home')
                        }
                    });
                }
                else{
                    window.axios.post(Vue.prototype.$apiHttpUrl + '/api/chats/' + this.$route.params.chatId + '/chat-messages',
                    {
                            //removing leading and trailing whitespaces
                            content: this.$data.message.match(/\S(.*\S)?/s)[0],
                    },
                    {
                        headers:{
                            'Authorization': 'Bearer ' + this.$store.state.token,
                        }
                    }).then(() => {}, (error) => {
                        if(error.response.status === 403){
                            if(error.response.data == "Unauthenticated"){
                                this.$router.push('/');
                            }
                            else if(error.response.data == "Unauthorized"){
                                this.$router.push('/home')
                            }
                        }
                        else if(error.response.status === 404){
                            this.$router.push('/home')
                        }
                    });
                }
            }
            
            this.$nextTick(function(){
                this.$data.message = "";
                this.$data.editing = {};
            });
        },
        scrollDownMessages: function(){
            var msgDiv = document.getElementById('messages');
            msgDiv.scrollTop = msgDiv.scrollHeight - msgDiv.clientHeight;
        },
        addMessages: function(chatMessages){

            if(chatMessages.length === 0){
                return;
            }
            
            this.$data.ignoreJSScroll = true;
            var scrollFlag = false;

            var msgDiv = document.getElementById("messages");

            let relativeNode;
            let preOffsetTop;
            let preScrollTop;

            //adding some extra space where it considers the div to be scrolled all the way down. This also conveniently fixes a
            //(browser?/vuetify?) bug where scrolling the div all the way down using javascript isn't possible.
            //                                                             ||
            //                                                             ||
            //                                                            \  /
            //                                                             \/

            if(msgDiv && msgDiv.scrollHeight - Math.ceil(msgDiv.scrollTop) -5 <= msgDiv.clientHeight && this.$data.hasNewest){
                scrollFlag = true;
            }

            //either all IDs are lower, or none
            if(this.$data.chatMessages.length > 0){
                if(this.$data.chatMessages[0].id > chatMessages[0].id){
                    //just pick an element
                    relativeNode = msgDiv.childNodes[2];
                    preOffsetTop = relativeNode.offsetTop;
                    preScrollTop = msgDiv.scrollTop;
                    if(this.$data.chatMessages.length + chatMessages.length > this.$store.state.userProfile.maxLoadedMessages){
                        this.$data.hasNewest = false;
                    }
                    this.$data.chatMessages = chatMessages.concat(this.$data.chatMessages.slice(0,this.$store.state.userProfile.maxLoadedMessages - chatMessages.length))
                }
                else{
                    //just pick an element
                    relativeNode = msgDiv.childNodes[msgDiv.childNodes.length-2];
                    preOffsetTop = relativeNode.offsetTop;
                    preScrollTop = msgDiv.scrollTop;
                    if(this.$data.chatMessages.length + chatMessages.length > this.$store.state.userProfile.maxLoadedMessages){
                        this.$data.hasOldest = false;
                    }
                    this.$data.chatMessages = this.$data.chatMessages.slice(chatMessages.length + this.$data.chatMessages.length - 
                    this.$store.state.userProfile.maxLoadedMessages, this.$data.chatMessages.length).concat(chatMessages);
                }
            }
            else{
                this.$data.chatMessages = chatMessages;
            }

            if(scrollFlag){
                this.$nextTick(function(){
                    this.scrollDownMessages();
                });
            }
            else{
                this.$nextTick(function(){
                    msgDiv.scrollTop = preScrollTop + relativeNode.offsetTop - preOffsetTop;
                });
            }

            this.$nextTick(function(){
                this.$data.ignoreJSScroll = false;
            });

            if(this.$data.chatMessages.length >= 0){
                this.$data.isEmpty = false;
            }

            
        },
        copyToClipboard: function(text){
            //random blog posts coming in clutch once again
            var el = document.createElement('textarea');
            el.value = text;
            el.setAttribute('readonly', '');
            el.style = {position: 'absolute', left: '-9999px'};
            document.body.appendChild(el);
            el.select();
            document.execCommand('copy');
            document.body.removeChild(el);
        },
        deleteMessage: function(chatMessage){
            window.axios.delete(Vue.prototype.$apiHttpUrl + '/api/chat-messages/' + chatMessage.id, {
                headers:{
                    'Authorization': 'Bearer ' + this.$store.state.token,
                }
            }).then(() => {}, (error) => {
                if(error.response.status === 403){
                    if(error.response.data == "Unauthenticated"){
                        this.$router.push('/');
                    }
                    else if(error.response.data == "Unauthorized"){
                        this.$router.push('/home')
                    }
                }
                else if(error.response.status === 404){
                    this.$router.push('/home')
                }
            });
        },
        removeMessage: function(chatMessage){
            var index = this.$data.chatMessages.findIndex((msg) => chatMessage.id === msg.id);
            this.$data.ignoreJSScroll = true;
            
            this.$data.chatMessages.splice(index, 1);
            
            if(this.$data.chatMessages.length === 0){
                this.$data.isEmpty = true;
            }

            this.$nextTick(function(){
                this.$data.ignoreJSScroll = false;
            });
        },
        editMessage: function(chatMessage){
            this.$data.editing = chatMessage;
            this.$data.message = chatMessage.content;
        },
        updateMessage: function(chatMessage){
            var index = this.$data.chatMessages.findIndex((msg) => chatMessage.id === msg.id );

            this.$data.ignoreJSScroll = true;

            this.$data.chatMessages.splice(index, 1, chatMessage);

            this.$nextTick(function(){
                this.$data.ignoreJSScroll = false;
            });

        },
        createWatcher: function(){
            //create watcher
            var _this = this;
            var ws = new WebSocket(Vue.prototype.$apiWsUrl + '/api/chats/' + this.$route.params.chatId + '/websockets');
            ws.onopen = function(){
                ws.send('Bearer ' + _this.$store.state.token);
            }
            ws.onmessage =  function(message){

                if(message.data.startsWith('updated: ')){
                    var chatMessage = JSON.parse(message.data.substring(9));
                    _this.updateMessage(chatMessage);
                }
                else if(message.data.startsWith('removed: ')){
                    chatMessage = JSON.parse(message.data.substring(9));
                    _this.removeMessage(chatMessage);
                }
            };
            ws.onerror = function(){
                setTimeout(_this.createWatcher,10000);
            }
            this.$data.ws = ws;
        },
        onNewMessage: function(chatMessage){
            if(this.$data.hasNewest && chatMessage.chat.id == this.$route.params.chatId){
                this.addMessages([chatMessage]);
            }
        },
        getPermissions: function(){
            window.axios.get(this.$apiHttpUrl + '/api/chats/' + this.$route.params.chatId + '/permissions',{
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.token,
                },
                params: {
                    user: this.$store.state.userId
                }
            }).then((response) => {
                if(response.status == 200){
                    var tmpCanRead = false;
                    var tmpCanWrite = false;
                    response.data.forEach((permission) => {
                        tmpCanRead = tmpCanRead || permission.canRead;
                        tmpCanWrite = tmpCanWrite || permission.canWrite;
                    });
                    this.canRead = tmpCanRead;
                    this.canWrite = tmpCanWrite;
                }
                else if(response.status == 204){
                    this.$data.canRead = true;
                    this.$data.canWrite = true;
                }
            }, (error) => {
                if(error.response.status === 403){
                    if(error.response.data == "Unauthenticated"){
                        this.$router.push('/');
                    }
                    else if(error.response.data == "Unauthorized"){
                        this.$router.push('/home')
                    }
                }
                else if(error.response.status === 404){
                    this.$router.push('/home')
                }
            })
        }
    },
    created: function(){

        this.updateMessages();

        this.createWatcher();

        //get permissions only if group chat
        if(this.$route.params.groupId){
            this.getPermissions();
        }
        else{
            this.$data.canRead = true;
            this.$data.canWrite = true;
        }

        this.$eventHub.$on('new-message', this.onNewMessage);


        //event listeners for loading new messages
        this.$nextTick(function(){

            var _this = this;
            document.getElementById("messages").addEventListener('scroll', function(event){
                if(_this.$data.ignoreJSScroll === false){
                    var msgDiv = event.target;
                    if(Math.floor(msgDiv.scrollHeight - msgDiv.scrollTop) === msgDiv.clientHeight
                    && !_this.$data.hasNewest){
                        _this.updateMessages(false, _this.$data.chatMessages[_this.$data.chatMessages.length-1]);
                    }
                    else if(msgDiv.scrollTop === 0 && !_this.$data.hasOldest){
                        _this.updateMessages(_this.$data.chatMessages[0]);
                    }
                }

            });
        });

    },
    beforeDestroy: function(){
        if(this.$data.ws){
            this.$data.ws.close();
        }
        this.$eventHub.$off('new-message', this.onNewMessage);
    },
    computed: {
        voidMessage: function(){
            return Vue.prototype.$void[Math.floor(Math.random()*Vue.prototype.$void.length)];
        },
    }
}
</script>