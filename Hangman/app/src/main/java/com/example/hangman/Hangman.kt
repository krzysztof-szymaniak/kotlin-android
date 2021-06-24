package com.example.hangman

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.hangman.databinding.ActivityHangmanBinding

class Hangman : AppCompatActivity() {
    private lateinit var binding: ActivityHangmanBinding
    private var keyword = ""
    private var misses = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHangmanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        keyword = resources.getStringArray(R.array.words_dictionary).random()
        binding.textView.text = "-".repeat(keyword.length)
    }

    fun make_guess(view: View) {
        val button = view as Button
        button.visibility = View.INVISIBLE
        val letter = button.text.single()
        if (keyword.contains(letter)) {
            val list = findOccurances(keyword, letter)
            val sb = StringBuilder(binding.textView.text)
            for (i in list) {
                sb.setCharAt(i, letter)
            }
            binding.textView.text = sb.toString()
            if (!binding.textView.text.contains("-"))
                end_dialog(won = true)
        } else {
            if (misses < 10)
                misses += 1

            val resId = this.resources.getIdentifier("h$misses", "drawable", this.packageName)
            binding.imageView.setImageResource(resId)
            if (misses == 10)
                end_dialog(won = false)
        }

    }

    private fun end_dialog(won: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Koniec Gry!")
        val message = if (won) {
            "Wygrana"
        } else {
            "Przegrana!\nSłowo to $keyword"
        }
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which -> finish() }
        builder.show()
    }

    fun findOccurances(word: String, letter: Char): List<Int> {
        var list: List<Int> = emptyList()
        for (i in word.indices) {
            if (word[i] == letter)
                list += i
        }
        return list
    }
}