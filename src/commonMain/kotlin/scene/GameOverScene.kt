package scene

import com.soywiz.korau.sound.readSound
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import data.GameOver

class GameOverScene(
	val gameOver: GameOver
) : Scene() {

    override suspend fun Container.sceneInit() {

		val bgImg = resourcesVfs["bg_game.png"].readBitmap()
		val gameoverBitmap = resourcesVfs["gui_gameover.png"].readBitmap()
		val replayButtonBitmap = resourcesVfs["gui_btn_replay.png"].readBitmap()
		val menuButtonBitmap = resourcesVfs["gui_btn_menu.png"].readBitmap()
		val gameOverSound = resourcesVfs["sound/snd_gameover.mp3"].readSound()

		val bg = Image(bgImg)
		bg.size(views.virtualWidth, views.virtualHeight)


		val gameOverGui = Image(gameoverBitmap).apply {
			//scale(1.3, 1.3)
			size(views.virtualWidth - 30, views.virtualHeight/2)
			position(10, 50)
		}

		val replayButtonGui = Image(replayButtonBitmap).apply {
			scale(0.6, 0.6)
			position(20, views.virtualHeight-70)
		}

		val menuButtonGui = Image(menuButtonBitmap).apply {
			scale(0.6, 0.6)
			position(views.virtualWidth-70, views.virtualHeight-70)
		}

		gameOverSound.play()

		addChild(bg)
		addChild(gameOverGui)
		addChild(replayButtonGui)
		addChild(menuButtonGui)

		val gratzText = text("GAMEOVER", textSize = 20.0, alignment = TextAlignment.LEFT).apply {
			setPositionRelativeTo(gameOverGui, Point(10, 10))
		}

		val scoreText = text("SCORE: ${gameOver.score}", textSize = 20.0, alignment = TextAlignment.LEFT).apply {
			setPositionRelativeTo(gratzText, Point(0, 40))
		}

		val timeText = text("TIME: ${gameOver.minutes}:${gameOver.seconds}", textSize = 20.0, alignment = TextAlignment.LEFT).apply {
			setPositionRelativeTo(scoreText, Point(0, 40))
		}

		replayButtonGui.onClick {
			launchImmediately {
				sceneContainer.changeTo<GameScene>()
			}
		}

		menuButtonGui.onClick {
			launchImmediately {
				sceneContainer.changeTo<MenuScene>()
			}
		}

    }
}
