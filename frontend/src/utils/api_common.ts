import type { AxiosProgressEvent } from 'axios'
import axios, { AxiosError } from 'axios'
import { Dialog, Notify } from 'quasar'
import queryString from 'query-string'

export const post = async (v: any, url?: string) => {
  try {
    if (url === undefined) throw 'PROVIDE URL'

    if (v === null) await axios.post(url)
    else await axios.post(url, v)

    Notify.create({
      type: 'positive',
      position: 'top-right',
      group: false,
      timeout: 1000,
      message: 'success!',
    })
  } catch (e) {
    throw e
  }
}

export const postWithProgress = async (v: any, url?: string) => {
  if (url === undefined) throw 'PROVIDE URL'

  const dialog = Dialog.create({
    message: 'Uploading... 0%',
    progress: true,
    persistent: true,
    ok: false,
  })

  const config = {
    headers: {
      'Content-Type': 'application/json',
    },
    onUploadProgress: (progressEvent: AxiosProgressEvent) => {
      if (progressEvent.total !== undefined) {
        const progress = Math.round((progressEvent.loaded / progressEvent?.total) * 100)
        dialog.update({
          message: `Uploading... ${progress}%`,
        })
        if (progress === 100) dialog.hide()
      } else {
        dialog.hide()
      }
    },
  }

  try {
    const res = await axios.post(url, v, config)

    Notify.create({
      type: 'positive',
      position: 'top-right',
      group: false,
      timeout: 1000,
      message: 'success!',
    })
    return res.data
  } catch (e) {
    if (e instanceof AxiosError) {
      Notify.create({
        type: 'negative',
        position: 'top-right',
        group: false,
        timeout: 1000,
        message: e.message,
      })
    }
    throw e
  }
}
