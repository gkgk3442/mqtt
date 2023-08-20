<template>
  <div class="row q-pb-sm items-center">
    <div class="col-3">
      <span class="text-weight-bold" :class="{ 'text-grey-5': $q.dark.isActive }">Date</span>
    </div>
    <q-input
      class="col-grow"
      outlined
      square
      dense
      debounce="500"
      :model-value="timeDateInput.date"
      mask="####-##-##"
      label="Start Date"
      @update:model-value="(v: any) => onChange('date', v)"
    >
      <template #append>
        <q-icon name="event" class="cursor-pointer" />
        <q-popup-proxy ref="qDateRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
          <q-date minimal no-unset :model-value="timeDateInput.date" mask="YYYY-MM-DD" @update:model-value="(v: any) => onChange('date', v)"
            ><div class="row items-center justify-end"><q-btn v-close-popup label="Close" color="primary" flat /></div
          ></q-date>
        </q-popup-proxy>
      </template>
    </q-input>
  </div>
  <div class="row q-pb-sm items-center">
    <div class="col-3">
      <span class="text-weight-bold" :class="{ 'text-grey-5': $q.dark.isActive }">Time</span>
    </div>
    <q-input
      class="col-grow"
      square
      outlined
      dense
      debounce="500"
      :model-value="timeDateInput.time"
      mask="##:##:##"
      label="Start Time"
      @update:model-value="(v: any) => onChange('time', v)"
    >
      <template #append>
        <q-icon name="schedule" class="cursor-pointer" />
        <q-popup-proxy ref="qTimeRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
          <q-time :model-value="timeDateInput.time" with-seconds mask="HH:mm:ss" format24h @update:model-value="(v: any) => onChange('time', v)">
            <div class="row items-center justify-end"><q-btn v-close-popup label="Close" color="primary" flat /></div>
          </q-time>
        </q-popup-proxy>
      </template>
    </q-input>
  </div>
</template>

<script setup lang="ts">
import { objectDeepCopy } from '@/utils/utils_global'
import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import timezone from 'dayjs/plugin/timezone'
import utc from 'dayjs/plugin/utc'
import { QPopupProxy } from 'quasar'
import { ref } from 'vue'

export interface ITimeDateData {
  dateTime: string
}

dayjs.extend(customParseFormat)
dayjs.extend(utc)
dayjs.extend(timezone)

const $props = defineProps<{
  init?: string
}>()

const $emit = defineEmits<{
  (e: 'update', v: ITimeDateData): void
}>()

const qDateRef = ref<InstanceType<typeof QPopupProxy> | null>(null)
const qTimeRef = ref<InstanceType<typeof QPopupProxy> | null>(null)

const currentTime = () => dayjs.tz(dayjs()).tz(dayjs.tz.guess())

const timeDateInput = ref<{ date: string; time: string }>({
  time: dayjs($props.init).format('HH:mm:ss'),
  date: dayjs($props.init).format('YYYY-MM-DD'),
})

const timeDateReqFormat = ref({
  dateTime: $props.init,
})

const onChange = (key: 'date' | 'time', val: string) => {
  handleClose(key, val)
  const data = objectDeepCopy(timeDateInput.value)
  Object.assign(data, { [key]: val })

  const timezone = currentTime().format('Z')

  timeDateInput.value = data
  timeDateReqFormat.value.dateTime = `${timeDateInput.value.date}T${timeDateInput.value.time}${timezone}`

  $emit(
    'update',
    timeDateReqFormat.value as {
      dateTime: string
    }
  )
}

const handleClose = (key: string, value: string) => {
  if (key.includes('Date')) {
    qDateRef.value?.hide()
  } else {
    if (key === 'time') {
      if (value.slice(-2) !== prevTimeVal.value[key].slice(-2)) {
        qTimeRef.value?.hide()
        prevTimeVal.value[key] = value
      }
    }
  }
}

const prevTimeVal = ref({
  time: timeDateInput.value.time,
})
</script>
