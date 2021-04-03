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
        <v-divider class="mt-3 mb-9"></v-divider>
        <v-row>
            <v-sheet class="mx-auto mb-4 rounded-lg" width="85%" color="secondary darken-3" elevation="7">
                <div class="mx-3 mt-1" style="display: block; height: 70px">
                    <v-col cols="auto" align-self="center">
                        <p v-if="this.$props.group.description" class="max-2-lines">{{$props.group.description}}</p>
                        <p v-else class="ml-3 secondary--text">This group doesn't have a description.</p>
                    </v-col>
                </div>
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