package game

import attribute.Colour
import attribute.Height
import attribute.Hole
import attribute.Shape
import CheckGame.Result
import CheckGame

class Game(var grid: Array<Array<Piece>> = Array(4) { Array(4) { Piece(Colour.EMPTY, Shape.EMPTY, Height.EMPTY, Hole.EMPTY) } }, starting: Colour = Colour.BLACK) {

    val players: Array<Player> = arrayOf(Player(Colour.BLACK), Player(Colour.WHITE))
    var moves = 0
    var turn = players.find { it.colour == starting }!!


    init {
        check(players.size == 2)
        check(grid.size == 4)
        grid.forEach { check(it.size == 4) }
    }

    fun newGame(newState: Array<Array<Piece>>) {
        check(newState.size == 4)
        newState.forEach { check(it.size == 4) }

        grid = newState
    }

    fun makeMove(x: Int, y: Int, piece: Int) : Result {
        val player = turn

        grid[x][y] = player.pieces[piece]
        player.pieces = player.pieces.filter { it != player.pieces[piece] }.toTypedArray()

        moves++
        val result = CheckGame(this).checkForWin()
        if (result == Result.NEXT) {
            println("game next")
            turn = players.find { it != player }!!
        }
        return result
    }

//    fun checkWin(game: Game) : Boolean{
//        val result = CheckGame(game).checkForWin()
//        return when (result) {
//            Result.WIN_B -> true
//            Result.WIN_W -> true
//            else -> moves > 16
//        }
//    }

    fun printGameState() {
        for (i in 1..grid.size) { if (i == 1) print("     $i   ") else print("  $i   ")}
        println()
        grid.forEachIndexed { i, it ->
            print("${i+1} [")
            it.forEachIndexed { j, p ->
                print(p.draw())
                if (j != it.lastIndex) { print(',') }
            }
            println("]\n")
        }
    }
}