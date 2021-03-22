<template>
    <div id="counter">
        Counter: {{ counter }}
    </div>
    <div id="bind-attribute">
        <span v-bind:title="message">
            Hover your mouse over me for a few seconds to see my dynamically bound
            title!
        </span>
    </div>
    <div id="event-handling">
        <p>{{ message }}</p>
        <button v-on:click="reverseMessage">Reverse Message</button>
    </div>
    <div id="two-way-binding">
        <p>{{ message }}</p>
        <input v-model="message" />
    </div>
    <div id="list-rendering">
        <ol>
            <li v-for="todo in todos" :key="todo">
            {{ todo.text }}
            </li>
        </ol>
    </div>
    <div id="basic-event">
        <button @click="counter += 1">Add 1</button>
        <p>The button above has been clicked {{ counter }} times.</p>
    </div>

</template>

<script>
    export default {

        name: 'test',
        data() {
            return {
                counter: 0,
                message: 'You loaded this page on ' + new Date().toLocaleString(),
                todos: [
                    { text: 'Learn JavaScript' },
                    { text: 'Learn Vue' },
                    { text: 'Build something awesome' }
                ]
            }
        },
        methods: {
            reverseMessage() {
            this.message = this.message
                .split('')
                .reverse()
                .join('')
            }
        }
    }
</script>
