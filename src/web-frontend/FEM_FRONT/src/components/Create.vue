<script setup>
import { ref } from 'vue'
const eventName = ref('')
const location = ref('')
const date = ref('')
const timeStart = ref('')
const timeEnd = ref('')
const capacity = ref(0)
const msj_time = ref('')
const emit = defineEmits([
  'created',
])

function checkConstraints(){
    if (checkTimes()){
        msj_time.value = ""
        return checkDate()
    }
    msj_time.value = "Time start must be earlier that time end."
}

function checkDate(){
    return true
}

function checkTimes(){
    const digits_start = timeStart.value.split(':');
    const digits_end = timeEnd.value.split(':');
    
    return Number(digits_end[0]) > Number(digits_start[0])
}
function createEvent(){
    if (checkConstraints()){
        emit('created')
    }

}
</script>
<template>
    <form @submit.prevent="createEvent"
        class="grid grid-cols-2 gap-1">
        <div class="grid grid-rows-3 font-poppins h-full text-xl p-10">
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Event Name</label>
                <input type="text" v-model="eventName" placeholder="  Enter the event name" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg pt-2 pb-2 pl-2"/>
            </div>
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Date</label>
                <input type="date" v-model="date" placeholder="  Enter the date" required class="box-content text-l bg-gray-200 text-gray-800
                rounded-2xl w-full text-lg pt-2 pb-2 pl-2"/>
            </div>
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Maximum capacity</label>
                <input type="number" v-model="capacity" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg pt-2 pb-2 pl-2"/>
            </div>
        </div>
        <div class="grid grid-rows-3 font-poppins h-full text-xl p-10">
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Location</label>
                <input type="text" v-model="location" placeholder="  Enter the location" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl  w-full text-lg pt-2 pb-2 pl-2"/>
            </div>
            <div class="flex flex-row justify-around">
                <div class="flex flex-col justify-start">
                    <label class="block mb-2">Time Start</label>
                    <input type="time" v-model="timeStart" required class="box-content text-l pl-2 bg-gray-200 text-gray-500
                    rounded-2xl w-full text-lg pt-2 pb-2"/>
                </div>
                <div class="flex flex-col justify-start">
                    <label class="block mb-2">Time End</label>
                    <input type="time" v-model="timeEnd" required class="box-content text-l pl-2 bg-gray-200 text-gray-500
                    rounded-2xl w-full text-lg pt-2 pb-2"/>
                </div>
            </div>
            <p class="text-red-800">{{ msj_time }}</p>
            <div class="flex items-center justify-center">
                <button  type="submit"
                    class="bg-purple-900 w-full 
                    font-poppins text-xl h-10 text-neutral-50 
                    hover:bg-purple-800 hover:cursor-pointer
                    rounded-xl">
                        Save
                </button>
            </div>
        </div>
    </form>
</template>
