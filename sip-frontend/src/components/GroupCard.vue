<template>
    <v-card width="350" min-width="300" class="rounded-xl" elevation="0">
        <div v-ripple class="clickable" @click="$router.push('/group/' + $props.group.id)">
            <v-row>
                <v-avatar size="130" class="mx-auto mt-6 mb-0" color="primary">
                    <img v-if="$props.group.picture" :src="$getAvatarUrl('group', $props.group)">
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

//@vuese
//@group COMPONENTS
//Zeigt den Namen, das Bild und die Beschreibung einer Gruppe auf einer Karte.
export default {
    name: 'GroupCard',
    props: {
        //Die angezeigte Gruppe.
        group: Object
    },
}
</script>