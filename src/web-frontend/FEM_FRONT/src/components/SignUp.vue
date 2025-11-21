<script setup>

const emit = defineEmits([
  'created',
])

function createAccount(){
    emit('created')
}
</script>
<template>
    <form @submit.prevent="createAccount"
        class="grid grid-cols-2 gap-1">
        <div class="grid grid-rows-3 font-poppins h-full text-xl p-10">
            <div class="flex flex-col justify-start">
                <label>Full Name</label><br></br>
                <input type="text" placeholder="  Enter your name" class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg"/>
            </div>
            <div class="flex flex-col justify-start">
                <label>Email</label><br></br>
                <input type="email" placeholder="  Enter your email" class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg"/>
            </div>
            <div class="flex flex-col justify-start">
                <label>Confirm Password</label><br></br>
                <input type="password" placeholder="  Confirm password" class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg"/>
            </div>
        </div>
        <div class="grid grid-rows-3 font-poppins h-full text-xl p-10">
            <div class="flex flex-col justify-start">
                <label>Username</label><br></br>
                <input type="text" placeholder="  Enter a username" class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl  w-full text-lg"/>
            </div>
            <div class="flex flex-col justify-start">
                <label>Password</label><br></br>
                <input type="password" placeholder="  Enter a password" class="box-content text-l bg-gray-200 text-gray-500
                rounded-2xl w-full text-lg"/>
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