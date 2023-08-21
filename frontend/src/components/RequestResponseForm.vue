<template>
  <q-form @submit.prevent="onClkSubmit()">
    <div class="row q-pa-md">
      <div class="col-md-5 col-10">
        <q-card>
          <q-card-section class="bg-primary text-white text-center text-h6 q-pa-none">Request</q-card-section>
          <q-input input-style="min-height: 220px" v-model="reqTextArea" type="textarea" outlined square />
        </q-card>
      </div>
      <div class="column col-md-2 items-center">
        <q-btn label="Submit" color="primary" type="submit" />
      </div>
      <div class="col-md-5 col-10">
        <q-card>
          <q-card-section class="bg-primary text-white text-center text-h6 q-pa-none">Response</q-card-section>
          <q-input input-style="min-height: 220px" v-model="resTextArea" type="textarea" outlined square />
        </q-card>
      </div>
    </div>
  </q-form>
</template>
<script setup lang="ts">
import { postWithProgress } from '@/utils/api_common'
import { Notify } from 'quasar'
import { ref } from 'vue'

const reqTextArea = ref('')
const resTextArea = ref('')

const toJson = (str: string) => {
  try {
    const json = JSON.parse(str)
    return json
  } catch (e) {
    return undefined
  }
}

const onClkSubmit = async () => {
  const json = toJson(reqTextArea.value)
  if (json) {
    try {
      const res = await postWithProgress(json, '/api/modbus/req-rep')
      resTextArea.value = JSON.stringify(res)
    } catch (e) {}
  } else {
    Notify.create({
      type: 'negative',
      position: 'top-right',
      group: false,
      timeout: 1000,
      message: 'Invalid JSON string!',
    })
  }
}
</script>
