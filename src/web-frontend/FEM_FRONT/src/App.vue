<script setup>
import NavBar from "./components/NavBar.vue"
import LoginForm from "./components/LoginForm.vue"
import SignForm from "./components/SignUp.vue"
import Events from "./components/Events.vue"
import PlusIcon from "./icons/IconAdd.vue"
import Create from "./components/Create.vue"
import {ref} from 'vue'

const msj = ref("")
const logIn = ref(false)
const signUp = ref(false)
const creation = ref(false)

function setMsj(new_msj) {
  msj.value = new_msj
}

function switchLogIn() {
  logIn.value = !logIn.value
  creation.value = false
  if (logIn.value === true){
    setMsj("EVENT SCHEDULER")
  }
}
function switchCreation() {
  creation.value = !creation.value
  if (creation.value === true){
    setMsj("CREATE EVENT")
  }
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
    <button @click="switchCreation" v-if= "logIn && !creation" class="flex flex-row bg-neutral-50 rounded-2xl p-2 font-poppins text-shadow-purple-900 mx-10 mt-15
    hover:text-neutral-50 hover:bg-violet-900 hover:cursor-pointer">
      <PlusIcon class="hover:cursor-pointer"/>
      <label class="hover:cursor-pointer">Create Event</label>
    </button>
    <main class="flex justify-center bg-neutral-50 m-10 mt-8 rounded-2xl min-h-100">
      <LoginForm v-if="!logIn && !signUp"
      @isVerified="switchLogIn"
      @signReq="switchSignUp"/>
      <SignForm v-if="!logIn && signUp"
      @created="switchSignUp"/>
      <Events v-if="logIn && !signUp && !creation"/>
      <Create v-if="logIn && creation"
      @created="switchCreation"/>
    </main>
</template>

<style scoped></style>
