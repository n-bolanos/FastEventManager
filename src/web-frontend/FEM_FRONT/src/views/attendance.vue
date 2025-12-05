<script setup>
import { ref, onBeforeMount } from 'vue'
import NavBar from "../components/NavBar.vue"
import IdForm from "../components/Document.vue"
import AttendanceForm from "../components/AttendanceForm.vue"
import {confirm_event} from "../assets/js/Attendance.js"
import { useRoute } from 'vue-router'

const route = useRoute()
const event_id = +route.params.id 
const msj = ref("Attendance")
const invalid_event = ref(true)
const is_looking = ref(true)
const person_id = ref("")
const event_info = ref({})

async function loadEvent() {
  const event = await confirm_event(event_id)
  if (event.response.length === 0){
    invalid_event.value = true
  } else {
      invalid_event.value = false
      const values = event.response[0]
      event_info.value = {
              capacity: values.attendance_capacity,
              event_name: values.name_event,
              date: values.date,
              location: values.location,
              creator_id: values.creator_id
      }
    }
}

function save_id(id){
  person_id.value = id
  is_looking.value = false
}

function register(name, email, number){
  is_looking.value = true
}

onBeforeMount(
  () => loadEvent()
)
</script>

<template>
    <header>
      <NavBar v-if="!invalid_event" :msj="msj" />
    </header>
    <main v-if="invalid_event" class="text-gray-100 text-9xl">
        404 Not Found
    </main>
    <main v-if="!invalid_event" class="flex justify-center bg-neutral-50 m-10 mt-8 rounded-2xl min-h-100">
        <IdForm v-if="is_looking" @create="save_id"/>
        <AttendanceForm :event_id="event_id" :person_id="person_id" :event_info="event_info" v-if="!is_looking" @created&updated="register"/>
    </main>

</template>