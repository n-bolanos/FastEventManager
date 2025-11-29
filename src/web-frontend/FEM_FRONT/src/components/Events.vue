<script setup>
import singleEvent from "./SingleEvent.vue"
import Register  from "./Register.vue"
import RechargeIcon from "../icons/IconRecharge.vue"
import { ref, onBeforeMount } from "vue"
import { get_events } from "../assets/js/Event.js"
import { get_attendances } from "@/assets/js/Attendance.js"

const isShowing = ref(false)

async function switchAttendances(id = -1){
    isShowing.value = !isShowing.value
    if (id !== -1){
        attendances.value = await get_attendances(id)

    }
}

const attendances = ref([])
const events = ref([])

async function recharge(id){
    events.value = await get_events(1)
}
onBeforeMount(
    //todo: change to the actual id
  async() => {events.value = await get_events(1)
            setTimeout(await recharge(1), 3000);
  }
)

</script>
<template>
    <div class="ml-2 mt-1"><button @click=recharge class="hover:cursor-pointer"><RechargeIcon class="hover:animate-spin"/></button></div>
    <div v-if="!isShowing" class="flex flex-wrap justify-between w-full m-5 overflow-y-auto max-h-90">
        <singleEvent @details="switchAttendances" @deletion="recharge" 
        class= "mb-4" v-for="event in events" :key="event.event_id"  
        :id="event.event_id ":name="event.name_event" 
        :capacity="event.attendance_capacity" :date="event.date"/>
        <label v-if="events.length === 0" class="text-5xl m-auto mt-20 font-poppins">No events found, create one!</label>

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