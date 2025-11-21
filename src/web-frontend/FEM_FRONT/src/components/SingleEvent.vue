<script setup>
import ShareIcon from "../icons/IconShare.vue"
const props = defineProps({
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
function showAttendance(){
    
}
</script>
<template>
    <div class="flex flex-row border-2 border-purple-900 rounded-2xl max-h-fit items-center justify-between mx-10 w-2/5 hover:bg-gray-300 ">
        <div class="flex flex-row bg-purple-900 items-end h-full rounded-2xl shrink-0">
            <button class="flex flex-row items-center p-2 pr-5 text-neutral-50 hover:cursor-pointer hover:text-neutral-400">
                <ShareIcon class="hover:cursor-pointer"/>
                <label class="font-poppins text-xl underline hover:cursor-pointer">Share event</label>
            </button>
        </div>
        <div @click=showAttendance class="flex flex-col min-w-12 font-poppins mx-4 py-2 items-center justify-middle
        hover:cursor-pointer">
            <label class="text-2xl hover:cursor-pointer">{{name}}</label>
            <label class="text-xl pt-2 pb-2 hover:cursor-pointer">{{ date }}</label>
            <label class="text-xl hover:cursor-pointer">{{capacity}} people</label>
        </div>
        <button class="bg-purple-900 text-neutral-50 rounded-2xl p-2 px-3 mr-2 ml-2 text-xl
        hover:bg-purple-800 hover:cursor-pointer hover:text-neutral-300">
                Delete
        </button>
    </div>
</template>