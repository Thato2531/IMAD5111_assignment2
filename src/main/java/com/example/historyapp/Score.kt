package com.example.historyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Score : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Retrieve score data
        val score = intent.getIntExtra("FINAL_SCORE", 0)
        val total = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val percent = if (total > 0) (score * 100) / total else 0

        // UI references
        val scoreText = findViewById<TextView>(R.id.scoresObtained)
        val starsText = findViewById<TextView>(R.id.starsText)
        val feedbackText = findViewById<TextView>(R.id.scoreText)
        val progressBar = findViewById<ProgressBar>(R.id.scoreProgressBar)
        val shareButton = findViewById<Button>(R.id.shareButton)
        val retryButton = findViewById<Button>(R.id.restartButton)

        // Display numeric score
        scoreText.text = "You scored $score out of $total"

        // Update progress bar
        progressBar.max = 100
        progressBar.progress = percent

        // Simple star rating using Unicode ★☆☆
        val starCount = when {
            percent >= 80 -> 3
            percent >= 50 -> 2
            percent >= 30 -> 1
            else           -> 0
        }
        val stars = "★".repeat(starCount) + "☆".repeat(3 - starCount)
        starsText.text = stars

        // Personalized feedback
        val feedback = when {
            percent >= 80 -> "Excellent! "
            percent >= 50 -> "Good job! "
            percent >= 30 -> "Not quite bad, try again"
            else          -> "Try again "
        }
        feedbackText.text = feedback

        // Share functionality
        shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "I scored $score/$total on the History Quiz!")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        // Retry quiz
        retryButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
