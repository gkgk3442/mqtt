<template>
  <table>
    <tr>
      <th>Publish</th>
      <th>Subscribe</th>
    </tr>
    <tr>
      <td>
        <EthernetConfig />
      </td>
      <td>
        <EthernetTable />
      </td>
    </tr>
    <tr>
      <td>
        <LogConfig />
      </td>
      <td>
        <LogTable />
      </td>
    </tr>
  </table>
</template>
<script setup lang="ts">
import EthernetConfig from '@/components/EthernetConfig.vue'
import LogConfig from '@/components/LogConfig.vue'
import EthernetTable from '@/components/EthernetTable.vue'
import LogTable from '@/components/LogTable.vue'
</script>
<style lang="css" scoped>
th {
  height: 20px;
}
table,
th,
td {
  border: 1px solid black;
}
table {
  height: calc(100vh - 16px);
  width: calc(100vh - 16px);
  margin: 8px;
}
</style>
