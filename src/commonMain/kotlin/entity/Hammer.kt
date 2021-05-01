package entity

import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point

class Hammer(
    private val container: Container
) : Sprite() {

    var showAnimation: SpriteAnimation? = null
    private val sprWidth = 195
    private val sprHeight = 182
    private val sprCenterW = sprWidth/2
    private val sprCenterH = sprHeight/2

    suspend fun initialize() {
        val showSprite = resourcesVfs["spr_hammer.png"].readBitmap()

        showAnimation = SpriteAnimation(
            spriteMap = showSprite,
            spriteWidth = sprWidth,
            spriteHeight = sprHeight,
            marginTop = 0,
            marginLeft = 0,
            columns = 4,
            rows = 1,
            offsetBetweenColumns = 0,
            offsetBetweenRows = 0,
        )
        onAnimationCompleted {
            removeFromParent()
        }
        scale(0.8, 0.8)
    }

    fun show(x: Double, y: Double) {
        addTo(container)
        position(x-sprCenterW, y-sprCenterH)
        playAnimation(showAnimation)

    }

}