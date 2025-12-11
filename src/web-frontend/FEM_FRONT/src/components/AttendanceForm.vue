<script setup>
import { ref, onBeforeMount } from 'vue'
import UserIcon from "../icons/IconUser.vue"
import PhoneIcon from "../icons/IconPhone.vue"
import EmailIcon from "../icons/IconMail.vue"
import { update_or_create, confirm, update } from '@/js/Attendance.js'
import { useToast } from 'vue-toastification'

const toast = useToast();

const name = ref('')
const email = ref('')
const number = ref('')
const isUpdating = ref(false)

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

async function submitAns(){
    const body = {
        name: name.value,
        email: email.value,
        contact_number: String(number.value),
        doc_id: props.person_id,
        event_assistance_id: props.event_id
    }
    
    if (!(isUpdating.value)){
        const ans = await confirm(body, props.event_info)
        if (ans == 201){
            toast.success("You have been succesfully registered into the event.")
            emit('created&updated', name.value, email.value, number.value)
        }else{
            toast.error(`Oops, womething went wrong. Error code: ${ans}`)
        }

    }else{
        const ans = await update(body)
        if (ans == 202){
            toast.success("You have succesfully updated your information.")
            emit('created&updated', name.value, email.value, number.value)
            isUpdating.value = false
        }else{
            toast.error(`Oops, womething went wrong. Error code: ${ans}`)
        }
    }
    
    
}

onBeforeMount(async() => {
    const ans = await update_or_create(props.person_id, props.event_id);
    if (!ans){
        isUpdating.value = false;
    } else {
        isUpdating.value = true;
        number.value = ans.contact_number;
        name.value = ans.name;
        email.value = ans.email;
    }
  })
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
                        <input v-model="number" required type="tel" pattern="[0-9]{10}"
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