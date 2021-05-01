import com.soywiz.klock.infiniteTimes
import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.*
import com.soywiz.korge.input.mouse
import com.soywiz.korge.input.onClick
import com.soywiz.korge.time.timers
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korio.time.traceTime
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing
import entity.Enemy
import entity.Hammer

suspend fun main() = Korge(width = 480, height = 640, title = "Moles Attack", bgcolor = RGBA(253, 247, 240)) {

	val bgImg = resourcesVfs["bg_game.png"].readBitmap()
	val bg = Image(bgImg)
	bg.size(views.virtualWidth, views.virtualHeight)

	addChild(bg)

	val hammer = Hammer(this).apply {
		initialize()
	}

	onClick {
		hammer.show(mouseX, mouseY)
	}

	val qtColumn = 4
	val qtRow = 4
	var qtEnemy = qtColumn * qtRow

	val enemyArray = arrayListOf<Enemy>()

	repeat(qtEnemy+1) {
		val enemy = Enemy(this)
		enemyArray.add(enemy)
	}

	var minuteStart = 3
	var secondStart = 30
	var minuteIncreaseDificulty = 1
	var secondGenerateEnemy = 4
	var currentEnemyX = 0
	var currentEnemyY = 120
	val enemyOnRow = 4
	val enemyWidth = 120
	val enemyHeight = 100
	var currentRow = 4

	// Gera inimigos escondidos
	enemyArray.forEachIndexed { index, sprite ->
		//sprite.size(enemyWidth, enemyHeight)
		sprite.initialize()
		sprite.scale(0.6, 0.6)
		sprite.position(currentEnemyX, currentEnemyY)
		sprite.hide()
		currentEnemyX += enemyWidth
		//sprite.show()
		//sprite.playAnimation(spriteDisplayTime = 2.seconds)

		if (index == currentRow) {
			currentEnemyY += enemyHeight
			currentRow += enemyOnRow
			currentEnemyX = 0
		}
	}

	var minutes = 0
	var seconds = 0
	var gameOver = false

	val gameTimer = timers.interval(1.seconds) {
		seconds++

		if (seconds % secondGenerateEnemy == 0) {

		}

		if (seconds == 60) {
			minutes++
			seconds = 0
		}
	}

	addUpdater {
		if (gameOver) {
			gameTimer.close()
		}
	}

	text("${minutes}:${seconds}").addUpdater { time ->
		text = "${minutes}:${seconds}"
		val scale = time / 16.milliseconds
	}



}