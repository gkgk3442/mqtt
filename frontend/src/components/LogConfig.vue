<template>
  <InputLeftLabel v-model="form.datetime" :error="formError.datetime" dense label="Date/Time" />
  <InputLeftLabel v-model="form.level" :error="formError.level" dense label="Level" :options="levelOptions" />
  <InputLeftLabel v-model="form.description" :error="formError.description" dense number label="Description" />

  <q-btn dense color="primary" size="md" label="apply" @click="onClkSubmit" />
</template>
<script setup lang="ts">
import InputLeftLabel from '@/components/InputLeftLabel.vue'
import { useModalForm } from '@/composables/useModalForm'
import { post } from '@/utils/api_common'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
dayjs.extend(customParseFormat)

const levelOptions = [
  { label: 'TRACE', value: 0 },
  { label: 'DEBUG', value: 1 },
  { label: 'INFO', value: 2 },
  { label: 'WARN', value: 3 },
  { label: 'ERROR', value: 4 },
  { label: 'FATAL', value: 5 },
]

const { form, formError, submit } = useModalForm({
  formInit: { datetime: dayjs(), level: 0, description: '' },
  submit: {
    url: '/api/modbus/log',
    handleRequest: (v) => ({ ...v, datetime: dayjs(v.datetime).format('yyyy-MM-dd HH:mm:ss.SSS') }),
  },
})

const onClkSubmit = async () => await submit(post)
</script>
