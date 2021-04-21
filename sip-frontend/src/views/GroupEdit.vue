<template>
    <v-app v-if="$store.state.initialized">
        <v-navigation-drawer app permanent floating color="secondary darken-4">
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
    }
}
</script>