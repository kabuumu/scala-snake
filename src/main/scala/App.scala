import Main.{GameState, Snake}

import scala.language.postfixOps
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.{KeyCode, KeyEvent}


/**
 * Created by rob on 03/03/16.
 */
object App extends JFXApp {
  val canvas = new Canvas(512, 512)
  var keyCode: KeyCode = null
  val startingState: GameState = GameState(
    snake = Snake(
      body = Seq(
        Main.getRandomPosition(32)
      ),
      facing = Main.Up
    ),
    food = Main.getRandomPosition(32),
    crash = false,
    score = 0
  )

  var gameState = startingState;

  stage = new PrimaryStage {
    title = "ScalaSnake"
    scene = new Scene {
      content = canvas

      onKeyPressed = {
        key:KeyEvent => keyCode = key.code
      }
    }


  }

  var lastDelta: Long = 0;

  AnimationTimer.apply(
    l => {
      if(l - lastDelta > 100000000-(gameState.score*1000000) && !gameState.crash){
        gameState = Main.gameLoop(gameState, keyCode)
        Main.output(gameState, canvas)
        lastDelta = l
      }
    }
  ).start()

  stage.show()
}
