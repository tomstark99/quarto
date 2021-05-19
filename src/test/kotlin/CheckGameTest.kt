import CheckGame.*
import game.Game
import game.Game.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CheckGameTest {
    @Test fun testSetGameState() {
        val classUnderTest = Game()

        val expected = arrayOf(
            arrayOf(Player.N, Player.N, Player.N),
            arrayOf(Player.N, Player.N, Player.N),
            arrayOf(Player.N, Player.N, Player.N))

        classUnderTest.grid.forEachIndexed { i, arrayOfPlayers ->
            arrayOfPlayers.forEachIndexed { j, _ ->
                assertEquals(expected[i][j], classUnderTest.grid[i][j])
            }
        }

        classUnderTest.newGame(arrayOf(
            arrayOf(Player.X, Player.X, Player.O),
            arrayOf(Player.X, Player.O, Player.O),
            arrayOf(Player.X, Player.O, Player.X)
        ))

        classUnderTest.grid.forEachIndexed { i, arrayOfPlayers ->
            arrayOfPlayers.forEachIndexed { j, _ ->
                assertNotEquals(expected[i][j], classUnderTest.grid[i][j])
            }
        }
    }

    @Test fun testLine() {
        val default = arrayOf(
            arrayOf(Player.N, Player.N, Player.N),
            arrayOf(Player.N, Player.N, Player.N),
            arrayOf(Player.N, Player.N, Player.N)
        )
        val classUnderTest = CheckGame(default)

        assertEquals(Player.X, classUnderTest.checkLine(Player.X, Player.X, Player.X))
        assertEquals(Player.O, classUnderTest.checkLine(Player.O, Player.O, Player.O))
        assertEquals(Player.N, classUnderTest.checkLine(Player.X, Player.O, Player.O))
        assertEquals(Player.N, classUnderTest.checkLine(Player.O, Player.X, Player.O))
        assertEquals(Player.N, classUnderTest.checkLine(Player.O, Player.O, Player.X))
        assertEquals(Player.N, classUnderTest.checkLine(Player.N, Player.N, Player.N))
        assertEquals(Player.N, classUnderTest.checkLine(Player.X, Player.N, Player.N))
    }

    @Test fun testWin() {
        val state1 = arrayOf(
            arrayOf(Player.X, Player.X, Player.X),
            arrayOf(Player.N, Player.O, Player.N),
            arrayOf(Player.N, Player.O, Player.N))
        val state2 = arrayOf(
            arrayOf(Player.N, Player.O, Player.N),
            arrayOf(Player.X, Player.X, Player.X),
            arrayOf(Player.N, Player.O, Player.N))
        val state3 = arrayOf(
            arrayOf(Player.N, Player.O, Player.N),
            arrayOf(Player.N, Player.O, Player.N),
            arrayOf(Player.X, Player.X, Player.X))
        val state4 = arrayOf(
            arrayOf(Player.O, Player.N, Player.N),
            arrayOf(Player.O, Player.X, Player.N),
            arrayOf(Player.O, Player.N, Player.X))
        val state5 = arrayOf(
            arrayOf(Player.N, Player.O, Player.N),
            arrayOf(Player.X, Player.O, Player.N),
            arrayOf(Player.N, Player.O, Player.X))
        val state6 = arrayOf(
            arrayOf(Player.N, Player.N, Player.O),
            arrayOf(Player.X, Player.N, Player.O),
            arrayOf(Player.N, Player.X, Player.O))
        val state7 = arrayOf(
            arrayOf(Player.X, Player.N, Player.O),
            arrayOf(Player.N, Player.X, Player.O),
            arrayOf(Player.N, Player.N, Player.X))
        val state8 = arrayOf(
            arrayOf(Player.X, Player.N, Player.O),
            arrayOf(Player.N, Player.O, Player.X),
            arrayOf(Player.O, Player.N, Player.N))

        assertEquals(Result.WIN_X, CheckGame(state1).checkForWin())
        assertEquals(Result.WIN_X, CheckGame(state2).checkForWin())
        assertEquals(Result.WIN_X, CheckGame(state3).checkForWin())
        assertEquals(Result.WIN_O, CheckGame(state4).checkForWin())
        assertEquals(Result.WIN_O, CheckGame(state5).checkForWin())
        assertEquals(Result.WIN_O, CheckGame(state6).checkForWin())
        assertEquals(Result.WIN_X, CheckGame(state7).checkForWin())
        assertEquals(Result.WIN_O, CheckGame(state8).checkForWin())
    }

    @Test fun testNoWin() {
        val state1 = arrayOf(
            arrayOf(Player.N, Player.N, Player.N),
            arrayOf(Player.N, Player.N, Player.N),
            arrayOf(Player.N, Player.N, Player.N))
        val state2 = arrayOf(
            arrayOf(Player.O, Player.N, Player.X),
            arrayOf(Player.X, Player.X, Player.O),
            arrayOf(Player.O, Player.X, Player.N))
        val state3 = arrayOf(
            arrayOf(Player.X, Player.O, Player.X),
            arrayOf(Player.X, Player.O, Player.O),
            arrayOf(Player.O, Player.X, Player.O))
        val state4 = arrayOf(
            arrayOf(Player.O, Player.O, Player.X),
            arrayOf(Player.X, Player.O, Player.O),
            arrayOf(Player.O, Player.X, Player.X))

        assertEquals(Result.NEXT, CheckGame(state1).checkForWin())
        assertEquals(Result.NEXT, CheckGame(state2).checkForWin())
        assertEquals(Result.NEXT, CheckGame(state3).checkForWin())
        assertEquals(Result.NEXT, CheckGame(state4).checkForWin())
    }
}
