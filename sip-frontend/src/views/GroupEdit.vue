<template>
    <v-app v-if="$store.state.initialized">
        <v-app-bar app v-if="$vuetify.breakpoint.xs">
            <v-app-bar-nav-icon @click="showNavDrawer = true"></v-app-bar-nav-icon>
            <v-app-bar-title>{{getTitle($route.path.split('/')[4])}}</v-app-bar-title>
        </v-app-bar>
        <v-navigation-drawer app :permanent="!$vuetify.breakpoint.xs" floating color="secondary darken-4" v-model="showNavDrawer">
            <div class="d-flex flex-column fill-height">
                <div class="flex-grow-1">
                    <v-list nav dense>
                        <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/overview'">
                            <v-list-item-icon>
                                <v-icon>mdi-home</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Overview</v-list-item-title>
                        </v-list-item>
                        <v-divider class="mb-2"></v-divider>
                        <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/users'">
                            <v-list-item-icon>
                                <v-icon>mdi-account-multiple</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Users</v-list-item-title>
                        </v-list-item>
                        <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/roles'">
                            <v-list-item-icon>
                                <v-icon>mdi-badge-account</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Roles</v-list-item-title>
                        </v-list-item>
                        <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/permissions'">
                            <v-list-item-icon>
                                <v-icon>mdi-lock</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Permissions</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </div>
                <div class="flex-shrink-1">
                    <v-list nav dense>
                        <v-divider class="mb-2"></v-divider>
                        <v-list-item link @click="$router.push('/group/' + $route.params.groupId)">
                            <v-list-item-icon>
                                <v-icon>mdi-keyboard-return</v-icon>
                            </v-list-item-icon>
                            <v-list-item-title>Return to group</v-list-item-title>
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
//Zeigt einen Navigation Drawer auf der linken Bildschirmseite, ein router-view mit den Sub-Routes "GroupEditOverview", "GroupEditPermissionsView", "GroupEditRolesView", "GroupEditUsersView" auf der rechten Seite.
//Der Navigation drawer verschwindet und lässt sich optional öffnen, sollte der Bildschirm weniger als 600px breit sein.
export default {
    name: 'GroupEdit',
    components: {
        LoadingScreen
    },
    beforeRouteEnter: function(to, from, next){        
        if(to.fullPath.endsWith('/edit/') || to.fullPath.endsWith('/edit')){
           next('/group/' + to.fullPath.split('/')[2] + '/edit/overview'); 
        }
        else{
            next();
        }
    },
    data: function(){
        return {
            showNavDrawer: false
        }
    },
    methods: {
        //@vuese
        //Generiert den Titel, basierend auf einem Schluesselwort aus der Route.
        getTitle: function(keyword){
            const dict = {
                'overview': 'Overview',
                'users': 'Users',
                'roles': 'Roles',
                'permissions': 'Permissions',
            };
            return dict[keyword];
        }
    }
}
</script>