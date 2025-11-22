<script setup>
import Check from "../icons/IconCheck.vue"
import Cross from "../icons/IconCross.vue"
const props = defineProps({
    id: {
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
        type: Number,
        default: " "
    },
    waitlist: {
        type: Boolean,
        default: false
    }

})
</script>
<template>
    <div
        class="grid grid-cols-4 text-xl w-full [&_label]:truncate [&_label]:overflow-hidden [&_label]:whitespace-nowrap [&_label]:px-2"
        :class="{ 'bg-red-500': waitlist, 'bg-neutral-50': !waitlist }"
    >
        <label class="truncate text-left">{{ name }}</label>
        <label class="truncate text-left">{{ email }}</label>
        <label class="truncate text-left">{{ contact }}</label>
        <div class="flex items-center justify-center">
            <Check v-if="waitlist"/>
            <Cross v-if="!waitlist"/>
        </div>
    </div>
</template>