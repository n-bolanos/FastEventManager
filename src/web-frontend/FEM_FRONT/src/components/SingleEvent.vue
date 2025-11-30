<script setup>
import ShareIcon from "../icons/IconShare.vue"
import { delete_events, get_share_link } from "../assets/js/Event.js"
const props = defineProps({
    id: {
        type: Number,
        default: 0
    },
    name: {
        type: String,
        default: " "
    },
    date: {
        type: String,
        default: " "
    },
    capacity: {
        type: Number,
        default: " "
    }
})
const emit = defineEmits([
  'details',
  'deletion'
])

function showAttendance(){
    emit('details', props.id)
}

async function deleteEvent(){
    await delete_events(props.id)
    emit('deletion')
}

async function shareEvent() {
    const link = await get_share_link(props.id)
    navigator.clipboard.writeText(link);
    
}
</script>
<template>
    <div class="flex flex-row border-2 border-purple-900 rounded-2xl max-h-[50%] items-center justify-between mx-10 w-[40%] min-w-md hover:bg-gray-300 ">
        <div class="flex flex-row bg-purple-900 items-end h-full rounded-2xl">
            <button @click="shareEvent" class="flex flex-row items-center p-2 pr-5 text-neutral-50 hover:cursor-pointer hover:text-neutral-400">
                <ShareIcon class="hover:cursor-pointer"/>
                <label class="font-poppins text-xl underline hover:cursor-pointer">Share event</label>
            </button>
        </div>
        <div @click=showAttendance class="flex flex-col min-w-12 font-poppins mx-4 py-2 justify-middle
        hover:cursor-pointer">
            <label class="text-2xl hover:cursor-pointer w-[25%]">{{name}}</label>
            <label class="text-xl pt-2 pb-2 hover:cursor-pointer">{{ date }}</label>
            <label class="text-xl hover:cursor-pointer">{{capacity}} people</label>
        </div>
        <button @click="deleteEvent" class="bg-purple-900 text-neutral-50 rounded-2xl p-2 px-3 mr-2 ml-2 text-xl
        hover:bg-purple-800 hover:cursor-pointer hover:text-neutral-300">
                Delete
        </button>
    </div>
</template>