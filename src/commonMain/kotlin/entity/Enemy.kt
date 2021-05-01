package entity

import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.random.Random

class Enemy(
    private val container: Container
) : Sprite() {

    var status: Status = Status.IDLE
    var hidingSprite: Bitmap? = null
    var showAnimation: SpriteAnimation? = null
    var deadAnimation: SpriteAnimation? = null
    var hidingAnimation: SpriteAnimation? = null
    var movmentSpeed = 3
    var movimentSpeeds = arrayOf(3, 4, 5, 2)

    suspend fun initialize() {
        val hidingSprite = resourcesVfs["spr_enemy_hiding.png"].readBitmap()
        val showSprite = resourcesVfs["spr_enemy_show.png"].readBitmap()
        val deadSprite = resourcesVfs["spr_enemy_dead.png"].readBitmap()

        hidingAnimation = SpriteAnimation(
            spriteMap = hidingSprite,
            spriteWidth = 187,
            spriteHeight = 150,
            marginTop = 0,
            marginLeft = 0,
            columns = 1,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0,
        )

        showAnimation = SpriteAnimation(
            spriteMap = showSprite,
            spriteWidth = 187,
            spriteHeight = 150,
            marginTop = 0,
            marginLeft = 0,
            columns = 3,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0,
        )

        deadAnimation = SpriteAnimation(
            spriteMap = deadSprite,
            spriteWidth = 187,
            spriteHeight = 150,
            marginTop = 0,
            marginLeft = 0,
            columns = 2,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0,
        )
        addTo(container)

        onAnimationStarted {

        }

        onAnimationCompleted {
            if (status == Status.DIED) {
                hide()
            }
            status = Status.HIDING
        }

        onFrameChanged {

            if (currentSpriteIndex == 2 && status == Status.SHOWING) {
                status = Status.IDLE
            }
        }


    }

    fun hide() {
        playAnimation(hidingAnimation, spriteDisplayTime = 0.milliseconds)
    }

    fun show() {
        val randomSpeedIndex = Random.nextInt(0, movimentSpeeds.size)
        playAnimation(showAnimation, spriteDisplayTime = movimentSpeeds[randomSpeedIndex].seconds)

    }

    fun showIfNotIdle() {
        if (status == Status.HIDING) {
            val randomSpeedIndex = Random.nextInt(0, movimentSpeeds.size)
            playAnimation(showAnimation, spriteDisplayTime = movimentSpeeds[randomSpeedIndex].seconds, endFrame = 3)
            status = Status.SHOWING
        }
    }

    fun kill() {
        status = Status.DIED
        playAnimation(deadAnimation, spriteDisplayTime = 2.seconds)
    }

    fun hit() {
        if (status == Status.IDLE) {
            kill()
        }
    }

    enum class Status {
        HIDING,
        IDLE,
        SHOWING,
        DIED
    }
}