import attribute.Colour
import attribute.Height
import attribute.Hole
import attribute.Shape
import game.Game
import game.PlayerPieces
import game.Piece
import game.Player
import CheckGame.Result

class CheckGame(game: Game) {

    private val grid = game.grid
    private val turn = game.turn.colour
    private val moves = game.moves

    enum class Result(val id: Colour, val result: String) {
        WIN_W(Colour.WHITE,"Green has won"),
        WIN_B(Colour.BLACK,"Red has won"),
        NEXT(Colour.EMPTY,"The outcome of the game is yet to be decided"),
        DRAW(Colour.EMPTY,"The outcome of the game is a draw")
    }

    init {
        check(grid.size == 4)
        grid.forEach { check(it.size == 4) }
    }

    fun checkLine(p0: Piece, p1: Piece, p2: Piece, p3: Piece) : Colour {
        return if (p0.colour == Colour.BLACK && p1.colour == Colour.BLACK && p2.colour == Colour.BLACK && p3.colour == Colour.BLACK) Colour.BLACK
        else if(p0.colour == Colour.WHITE && p1.colour == Colour.WHITE && p2.colour == Colour.WHITE && p3.colour == Colour.WHITE) Colour.WHITE
        else if(p0.shape == Shape.ROUND && p1.shape == Shape.ROUND && p2.shape == Shape.ROUND && p3.shape == Shape.ROUND) turn
        else if(p0.shape == Shape.SQUARE && p1.shape == Shape.SQUARE && p2.shape == Shape.SQUARE && p3.shape == Shape.SQUARE) turn
        else if(p0.height == Height.TALL && p1.height == Height.TALL && p2.height == Height.TALL && p3.height == Height.TALL) turn
        else if(p0.height == Height.SMALL && p1.height == Height.SMALL && p2.height == Height.SMALL && p3.height == Height.SMALL) turn
        else if(p0.hole == Hole.YES && p1.hole == Hole.YES && p2.hole == Hole.YES && p3.hole == Hole.YES) turn
        else if(p0.hole == Hole.NO && p1.hole == Hole.NO && p2.hole == Hole.NO && p3.hole == Hole.NO) turn
        else Colour.EMPTY

    }
//
    fun checkForWin() : Result {
        val map = Result.values().associateBy(Result::id)
        if(checkLine(grid[0][0], grid[1][1], grid[2][2], grid[3][3]) != Colour.EMPTY) {
            return map[checkLine(grid[0][0], grid[1][1], grid[2][2], grid[3][3])]!!
        } else if (checkLine(grid[0][3], grid[1][2], grid[2][1], grid[3][0]) != Colour.EMPTY) {
            return map[checkLine(grid[0][3], grid[1][2], grid[2][1], grid[3][0])]!!
        }
        grid.forEachIndexed { i, _ ->
            if(checkLine(grid[i][0], grid[i][1], grid[i][2], grid[i][3]) != Colour.EMPTY) {
                return map[checkLine(grid[i][0], grid[i][1], grid[i][2], grid[i][3])]!!
            }
        }
        grid.forEachIndexed { j, _ ->
            if(checkLine(grid[0][j], grid[1][j], grid[2][j], grid[3][j]) != Colour.EMPTY) {
                return map[checkLine(grid[0][j], grid[1][j], grid[2][j], grid[3][j])]!!
            }
        }
        return if (moves == 16) Result.DRAW
        else Result.NEXT//map[Colour.EMPTY]!!
    }

//    fun printOutcome(result: Result) {
//        println(result.result)
//    }
}

fun main(args: Array<String>) {
    val game = Game(starting = Colour.WHITE)
    var result = Result.NEXT

    val empty = Piece()

//    game.grid = arrayOf(
//        arrayOf(Piece(Colour.WHITE, Shape.ROUND, Height.TALL, Hole.NO), empty, empty, empty),
//        arrayOf(empty, Piece(Colour.WHITE, Shape.ROUND, Height.SMALL, Hole.YES), empty, empty),
//        arrayOf(empty, Piece(Colour.WHITE, Shape.SQUARE, Height.SMALL, Hole.YES), empty, empty),
//        arrayOf(Piece(Colour.BLACK, Shape.ROUND, Height.SMALL, Hole.NO), empty, empty, Piece(Colour.BLACK, Shape.SQUARE, Height.SMALL, Hole.NO))
//    )

//    game.grid = arrayOf(
//        arrayOf(Piece(Colour.BLACK, Shape.ROUND, Height.SMALL, Hole.YES), empty, Piece(Colour.BLACK, Shape.SQUARE, Height.TALL, Hole.NO), Piece(Colour.WHITE, Shape.SQUARE, Height.TALL, Hole.NO)),
//        arrayOf(Piece(Colour.WHITE, Shape.ROUND, Height.SMALL, Hole.YES), Piece(Colour.BLACK, Shape.SQUARE, Height.TALL, Hole.YES),Piece(Colour.WHITE, Shape.SQUARE, Height.TALL, Hole.YES), Piece(Colour.WHITE, Shape.ROUND, Height.TALL, Hole.NO)),
//        arrayOf(empty, empty, Piece(Colour.BLACK, Shape.ROUND, Height.SMALL, Hole.NO), empty),
//        arrayOf(empty, Piece(Colour.BLACK, Shape.ROUND, Height.TALL, Hole.NO), empty, empty)
//    )
//
//    game.grid = arrayOf(
//        arrayOf(empty, empty, empty, empty),
//        arrayOf(empty, empty, empty, empty),
//        arrayOf(empty, Piece(Colour.WHITE, Shape.ROUND, Height.TALL, Hole.YES), Piece(Colour.BLACK, Shape.ROUND, Height.SMALL, Hole.NO), empty),
//        arrayOf(Piece(Colour.WHITE, Shape.ROUND, Height.SMALL, Hole.NO), empty, empty, Piece(Colour.BLACK, Shape.ROUND, Height.TALL, Hole.YES))
//    )
//
//    game.printGameState()
//    println(game.turn.colour.colour)
//    game.turn.pieces = arrayOf(
//        Piece(game.turn.colour, Shape.ROUND, Height.TALL, Hole.NO),
//        Piece(game.turn.colour, Shape.ROUND, Height.SMALL, Hole.YES),
//        Piece(game.turn.colour, Shape.SQUARE, Height.SMALL, Hole.YES),
//        Piece(game.turn.colour, Shape.SQUARE, Height.SMALL, Hole.NO),
//        Piece(game.turn.colour, Shape.SQUARE, Height.TALL, Hole.YES),
//        Piece(game.turn.colour, Shape.SQUARE, Height.TALL, Hole.NO)
//    )
//    game.turn.pieces.forEach { print(" ${it.draw()} ") }
//    val move = game.turn.chooseMove(game)
//    game.makeMove(move.first, move.second, move.third)
//    game.printGameState()

    while(game.moves <= 16) {
        game.printGameState()

        if (game.turn.colour == Colour.EMPTY) {
            println("${game.turn.colour.colour} pieces:")
            for (i in 1..game.turn.pieces.size) { print("   $i   ")}
            println()
            game.turn.pieces.forEach { print(" ${it.draw()} ") }
            println()
            while(true) {
                print("place which piece:")
                val (piece, x, y) = readLine()!!.split(" ")

                if((piece.toInt() in 1..game.turn.pieces.size) &&
                    (x.toInt() in 1..4 && y.toInt() in 1..4) &&
                    (game.grid[x.toInt() - 1][y.toInt() - 1].colour == Colour.EMPTY)) {

                    result = game.makeMove(x.toInt() - 1, y.toInt() - 1, piece.toInt() - 1)
                    break
                }
            }
        } else {
            val move = game.turn.chooseMove(game)
            println("${move.first}, ${move.second}, ${move.third}")
            result = game.makeMove(move.first, move.second, move.third)
        }
        if(result != Result.NEXT) break
        println(result.result)
    }
    println(result.result)
    game.printGameState()
}