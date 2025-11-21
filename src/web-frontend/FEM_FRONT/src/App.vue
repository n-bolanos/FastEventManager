<script setup>
import NavBar from "./components/NavBar.vue"
import LoginForm from "./components/LoginForm.vue"
import SignForm from "./components/SignUp.vue"
import {ref} from 'vue'

const msj = ref("")
const logIn = ref(false)
const signUp = ref(false)

function setMsj(new_msj) {
  msj.value = new_msj
}

function switchLogIn() {
  logIn.value = !logIn.value
}

function switchSignUp() {
  signUp.value = !signUp.value
  if (msj.value !== 'SignUp'){
    setMsj("SignUp")
  } else {
    setMsj("")
  }
  
}
</script>

<template>
    <header>
      <NavBar :msj="msj" 
      :is-logged-in="logIn" 
      :is-signing-up="signUp" 
      @LogOut="switchLogIn"/>
    </header>
    <main class="flex justify-center bg-neutral-50 m-10 mt-15 rounded-2xl min-h-100">
      <LoginForm v-if="!logIn && !signUp"
      @isVerified="switchLogIn"
      @signReq="switchSignUp"/>
      <SignForm v-if="!logIn && signUp"
      @created="switchSignUp"/>
    </main>
</template>

<style scoped></style>
