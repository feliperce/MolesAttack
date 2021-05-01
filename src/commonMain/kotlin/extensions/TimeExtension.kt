package extensions

import com.soywiz.klock.seconds
import com.soywiz.korge.time.TimerComponents

fun TimerComponents.countDownTimer(minuteStart: Int, secondStart: Int, block: (Int, Int) -> Unit) {
    var minutes = minuteStart
    var seconds = secondStart
    var stopCount = false
    this.interval(1.seconds) {
        if (!stopCount) {
            seconds--
            if (seconds == 0) {
                minutes--
                seconds = 59
            }
            if (minutes < 0) {
                stopCount = true
                minutes = 0
                seconds = 0
            }
            block(minutes, seconds)
        }
    }
}