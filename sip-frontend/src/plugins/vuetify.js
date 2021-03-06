import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

import colors from 'vuetify/lib/util/colors'


Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        options: {
          customProperties: true
        },
        themes: {
            dark: {
                primary: colors.blue.lighten1,
                secondary: colors.grey.darken1,
                accent: colors.shades.black,
                error: colors.red,
            },
        },
        dark: true,
    },
});
