<template>
  <div class="row q-pb-sm">
    <div class="col-3">
      <span class="text-weight-bold" :class="{ 'text-grey-5': $q.dark.isActive }">Date</span>
    </div>
    <q-input
      class="col-grow"
      outlined
      square
      dense
      debounce="500"
      :model-value="timeDateInput.startDate"
      mask="####-##-##"
      label="Start Date"
      @update:model-value="(v: any) => onChange('startDate', v)"
    >
      <template #append>
        <q-icon name="event" class="cursor-pointer" />
        <q-popup-proxy ref="qDateRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
          <q-date minimal no-unset :model-value="timeDateInput.startDate" mask="YYYY-MM-DD" @update:model-value="(v: any) => onChange('startDate', v)"
            ><div class="row items-center justify-end"><q-btn v-close-popup label="Close" color="primary" flat /></div
          ></q-date>
        </q-popup-proxy>
      </template>
    </q-input>
  </div>
  <div class="row q-pb-sm">
    <div class="col-3">
      <span class="text-weight-bold" :class="{ 'text-grey-5': $q.dark.isActive }">Time</span>
    </div>
    <q-input
      class="col-grow"
      square
      outlined
      dense
      debounce="500"
      :model-value="timeDateInput.startTime"
      mask="##:##:##"
      label="Start Time"
      @update:model-value="(v: any) => onChange('startTime', v)"
    >
      <template #append>
        <q-icon name="schedule" class="cursor-pointer" />
        <q-popup-proxy ref="qTimeRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
          <q-time :model-value="timeDateInput.startTime" with-seconds mask="HH:mm:ss" format24h @update:model-value="(v: any) => onChange('startTime', v)">
            <div class="row items-center justify-end"><q-btn v-close-popup label="Close" color="primary" flat /></div>
          </q-time>
        </q-popup-proxy>
      </template>
    </q-input>
  </div>
  <!-- <q-input
    class="col-xs-6 col-md-3 col-lg-2 col-xl-1"
    square
    outlined
    dense
    debounce="500"
    :model-value="timeDateInput.endDate"
    mask="####-##-##"
    label="End date"
    @update:model-value="(v: any) => onChange('endDate', v)"
  >
    <template #append>
      <q-icon name="event" class="cursor-pointer" />
      <q-popup-proxy ref="qDateRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
        <q-date minimal no-unset :model-value="timeDateInput.endDate" mask="YYYY-MM-DD" @update:model-value="(v: any) => onChange('endDate', v)">
          <div class="row items-center justify-end"><q-btn v-close-popup label="Close" color="primary" flat /></div>
        </q-date>
      </q-popup-proxy>
    </template>
  </q-input>
  <q-input
    class="col-xs-6 col-md-3 col-lg-2 col-xl-1"
    square
    outlined
    dense
    debounce="500"
    :model-value="timeDateInput.endTime"
    mask="##:##:##"
    label="end time"
    @update:model-value="(v: any) => onChange('endTime', v)"
  >
    <template #append>
      <q-icon name="schedule" class="cursor-pointer" />
      <q-popup-proxy ref="qTimeRef" self="top middle" anchor="bottom end" breakpoint="750" transition-show="scale" transition-hide="scale">
        <q-time :model-value="timeDateInput.endTime" with-seconds mask="HH:mm:ss" format24h @update:model-value="(v: any) => onChange('endTime', v)">
          <div class="row items-center justify-end">
            <q-btn v-close-popup label="Close" color="primary" flat />
          </div>
        </q-time>
      </q-popup-proxy>
    </template>
  </q-input> -->
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
  startDateTime: string
  endDateTime: string
}

dayjs.extend(customParseFormat)
dayjs.extend(utc)
dayjs.extend(timezone)

const $props = defineProps<{
  start?: string
  end?: string
}>()

const $emit = defineEmits<{
  (e: 'update', v: ITimeDateData): void
}>()

const qDateRef = ref<InstanceType<typeof QPopupProxy> | null>(null)
const qTimeRef = ref<InstanceType<typeof QPopupProxy> | null>(null)

const currentTime = () => dayjs.tz(dayjs()).tz(dayjs.tz.guess())

const timeDateInput = ref<{ startDate: string; startTime: string; endDate: string; endTime: string }>({
  startTime: dayjs($props.start).format('HH:mm:ss'),
  startDate: dayjs($props.start).format('YYYY-MM-DD'),
  endTime: dayjs($props.end).format('HH:mm:ss'),
  endDate: dayjs($props.end).format('YYYY-MM-DD'),
})

const timeDateReqFormat = ref({
  startDateTime: $props.start,
  endDateTime: $props.end,
})

// const prevValidTimeDate = computed(() => {
//   return {
//     startTime: dayjs(timeDateReqFormat.value.startDateTime).format('HH:mm:ss'),
//     startDate: dayjs(timeDateReqFormat.value.startDateTime).format('YYYY-MM-DD'),
//     endTime: dayjs(timeDateReqFormat.value.endDateTime).format('HH:mm:ss'),
//     endDate: dayjs(timeDateReqFormat.value.endDateTime).format('YYYY-MM-DD'),
//   }
// })

const onChange = (key: 'startDate' | 'startTime' | 'endDate' | 'endTime', val: string) => {
  handleClose(key, val)
  const data = objectDeepCopy(timeDateInput.value)
  Object.assign(data, { [key]: val })

  const timezone = currentTime().format('Z')

  // const newStartTime = `${data.startDate}T${data.startTime}${timezone}`
  // const newEndTime = `${data.endDate}T${data.endTime}${timezone}`

  // if (dayjs(newStartTime).isAfter(dayjs(newEndTime))) {
  //   Dialog.create({
  //     message: 'Invalid range',
  //     persistent: true,
  //   })
  //   timeDateInput.value = prevValidTimeDate.value
  // } else if (!(dayjs(newStartTime).isValid() && dayjs(newEndTime).isValid())) {
  //   timeDateInput.value[key] = prevValidTimeDate.value[key]
  // } else {
  timeDateInput.value = data
  timeDateReqFormat.value.startDateTime = `${timeDateInput.value.startDate}T${timeDateInput.value.startTime}${timezone}`
  timeDateReqFormat.value.endDateTime = `${timeDateInput.value.endDate}T${timeDateInput.value.endTime}${timezone}`

  $emit(
    'update',
    timeDateReqFormat.value as {
      startDateTime: string
      endDateTime: string
    }
  )
  // }
}

const handleClose = (key: string, value: string) => {
  if (key.includes('Date')) {
    qDateRef.value?.hide()
  } else {
    if (key === 'startTime' || key === 'endTime') {
      if (value.slice(-2) !== prevTimeVal.value[key].slice(-2)) {
        qTimeRef.value?.hide()
        prevTimeVal.value[key] = value
      }
    }
  }
}

const prevTimeVal = ref({
  startTime: timeDateInput.value.startTime,
  endTime: timeDateInput.value.endTime,
})
</script>
