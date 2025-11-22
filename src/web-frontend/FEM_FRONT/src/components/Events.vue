<script setup>
import singleEvent from "./SingleEvent.vue"
import Register  from "./Register.vue"
import { ref } from "vue"

const isShowing = ref(false)
function switchAttendances(id = -1){
    isShowing.value = !isShowing.value
    if (id !== -1){

    }

}
const attendances = ref([
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: true
    },
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: false
    },
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: true
    },
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: false
    },
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: true
    },
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: false
    },
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: true
    },
    {   id:0,
        name: "Santiago Fetecua",
        email: "sfetecua@unal.edu.co",
        contact: "3107799009",
        waitlist: false
    },
])
const events = ref([
    {
        id: 0,
        name : "Swifty party",
        date : "17:00 - 16:00",
        capacity : 200
    },
    {   
        id:1,
        name : "Bank robery",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {   
        id:2,
        name : "Bank robery",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {   
        id:3,
        name : "banana",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {   
        id:4,
        name : "mango",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {   
        id:5,
        name : "peach",
        date : "20:00 - 25:00",
        capacity : 200
    },
    {   
        id:4,
        name : "mango",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {   
        id:5,
        name : "peach",
        date : "20:00 - 25:00",
        capacity : 200
    },
    {   
        id:4,
        name : "mango",
        date : "17:00 - 16:00",
        capacity : 100
    },
    {   
        id:5,
        name : "peach",
        date : "20:00 - 25:00",
        capacity : 200
    },
])
</script>
<template>
    <div v-if="!isShowing" class="flex flex-wrap justify-between w-full m-5 overflow-y-auto max-h-90">
        <singleEvent @details="switchAttendances" class= "mb-4" v-for="event in events" :key="event.id"  :id="event.id ":name="event.name" :capacity="event.capacity" :date="event.date"/>
    </div>
    <div v-if="isShowing" class="flex flex-col w-full m-5 overflow-x-hidden max-h-80 max-w-full">
        <div class="flex justify-between items-start">
            <button @click="switchAttendances"
            class="text-grey-300 underline text-lg inline-flex h-fit w-4
            hover:text-gray-500 hover:cursor-pointer">back</button>
            <label class=" text-4xl h-fit">Confirmed Guest List</label>
            <br class="w-4"></br>
        </div>
        <div class="flex flex-col w-full">
            <div id="headers" class="grid grid-cols-4 w-full mt-5 text-2xl">
                <span class="px-2 text-left">Name</span>
                <span class="px-2 text-left">Mail</span>
                <span class="px-2 text-left">Number</span>
                <span class="px-2 text-center">Waitlist</span>
            </div>
            <div class="flex flex-col w-full mt-3">
                <Register
                    class="mb-2"
                    v-for="person in attendances"
                    :key="person.id"
                    :id="person.id"
                    :name="person.name"
                    :email="person.email"
                    :contact="person.contact"
                    :waitlist="person.waitlist"
                />
            </div>
        </div>
    </div>
</template>