<template>
  <q-form @submit.prevent="onClkSubmit">
    <InputLeftLabel v-model="form.protocol" :error="formError.protocol" dense label="Protocol" :options="protocolOptions" />
    <InputLeftLabel v-model="form.ip" :error="formError.ip" dense label="IP" />
    <InputLeftLabel v-model="form.port" :error="formError.port" dense number label="Port" type="number" :min="0" :max="65535" />
    <InputLeftLabel v-model="form.transactionDelay" :error="formError.transactionDelay" dense number label="Transaction Delay" type="number" :min="10" :max="100" />
    <InputLeftLabel v-model="form.timeout" :error="formError.timeout" dense number label="Timeout" type="number" :min="1000" :max="3000" />

    <!-- <div class="full-width row reverse"> -->
    <q-btn dense color="primary" size="md" label="apply" type="submit" />
    <!-- </div> -->
  </q-form>
</template>
<script setup lang="ts">
import InputLeftLabel from '@/components/small/InputLeftLabel.vue'
import { useModalForm } from '@/composables/useModalForm'
import { post } from '@/utils/api_common'

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

const onClkSubmit = async () => await submit(post)
</script>
