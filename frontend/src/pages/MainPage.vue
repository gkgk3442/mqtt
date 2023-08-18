<template>
  <div class="row q-pa-sm q-gutter-sm">
    <div class="row q-gutter-sm col-lg-6 col-12 q-pr-md">
      <q-card class="col-12 text-center text-h6 bg-primary text-white">Publish</q-card>
      <q-card style="height: 320px" class="col-12 q-pa-lg"><EthernetConfig /></q-card>
      <q-card style="height: 320px" class="col-12 q-pa-lg"><LogConfig /></q-card>
    </div>
    <div class="row q-gutter-sm col-md-grow col-12 q-pr-md">
      <q-card class="col-12 text-center text-h6 bg-primary text-white">Subscribe</q-card>
      <q-card style="height: 320px" class="col-12"><EthernetTable /></q-card>
      <q-card style="height: 320px" class="col-12"><LogTable /></q-card>
    </div>
  </div>
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
