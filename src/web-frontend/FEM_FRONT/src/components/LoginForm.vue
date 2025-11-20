<script setup>
import { ref } from 'vue'
import UserIcon from "../icons/IconUser.vue"
import PwdIcon from "../icons/IconPwd.vue"
const username = ref('')
const password = ref('')

const emit = defineEmits([
  'isVerified',
  'signReq'
])

function verifyCredentials(){
    emit('isVerified')
}

function signUp(){
    emit('signReq')
}
</script>
<template>
    <div class="flex flex-col justify-center items-center">
        <form @submit.prevent="verifyCredentials"
        class="flex flex-col justify-center items-center">
                <label class="font-poppins text-3xl mb-5">
                    Welcome
                </label>
                <div class="flex flex-col justify-items-start text-xl mb-4">
                    Username
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <UserIcon class="mx-4 bg-purple-100"/>
                        <input v-model="username" type="text" 
                        class= "box-content text-xl bg-purple-100"
                        placeholder="Enter your username" />
                    </div>
                </div>
                <div class="flex flex-col justify-items-start text-xl">
                    Password
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <PwdIcon class="mx-4 bg-purple-100"/>
                        <input v-model="password" type="password" 
                        class= "box-content text-xl bg-purple-100"
                        placeholder="Enter your password" />
                    </div>
                </div>

                <button  type="submit"
                class="bg-purple-900 mt-5 mb-5 w-full 
                font-poppins text-xl text-neutral-50 
                hover:bg-purple-800 hover:cursor-pointer
                rounded-xl">
                    Continue
                </button>
        </form>
        <div class="flex flex-row justify-around w-full mt-2">
            <button @click="signUp" class="text-xs underline hover:text-purple-800 hover:cursor-pointer">
                Create new account
            </button>
            <button class="text-xs underline hover:text-purple-800 hover:cursor-pointer">
                Forgot password
            </button>
        </div>
    </div>
</template>