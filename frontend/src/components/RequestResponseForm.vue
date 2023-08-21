<template>
  <q-form @submit.prevent="onClkSubmit()">
    <div class="row q-pa-md">
      <div class="col-md-5">
        <q-card class="text-center text-h6 bg-primary text-white">Request</q-card>
        <q-card>
          <q-input input-style="min-height: 220px" v-model="reqTextArea" type="textarea" outlined />
        </q-card>
      </div>
      <div class="column col-md-2 items-center">
        <q-btn label="Submit" color="primary" type="submit" />
      </div>
      <div class="col-md-5">
        <q-card class="text-center text-h6 bg-primary text-white">Response</q-card>
        <q-card>
          <q-input input-style="min-height: 220px" v-model="resTextArea" type="textarea" outlined />
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

const onClkSubmit = () => {
  const json = toJson(reqTextArea.value)
  if (json) {
    const res = postWithProgress(json, 'REQUEST_URL')
    resTextArea.value = JSON.stringify(res)
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
