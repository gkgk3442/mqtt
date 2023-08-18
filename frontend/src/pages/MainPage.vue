<template>
  <table>
    <tr>
      <th>Publish</th>
      <th>Subscribe</th>
    </tr>
    <tr>
      <td class="config">
        <EthernetConfig />
      </td>
      <td class="table top-align" colspan="1">
        <EthernetTable />
      </td>
    </tr>
    <tr>
      <td class="config">
        <LogConfig />
      </td>
      <td class="table top-align">
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
  margin: 8px;
}

.config {
  width: fit-content;
  padding: 20px;
}
.table {
  width: 500px;
}

.top-align {
  vertical-align: top;
}
</style>
