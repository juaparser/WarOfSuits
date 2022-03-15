package es.juaparser.warofsuits

import android.app.AlertDialog
import android.content.DialogInterface
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

/**
 * GameFragment: Fragmento con toda la información del juego.
 */

class GameFragment : Fragment(R.layout.fragment_game) {

    /**
     * Explicación variables:
     *
     * currentDeck: Copia del listado original
     * player1 and player2: Jugadores de la partida, donde se matendrá la información actualizada
     *                      de las cartas de cada jugador.
     *
     * playableCards: LiveData para detectar cuando ambos jugadores han jugado su carta.
     *
     * cardsPlayed: Contador con las cartas jugadas en la partida. Se utiliza para detectar
     *                  cuando se acaba la misma.
     *
     * textDiscardCount y textDeckCount: Textos para ir mostrando las cartas que tiene cada jugador.
     */

    var currentDeck = deck.toMutableList()
    lateinit var player1: Player
    lateinit var player2: Player
    var playableCards = MutableLiveData<MutableList<Card>>(mutableListOf())
    var cardsPlayed = 0

    val textDiscardCount = "Discard count: "
    val textDeckCount = "Deck count: "


    /**
     * onViewCreated: Método principal en el que está toda la lógica del juego.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGameBinding.bind(view)

        /**
         * Llamada al método createGame para iniciar el juego.
         */
        createGame(binding)


        /**
         * Observer para comprobar cuando ambos jugadores hayan jugado una carta. Es decir,
         * se ejecutará el contenido cuando el tamaño de la lista sea 2.
         *
         * En primer lugar se realiza la comparación por valor, y en caso de ser iguales, por palo.
         *
         * Teniendo la carta con mayor valor, se identifica al usuario cuya carta ganadora sea la suya
         * y se indica quién ha ganado la ronda.
         *
         * Para dar un efecto entre ronda y otra, este indicador se muestra durante 3 segundos, de forma
         * que al acabar de la sensación que se ha limpiado la mesa para la siguiente ronda.
         *
         * Para ello, al jugador ganador se le añaden ambas cartas a su pila de descarte y se pone a
         * null su valor de carta en juego.
         *
         * En el caso de que sea la última ronda, tras indicar quién ha ganado en la misma, la
         * pantalla se indica el fin de la partida y quién de los dos
         * jugadores ha ganado.
         *
         * Se ha puesto el fondo translúcido para que ambos jugadores puedan comprobar los resultados
         * finales, así como un botón por si quieren reiniciar la partida.
         */

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


        /**
         * Se llama a binding.apply para indicar a la vista como debe comportarse cuando se pulsa
         * en los distintos botones o imágenes.
         */

        binding.apply {
                start.setOnClickListener {
                        groupOverlayStart.visibility = View.GONE
                    tvPlayer1Counter.visibility = View.VISIBLE
                    tvPlayer2Counter.visibility = View.VISIBLE
                    tvPlayer2Discard.visibility = View.VISIBLE
                    tvPlayer1Discard.visibility = View.VISIBLE
                    info.visibility = View.VISIBLE
                }

            restart.setOnClickListener {
                groupOverlayRestart.visibility = View.GONE
                createGame(this)
            }

            info.setOnClickListener {
                val dialog = AlertDialog.Builder(requireContext())

                dialog.setTitle("Suits preference")
                var suitString = ""

                for (s in suits) {
                    suitString += (suits.indexOf(s)+1).toString() + ". $s \n"
                }
                dialog.setMessage("Order of suits by value : \n\n" + suitString)

                dialog.setPositiveButton("Ok", DialogInterface.OnClickListener { _, _ ->  })

                dialog.show()
            }

            tvPlayer1.text = player1.name
            tvPlayer2.text = player2.name

            discardDeckPlayer1.visibility = if(player1.discardDeck.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            discardDeckPlayer2.visibility = if(player2.discardDeck.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            deckPlayer1.visibility = if(player1.deck.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            deckPlayer2.visibility = if(player2.deck.isNotEmpty()) View.VISIBLE else View.INVISIBLE


            /**
             * Al pulsar cada jugador en su deck, se "saca" la primera carta de este y se muestra al usuario.
             * Esta carta se indica como la actual en juego para el jugador y se añade a la lista de cartas en
             * juego para su posterior prcesado.
             */

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


    /**
     * createGame(binding): Función para iniciar o reiniciar el juego.
     *
     * Restaura el valor de las variables principales usadas en el juego, y reinicia las cartas
     * y los contadores de los jugadores así como los elementos de la vista.
     */
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