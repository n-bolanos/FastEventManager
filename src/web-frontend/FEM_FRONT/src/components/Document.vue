<script setup>
import { ref } from 'vue'
import IdIcon from "../icons/IconId.vue"

const emit = defineEmits([
  'update',
  'create'
])

const id = ref("")
const error_msj = ref("")

function checkId(){
    id.value = +id.value
    if (isNaN(id.value)){
        error_msj.value = "Make sure your id is a valid number."
        id.value=""
    }
    else {
        emit('create', id.value)
    }
}
</script>
<template>
    <div class="flex flex-col justify-center items-center">
        <form @submit.prevent="checkId"
        class="flex flex-col justify-center items-center">
                <label class="font-poppins text-3xl mb-5">
                    Personal ID
                </label>
                <div class="flex flex-col justify-items-start text-xl mb-4">
                    <div class="flex flex-row items-center justify-between border-2 border-gray-500 bg-purple-100">
                        <IdIcon class="mx-4 bg-purple-100"/>
                        <input v-model="id" required 
                        class= "box-content text-xl bg-purple-100"
                        placeholder="Enter your personal ID" />
                    </div>
                </div>
                <p class="text-red-500 text-lg">{{error_msj}}</p>
                <button  type="submit"
                class="bg-purple-900 mt-5 mb-5 w-full 
                font-poppins text-xl text-neutral-50 
                hover:bg-purple-800 hover:cursor-pointer
                rounded-xl">
                    Continue
                </button>
        </form>
        
    </div>
</template>