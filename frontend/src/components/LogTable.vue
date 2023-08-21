<template>
  <q-table dense :columns="columns" :rows="rows" table-header-class="bg-secondary"> </q-table>
</template>
<script setup lang="ts">
import { useSse } from '@/composables/useSse'
import { ref, watch } from 'vue'

interface IData {
  datetime: string
  level: number
  description: string
}

const rows = ref<IData[]>([])

const { data } = useSse('/api/modbus/log')
const columns = ref([
  { name: 'datetime', field: 'datetime', label: 'Date/Time' },
  {
    name: 'level',
    field: 'level',
    label: 'Level',
    format: (v: number) => {
      if (v === 0) {
        return 'TRACE'
      } else if (v === 1) {
        return 'DEBUG'
      } else if (v === 2) {
        return 'INFO'
      } else if (v === 3) {
        return 'WARN'
      } else if (v === 4) {
        return 'ERROR'
      } else if (v === 5) {
        return 'FATAL'
      } else {
        return '-'
      }
    },
  },
  { name: 'description', field: 'description', label: 'Description' },
])
watch(data, () => {
  if (data.value) {
    const json = JSON.parse(data.value)
    rows.value = [json, ...rows.value]
  }
})
</script>
<style lang="scss" scoped></style>
