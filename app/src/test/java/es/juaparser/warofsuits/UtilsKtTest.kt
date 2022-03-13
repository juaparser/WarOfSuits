package es.juaparser.warofsuits

import es.juaparser.warofsuits.model.Card
import org.junit.Assert
import org.junit.Test

class UtilsKtTest {
    @Test
    fun cardGreaterThanCurrent_value() {
        val card1 = Card("8","spades")
        val card2 = Card("5", "spades")
        Assert.assertEquals(1, card1.compareWith(card2))
    }

    @Test
    fun cardLowerThanCurrent_value() {
        val card1 = Card("Q","spades")
        val card2 = Card("K", "hearts")

        Assert.assertEquals(-1, card1.compareWith(card2))
    }

    @Test
    fun cardSameThanCurrent_value() {
        val card1 = Card("K","spades")
        val card2 = Card("K", "hearts")

        Assert.assertEquals(0, card1.compareWith(card2))
    }

    @Test
    fun cardGreaterThanCurrent_suit() {
        val card1 = Card("K","diamonds")
        val card2 = Card("K", "spades")

        Assert.assertEquals(card1.compareSuit(card2), true)
    }

    @Test
    fun cardLowerThanCurrent_suit() {
        val card1 = Card("K","hearts")
        val card2 = Card("K", "clubs")

        Assert.assertEquals(card1.compareSuit(card2), false)
    }
}