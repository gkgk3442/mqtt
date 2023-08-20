<template>
  <q-table dense :columns="columns" :rows="rows" table-header-class="bg-secondary"> </q-table>
</template>
<script setup lang="ts">
import { useSse } from '@/composables/useSse'
import { ref, watch } from 'vue'

interface IData {
  protocol: number
  ip: string
  port: number
  transactionDelay: number
  timeout: number
}
const columns = ref([
  {
    name: 'protocol',
    field: 'protocol',
    label: 'Protocol',
    format: (v: number) => {
      if (v === 0) {
        return 'TCP'
      } else if (v === 1) {
        return 'RTU'
      } else if (v === 2) {
        return 'ASCII'
      } else {
        return '-'
      }
    },
  },
  { name: 'ip', field: 'ip', label: 'IP' },
  { name: 'port', field: 'port', label: 'Port' },
  { name: 'transactionDelay', field: 'transactionDelay', label: 'Transaction Delay' },
  { name: 'timeout', field: 'timeout', label: 'Timeout' },
])

const rows = ref<IData[]>([])

const { data } = useSse('/api/modbus/ethernet')

watch(data, () => {
  if (data.value) {
    const json = JSON.parse(data.value)
    rows.value = [json, ...rows.value]
  }
})
</script>

<style lang="scss" scoped></style>
