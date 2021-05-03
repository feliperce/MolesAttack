package scene

import com.soywiz.klock.seconds
import com.soywiz.korau.sound.Sound
import com.soywiz.korau.sound.infinitePlaybackTimes
import com.soywiz.korau.sound.readSound
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.timers
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import data.GameOver
import entity.Enemy
import entity.Hammer
import kotlinx.coroutines.coroutineScope
import kotlin.random.Random

class GameScene : Scene() {

    private lateinit var bgImg: Bitmap
    private lateinit var lifeBitmap: Bitmap
    private lateinit var gameMusic: Sound
    private lateinit var scoreSound: Sound
    private lateinit var errorSound: Sound
    private lateinit var hitSound: Sound
    private lateinit var enemySoundArray: Array<Sound>

    override suspend fun Container.sceneInit() {

        bgImg = resourcesVfs["img/bg_game.png"].readBitmap()
        lifeBitmap = resourcesVfs["img/gui_life.png"].readBitmap()

        // Sounds
        gameMusic = resourcesVfs["sound/msc_game.wav"].readSound()
        scoreSound = resourcesVfs["sound/snd_score.wav"].readSound()
        errorSound = resourcesVfs["sound/snd_error.mp3"].readSound()
        hitSound = resourcesVfs["sound/snd_hit.mp3"].readSound()
        //Enemy
        enemySoundArray = arrayOf(
            resourcesVfs["sound/enemy/snd_enemy1.wav"].readSound(),
            resourcesVfs["sound/enemy/snd_enemy2.wav"].readSound(),
            resourcesVfs["sound/enemy/snd_enemy3.wav"].readSound(),
            resourcesVfs["sound/enemy/snd_enemy4.wav"].readSound(),
            resourcesVfs["sound/enemy/snd_enemy5.wav"].readSound(),
            resourcesVfs["sound/enemy/snd_enemy6.wav"].readSound()
        )

    }

    override suspend fun Container.sceneMain() {
        val isDebugMode = false

        val gameMusicChannel = gameMusic.play(infinitePlaybackTimes)

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

        var minutes = 0
        var seconds = 0

        val bg = Image(bgImg)
        bg.size(views.virtualWidth, views.virtualHeight)

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

        for (i in 1..qtEnemy) {
            val enemy = Enemy(this).apply {
                id = i
            }
            enemyArray.add(enemy)
        }

        // Gera inimigos escondidos
        enemyArray.forEachIndexed { index, enemy ->
            enemy.initialize()
            enemy.scale(0.6, 0.6)
            enemy.position(currentEnemyX, currentEnemyY)
            enemy.hide()
            enemy.onClick {
                hammer.show(localMouseX(views), localMouseY(views))
                enemy.hit()
                hitSound.play()
            }

            if (isDebugMode) {
                text("ID: ${enemy.id} failure: ${enemy.failure}").position(enemy.x, enemy.y)
            }

            enemy.addUpdater {
                if (this.failure) {
                    errorSound.play()
                    val error = text("X", textSize = this.width/2, color = Colors.RED).position(this.x+30, this.y)
                    this.failure = false
                    life--

                    timers.interval(2.seconds) {
                        removeChild(error)
                    }
                }

                if (this.canScore) {
                    scoreSound.play()
                    val randomScore = Random.nextInt(0, scores.size)
                    score += scores[randomScore]
                    canScore = false
                }
            }
            currentEnemyX += enemyWidth

            if (index == currentRow-1) {
                currentEnemyY += enemyHeight
                currentRow += enemyOnRow
                currentEnemyX = 0
            }
        }

        val gameTimer = timers.interval(1.seconds) {
            seconds++

            // enemy show
            if (seconds % secondGenerateEnemy == 0) {
                val randomPositionIndex = Random.nextInt(0, enemyArray.size)
                val generateEnemySound = Random.nextBoolean()
                enemyArray[randomPositionIndex].showIfNotIdle()

                if (generateEnemySound) {
                    Random.nextInt(0, enemySoundArray.size).let {
                        enemySoundArray[it].play()
                    }
                }
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
            if (life <= 0) {

                gameTimer.close()

                val gameOver = GameOver(
                    score, minutes, seconds
                )

                launchImmediately {
                    gameMusicChannel.stop()
                    sceneContainer.changeTo<GameOverScene>(gameOver)
                }
            }
        }

        /*text("${minutes}:${seconds}").addUpdater { time ->
            text = "${minutes}:${seconds}"
        }*/
    }
}