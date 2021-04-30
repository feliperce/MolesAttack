import com.soywiz.klock.seconds
import com.soywiz.korge.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing

suspend fun main() = Korge(width = 480, height = 640, title = "2048", bgcolor = RGBA(253, 247, 240)) {

	val bgImg = resourcesVfs["bg_game.png"].readBitmap()
	val bg = Image(bgImg)
	bg.size(views.virtualWidth, views.virtualHeight)

	addChild(bg)

	val spriteMap = resourcesVfs["spr_enemy_show.png"].readBitmap()
	val holeAnimation = SpriteAnimation(
		spriteMap = spriteMap,
		spriteWidth = 187,
		spriteHeight = 150,
		marginTop = 0,
		marginLeft = 0,
		columns = 4,
		rows = 1,
		offsetBetweenColumns = 0,
		offsetBetweenRows = 0,
	)

	val qtColumn = 4
	val qtRow = 4
	var qtEnemy = qtColumn * qtRow

	val enemyArray = arrayListOf<Sprite>()

	repeat(qtEnemy+1) {
		enemyArray.add(sprite(holeAnimation))
	}

	var currentEnemyX = 0
	var currentEnemyY = 120
	val enemyOnRow = 4
	val enemyWidth = 120
	val enemyHeight = 100
	var currentRow = 4

	enemyArray.forEachIndexed { index, sprite ->
		sprite.position(currentEnemyX, currentEnemyY)
		sprite.size(enemyWidth, enemyHeight)
		currentEnemyX += enemyWidth
		sprite.playAnimationLooped()

		if (index == currentRow) {
			currentEnemyY += enemyHeight
			currentRow += enemyOnRow
			currentEnemyX = 0
		}
	}

}