<template>
  <div class="input-left-label row items-center q-pb-sm">
    <div :class="toggle ? 'col-shrink' : 'col-3'">
      <span class="text-weight-bold" :class="{ 'text-grey-5': $q.dark.isActive }">{{ label }}</span>
      <span v-if="required" class="text-weight-bold text-negative q-pr-xs">*</span>
    </div>

    <q-input
      class="col-grow"
      ref="inputRef"
      label-slot
      v-if="input === 'input' && options === undefined && !toggle && !useSlot && accept === undefined && type !== 'file' && !span && !error"
      :model-value="modelValue"
      @change="(v: any) => $emit('update:modelValue', v)"
      :type="type"
      dense
      :filled="$q.dark.isActive"
      :outlined="!$q.dark.isActive"
      :disable="disable"
      :min="min"
      :max="max"
      square
    >
      <template #prepend> <q-icon v-if="icon !== undefined" :name="icon" /> </template>
      <!-- <template #label></template>    -->
    </q-input>
    <!-- <div v-if="span" style="height: 40px; display: flex; align-items: center" class="col-5"> -->
    <!-- <div style="height: 40px; display: flex; align-items: center; overflow-x:auto" class="col-5"> -->
    <!-- <span v-if="span">{{ modelValue }}</span> -->
    <!-- </div> -->
    <q-input
      class="col-grow input"
      ref="inputRef"
      label-slot
      v-if="input === 'input' && options === undefined && !toggle && !useSlot && accept === undefined && type !== 'file' && !span && error"
      :model-value="modelValue"
      @change="(v: any) => $emit('update:modelValue', v)"
      :type="type"
      dense
      :filled="$q.dark.isActive"
      :outlined="!$q.dark.isActive"
      :disable="disable"
      :min="min"
      :max="max"
      :error="error.length > 0"
      :error-message="error"
      square
    >
      <template #prepend> <q-icon v-if="icon !== undefined" :name="icon" /> </template>
      <!-- <template #label></template>    -->
    </q-input>
    <div v-if="toggle" full-width>
      <q-toggle :model-value="modelValue" size="lg" :color="!$q.dark.isActive ? 'primary' : 'blue-2'" @update:model-value="(v: any) => $emit('update:modelValue', v)" />
    </div>
    <slot v-if="useSlot"></slot>
    <slot v-if="useSlot"></slot>
    <div v-if="span" style="height: 30px; display: flex; align-items: center" class="col-5">
      <!-- <div style="height: 40px; display: flex; align-items: center; overflow-x:auto" class="col-5"> -->
      <span v-if="span">{{ modelValue }}</span>
    </div>

    <q-select
      class="col-grow"
      :label="label"
      v-if="options"
      :filled="$q.dark.isActive"
      :outlined="!$q.dark.isActive"
      :model-value="modelValue"
      @update:model-value="(v: string | number) => $emit('update:modelValue', v)"
      :options="options"
      :disable="disable"
      :error="error !== undefined && error.length > 0"
      :error-message="error"
      dense
      square
      options-dense
      emit-value
      map-options
      hide-bottom-space
    >
      <template #prepend> <q-icon v-if="icon !== undefined" :name="icon" /> </template>
      <template #label></template>
    </q-select>

    <q-file
      v-if="accept !== undefined || type === 'file'"
      :filled="$q.dark.isActive"
      :outlined="!$q.dark.isActive"
      dense
      square
      :accept="accept"
      :model-value="modelValue"
      :error="error !== undefined && error.length > 0"
      :error-message="error"
      :label="label"
      :hint="hint"
      hide-bottom-space
      :disable="disable"
      @update:model-value="
        (v: boolean) => {
          $emit('update:modelValue', v)
        }
      "
    >
      <template #prepend>
        <q-icon name="folder" />
      </template>
      <template #label>
        <span v-if="required !== undefined" class="text-weight-bold text-negative q-pr-xs">*</span>
        <span>{{ label }}</span>
      </template>
    </q-file>
    <slot name="after"></slot>
  </div>
</template>
<script setup lang="ts">
import type { QInput } from 'quasar'
import { onMounted, onUnmounted, ref } from 'vue'

const $props = withDefaults(
  defineProps<{
    label: string
    required?: boolean
    input?: 'input'
    type?: 'number' | 'textarea' | 'time' | 'text' | 'password' | 'email' | 'search' | 'tel' | 'file' | 'url' | 'date'
    modelValue?: any
    error?: string
    options?: { label: string; value: any }[] | string[] | number[]
    disable?: boolean
    toggle?: boolean
    useSlot?: boolean
    icon?: string
    maxlength?: number
    min?: number
    max?: number
    accept?: string
    inputStyle?: string | string[] | object
    inputClass?: string | string[] | object
    hint?: string
    span?: boolean
    qInputClass?: string
  }>(),
  {
    min: 0,
    max: 2147483647,
    maxlength: 2147483647,
    type: 'text',
    input: 'input',
    disable: false,
    inputClass: 'default-class',
    // error: 'BRUH'
  }
)
const $emit = defineEmits<{
  (e: 'update:modelValue', v: any): void
}>()

const getStrLength = (str: string) => [...str].length
const getStrSlice = (str: string, maxLength: number) => [...str].slice(0, maxLength).join('')

const inputRef = ref<InstanceType<typeof QInput> | null>(null)

const inputEvent = (el: HTMLInputElement) => {
  switch (el.type) {
    case 'number':
      if (!Number.isNaN(parseInt(el.value))) {
        if (Number(el.value) > $props.max) el.value = $props.max.toString()
        else if (Number(el.value) < $props.min) el.value = $props.min.toString()
      }
      break
    default:
      if (getStrLength(el.value) > $props.maxlength) el.value = getStrSlice(el.value, $props.maxlength)
  }
}

const eventMounted = (isMounted: boolean) => {
  if (inputRef.value !== null) {
    const el = inputRef.value.getNativeElement()

    if (isMounted) el.addEventListener('input', () => inputEvent(el as HTMLInputElement))
    else el.removeEventListener('input', () => inputEvent(el as HTMLInputElement))
  }
}

onMounted(() => eventMounted(true))
onUnmounted(() => eventMounted(false))
</script>

<style lang="scss" scoped>
.input-left-label :deep(.q-field--labeled.q-field--dense .q-field__native) {
  padding-top: 0;
  padding-bottom: 0;
  height: 30px;
}

.input-left-label :deep(.q-field--auto-height.q-field--dense.q-field--labeled .q-field__control-container) {
  padding-top: 0;
  padding-bottom: 0;
  height: 30px;
}

// .input-left-label > .q-field {
//   height: 30px;
//   max-width: 200px;
// }

// .input-left-label > :deep(.q-field--dense .q-field__control) {
//   height: 30px;
//   max-width: 200px;
// }
</style>
