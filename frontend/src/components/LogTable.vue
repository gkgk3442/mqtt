<template>
  <table class="table">
    <tr>
      <th>datetime</th>
      <th>level</th>
      <th>description</th>
    </tr>
    <tr v-for="row in rows" :key="`${row.datetime}_${row.level}_${row.description}`">
      <td>{{ row.datetime }}</td>
      <td>{{ row.level }}</td>
      <td>{{ row.description }}</td>
    </tr>
  </table>
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
.table {
  width: 100%;
  table-layout: fixed;
  margin: 0px;
}
</style>
