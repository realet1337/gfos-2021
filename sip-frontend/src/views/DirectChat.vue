<template>
    <v-app>
        <v-main>
            <v-container fluid>
                <v-row>
                    <v-col cols="auto">
                        <v-responsive max-width="250" min-width="250">
                            <p>ogchamp</p>
                        </v-responsive>
                    </v-col>
                    <v-col cols="auto">
                        <v-responsive>
                            <ChatWindow/>
                        </v-responsive>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
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
}
</script>