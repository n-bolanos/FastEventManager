<script setup>
import { ref } from 'vue'
const name = ref('')
const email = ref('')
const username = ref('')
const password = ref('')
const password_conf = ref('')
const error_msj = ref('')

const emit = defineEmits([
  'created',
])

function createAccount(){
    if (password_conf.value === password.value){
        emit('created')
        error_msj.value = ''
    } else {
        error_msj.value = 'Error - passwords must match.'
    }
}
</script>
<template>
    <form @submit.prevent="createAccount"
        class="grid grid-cols-2 gap-1">
        <div class="grid grid-rows-3 font-poppins h-full text-xl p-10">
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Full Name</label>
                <input type="text" v-model="name" placeholder="  Enter your name" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg pt-2 pb-2"/>
            </div>
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Email</label>
                <input type="email" v-model="email" placeholder="  Enter your email" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg pt-2 pb-2"/>
            </div>
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Confirm Password</label>
                <input type="password" v-model="password_conf" ref="pwd_conf" placeholder="  Confirm password" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg pt-2 pb-2"/>
                <p class="text-red-800">{{ error_msj }}</p>
            </div>
        </div>
        <div class="grid grid-rows-3 font-poppins h-full text-xl p-10">
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Username</label>
                <input type="text" v-model="username" placeholder="  Enter a username" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl  w-full text-lg pt-2 pb-2"/>
            </div>
            <div class="flex flex-col justify-start">
                <label class="block mb-2">Password</label>
                <input type="password" v-model="password" placeholder="  Enter a password" required class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg pt-2 pb-2"/>
            </div>
            <div class="flex items-center justify-center">
                <button  type="submit"
                    class="bg-purple-900 w-full 
                    font-poppins text-xl h-10 text-neutral-50 
                    hover:bg-purple-800 hover:cursor-pointer
                    rounded-xl">
                        Create account
                </button>
            </div>
        </div>
    </form>
</template>