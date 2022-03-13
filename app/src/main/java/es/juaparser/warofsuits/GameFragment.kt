package es.juaparser.warofsuits

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import es.juaparser.warofsuits.databinding.FragmentGameBinding
import es.juaparser.warofsuits.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


class GameFragment : Fragment(R.layout.fragment_game) {

    var currentDeck = deck.toMutableList()
    lateinit var player1: Player
    lateinit var player2: Player
    var playableCards = MutableLiveData<MutableList<Card>>(mutableListOf())
    var cardsPlayed = 0

    val textDiscardCount = "Discard count: "
    val textDeckCount = "Deck count: "

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGameBinding.bind(view)

        createGame(binding)

        playableCards.observe(viewLifecycleOwner) {
            if(it.size == 2) {
                cardsPlayed += 2
                var result = it.first().compareWith(it.last())
                var compareSuit = if(result == 0) {
                    it.first().compareSuit(it.last())
                } else {
                    result == 1
                }
                var playerwin = if(compareSuit) {
                    if (player1.playableCard == it.first()) player1 else player2
                } else {
                    if (player1.playableCard == it.last()) player1 else player2
                }

                    binding.tvWinner.visibility = View.VISIBLE
                    binding.tvWinner.text = playerwin.name + " WON THIS ROUND"

                    Handler().postDelayed({

                        if(cardsPlayed < 52) {

                            playableCards.value = mutableListOf()
                        playerwin.discardDeck.add(player1.playableCard!!)
                        playerwin.discardDeck.add(player2.playableCard!!)

                        if(playerwin.id == 1) {
                            binding.discardDeckPlayer1.visibility = View.VISIBLE
                            binding.tvPlayer1Discard.text = textDiscardCount + playerwin.discardDeck.size
                        }
                        else{
                            binding.discardDeckPlayer2.visibility = View.VISIBLE
                            binding.tvPlayer2Discard.text = textDiscardCount + playerwin.discardDeck.size
                        }
                        player1.playableCard = null
                        player2.playableCard = null
                        binding.playableCardPlayer1.visibility = View.INVISIBLE
                        binding.playableCardPlayer2.visibility = View.INVISIBLE
                        binding.tvWinner.visibility = View.INVISIBLE
                        } else {
                            Log.d("JPS", "ELSE CARDS PLAYED")
                            binding.tvWinner.visibility = View.VISIBLE
                            var playerfinish = if(player1.discardDeck.size > player2.discardDeck.size) player1 else player2
                            binding.tvWinner.text = playerfinish.name + " WON"
                            binding.groupOverlayRestart.visibility = View.VISIBLE
                        }
                    }, 3000)

            }
        }


        binding.apply {
                start.setOnClickListener {
                        groupOverlayStart.visibility = View.GONE
                    tvPlayer1Counter.visibility = View.VISIBLE
                    tvPlayer2Counter.visibility = View.VISIBLE
                    tvPlayer2Discard.visibility = View.VISIBLE
                    tvPlayer1Discard.visibility = View.VISIBLE
                }

            restart.setOnClickListener {
                groupOverlayRestart.visibility = View.GONE
                createGame(this)
            }

            tvPlayer1.text = player1.name
            tvPlayer2.text = player2.name

            discardDeckPlayer1.visibility = if(player1.discardDeck.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            discardDeckPlayer2.visibility = if(player2.discardDeck.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            deckPlayer1.visibility = if(player1.deck.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            deckPlayer2.visibility = if(player2.deck.isNotEmpty()) View.VISIBLE else View.INVISIBLE

            deckPlayer1.setOnClickListener {
                Log.d("JPS", "DECKPLAYER1 CLICK " + player1.playableCard + " y " + player1.deck.size)
                if(player1.playableCard == null && player1.deck.isNotEmpty()) {
                    val newCard = player1.deck.first()
                    player1.playableCard = newCard
                    playableCardPlayer1.setBackgroundResource(newCard.getDrawable())
                    playableCardPlayer1.visibility = View.VISIBLE

                    deckPlayer1.visibility = if(player1.deck.size == 1) View.INVISIBLE else View.VISIBLE

                    val playableCardList = playableCards.value
                    playableCardList?.add(newCard)
                    playableCards.value = playableCardList!!

                    player1.deck.removeFirst()

                    tvPlayer1Counter.text = textDeckCount + player1.deck.size
                }
            }

            deckPlayer2.setOnClickListener {
                if(player2.playableCard == null && player2.deck.isNotEmpty()) {
                    val newCard = player2.deck.first()
                    player2.playableCard = newCard
                    playableCardPlayer2.setBackgroundResource(newCard.getDrawable())
                    playableCardPlayer2.visibility = View.VISIBLE

                    deckPlayer2.visibility = if(player1.deck.size == 1) View.INVISIBLE else View.VISIBLE

                    val playableCardList = playableCards.value
                    playableCardList?.add(newCard)
                    playableCards.value = playableCardList!!

                    player2.deck.removeFirst()
                    tvPlayer2Counter.text = textDeckCount + player2.deck.size
                }
            }
        }

    }

    fun createGame(binding: FragmentGameBinding) {
        currentDeck.clear()
        playableCards.value?.clear()
        cardsPlayed = 0

        if(deck.isEmpty()) generateDeck()

        suits.shuffle()

        currentDeck = deck.toMutableList()

        currentDeck.shuffle()

        Log.d("JPS","CURRENDECK SUFFLED " + currentDeck.size)

        val player1deck = currentDeck.subList(0, (currentDeck.size/2)).toMutableList()
        val player2deck = currentDeck.subList(currentDeck.size/2, currentDeck.size)

        Log.d("JPS", "Player 1 deck: " + player1deck.size)
        Log.d("JPS", "Player 2 deck: " + player2deck.size)


        player1 = Player(1, "Magneto", null ,player1deck, mutableListOf())

        player2 = Player(2, "Charles Xavier", null,  player2deck, mutableListOf())

        binding.deckPlayer1.visibility = View.VISIBLE
        binding.deckPlayer2.visibility = View.VISIBLE
        binding.discardDeckPlayer1.visibility = View.INVISIBLE
        binding.discardDeckPlayer2.visibility = View.INVISIBLE
        binding.playableCardPlayer1.visibility = View.INVISIBLE
        binding.playableCardPlayer2.visibility = View.INVISIBLE

        binding.tvPlayer1Counter.text = textDeckCount + player1.deck.size
        binding.tvPlayer2Counter.text = textDeckCount + player2.deck.size

        binding.tvPlayer1Discard.text = textDiscardCount + player1.discardDeck.size
        binding.tvPlayer2Discard.text = textDiscardCount + player2.discardDeck.size
        binding.tvWinner.text = ""
        binding.tvWinner.visibility = View.INVISIBLE
    }


}