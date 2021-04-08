<template>
    <div class="d-flex flex-column fill-height">

        <!-- message section -->
        <div class="flex-grow-1 overflow-y-auto" id="messages">
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
            <div v-for="(chatMessage, index) in chatMessages" :key="chatMessage.id" no-gutters>
                <v-row v-if="index === 0 || new Date(chatMessages[index - 1].sent).getDate() < new Date(chatMessage.sent).getDate()" no-gutters>
                    <v-divider class="mt-3 mb-1"></v-divider>
                    <span class="date-divider-label mx-2">{{new Date(chatMessage.sent).toDateString()}}</span>
                    <v-divider inset class="mt-3 ml-0 mb-1"></v-divider>
                </v-row>
                <v-row v-if="index === 0 || chatMessages[index - 1].author.id !== chatMessage.author.id
                || new Date(chatMessages[index - 1].sent).getDate() < new Date(chatMessage.sent).getDate()" class="text-message mt-5" no-gutters>
                    <!-- beeg -->
                    <v-col cols="auto">
                        <v-avatar @click="$emit('showUser', chatMessage.author)" color="primary" class="clickable" size="48">
                            <img v-if="chatMessage.author.profilePicture" :src="$getAvatarUrl('user', chatMessage.author)">
                            <span v-else>{{chatMessage.author.username.substring(0,1)}}</span>
                        </v-avatar>
                    </v-col>
                    <v-col cols="auto" align-self="center">
                        <p @click="$emit('showUser', chatMessage.author)" class="ml-4 my-auto clickable underline-on-hover">
                            <b>{{chatMessage.author.username}}</b>
                        </p>
                        <p class="ml-4 my-auto">
                            {{chatMessage.content}}
                        </p>
                    </v-col>
                </v-row>

                <!-- smol -->
                <v-row style="height: 25px;" class="flat-text-message text-message my-1" v-else no-gutters>
                    <v-col style="max-width: 63px;">
                        <span class="date reveal-on-hover ml-2 pt-1">{{new Date(chatMessage.sent).getHours().toString().padStart(2,'0') + ":"
                            + new Date(chatMessage.sent).getMinutes().toString().padStart(2,'0')}}</span>
                    </v-col>
                    <v-col>
                        <p>
                            {{chatMessage.content}}
                        </p>
                    </v-col>
                    <v-col style="max-width: 63px;" class="ml-auto mr-1" cols="auto">
                        <MessageOptionsMenu 
                        @deleteMessage="deleteMessage(chatMessage)"
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
            <v-row no-gutters>
                <v-col>
                    <v-text-field
                    @keyup.enter="sendMessage"
                    v-model="message"
                    outlined></v-text-field>
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
        }
    },
    methods: {
        updateMessages: function(before, after){

            var queryParams = {
                count: Vue.prototype.$messageChunkSize,
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
                    if(response.data.length < Vue.prototype.$messageChunkSize){
                        this.$data.hasOldest = true;
                    }
                }
                else if(after){
                    if(response.data.length < Vue.prototype.$messageChunkSize){
                        this.$data.hasNewest = true;
                    }
                }
                else{
                    if(response.data.length < Vue.prototype.$messageChunkSize){
                        this.$data.hasOldest = true;
                    }
                    else{
                        this.$data.hasOldest = false;
                    }
                    this.$data.hasNewest = true;
                }

                this.addMessages(response.data);

            }, (error) => {
                if(error.response.status === 403){
                    console.log("IMPLEMENT ERROR HANDLING BASED ON RESPONSE BODY");
                    console.log(error);
                }else if(error.response.status === 404){
                    console.log("IMPLEMENT ERROR HANDLING BASED ON RESPONSE BODY");
                    console.log(error);
                }
            })
        },
        sendMessage: function(){
            if(this.$data.message){
                window.axios.post(Vue.prototype.$apiHttpUrl + '/api/chats/' + this.$route.params.chatId + '/chat-messages',
                {
                        content: this.$data.message,
                    },
                {
                    headers:{
                        'Authorization': 'Bearer ' + this.$store.state.token,
                    }
                }).then(() => {
                    this.$data.message = "";
                })
            }
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

            //adding some extra spaces where it considers the div to be scrolled all the way down. This also conveniently fixes a
            //(browser?/vuetify?) bug where scrolling the div all the way down using javascript isn't possible.
            //                                                     ||
            //                                                     ||
            //                                                    \  /
            //                                                     \/

            if(msgDiv.scrollHeight - Math.ceil(msgDiv.scrollTop) -5 <= msgDiv.clientHeight && this.$data.hasNewest){
                scrollFlag = true;
            }

            //either all IDs are lower, or none
            if(this.$data.chatMessages.length > 0){
                if(this.$data.chatMessages[0].id > chatMessages[0].id){
                    //just pick an element
                    relativeNode = msgDiv.childNodes[1];
                    preOffsetTop = relativeNode.offsetTop;
                    preScrollTop = msgDiv.scrollTop;
                    if(this.$data.chatMessages.length + chatMessages.length > Vue.prototype.$maxLoadedMessages){
                        this.$data.hasNewest = false;
                    }
                    this.$data.chatMessages = chatMessages.concat(this.$data.chatMessages.slice(0,Vue.prototype.$maxLoadedMessages - chatMessages.length))
                }
                else{
                    //just pick an element
                    relativeNode = msgDiv.childNodes[msgDiv.childNodes.length-2];
                    preOffsetTop = relativeNode.offsetTop;
                    preScrollTop = msgDiv.scrollTop;
                    if(this.$data.chatMessages.length + chatMessages.length > Vue.prototype.$maxLoadedMessages){
                        this.$data.hasOldest = false;
                    }
                    this.$data.chatMessages = this.$data.chatMessages.slice(chatMessages.length + this.$data.chatMessages.length - 
                    Vue.prototype.$maxLoadedMessages, this.$data.chatMessages.length).concat(chatMessages);
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
            }).then(() => {
                console.log("server ate delete req");
            }, (error) => {
                console.log(error);
            });
        }
    },
    watch: {
        $route: function () {
            this.$data.hasOldest = true;
            this.$data.hasNewest = true;
            this.updateMessages();
        }
    },
    created: function(){

        this.updateMessages();

        //create watcher
        var ws = new WebSocket(Vue.prototype.$apiWsUrl + '/api/chats/' + this.$route.params.chatId + '/watch');
        var _this = this;
        ws.onmessage =  function(message){

            if(message.data.startsWith('new: ')){
                var chatMessage = JSON.parse(message.data.substring(5));
                if(_this.$data.hasNewest){
                    _this.addMessages([chatMessage]);
                }
            }else{
                chatMessage = JSON.parse(message.data);
                var index = _this.$data.chatMessages.find((msg) => chatMessage.id === msg.id );
                if(index !== -1){
                    _this.$data.chatMessages[index] = chatMessage;
                }
            }
        };


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
        })

    },
}
</script>