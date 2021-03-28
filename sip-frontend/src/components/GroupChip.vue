<template>
    <v-card width="35%" max-width="50%" class="rounded-pill" outlined>
        <v-row no-gutters>
            <v-col cols="auto">
                <v-avatar class="rounded-circle" color="primary">
                    <img v-if="$props.group.picture" :src="imageUrl">
                    <span v-else class="white--text headline">{{$props.group.name.substring(0,1).toUpperCase()}}</span>
                </v-avatar>
            </v-col>
            <v-col align-self="start" class="">
                <v-card-text class="my-n2"><h3 class="max-1-line">{{$props.group.name}}eeeeeeeeeeeeeeeeeeeeeee</h3></v-card-text>
            </v-col>
        </v-row>
    </v-card>
</template>
<script>
import Vue from 'vue';

export default {

    name: 'GroupChip',
    props: ['group'],
    computed: {
        imageUrl: function(){
            return Vue.prototype.$apiBaseUrl + '/upload/images/' + this.$props.group.picture + '.jpg';
        }
    }
}
</script>