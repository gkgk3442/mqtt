<template>
  <table class="table">
    <tr>
      <th>protocol</th>
      <th>ip</th>
      <th>port</th>
      <th>transaction delay</th>
      <th>timeout</th>
    </tr>
    <tr v-for="row in rows" :key="`${row.protocol}_${row.ip}_${row.port}`">
      <td>{{ row.protocol }}</td>
      <td>{{ row.ip }}</td>
      <td>{{ row.port }}</td>
      <td>{{ row.transactionDelay }}</td>
      <td>{{ row.timeout }}</td>
    </tr>
  </table>
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

const rows = ref<IData[]>([])

const { data } = useSse('/api/modbus/ethernet')

watch(data, () => {
  if (data.value) rows.value.push(JSON.parse(data.value) as IData)
})
</script>

<style lang="scss" scoped>
th,
td {
  border: 1px solid black;
  border-collapse: collapse;
}
table {
  margin: 0px;
  width: 100%;
  table-layout: fixed;
}
</style>
