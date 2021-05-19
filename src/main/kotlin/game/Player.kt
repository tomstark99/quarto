package game

import attribute.Colour
import attribute.Height
import attribute.Hole
import attribute.Shape

class Player(val colour: Colour) {

    var moves = 0
    var pieces = PlayerPieces(colour).pieces

    private fun assertLine(line: Array<Piece>, p: Piece) : Boolean {
        return (line.filter { it.shape == p.shape }.size < 2 &&
                line.filter { it.height == p.height }.size < 2 &&
                line.filter { it.hole == p.hole }.size < 2)
    }

    private fun checkLinesAtPos(grid: Array<Array<Piece>>, line: Array<Piece>, i: Int, j: Int, p: Piece, multiplier: Int) : Int {
        val lineX = arrayOf(grid[i][0], grid[i][1], grid[i][2], grid[i][3])
        val lineY = arrayOf(grid[0][j], grid[1][j], grid[2][j], grid[3][j])
        val lineD = arrayOf(grid[0][0], grid[1][1], grid[2][2], grid[3][3])
        val lineR = arrayOf(grid[0][3], grid[1][2], grid[2][1], grid[3][0])
        if (lineX.contentEquals(line)) {
            if (assertLine(lineY, p)) {
                if (i == j && !lineD.contentEquals(line)) {
                    if (assertLine(lineD, p)) {
                        return 8 * multiplier
                    }
                } else if ((i == 0 && j == 3 || i == 1 && j == 2 || i == 2 && j == 1 || i == 3 && j == 0) && !lineR.contentEquals(line)) {
                    if (assertLine(lineR, p)) {
                        return 8 * multiplier
                    }
                }
                else {
                    return 8 * multiplier
                }
            } else {
                return -1
            }
        } else if (lineY.contentEquals(line)) {
            if (assertLine(lineX, p)) {
                if (i == j) {
                    if (assertLine(lineD, p)) {
                        return 8 * multiplier
                    }
                } else if (i == 0 && j == 3 || i == 1 && j == 2 || i == 2 && j == 1 || i == 3 && j == 0) {
                    if (assertLine(lineR, p)) {
                        return 8 * multiplier
                    }
                }
                else {
                    return 8 * multiplier
                }
            } else {
                return -1
            }
        } else if(lineD.contentEquals(line)) {
            if (assertLine(lineX, p) && assertLine(lineY, p)) {
                return 8 * multiplier
            } else {
                return -1
            }
        } else if(lineR.contentEquals(line)) {
            if (assertLine(lineX, p) && assertLine(lineY, p)) {
                return 8 * multiplier
            } else {
                return -1
            }
        }
        return 0
    }

    private fun checkLine(line: Array<Piece>, map: MutableMap<Piece, Int>, grid: Array<Array<Piece>>, i: Int, j: Int) : MutableMap<Piece, Int> {
        map.forEach { p, _ ->
            if(line.filter { it.shape == p.shape }.size < 2) map[p] = map[p]!! + 2
            if(line.filter { it.shape == p.shape }.size == 3) map[p] = map[p]!! + 64
            if(line.filter { it.height == p.height }.size < 2) map[p] = map[p]!! + 2
            if(line.filter { it.height == p.height }.size == 3) map[p] = map[p]!! + 64
            if(line.filter { it.hole == p.hole }.size < 2) map[p] = map[p]!! + 2
            if(line.filter { it.hole == p.hole }.size == 3) map[p] = map[p]!! + 64
            if(line.filter { it.colour == p.colour }.size < 4) map[p] = map[p]!! + 2
            if(line.filter { it.colour == p.colour }.size >= 2 &&
                line.none { it.colour != p.colour && it.colour != Colour.EMPTY } &&
                assertLine(line, p)) {
                    val added = checkLinesAtPos(grid, line, i, j, p, 1)
                    println("(${i+1},${j+1}) $added added for line with 2 or more same colour")
                    map[p] = map[p]!! + added
            }
            if(line.filter { it.colour != p.colour && it.colour != Colour.EMPTY }.size == 3) {
                val added = checkLinesAtPos(grid, line, i, j, p, 8)
                println("(${i+1},${j+1}) $added added to prevent win for 3 same")
                map[p] = map[p]!! + added
            }
        }
        return map
    }

    fun chooseMove(game: Game) : Triple<Int, Int, Int> {
        val grid = game.grid
        val scores = Array (4) { Array (4) { pieces.map { p -> p to 0 }.toMap().toMutableMap()} }
        check(grid.size == scores.size)

        grid.forEachIndexed { i, ps ->
            ps.forEachIndexed { j, _ ->
                if (grid[i][j].colour == Colour.EMPTY) {
                    scores[i][j] = checkLine(arrayOf(grid[i][0], grid[i][1], grid[i][2], grid[i][3]), scores[i][j], grid, i, j)
                    scores[i][j] = checkLine(arrayOf(grid[0][j], grid[1][j], grid[2][j], grid[3][j]), scores[i][j], grid, i, j)
                    if(i==j){
                        val map = checkLine(arrayOf(grid[0][0], grid[1][1], grid[2][2], grid[3][3]), scores[i][j], grid, i, j)
                        scores[i][j] = map.mapValues { Math.floorDiv((it.value*2),3) }.toMutableMap()
                    }
                    else if(i == 0 && j == 3 || i == 1 && j == 2 || i == 2 && j == 1 || i == 3 && j == 0) {
                        val map = checkLine(arrayOf(grid[0][3], grid[1][2], grid[2][1], grid[3][0]), scores[i][j], grid, i, j)
                        scores[i][j] = map.mapValues { Math.floorDiv((it.value*2),3) }.toMutableMap()
                    }
                }
            }
        }

        var scores2: Array<Array<Int>> = Array(4) { Array(4) {0} }
        scores.forEachIndexed { i, sc ->
            sc.forEachIndexed { j, _ ->
                if (grid[i][j].colour == Colour.EMPTY) {
                    scores2[i][j] = scores[i][j].toList().sortedByDescending { (_, value) -> value }.first().second
                }
            }
        }
        var moves = mutableListOf<Triple<Int, Int, List<Piece>>>()
        var max = scores2.flatten().sortedDescending().take(1).first()
        scores.forEachIndexed { i, sc ->
            sc.forEachIndexed { j, s ->
                if(s.filterValues { it == max }.isNotEmpty()) {
                    moves.add(Triple(i, j, s.filterValues { it == max }.keys.toMutableList()))
                }
            }
        }
        var move = moves.random()
        printGameState(scores2, moves, max)
        return Triple(move.first, move.second, pieces.indexOf(move.third.random()))
    }

    fun printGameState(grid: Array<Array<Int>>, moves: MutableList<Triple<Int, Int, List<Piece>>>, max: Int) {
        println()
        for (i in 1..grid.size) { if (i == 1) print("     $i   ") else print("  $i   ")}
        println()
        grid.forEachIndexed { i, it ->
            print("${i+1} [")
            it.forEachIndexed { j, p ->
                if(p >= 0) {
                    print(if (p == 0) "     " else if (p < 10) "   $p " else if (p < 100) "  $p " else " $p ")
                    if (j != it.lastIndex) {
                        print(',')
                    }
                }
            }
            println("]\n")
        }
        moves.forEach { move ->
            println("move at (${move.first+1},${move.second+1}) with score: $max")
            pieces.forEach { print(" ${it.draw()} ") }
            println()
            pieces.forEach {
                if (move.third.contains(it)) {
                    print(if (max == 0) "       " else if (max < 10) "   $max   " else if (max < 100) "  $max   " else "  $max  ")
                } else {
                    print("       ")
                }
            }
            println()
        }

    }
}