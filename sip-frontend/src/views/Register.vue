<template>
    <v-app>
        <v-main>
            <v-container fill-height>
                <v-row
                    align="center"
                    justify="center"
                >
                    <v-col>
                                            
                        <v-card v-if="!$vuetify.breakpoint.xs" class="mx-auto rounded-lg" width="700">
                            <RegisterForm/>
                        </v-card>
                        <RegisterForm v-else class="mx-auto"/>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
</template>

<style>

</style>

<script>
import RegisterForm from '@/components/RegisterForm'

export default {
    components: {
        RegisterForm
    },
    name: 'Register'
}
</script>
