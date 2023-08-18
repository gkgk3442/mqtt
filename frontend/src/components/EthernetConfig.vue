<template>
  <InputLeftLabel v-model="form.protocol" :error="formError.protocol" dense label="Protocol" :options="protocolOptions" />
  <InputLeftLabel v-model="form.ip" :error="formError.ip" dense label="IP" />
  <InputLeftLabel v-model="form.port" :error="formError.port" dense number label="Port" />
  <InputLeftLabel v-model="form.transactionDelay" :error="formError.transactionDelay" dense number label="Transaction Delay" />
  <InputLeftLabel v-model="form.timeout" :error="formError.timeout" dense number label="Timeout" />

  <q-btn label="apply" @click="onClkSubmit" />
</template>
<script setup lang="ts">
import InputLeftLabel from '@/components/InputLeftLabel.vue'
import { useModalForm } from '@/composables/useModalForm'
import { post } from '@/utils/api_common'

enum Protocol {
  TCP = 0,
  RTU = 1,
  ASCII = 2,
}

const protocolOptions = [
  { label: 'TCP', value: 0 },
  { label: 'RTU', value: 1 },
  { label: 'ASCII', value: 2 },
]

const { form, formError, submit } = useModalForm({
  formInit: { protocol: 0, ip: '', port: '', transactionDelay: '', timeout: '' },
  submit: {
    url: '/api/modbus/ethernet',
  },
})

const onClkSubmit = async () => {
  submit(post)
}
</script>
