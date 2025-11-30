<script setup>
import { ref } from 'vue'
import UserIcon from "../icons/IconUser.vue"
import PwdIcon from "../icons/IconPwd.vue"
import {checkCredentials} from "../assets/js/Authentication.js"
import { useAuthStore } from '@/stores/auth'
const username = ref('')
const password = ref('')
const error_msj = ref('')

const emit = defineEmits([
  'isVerified',
  'signReq'
])

async function verifyCredentials(){
    const authStore = useAuthStore()
    const res = await checkCredentials(username.value, password.value);
    if (res.status === 200){
        const data = await res.data;
        console.log(data)
        authStore.setTokens(data.accessToken, data.refreshToken)
        emit('isVerified')
    } else if (res.status === 409){
        const msg = await res.data
        error_msj.value = 'Sorry! - '+ msg
    }
    
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
                <div class="flex flex-col justify-start">
                    <p class="text-red-800">{{ error_msj }}</p>
                </div>
                <div class="flex flex-col justify-items-start text-xl mb-4">
                    Username or Email
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <UserIcon class="mx-4 bg-purple-100"/>
                        <input v-model="username" required type="text" 
                        class= "box-content text-xl bg-purple-100"
                        placeholder="Enter your username" />
                    </div>
                </div>
                <div class="flex flex-col justify-items-start text-xl">
                    Password
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <PwdIcon class="mx-4 bg-purple-100"/>
                        <input v-model="password" required type="password" 
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