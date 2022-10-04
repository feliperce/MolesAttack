package scene

import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.readSound
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import data.GameOver

class GameOverScene(
	val gameOver: GameOver
) : Scene() {

	private lateinit var bgImg: Bitmap
	private lateinit var gameoverBitmap: Bitmap
	private lateinit var replayButtonBitmap: Bitmap
	private lateinit var menuButtonBitmap: Bitmap
	private lateinit var gameOverSound: Sound

	override suspend fun SContainer.sceneInit() {
		bgImg = resourcesVfs["img/bg_game.png"].readBitmap()
		gameoverBitmap = resourcesVfs["img/gui_gameover.png"].readBitmap()
		replayButtonBitmap = resourcesVfs["img/gui_btn_replay.png"].readBitmap()
		menuButtonBitmap = resourcesVfs["img/gui_btn_menu.png"].readBitmap()
		gameOverSound = resourcesVfs["sound/snd_gameover.mp3"].readSound()
    }

	override suspend fun SContainer.sceneMain() {

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

		gameOverSound.play()

		addChild(bg)
		addChild(gameOverGui)
		addChild(replayButtonGui)

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

	}
}
