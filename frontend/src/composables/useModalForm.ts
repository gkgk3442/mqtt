import { objectDeepCopy, objectDiffKeys, setObjectValueClear } from '@/utils/utils_global'
import { useFormValid } from '@/utils/validation'
import axios from 'axios'
import { ref, watch } from 'vue'

type IForm = Record<string, any>

interface IModalFormOpt<T extends IForm> {
  formInit: T
  formSetModal?: (() => T) | (() => Promise<T>)
  submit?: {
    url: string | ((v: T) => string)
    handleRequest?: (v: T, args?: any) => Record<string, any>
    args?: any
  }
}

export const useModalForm = <T extends IForm>(opt: IModalFormOpt<T>) => {
  const showModal = ref(false)
  const form = ref<T>(objectDeepCopy(opt.formInit))
  const formError = ref<Record<string, any>>(setObjEmptyStrings<T>(opt.formInit))

  const clickBtn = async () => {
    form.value = opt.formSetModal !== undefined ? await opt.formSetModal() : objectDeepCopy(opt.formInit)
    formError.value = await setObjEmptyStrings<T>(opt.formInit)
    showModal.value = true
  }

  const submit = async (proc: (v: any, url?: string) => void | Promise<void>, refreshCb?: () => void | Promise<void>) => {
    const { submit } = opt
    try {
      if (submit) {
        const { handleRequest, args, url: submitUrl } = submit
        const url = typeof submitUrl === 'string' ? submitUrl : submitUrl(form.value as T)
        const data = await (handleRequest ? (args ? handleRequest(form.value as T, args) : handleRequest(form.value as T)) : form.value)
        await proc(data, url)
      } else {
        await proc(form.value)
      }
      if (refreshCb !== undefined) {
        await refreshCb()
      }
      showModal.value = false
    } catch (e) {
      if (axios.isAxiosError(e)) {
        useFormValid(e, formError)
      } else console.error(e)
    }
  }

  watch(
    () => JSON.stringify(form.value),
    (newVal, oldVal) => {
      const diffKeyArr = objectDiffKeys(JSON.parse(oldVal), JSON.parse(newVal))

      diffKeyArr.forEach((v) => {
        if (formError.value[v] !== undefined) setObjectValueClear(formError.value)
      })
    }
  )

  return {
    showModal,
    form,
    formError,
    submit,
    clickBtn,
  }
}

const setObjEmptyStrings = <T extends IForm>(obj: T) => {
  const tmp: Record<keyof T, string> = <Record<keyof T, string>>{}

  Object.entries(obj).forEach(([key, val]) => {
    if (typeof val === 'object' && val === null && val === undefined) Object.assign(tmp, { [key]: setObjEmptyStrings(val) })
    else if (typeof val === 'object' && val !== null && val !== undefined) Object.assign(tmp, { [key]: setObjEmptyStrings(val) })
    else Object.assign(tmp, { [key]: '' })
  })

  return tmp
}
