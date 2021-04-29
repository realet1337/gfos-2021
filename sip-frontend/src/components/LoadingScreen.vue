<template>
    <v-main>
        <v-container fill-height>
            <v-row justify="center">
                <v-col align-self="center">
                    <v-img class="mx-auto my-auto" src="@/assets/sip.png" width="200"/>
                </v-col>
            </v-row>
        </v-container>
    </v-main>
</template>
<script>

//@vuese
//@group COMPONENTS
//Zeigt das Logo mittig auf einem dunkelgrauen Hintergrund.
//Wird von einigen Komponenten angezeigt, wenn die App noch nicht initialisiert ist.
export default {
    name: 'LoadingScreen'
}
</script>