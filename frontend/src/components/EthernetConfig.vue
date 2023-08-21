<template>
  <q-form @submit.prevent="onClkSubmit">
    <q-select
      v-model="form.protocol"
      emit-value
      map-options
      dense
      outlined
      label="Protocol"
      :options="protocolOptions"
      :error="formError.protocol.length > 0"
      :error-message="formError.protocol"
    />
    <q-input v-model="form.ip" dense outlined label="IP" :error="formError.ip.length > 0" :error-message="formError.ip" />
    <q-input v-model="form.port" dense outlined label="Port" type="number" :error="formError.port.length > 0" :error-message="formError.port" />
    <q-input
      v-model="form.transactionDelay"
      dense
      outlined
      label="Transaction Delay"
      type="number"
      :error="formError.transactionDelay.length > 0"
      :error-message="formError.transactionDelay"
    />
    <q-input v-model="form.timeout" dense outlined label="Timeout" type="number" :error="formError.timeout.length > 0" :error-message="formError.timeout" />

    <q-btn dense color="primary" size="md" label="apply" type="submit" />
  </q-form>
</template>
<script setup lang="ts">
import { post } from '@/utils/api_common'
import { objectDiffKeys, setObjectValueClear } from '@/utils/utils_global'
import { useFormValid } from '@/utils/validation'
import axios from 'axios'
import { ref, watch } from 'vue'

const protocolOptions = [
  { label: 'TCP', value: 0 },
  { label: 'RTU', value: 1 },
  { label: 'ASCII', value: 2 },
]

const form = ref({ protocol: 0, ip: '', port: '', transactionDelay: '', timeout: '' })
const formError = ref({ protocol: '', ip: '', port: '', transactionDelay: '', timeout: '' })

const onClkSubmit = async () => {
  try {
    await post(form.value, '/api/modbus/ethernet')
  } catch (e) {
    if (axios.isAxiosError(e)) {
      useFormValid(e, formError)
    } else console.error(e)
  }
}

watch(
  () => JSON.stringify(form.value),
  (newVal, oldVal) => {
    const diffKeyArr = objectDiffKeys(JSON.parse(oldVal), JSON.parse(newVal))

    diffKeyArr.forEach((v) => {
      if (formError.value[v as keyof typeof formError.value] !== undefined) setObjectValueClear(formError.value)
    })
  }
)
</script>
