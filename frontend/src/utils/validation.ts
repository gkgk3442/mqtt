import type { AxiosError } from 'axios'
import type { Ref } from 'vue'

export interface IRestErrorDetail {
  exception?: string
  objectName?: string

  field?: string
  code?: string
  arguments?: any[]
  message?: string

  rejectValue?: object
}

export interface IRestError {
  exception?: Array<IRestErrorDetail>
  field?: Array<IRestErrorDetail>
  global?: Array<IRestErrorDetail>
}

const useFormValid = (e: AxiosError, formError: Ref<Record<string, any>>) => {
  if (e.response?.status === 400) {
    const data = e.response.data as IRestError

    if (Array.isArray(data.field) && data.field.length > 0) {
      data.field.forEach((v: IRestErrorDetail) => {
        if (v.field !== undefined) {
          let message = 'undefined'

          if (v.code !== undefined && v.arguments !== undefined) {
            message = `error.${v.code} : ${v.arguments.reverse()}`
          } else if (v.code !== undefined) {
            message = `error.${v.code}`
          } else if (v.message !== undefined) {
            message = v.message
          }

          if (import.meta.env.DEV) console.error(`${v.field} : ${message}`)

          if (v.field.includes('.')) {
            const tmp = v.field.split('.')
            formError.value[tmp[0]][tmp[1]] = message
          }
          if (formError.value[v.field] !== undefined) formError.value[v.field] = message
        }
      })
    }
  }
}

export { useFormValid }
