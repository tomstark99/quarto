package game

import attribute.Colour
import attribute.Height
import attribute.Hole
import attribute.Shape

class PlayerPieces(colour: Colour) {

    var pieces: Array<Piece> = arrayOf(
        Piece(colour, Shape.ROUND, Height.TALL, Hole.YES),
        Piece(colour, Shape.ROUND, Height.TALL, Hole.NO),
        Piece(colour, Shape.ROUND, Height.SMALL, Hole.YES),
        Piece(colour, Shape.ROUND, Height.SMALL, Hole.NO),
        Piece(colour, Shape.SQUARE, Height.TALL, Hole.YES),
        Piece(colour, Shape.SQUARE, Height.TALL, Hole.NO),
        Piece(colour, Shape.SQUARE, Height.SMALL, Hole.YES),
        Piece(colour, Shape.SQUARE, Height.SMALL, Hole.NO)
    )

}