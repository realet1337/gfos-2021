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
                @click="$emit('copy-to-clipboard')"
            >
                <v-list-item-content
                    style="min-width: 100px;"
                >Copy</v-list-item-content>
            </v-list-item>
            <v-list-item
                v-if="chatMessage.author.id === $store.state.userId"
                link
                @click="$emit('edit-message')"
            >
                <v-list-item-content
                    style="min-width: 100px;"
                >Edit</v-list-item-content>
            </v-list-item>
            <v-list-item
                v-if="chatMessage.author.id === $store.state.userId"
                link
                @click="$emit('delete-message')"
            >
                <v-list-item-content
                    class="red--text text--lighten-2"
                    style="min-width: 100px;"
                >Delete</v-list-item-content>
            </v-list-item>
        </v-list>
    </v-menu>
</template>

<script>
//@vuese
//@group COMPONENTS
//Ein Menü mit Aktionen für Nachrichten.
export default {
    name: 'MessageOptionsMenu',
    props: {
        //Die Nachricht, für die das Menü angezeigt wird.
        chatMessage: Object
    },
    methods: {
        dummy: function(){
            //@vuese
            //Wenn der Nutzer den "Copy" Menüeintrag anklickt.
            this.$emit('copy-to-clipboard');
            //@vuese
            //Wenn der Nutzer den "Edit" Menüeintrag anklickt.
            this.$emit('edit-message');
            //@vuese
            //Wenn der Nutzer den "Delete" Menüeintrag anklickt.
            this.$emit('delete-message');
        }
    }
}
</script>