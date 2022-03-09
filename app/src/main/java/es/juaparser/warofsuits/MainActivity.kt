package es.juaparser.warofsuits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.juaparser.warofsuits.model.Card

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val card = Card("2","diamonds")


    }
}