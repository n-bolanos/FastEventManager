<script setup>
import singleEvent from "./SingleEvent.vue"
import { ref } from "vue"

const events = ref([
    {
        name : "Swifty party",
        date : "17:00 - 16:00",
        capacity : 200
    },
    {
        name : "Bank robery",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {
        name : "Bank robery",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {
        name : "banana",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {
        name : "mango",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {
        name : "peach",
        date : "20:00 - 25:00",
        capacity : 200
    }
])
</script>
<template>
    <div class="flex flex-wrap justify-between w-full m-5 overflow-y-auto max-h-80">
        <singleEvent class= "mb-4" v-for="event in events" :key="event.name" :name="event.name" :capacity="event.capacity" :date="event.date"/>
    </div>
</template>