<template>
    <v-app>
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
            <!-- only load view if user is certainly logged in -->
            <div v-if="this.$store.state.userId">
                <router-view></router-view>
            </div>
        </v-main>
    </v-app>
</template>

<script>

export default {
    name: 'Home',
        components: {
    },
}
</script>
