<script setup>
import { ref } from 'vue'
import NavBar from "../components/NavBar.vue"
import IdForm from "../components/Document.vue"
import AttendanceForm from "../components/AttendanceForm.vue"
import { useRoute } from 'vue-router'

const route = useRoute()
const eventId = route.params.id 
const msj = ref("Attendance")
const isLooking = ref( true )
const personId = ref("")

function checkId(){

}

function saveId(id){
  personId.value = id
  isLooking.value = false
}


function register(name, email, number){
  isLooking.value = true
}
</script>

<template>
    <header>
      <NavBar :msj="msj" />
    </header>
    <main class="flex justify-center bg-neutral-50 m-10 mt-8 rounded-2xl min-h-100">
        <IdForm v-if="isLooking" @create="saveId"/>
        <AttendanceForm v-if="!isLooking" @created&updated="register"/>
    </main>

</template>