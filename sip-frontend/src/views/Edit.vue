<template>
    <v-app v-if="$store.state.initialized">
        <v-app-bar app v-if="$vuetify.breakpoint.xs">
            <v-app-bar-nav-icon @click="showNavDrawer = true"></v-app-bar-nav-icon>
            <v-app-bar-title>{{getTitle($route.path.split('/')[2])}}</v-app-bar-title>
        </v-app-bar>
        <v-navigation-drawer app :permanent="!$vuetify.breakpoint.xs" floating color="secondary darken-4" v-model="showNavDrawer">
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

//@vuese
//Zeigt einen Navigation Drawer auf der linken Bildschirmseite, ein router-view mit den Sub-Routes "EditProfile", "EditSettings" auf der rechten Seite.
//Der Navigation drawer verschwindet und lässt sich optional öffnen, sollte der Bildschirm weniger als 600px breit sein.
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
    },
    data: function(){
        return {
            showNavDrawer: false,
        }
    },
    methods: {
        //@vuese
        //Generiert den Titel, basierend auf einem Schlüsselwort aus der Route.
        getTitle: function(keyword){
            const dict = {
                'profile': 'Profile',
                'settings': 'Settings'
            };
            return dict[keyword];
        }
    }
}
</script>