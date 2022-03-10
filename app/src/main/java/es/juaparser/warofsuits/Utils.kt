package es.juaparser.warofsuits

import android.util.Log
import es.juaparser.warofsuits.model.Card

    val suits = mutableListOf("diamonds", "clubs", "hearts", "spades")
    val top = mutableListOf("J", "Q", "K", "A")

    val deck = mutableListOf<Card>()

    fun generateDeck() {
        for(s in suits) {
            for(i in 2..10) {
                deck.add(Card(i.toString(), s))
            }
            for(t in top) {
                deck.add(Card(t,s))
            }
        }
        Log.d("JPS", "Deck generated with ${deck.size} cards: " + deck)
    }
