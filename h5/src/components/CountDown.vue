<template>
  <div>{{ formattedTime }}</div>
</template>

<script>
export default {
  props: {
    time: {
      type: [Number, String],
      default: 0
    },
    autoStart: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      remain: 0
    }
  },
  computed: {
    timeData() {
      return this.parseTimeData(this.remain)
    },

    formattedTime() {
      return this.parseFormat(this.timeData)
    }
  },
  watch: {
    time: {
      immediate: true,
      handler: 'reset'
    }
  },
  beforeDestroy() {
    this.pause()
  },
  methods: {
    start() {
      if (this.counting) {
        return
      }

      this.counting = true
      this.endTime = Date.now() + this.remain
      this.tick()
    },

    pause() {
      this.counting = false
      clearTimeout(this._setTimeout)
    },

    reset() {
      this.pause()
      this.remain = +this.time

      if (this.autoStart) {
        this.start()
      }
    },

    tick() {
      this._setTimeout = setTimeout(() => {
        if (!this.counting) {
          return
        }

        this.setRemain(this.getRemain())

        if (this.remain > 0) {
          this.tick()
        }
      }, 1000)
    },

    getRemain() {
      return Math.max(this.endTime - Date.now(), 0)
    },

    setRemain(remain) {
      this.remain = remain
      this.$emit('change', this.timeData)

      if (remain === 0) {
        this.pause()
        this.$emit('finish')
      }
    },
    parseTimeData(time) {
      const SECOND = 1000
      const MINUTE = 60 * SECOND
      const HOUR = 60 * MINUTE
      const DAY = 24 * HOUR
      const days = Math.floor(time / DAY)
      const hours = Math.floor((time % DAY) / HOUR)
      const minutes = Math.floor((time % HOUR) / MINUTE)
      const seconds = Math.floor((time % MINUTE) / SECOND)
      const milliseconds = Math.floor(time % SECOND)

      return {
        days,
        hours,
        minutes,
        seconds,
        milliseconds
      }
    },
    parseFormat(timeData) {
      const days = String(timeData.days).padStart(2, '0')
      const hours = String(timeData.hours).padStart(2, '0')
      const minutes = String(timeData.minutes).padStart(2, '0')
      const seconds = String(timeData.seconds).padStart(2, '0')
      if (days) {
        return `${days}天${hours}:${minutes}:${seconds}`
      } else {
        return `${hours}:${minutes}:${seconds}`
      }
    }
  }
}
</script>

<style lang="scss" scoped>
/deep/.el-select-dropdown__list {
  padding-bottom: 35px;
}
.custom-pager {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  line-height: 30px;
  background: #fff;
}
</style>
