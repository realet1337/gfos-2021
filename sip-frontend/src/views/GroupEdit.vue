<template>
    <v-app v-if="$store.state.initialized">
        <v-navigation-drawer app floating color="secondary darken-4">
            <v-list nav dense>
                <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/overview'">
                    <v-list-item-title>Overview</v-list-item-title>
                </v-list-item>
                <v-divider class="mb-2"></v-divider>
                <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/users'">
                    <v-list-item-title>Users</v-list-item-title>
                </v-list-item>
                <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/roles'">
                    <v-list-item-title>Roles</v-list-item-title>
                </v-list-item>
                <v-list-item link :to="'/group/' + $route.params.groupId + '/edit/manage-roles'">
                    <v-list-item-title>Manage roles</v-list-item-title>
                </v-list-item>
            </v-list>
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