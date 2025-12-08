<script setup>
import singleEvent from "./SingleEvent.vue"
import Register  from "./Register.vue"
import RechargeIcon from "../icons/IconRecharge.vue"
import { ref, onBeforeMount } from "vue"
import { get_events } from "../js/Event.js"
import { get_attendances } from "@/js/Attendance.js"
import { useAuthStore } from "@/stores/auth"

const creatorId = useAuthStore().user_id
const isShowing = ref(false)

async function switchAttendances(id = -1){
    isShowing.value = !isShowing.value
    if (id !== -1){
        attendances.value = await get_attendances(id)
    }else{
        attendances.value = []
    }
}

const attendances = ref([])
const events = ref([])

async function recharge(){
    events.value = await get_events(creatorId)
}
onBeforeMount(
  async() => {events.value = await get_events(creatorId)
            setTimeout(await recharge(), 3000);
  }
)

</script>
<template>
    <div class="ml-2 mt-1"><button @click=recharge class="hover:cursor-pointer"><RechargeIcon class="hover:animate-spin"/></button></div>
    <div v-if="!isShowing" class="flex flex-wrap justify-between w-full m-5 overflow-y-auto max-h-90">
        <singleEvent @details="switchAttendances" @deletion="recharge" 
        class= "mb-4" v-for="event in events" :key="event.event_id"  
        :id="event.event_id " :name="event.name_event" :time="event.time"
        :capacity="event.attendance_capacity" :date="event.date"/>
        <label v-if="events.length === 0" class="text-5xl m-auto mt-20 font-poppins">No events found, create one!</label>

    </div>
    <div v-if="isShowing" class="flex flex-col w-full m-5 overflow-x-hidden max-h-80 max-w-full">
        <div class="flex justify-between items-start">
            <button @click="switchAttendances(-1)"
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
                    :key="person.attendanceID"
                    :id="person.attendanceID"
                    :name="person.nameAttendance"
                    :email="person.emailAttendance"
                    :contact="person.contactNumber"
                    :waitlist="person.waitlist"
                />
            </div>
        </div>
    </div>
</template>