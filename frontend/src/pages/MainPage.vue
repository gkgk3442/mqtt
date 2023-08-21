<template>
  <RequestResponseForm />

  <div class="row q-pa-md q-col-gutter-sm">
    <div class="col-12 col-md-6">
      <q-card class="q-pa-none">
        <q-card-section class="bg-primary text-white q-pa-none">
          <div class="text-subtitle1 text-center">Publish</div>
        </q-card-section>

        <q-separator />

        <q-card-actions>
          <EthernetConfig class="col-12" />
        </q-card-actions>
      </q-card>
    </div>
    <div class="col-12 col-md-6">
      <q-card class="q-pa-none">
        <q-card-section class="bg-primary text-white q-pa-none">
          <div class="text-subtitle1 text-center">Subscribe</div>
        </q-card-section>

        <q-separator />

        <q-card-actions class="q-pa-none">
          <EthernetTable class="col-12" />
        </q-card-actions>
      </q-card>
    </div>
  </div>

  <div class="row q-pa-md q-col-gutter-sm">
    <div class="col-12 col-md-6">
      <q-card class="q-pa-none">
        <q-card-section class="bg-primary text-white q-pa-none">
          <div class="text-subtitle1 text-center">Publish</div>
        </q-card-section>

        <q-separator />

        <q-card-actions>
          <LogConfig class="col-12" />
        </q-card-actions>
      </q-card>
    </div>
    <div class="col-12 col-md-6">
      <q-card class="q-pa-none">
        <q-card-section class="bg-primary text-white q-pa-none">
          <div class="text-subtitle1 text-center">Subscribe</div>
        </q-card-section>

        <q-separator />

        <q-card-actions class="q-pa-none">
          <LogTable class="col-12" />
        </q-card-actions>
      </q-card>
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
