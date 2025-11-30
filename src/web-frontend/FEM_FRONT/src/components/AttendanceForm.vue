<script setup>
import { ref } from 'vue'
import UserIcon from "../icons/IconUser.vue"
import PhoneIcon from "../icons/IconPhone.vue"
import EmailIcon from "../icons/IconMail.vue"
import { update_or_create, confirm } from '@/assets/js/Attendance.js'

const name = ref('')
const email = ref('')
const number = ref('')

const props = defineProps({
    event_id: {
        type: Number,
        default: 0
    },
    person_id: {
        type: String,
        default: 0
    },
    event_info: {
        type: Object,
        default: () => ({})
    }
})

const emit = defineEmits([
  'created&updated',
])

function checkNumber(){
    number.value = +number.value
    if (isNaN(number.value)){
        number.value=""
        return false
    }
    return true
}
async function submitAns(){
    if (!checkNumber()){return}
    
    const ans = await update_or_create(props.person_id, props.event_id)
    if (ans.length === 0){
        const body = {
            name: name.value,
            email: email.value,
            contact_number: number.value,
            doc_id: props.person_id,
            event_assistance_id: props.event_id
        }
        
        await confirm(body, props.event_info)
    }
    emit('created&updated', name.value, email.value, number.value)
}
</script>
<template>
    <div class="flex flex-col justify-center items-center">
        <form @submit.prevent="submitAns"
        class="flex flex-col justify-center items-center">
                <div class="flex flex-col justify-items-start text-xl mb-4">
                    Full Name
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <UserIcon class="mx-4 bg-purple-100"/>
                        <input v-model="name" required type="text" 
                        class= "box-content text-xl bg-purple-100"
                        placeholder="Enter your name" />
                    </div>
                </div>
                <div class="flex flex-col justify-items-start text-xl mb-4">
                    Email Adress
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <EmailIcon class="mx-4 bg-purple-100"/>
                        <input v-model="email" required type="email"
                        class= "box-content text-xl bg-purple-100"
                        placeholder="Enter your email-address" />
                    </div>
                </div>
                <div class="flex flex-col justify-items-start text-xl mb-4">
                    Contact Number
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <PhoneIcon class="mx-4 bg-purple-100"/>
                        <input v-model="number" required type="text"
                        class= "box-content text-xl bg-purple-100"
                        placeholder="Enter your number" />
                    </div>
                </div>
                <button  type="submit"
                class="bg-purple-900 mt-5 mb-5 w-full 
                font-poppins text-xl text-neutral-50 
                hover:bg-purple-800 hover:cursor-pointer
                rounded-xl">
                    Submit
                </button>
        </form>
    </div>
</template>