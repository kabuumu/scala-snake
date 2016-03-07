import scala.util.Random
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.KeyCode
import scalafx.scene.paint.Color

/**
 * Created by rob on 03/03/16.
 */
object Main {
  val bounds = 16

  def getUserInput: Input = {
    Input(App.keyCode)
  }

  def moveSnake(snakeHead: Position, snake: Seq[Position], eaten: Boolean) = {
    val newSnake = snake.+:(snakeHead)
    if (eaten) newSnake else newSnake.dropRight(1)
  }

  def getFoodCollision(snakeHead: Position, food: Position) = snakeHead == food

  def getFacing(facing: Direction, input: KeyCode): Direction = {
    (facing, input) match {
      case (Right | Left, KeyCode.UP) => Up
      case (Right | Left, KeyCode.DOWN) => Down
      case (Up | Down, KeyCode.LEFT) => Left
      case (Up | Down, KeyCode.RIGHT) => Right
      case _ => facing
    }
  }

  def getCrash(snakeHead: Position, body: Seq[Position]) = body.contains(snakeHead)

  def getFoodPos(food: Position, eaten: Boolean) = if (eaten) getRandomPosition(bounds) else food

  def gameLoop(state: GameState, input: KeyCode) = {
    val facing = getFacing(state.snake.facing, input)
    val snakeHead = moveHead(state.snake.body.head, facing)
    val eaten = getFoodCollision(snakeHead, state.food)
    val snakeBody = moveSnake(snakeHead, state.snake.body, eaten)
    val crash = getCrash(snakeHead, state.snake.body)
    val food = getFoodPos(state.food, eaten)
    val score = if (eaten) state.score + 1 else state.score

    GameState(Snake(snakeBody, facing), food, crash, score)
  }

  def outOfBounds(pos: Position) = pos.x>=0 && pos.x<bounds && pos.y>=0 && pos.y<bounds

  def reverse(pos: Position, bounds: Int) = {
    def reverseInt(n: Int) = {
      if(n < 0) bounds -1
      else if(n>=bounds) 0
      else n
    }
    Position(reverseInt(pos.x), reverseInt(pos.y))
  }

  def moveHead(snakeHead: Position, dir: Direction): Position = {
    val pos = dir match {
      case Up => Position(snakeHead.x, snakeHead.y - 1)
      case Down => Position(snakeHead.x, snakeHead.y + 1)
      case Right => Position(snakeHead.x + 1, snakeHead.y)
      case Left => Position(snakeHead.x - 1, snakeHead.y)
    }

    reverse(pos, bounds)
  }

  def output(state: GameState, canvas: Canvas) = {
    canvas.graphicsContext2D.clearRect(0,0,512,512)
    val size = 32
    val g2d = canvas.getGraphicsContext2D

    g2d.setFill(Color.Green)
    state.snake.body.foreach((pos) => {
      g2d.fillRect(size * pos.x, size * pos.y, size, size)
    })

    g2d.setFill(Color.Red)
    g2d.fillRect(state.food.x*size, state.food.y*size, size, size)

    g2d.fillText(state.score.toString, bounds/2, 20)
  }

  def getRandomPosition(bounds: Int) = Position(Random.nextInt(bounds), Random.nextInt(bounds))

  case class Position(x: Int, y: Int)

  case class Snake(body: Seq[Position], facing: Direction)

  case class GameState(snake: Snake, food: Position, crash: Boolean, score: Int)

  class Direction

  case object Up extends Direction

  case object Down extends Direction

  case object Left extends Direction

  case object Right extends Direction


  case class Input(key: KeyCode)

}
