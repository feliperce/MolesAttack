package entity

import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.SpriteAnimation
import com.soywiz.korge.view.addTo
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.displayImage
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import kotlin.random.Random

class Enemy(
    private val container: Container
) : Sprite() {

    var hidingSprite: Bitmap? = null
    var showAnimation: SpriteAnimation? = null
    var deadAnimation: SpriteAnimation? = null
    var hidingAnimation: SpriteAnimation? = null
    var dead = false
    var idle = false
    var animating = false
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
            animating = true
        }

        onAnimationCompleted {
            animating = false
        }

    }

    fun hide() {
        playAnimation(hidingAnimation, spriteDisplayTime = 0.milliseconds, endFrame = 0, startFrame = 0)
    }

    fun show() {
        val randomSpeedIndex = Random.nextInt(0, movimentSpeeds.size)
        playAnimation(showAnimation, spriteDisplayTime = movimentSpeeds[randomSpeedIndex].seconds)
    }

    fun kill() {
        dead = true
        playAnimation(deadAnimation, spriteDisplayTime = 2.seconds)
    }

    fun hit() {

    }


}