<template>
  <div class="full-width">
    <RequestResponseForm />
  </div>
  <div class="row q-pa-md q-col-gutter-sm">
    <div class="row col-md-6 col-12">
      <q-card class="col-12 text-center text-h6 bg-primary text-white">Publish</q-card>
      <q-card class="card-min-height col-12 q-pa-lg q-mb-sm"><EthernetConfig /></q-card>
      <q-card class="card-min-height col-12 q-pa-lg"><LogConfig /></q-card>
    </div>
    <div class="row col-md-6 col-12">
      <q-card class="col-12 text-center text-h6 bg-primary text-white">Subscribe</q-card>
      <q-card class="card-min-height col-12 q-mb-sm"><EthernetTable /></q-card>
      <q-card class="card-min-height col-12"><LogTable /></q-card>
    </div>
  </div>
</template>
<script setup lang="ts">
import EthernetConfig from '@/components/EthernetConfig.vue'
import LogConfig from '@/components/LogConfig.vue'
import EthernetTable from '@/components/EthernetTable.vue'
import LogTable from '@/components/LogTable.vue'
import RequestResponseForm from '@/components/RequestResponseForm.vue'
</script>
<style lang="css" scoped>
.card-min-height {
  min-height: 320px;
}
</style>
