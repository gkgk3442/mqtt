export const objectDiffKeys = (oldObj: Record<string, any>, newObj: Record<string, any>) => {
  return Object.entries(newObj)
    .filter(([key, value]) => value !== oldObj[key])
    .map((entry) => entry[0])
}

export const objectDeepCopy = (obj: any) => JSON.parse(JSON.stringify(obj))

export const setObjectValueClear = (v: Record<string, any>) => {
  for (const [key, value] of Object.entries(v)) {
    if (typeof value === 'object') setObjectValueClear(value)
    else if (typeof value === 'string' && value !== '') v[key] = ''
    else if (typeof value === 'number' && value !== 0) v[key] = 0
  }
}

// {test.val:'1234'} => {test : {val : '1234'}}
export const dotStringToObject = (obj: Record<string, any>) => {
  const result: Record<string, any> = {}

  for (const objectPath in obj) {
    const parts = objectPath.split('.')

    let target = result
    while (parts.length > 1) {
      const part = parts.shift()
      if (part !== undefined) target = target[part] = target[part] || {}
    }

    target[parts[0]] = obj[objectPath]
  }

  return result
}

// 범위 지정 랜덤 숫자 반환
export const genRandomNumber = (min: number, max: number) => Math.floor(Math.random() * (max - min + 1)) + min

// 랜덤 길이 문자열 반환
export const genRandomString = (length: number, chars?: string) =>
  Array(length)
    .fill(0)
    .reduce((prev) => (prev += genRandomChar(chars)), '')

// 랜덤 문자 반환
export const genRandomChar = (chars?: string) => {
  const characters = chars ?? '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'
  const charactersLength = characters.length
  return characters.charAt(Math.floor(Math.random() * charactersLength))
}

// boolean 값 반환
export const parseBoolean = (value: string | boolean | undefined, defaultValue = false) => {
  if (typeof value === 'undefined') {
    return defaultValue
  }
  if (typeof value === 'boolean') {
    return value
  }
  switch (value.toLowerCase().trim()) {
    case 'true':
      return true
    case 'false':
      return false
    default:
      return defaultValue
  }
}

// array 나누기
export const divisionArr = (arr: any[] | undefined, n: number) => {
  if (arr === undefined) return []

  const _arr = objectDeepCopy(arr)
  const len = _arr.length
  const cnt = Math.ceil(len / n)

  const tmp = []
  for (let i = 0; i < cnt; i++) tmp.push(_arr.splice(0, n))

  return tmp
}

// ms to 'day DD HH:mm:ss'
export const msToHMS = (ms: number) => {
  const seconds = Math.floor((ms / 1000) % 60)
  const minutes = Math.floor((ms / 1000 / 60) % 60)
  const hours = Math.floor((ms / 1000 / 3600) % 24)
  const days = Math.floor(ms / 1000 / 3600 / 24)

  const arr = []
  arr.push(hours.toString().padStart(2, '0'))
  arr.push(minutes.toString().padStart(2, '0'))
  arr.push(seconds.toString().padStart(2, '0'))

  if (days > 0) return `${days}day ${arr.join(':')}`
  else return arr.join(':')
}

export const bytesToStringAutoConvert = (bytes: number, divNum = 1000, SUFFIX = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']) => {
  let retFormat = '0'

  if (bytes > 0) {
    const idx = Math.floor(Math.log(bytes) / Math.log(divNum))

    let ret = bytes / Math.pow(divNum, Math.floor(idx))
    ret = Math.ceil(ret * 10) / 10
    const formatVal = ret.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')

    retFormat = formatVal + ' ' + SUFFIX[idx]
  } else {
    retFormat += ' ' + SUFFIX[0]
  }

  return retFormat
}

export const formatFloatFixed = (value: number, fixed: number) => (Math.round(value * 100.0) / 100.0).toFixed(fixed)

export const sizeConverter = (bytes: number) => {
  const bytesInKB = 1024
  const bytesInMB = 1024 * 1024
  const bytesInGB = 1024 * 1024 * 1024

  if (bytes >= bytesInGB) {
    const ret = bytes / bytesInGB
    return `${ret.toFixed(2)}GB`
  } else if (bytes >= bytesInMB) {
    const ret = bytes / bytesInMB
    return `${ret.toFixed(2)}MB`
  } else if (bytes >= bytesInKB) {
    const ret = bytes / bytesInKB
    return `${ret.toFixed(2)}KB`
  } else {
    return `${bytes > 0 ? bytes.toFixed(5) : 0}B`
  }
}

export const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms))

export const decimalFormat = (dec: number) => (dec > 0 ? Math.round(dec * 100) / 100 : 0)

export const bytesToMb = (bytes: number) => {
  const mb = bytes / (1024 * 1024)
  return Number(mb.toFixed(2))
}
export const bytesToKb = (bytes: number) => {
  const mb = bytes / 1024
  return Number(mb.toFixed(2))
}
// export const toK = (bytes: number) => {
//   const mb = bytes / 1024
//   return Number(mb.toFixed(2))
// }

export const trafficConverter = (bytes: number) => {
  const bytesInK = 1000
  const bytesInM = 1000 * 1000
  const bytesInG = 1000 * 1000 * 1000

  if (bytes >= bytesInG) {
    const ret = bytes / bytesInG
    return `${ret.toFixed(2)}G`
  } else if (bytes >= bytesInM) {
    const ret = bytes / bytesInM
    return `${ret.toFixed(2)}M`
  } else if (bytes >= bytesInK) {
    const ret = bytes / bytesInK
    return `${ret.toFixed(2)}K`
  } else {
    return bytes
  }
}
