package com.example.historyapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuestionsActivity : AppCompatActivity() {

    private var score = 0
    private var questionIndex = 0
    private lateinit var countDownTimer: CountDownTimer

    private val timePerQuestionMillis = 30_000L

    private val questions = listOf(
        Triple("Which ancient civilization built the Machu Picchu complex in Peru?",
            listOf("Inca", "Maya", "Aztec", "Olmec"),
            "Nelson Mandela"),
        Triple("What year did India gain independence from British rule?",
            listOf("1920", "1945", "1947", "1950"),
            "1947"),
        Triple("Who was the British Prime Minister during most of World War II?",
            listOf("Winston Churchill", "Tony Blair", "Margaret Thatcher", "Neville Chamberlain"),
            "Winston Churchill"),
        Triple("Which empire built the Colosseum?",
            listOf("Roman Empire", "Greek Empire", "Ottoman Empire", "British Empire"),
            "Roman Empire"),
        Triple("What was the name of the ship on which the Pilgrims traveled to North America in 1620?",
            listOf("The Beagle", "Santa Maria", "Mayflower", "HMS Victory"),
            "Mayflower")
    )

    private lateinit var questionText: TextView
    private lateinit var optionA: Button
    private lateinit var optionB: Button
    private lateinit var optionC: Button
    private lateinit var optionD: Button
    private lateinit var nextButton: Button
    private lateinit var timerText: TextView

    private var selectedAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        questionText = findViewById(R.id.questionText)
        optionA = findViewById(R.id.answerA)
        optionB = findViewById(R.id.answerB)
        optionC = findViewById(R.id.answerC)
        optionD = findViewById(R.id.answerD)
        nextButton = findViewById(R.id.nextButton)
        timerText = findViewById(R.id.timeText)

        // Set up answer buttons
        listOf(optionA, optionB, optionC, optionD).forEach { btn ->
            btn.setOnClickListener {
                countDownTimer.cancel()
                selectedAnswer = btn.text.toString()
                nextButton.isEnabled = true
                btn.setBackgroundColor(0xFFAAF0D1.toInt()) // highlight selected
            }
        }

        // Next button
        nextButton.setOnClickListener {
            countDownTimer.cancel()
            checkAnswerAndProceed()
        }

        // Load first question
        loadQuestion()
    }

    private fun loadQuestion() {
        // Reset UI
        listOf(optionA, optionB, optionC, optionD).forEach {
            it.setBackgroundColor(0xFFDDDDDD.toInt())
        }
        nextButton.isEnabled = false
        selectedAnswer = null

        // Display question and options
        val (qText, opts, _) = questions[questionIndex]
        questionText.text = qText
        optionA.text = opts[0]
        optionB.text = opts[1]
        optionC.text = opts[2]
        optionD.text = opts[3]

        // Start timer for this question
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timePerQuestionMillis, 1_000) {
            override fun onTick(ms: Long) {
                timerText.text = "Time left: ${ms / 1000}s"
            }
            override fun onFinish() {
                timerText.text = "Time's up!"
                checkAnswerAndProceed()
            }
        }.start()
    }

    private fun checkAnswerAndProceed() {
        // Score it
        if (selectedAnswer == questions[questionIndex].third) {
            score++
        }

        // Move to next
        questionIndex++
        if (questionIndex < questions.size) {
            loadQuestion()
        } else {
            // Quiz over
            Intent(this, Score::class.java).also {
                it.putExtra("FINAL_SCORE", score)
                it.putExtra("TOTAL_QUESTIONS", questions.size)
                startActivity(it)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::countDownTimer.isInitialized) countDownTimer.cancel()
    }
}
