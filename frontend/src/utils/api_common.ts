import type { AxiosProgressEvent } from 'axios'
import axios from 'axios'
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
    await axios.post(url, v, config)

    Notify.create({
      type: 'positive',
      position: 'top-right',
      group: false,
      timeout: 1000,
      message: 'success!',
    })
  } catch (e) {
    Notify.create({
      type: 'negative',
      position: 'top-right',
      group: false,
      timeout: 1000,
      message: 'invalid!',
    })
    throw e
  }
}

export const getWithProgress = async (v: Record<string, any>, url: string) => {
  const dialog = Dialog.create({
    message: 'Downloading... 0%',
    progress: true,
    persistent: true,
    ok: false,
  })

  const config = {
    onUploadProgress: (progressEvent: AxiosProgressEvent) => {
      if (progressEvent.total !== undefined) {
        const progress = Math.round((progressEvent.loaded / progressEvent?.total) * 100)
        dialog.update({
          message: `Downloading... ${progress}%`,
        })
        if (progress === 100) dialog.hide()
      } else {
        dialog.hide()
      }
    },
  }
  try {
    await axios.get(`${url}/download?${queryString.stringify({ ...v })}`, config)
  } catch (e) {
    // dialog.hide()
  }
}

export const put = async (v: Record<string, any>, url?: string) => {
  try {
    if (url === undefined) throw 'PROVIDE URL'

    await axios.put(url, v)

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

export const patch = async (v: Record<string, any>, url?: string) => {
  try {
    if (url === undefined) throw 'PROVIDE URL'

    await axios.patch(url, v)

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
