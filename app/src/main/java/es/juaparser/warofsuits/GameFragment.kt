package es.juaparser.warofsuits

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import es.juaparser.warofsuits.databinding.FragmentGameBinding
import es.juaparser.warofsuits.model.Player
import es.juaparser.warofsuits.model.getDrawable


class GameFragment : Fragment(R.layout.fragment_game) {

    var currentDeck = deck.toMutableList()
    lateinit var player1: Player
    lateinit var player2: Player

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGameBinding.bind(view)

        generateDeck()

        currentDeck = deck.toMutableList()

        currentDeck.shuffle()

        Log.d("JPS","CURRENDECK SUFFLED " + currentDeck)

        val player1deck = currentDeck.subList(0, (currentDeck.size/2)).toMutableList()
        val player2deck = currentDeck.subList(currentDeck.size/2, currentDeck.size)

        Log.d("JPS", "Player 1 deck: " + player1deck.size)
        Log.d("JPS", "Player 2 deck: " + player2deck.size)


        player1 = Player(1, "Magneto", null ,player1deck, mutableListOf())

        player2 = Player(2, "Charles Xavier", null,  player2deck, mutableListOf())


        binding.apply {
            start.setOnClickListener {
                groupOverlayStart.visibility = View.GONE
            }

            deckPlayer1.setOnClickListener {
                if(player1.playableCard == null) {
                    val newCard = player1.deck.first()
                    player1.playableCard = newCard
                    playableCardPlayer1.setBackgroundResource(newCard.getDrawable())
                    playableCardPlayer1.visibility = View.VISIBLE
                    player1.deck.removeFirst()
                }
            }

            deckPlayer2.setOnClickListener {
                if(player2.playableCard == null) {
                    val newCard = player2.deck.first()
                    player2.playableCard = newCard
                    playableCardPlayer2.setBackgroundResource(newCard.getDrawable())
                    playableCardPlayer2.visibility = View.VISIBLE
                    player2.deck.removeFirst()
                }
            }
        }

    }


}