<template>
    <v-app v-if="$store.state.initialized">
        <v-navigation-drawer app permanent floating color="secondary darken-4">
            <div class="d-flex flex-column fill-height">
                <div class="flex-grow-1">
                    <v-list nav dense>
                        <v-list-item link to="/edit/profile">
                            <v-list-item-icon>
                                <v-icon>mdi-home</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Profile</v-list-item-title>
                        </v-list-item>
                        <v-divider class="mb-2"></v-divider>
                        <v-list-item link to="/edit/settings">
                            <v-list-item-icon>
                                <v-icon>mdi-cog</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Settings</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </div>
                <div class="flex-shrink-1">
                    <v-list nav dense>
                        <v-divider class="mb-2"></v-divider>
                        <v-list-item link @click="$router.push('/home')">
                            <v-list-item-icon>
                                <v-icon>mdi-keyboard-return</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Return to home</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </div>
            </div>
        </v-navigation-drawer>
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
    name: 'Edit',
    components: {
        LoadingScreen
    },
    beforeRouteEnter: function(to, from, next){        
        if(to.fullPath.endsWith('/edit/') || to.fullPath.endsWith('/edit')){
           next('/edit/profile'); 
        }
        else{
            next();
        }
    }
}
</script>