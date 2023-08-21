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
