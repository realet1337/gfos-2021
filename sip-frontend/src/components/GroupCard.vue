<template>
    <v-card width="350" min-width="350" class="rounded-xl" elevation="0">
        <div v-ripple class="clickable" @click="$router.push('/group/' + $props.group.id)">
            <v-row>
                <v-avatar size="130" class="mx-auto mt-6 mb-0" color="primary">
                    <img v-if="$props.group.picture" :src="imageUrl">
                    <span v-else class="headline">{{$props.group.name.substring(0,1)}}</span>
                </v-avatar>
            </v-row>
            <v-row>
                <v-card-title class="mx-auto">{{ this.$props.group.name}}</v-card-title>
            </v-row>
        </div>
        <v-divider class="mt-3 mb-6"></v-divider>
        <v-row>
            <v-sheet class="mx-auto mb-4 rounded-lg" width="85%" color="secondary darken-3" elevation="7">
                <v-row no-gutters style="display: block; height: 100px;">
                    <p v-if="$props.group.description" class="ml-3 pt-2 max-2-lines">{{$props.group.description}}</p>
                    <p v-else class="ml-13 pt-9 secondary--text">This group has no description</p>
                </v-row>
            </v-sheet>
        </v-row>
    </v-card>
</template>
<script>
import Vue from 'vue'

export default {
    name: 'GroupCard',
    props: ['group'],
    computed:{
        imageUrl: function(){
            return Vue.prototype.$apiBaseUrl + "/upload/pic/group/" + this.$props.group.picture + ".jpg";
        }
    },
}
</script>