<template>
    <v-main>
        <v-container fluid>
            <v-row no-gutters>
                <v-col cols="auto">
                    <div style="max-width:250px; min-width:250px">
                        <p>ogchamp</p>
                    </div>
                </v-col>
                <v-col>
                    <div style="height: 95vh;">
                        <ChatWindow @showUser="showUser"/>
                    </div>
                </v-col>
            </v-row>
        </v-container>
    </v-main>
</template>

<script>
import ChatWindow from '@/components/ChatWindow'

export default {
    name: 'DirectChat',
    components: {
        ChatWindow,
    },
    data: function(){
        return {}
    },
    methods: {
        showUser: function(user){
            console.log(user);
        }
    }
}
</script>