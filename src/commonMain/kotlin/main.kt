import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.time.timers
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.*
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.Point
import entity.Enemy
import entity.Hammer
import kotlin.random.Random

suspend fun main() = Korge(width = 480, height = 640, title = "Moles Attack", bgcolor = RGBA(253, 247, 240)) {

	val qtColumn = 4
	val qtRow = 4
	val qtEnemy = qtColumn * qtRow

	var score = 0
	val scores = arrayOf(100, 50, 200, 400)
	var life = 5
	var secondGenerateEnemy = 5
	val secondsIncreaseDifficulty = 15
	var currentEnemyX = 0
	var currentEnemyY = 120
	val enemyOnRow = 4
	val enemyWidth = 120
	val enemyHeight = 100
	var currentRow = 4

	val bgImg = resourcesVfs["bg_game.png"].readBitmap()
	val bg = Image(bgImg)
	bg.size(views.virtualWidth, views.virtualHeight)

	val lifeBitmap = resourcesVfs["gui_life.png"].readBitmap()
	val lifeGui = Image(lifeBitmap).apply {
		scale(0.5, 0.5)
		position(10, 10)
	}

	val lifeText = text("$life", textSize = 20.0).apply {
		setPositionRelativeTo(lifeGui, Point(lifeGui.width, 10.0))
		addUpdater { time ->
			text = "$life"
		}
	}

	val scoreText = text("$score", textSize = 20.0, alignment = TextAlignment.RIGHT).apply {
		position(views.virtualWidth-10, 10)
		addUpdater { time ->
			text = "$score"
		}
	}

	addChild(bg)
	addChild(lifeGui)
	addChild(lifeText)
	addChild(scoreText)

	val hammer = Hammer(this).apply {
		initialize()
	}

	val enemyArray = arrayListOf<Enemy>()

	repeat(qtEnemy+1) {
		val enemy = Enemy(this)
		enemyArray.add(enemy)
	}

	// Gera inimigos escondidos
	enemyArray.forEachIndexed { index, enemy ->
		enemy.initialize()
		enemy.scale(0.6, 0.6)
		enemy.position(currentEnemyX, currentEnemyY)
		enemy.hide()
		enemy.onClick {
			hammer.show(mouseX, mouseY)
			enemy.hit()
		}
		enemy.addUpdater {
			if (this.failure) {
				val error = text("X", textSize = this.width/2, color = Colors.RED).position(this.x+30, this.y)
				this.failure = false
				life--
				timers.interval(2.seconds) {
					removeChild(error)
				}
			}

			if (this.canScore) {
				val randomScore = Random.nextInt(0, scores.size)
				score += scores[randomScore]
				canScore = false
			}
		}
		currentEnemyX += enemyWidth

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

		// enemy show
		if (seconds % secondGenerateEnemy == 0) {
			val randomPositionIndex = Random.nextInt(0, enemyArray.size)
			enemyArray[randomPositionIndex].showIfNotIdle()
		}

		// increase dificulty
		if (seconds % secondsIncreaseDifficulty == 0) {
			if (secondGenerateEnemy > 1) {
				secondGenerateEnemy--
			}
		}

		if (seconds == 60) {
			minutes++
			seconds = 0
		}
	}

	addUpdater {
		if (life == 0) {
			gameOver = true
		}

		if (gameOver) {
			gameTimer.close()
		}
	}

	text("${minutes}:${seconds}").addUpdater { time ->
		text = "${minutes}:${seconds}"
		val scale = time / 16.milliseconds
	}

}