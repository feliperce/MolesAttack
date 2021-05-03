package scene

import com.soywiz.korau.sound.readSound
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs

class MenuScene() : Scene() {

    override suspend fun Container.sceneInit() {

        val menuMusic = resourcesVfs["sound/msc_menu.wav"].readSound().play()

        val bgImg = resourcesVfs["img/bg_game.png"].readBitmap()
        val logoBitmap = resourcesVfs["img/logo.png"].readBitmap()
        val playButtonBitmap = resourcesVfs["img/gui_btn_play.png"].readBitmap()

        val centerW = views.virtualWidth/2
        val centerH = views.virtualHeight/2

        val bg = Image(bgImg)
        bg.size(views.virtualWidth, views.virtualHeight)

        val logoImg = Image(logoBitmap).apply {
            center()
            position(centerW, centerH-50)
        }

        val playButtonGui = Image(playButtonBitmap).apply {
            center()
            position(logoImg.x, logoImg.y+logoImg.height)
            onClick {
                scale(1.0, 1.0)
                it.onDown {
                    scale(0.8, 0.8)
                }

                menuMusic.stop()

                launchImmediately {
                    sceneContainer.changeTo<GameScene>()
                }
            }
        }

        addChild(bg)
        addChild(logoImg)
        addChild(playButtonGui)

    }
}
