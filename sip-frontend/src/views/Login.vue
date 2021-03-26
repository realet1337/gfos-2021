<template>
    <v-app>
        <v-main>
            <v-container fill-height>
                <v-row
                    align="center"
                    justify="center"
                >
                    <v-col>
                       <LoginCard/>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
</template>

<style>

</style>

<script>
import LoginCard from '@/components/LoginCard.vue'

export default {
    components: {
        LoginCard
    },
    name: 'LoginPage'
}
</script>
