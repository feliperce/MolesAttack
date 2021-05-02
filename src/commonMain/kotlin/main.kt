import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.*
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.timers
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.*
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.SizeInt
import data.GameOver
import entity.Enemy
import entity.Hammer
import scene.GameOverScene
import scene.GameScene
import scene.MenuScene
import kotlin.random.Random
import kotlin.reflect.KClass

suspend fun main() = Korge(Korge.Config(module = MainModule))

object MainModule : Module() {
	override val mainScene: KClass<out Scene> = MenuScene::class
	override val title: String = "Moles Attack"
	override val size: SizeInt = SizeInt(480,640)

	override suspend fun AsyncInjector.configure() {
		mapInstance(GameOver())
		mapPrototype { MenuScene() }
		mapPrototype { GameScene() }
		mapPrototype { GameOverScene(get()) }
	}
}