<template>
    <v-app>
        <v-main>
            <canvas class="background-particles"></canvas>
            <v-container fluid fill-height class="black">
                <v-row justify="center">
                    <v-col align-self="center">
                        <v-img @click="$router.push('login')" class="mx-auto clickable" width="400" src="@/assets/sip.png"></v-img>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </v-app>
</template>
<script>
export default {
    name: "FrontPage",
    data: function(){
        return {
            particles: undefined
        }
    },
    created: function(){
        this.$nextTick(
            function(){
                this.particles = window.Particles.init({
                    selector: '.background-particles',
                    connectParticles: true,
                    color: this.$vuetify.theme.currentTheme.primary,
                    sizeVariations: 1,
                    maxParticles: 100,
                });
            }
        );
    },
    beforeDestroy: function(){
        this.particles.destroy();
    }
}
</script>