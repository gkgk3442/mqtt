<template>
  <q-form @submit.prevent="onClkSubmit">
    <q-input class="col-grow q-pb-md" outlined dense debounce="500" v-model="form.date" mask="####-##-##" label="Start Date">
      <template #append>
        <q-icon name="event" class="cursor-pointer" />
        <q-popup-proxy ref="qDateRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
          <q-date minimal no-unset v-model="form.date" mask="YYYY-MM-DD">
            <div class="row items-center justify-end"><q-btn v-close-popup label="Close" color="primary" flat /></div>
          </q-date>
        </q-popup-proxy>
      </template>
    </q-input>
    <q-input class="col-grow q-pb-md" outlined dense debounce="500" v-model="form.time" mask="##:##:##" label="Start Time">
      <template #append>
        <q-icon name="schedule" class="cursor-pointer" />
        <q-popup-proxy ref="qTimeRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
          <q-time v-model="form.time" with-seconds mask="HH:mm:ss" format24h>
            <div class="row items-center justify-end"><q-btn v-close-popup label="Close" color="primary" flat /></div>
          </q-time>
        </q-popup-proxy>
      </template>
    </q-input>
    <q-select
      map-options
      emit-value
      outlined
      v-model="form.level"
      :error="formError.level.length > 0"
      dense
      label="Level"
      :error-message="formError.level"
      :options="levelOptions"
    />
    <q-input outlined v-model="form.description" :error="formError.description.length > 0" :error-message="formError.description" dense number label="Description" />
    <q-btn dense color="primary" size="md" label="apply" type="submit" />
  </q-form>
</template>
<script setup lang="ts">
import { post } from '@/utils/api_common'
import { objectDiffKeys, setObjectValueClear } from '@/utils/utils_global'
import { useFormValid } from '@/utils/validation'
import axios from 'axios'
import dayjs from 'dayjs'
import { ref, watch } from 'vue'

const levelOptions = [
  { label: 'TRACE', value: 0 },
  { label: 'DEBUG', value: 1 },
  { label: 'INFO', value: 2 },
  { label: 'WARN', value: 3 },
  { label: 'ERROR', value: 4 },
  { label: 'FATAL', value: 5 },
]

const form = ref({ date: dayjs().format('YYYY-MM-DD').toString(), time: dayjs().format('HH:mm:ss').toString(), level: 0, description: '' })
const formError = ref({ date: '', time: '', level: '', description: '' })

const onClkSubmit = async () => {
  try {
    await post(form.value, '/api/modbus/log')
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
