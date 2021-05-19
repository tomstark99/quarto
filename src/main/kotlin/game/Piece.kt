package game

import attribute.Colour
import attribute.Height
import attribute.Hole
import attribute.Shape
import utils.Console

class Piece(var colour: Colour = Colour.EMPTY, var shape: Shape = Shape.EMPTY, var height: Height = Height.EMPTY, var hole: Hole = Hole.EMPTY) {

    fun draw() : String {

        var char: String = if(hole.hasHole) "*" else " "

        if (height == Height.TALL && shape == Shape.ROUND) {
            char = "(($char))"
        } else if(height == Height.SMALL && shape == Shape.ROUND) {
            char = " ($char) "
        } else if(height == Height.TALL && shape == Shape.SQUARE) {
            char = "[[$char]]"
        } else if(height == Height.SMALL && shape == Shape.SQUARE) {
            char = " [$char] "
        }

        char = if (colour == Colour.WHITE) {
            "${Console.GREEN}$char${Console.RESET}"
        } else {
            "${Console.RED}$char${Console.RESET}"
        }

        return if (colour == Colour.EMPTY) "     " else char
    }
}