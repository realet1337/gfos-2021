<template>
    <v-menu
        rounded="lg"
        offset-y
        offset-x
        >
            <template v-slot:activator="{ on }">
                <v-icon v-on="on" small color="secondary darken-2" class="mt-1 reveal-on-hover">mdi-dots-vertical</v-icon>
            </template>

            <v-list
            nav
            dense>
                <v-list-item
                    link
                    @click="$emit('copyToClipboard')"
                    >
                    <v-list-item-content
                    style="min-width: 100px;">Copy</v-list-item-content>
                </v-list-item>
                <v-list-item
                link
                @click="$emit('deleteMessage')"
                >
                    <v-list-item-content class="red--text text--lighten-2"
                    style="min-width: 100px;">Delete</v-list-item-content>
                </v-list-item>
            </v-list>
        </v-menu>
</template>

<script>
export default {
    name: 'MessageOptionsMenu',

}
</script>