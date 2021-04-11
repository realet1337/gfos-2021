<template>
    <v-app v-if="$store.state.initialized">
        <v-app-bar app hide-on-scroll>
            <v-row no-gutters justify="center">
                <v-col cols="auto">
                    <img src="@/assets/sip-banner.png" width="200">
                </v-col>
            </v-row>
            <template v-slot:extension>
                <v-tabs centered>
                    <v-tab to="/home/direct-chats">DIRECT CHATS</v-tab>
                    <v-tab to="/home/groups">GROUPS</v-tab>
                </v-tabs>
            </template>
        </v-app-bar>
        <v-main>
            <router-view></router-view>
        </v-main>
    </v-app>
    <v-app v-else>
        <LoadingScreen></LoadingScreen>
    </v-app>
</template>

<script>
import LoadingScreen from '@/components/LoadingScreen'

export default {
    name: 'Home',
    components: {
        LoadingScreen
    },
}
</script>
