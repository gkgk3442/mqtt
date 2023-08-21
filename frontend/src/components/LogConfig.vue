<template>
  <q-form @submit.prevent="onClkSubmit">
    <DateTimePicker @update="(v) => updateTime(v)" :init="start" />
    <InputLeftLabel v-model="form.level" :error="formError.level" dense label="Level" :options="levelOptions" />
    <InputLeftLabel v-model="form.description" :error="formError.description" dense number label="Description" />
    <!-- <div class="full-width row reverse"> -->
    <q-btn dense color="primary" size="md" label="apply" type="submit" />
    <!-- </div> -->
  </q-form>
</template>
<script setup lang="ts">
import DateTimePicker from '@/components/small/DateTimePicker.vue'
import InputLeftLabel from '@/components/small/InputLeftLabel.vue'
import { useModalForm } from '@/composables/useModalForm'
import { post } from '@/utils/api_common'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import timezone from 'dayjs/plugin/timezone'
import utc from 'dayjs/plugin/utc'
dayjs.extend(customParseFormat)
dayjs.extend(timezone)
dayjs.extend(utc)

const levelOptions = [
  { label: 'TRACE', value: 0 },
  { label: 'DEBUG', value: 1 },
  { label: 'INFO', value: 2 },
  { label: 'WARN', value: 3 },
  { label: 'ERROR', value: 4 },
  { label: 'FATAL', value: 5 },
]

const { form, formError, submit } = useModalForm({
  formInit: { datetime: dayjs().toString(), level: 0, description: '' },
  submit: {
    url: '/api/modbus/log',
    handleRequest: (v) => {
      const timezone = dayjs.tz.guess()

      const validDate = dayjs(v.datetime, 'YYYY-MM-DDTHH:mm:ss.SSSZ').tz(timezone).isValid()

      return { ...v, datetime: validDate ? validDate : dayjs(v.datetime).tz(timezone).format('YYYY-MM-DDTHH:mm:ss.SSSZ') }
    },
  },
})

const start = dayjs.tz(dayjs()).tz(dayjs.tz.guess()).toString()

const updateTime = (v: { dateTime: string }) => {
  form.value.datetime = v.dateTime
}

const onClkSubmit = async () => await submit(post)
</script>