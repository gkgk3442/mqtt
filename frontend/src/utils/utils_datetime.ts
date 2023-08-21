import dayjs from 'dayjs'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import duration from 'dayjs/plugin/duration'
import timezone from 'dayjs/plugin/timezone'
import utc from 'dayjs/plugin/utc'
dayjs.extend(customParseFormat)
dayjs.extend(utc)
dayjs.extend(timezone)

type TDate = string | number | dayjs.Dayjs | Date | null | undefined

export const customTimezoneDispFormat = (val: TDate) => {
  const isValid = dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').isValid() || dayjs(val, 'YYYY-MM-DD HH:mm:ssZ').isValid()
  const hasMs = dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').get('milliseconds') > 0

  if (isValid && hasMs) return dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').tz(dayjs.tz.guess()).format('YYYY-MM-DD HH:mm:ss.SSS')
  else if (isValid) return dayjs(val, 'YYYY-MM-DDTHH:mm:ssZ').tz(dayjs.tz.guess()).format('YYYY-MM-DD HH:mm:ss.SSS')
  else return import.meta.env.DEV ? `[INVALID]${val}` : val
}
export const customTimezoneDispFormatMs = (val: TDate) => {
  const isValid = dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').isValid() || dayjs(val, 'YYYY-MM-DD HH:mm:ssZ').isValid()
  const hasMs = dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').get('milliseconds') > 0

  if (isValid && hasMs) return dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').tz(dayjs.tz.guess()).format('YYYY-MM-DD HH:mm:ss')
  else if (isValid) return dayjs(val, 'YYYY-MM-DDTHH:mm:ssZ').tz(dayjs.tz.guess()).format('YYYY-MM-DD HH:mm:ss')
  else return import.meta.env.DEV ? `[INVALID]${val}` : val
}

export const serverTimeDispFormat = (val: TDate) => {
  if (dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').isValid() && dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').get('milliseconds') > 0) {
    return dayjs(val, 'YYYY-MM-DD HH:mm:ss.SSSZ').tz(dayjs.tz.guess()).format('YYYY-MM-DD HH:mm:ss')
  } else {
    return dayjs(val, 'YYYY-MM-DDTHH:mm:ssZ').tz(dayjs.tz.guess()).format('YYYY-MM-DD HH:mm:ss')
  }
}

export const secToMMSS = (val: number) => {
  dayjs.extend(duration)
  if (isNaN(val)) return '--:--:--'
  return dayjs.duration(val, 'seconds').format('HH:mm:ss')
}

export const unixDispFormat = (val: number) => {
  return dayjs.unix(val).tz(dayjs.tz.guess()).format('YYYY-MM-DD HH:mm:ss')
}
