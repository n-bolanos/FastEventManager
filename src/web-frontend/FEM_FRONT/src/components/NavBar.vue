<script setup>
import LoginIcon from '../icons/IconLogin.vue'

const props = defineProps({
    msj: {
        type: String,
        default: ""
    },
    isLoggedIn: {
        type: Boolean,
        default: false
    },
    isSigningUp: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits([
  'LogOut'
])


function switchLogin() {
    emit('LogOut')
}

</script>
<template>
    <nav class="flex h-[7vh] justify-between">
        <img src="../assets/images/FEM_icon.png" class="object-contain"/>
        <div v-if="isLoggedIn | isSigningUp" class="felx items-end text-3xl mt-3 text-neutral-50 font-poppins">
            <label>{{ msj }}</label>
        </div>
        <div id="login-logout" class="flex items-center justify-end text-l text-neutral-50 w-fit">
            <button v-if="isLoggedIn"
            class= "flex items-center justify-center transition-transform duration-500 hover:scale-110 hover:cursor-pointer" 
            @click="switchLogin">
                 <LoginIcon class="size-8 hover:cursor-pointer"/>
                 <label class="hover:cursor-pointer">
                    Log out
                 </label>
            </button>
            <label v-if="!isLoggedIn"
            :class="{ 'opacity-100': isLoggedIn, 'opacity-0': !isLoggedIn }"
            class= "flex items-center justify-center transition-transform duration-500" >
                 <LoginIcon/>
                 <label>
                    Log out
                 </label>
                </label>
        </div>
    </nav>
</template>