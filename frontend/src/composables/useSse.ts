import { useEventSource } from '@vueuse/core'
import { onUnmounted, watch } from 'vue'

export const useSse = (url: string) => {
  const { data, status, close } = useEventSource(url)
  watch(status, () => {
    if (status.value === 'CLOSED') close()
  })
  onUnmounted(() => close())
  return {
    data,
  }
}
