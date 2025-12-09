<script setup>
import { switch_att } from "@/js/Attendance";
import Check from "../icons/IconCheck.vue"
import Cross from "../icons/IconCross.vue"
const props = defineProps({
    id: {
        type: String,
        default: 0
    },
    event_id: {
        type: Number,
        default: 0
    },
    name: {
        type: String,
        default: " "
    },
    email: {
        type: String,
        default: " "
    },
    contact: {
        type: String,
        default: " "
    },
    waitlist: {
        type: Boolean,
        default: false
    }

})

const emit = defineEmits([
  'update'
])

async function switch_() {
    await switch_att(props.id, props.event_id)
    emit('update')
}
</script>
<template>
    <div @click="switch_ "
        class="grid rounded-2xl grid-cols-4 text-xl w-full [&_label]:truncate hover:cursor-pointer
        [&_label]:overflow-hidden [&_label]:whitespace-nowrap [&_label]:px-2  [&_label]:hover:cursor-pointer"
        :class="{ 'bg-red-500': waitlist, 'bg-neutral-50': !waitlist }"
    >
        <label class="truncate text-left ">{{ props.name }}</label>
        <label class="truncate text-left">{{ email }}</label>
        <label class="truncate text-left">{{ contact }}</label>
        <div class="flex items-center justify-center">
            <Check v-if="waitlist"/>
            <Cross v-if="!waitlist"/>
        </div>
    </div>
</template>