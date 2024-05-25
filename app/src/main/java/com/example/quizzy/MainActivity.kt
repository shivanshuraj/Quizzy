package com.example.quizzy

import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var mQuizScore = 0
    private var mQuestionIndex = 0
    private val USER_PROGRESS = 10
    private lateinit var imageView: ImageView
    private lateinit var linearLayout: LinearLayout
    private lateinit var clockAnimation: AnimationDrawable
    private lateinit var bgAnimation: AnimationDrawable
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private val SCORE_KEY = "userScore"
    private val QUESTION_KEY = "currentQuestion"
    private lateinit var quizPb: ProgressBar
    private lateinit var mQuizQuestionTextView: TextView
    private lateinit var mQuizScoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageView = findViewById(R.id.imageView)
        imageView.setBackgroundResource(R.drawable.animation)
        clockAnimation = imageView.background as AnimationDrawable
        linearLayout = findViewById(R.id.main)
        clockAnimation.start()
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        quizPb = findViewById(R.id.quizProgressBar)
        mQuizQuestionTextView = findViewById(R.id.txtQuestion)
        mQuizScoreTextView = findViewById(R.id.txtQuizStats)
        val currentQuestion: QuizModel = questionsCollection[mQuestionIndex]
        mQuizQuestionTextView.setText(currentQuestion.quizQuestion)
        option1.setText(currentQuestion.options[0])
        option2.setText(currentQuestion.options[1])
        option3.setText(currentQuestion.options[2])
        option4.setText(currentQuestion.options[3])
        mQuizScoreTextView.text = mQuizScore.toString() + ""

        option1.setOnClickListener {
            evaluateUserAnswer('A')
            changeQuestionOnButtonClick()
        }
        option2.setOnClickListener {
            evaluateUserAnswer('B')
            changeQuestionOnButtonClick()
        }
        option3.setOnClickListener {
            evaluateUserAnswer('C')
            changeQuestionOnButtonClick()
        }
        option4.setOnClickListener {
            evaluateUserAnswer('D')
            changeQuestionOnButtonClick()
        }


        if (savedInstanceState != null) {
            mQuestionIndex = savedInstanceState.getInt(QUESTION_KEY)
            mQuizScore = savedInstanceState.getInt(SCORE_KEY)
            mQuizQuestionTextView.setText(questionsCollection[mQuestionIndex].quizQuestion)
            mQuizScoreTextView.setText(mQuizScore.toString() + "")
        }
    }

    var questionsCollection: Array<QuizModel> = arrayOf(
        QuizModel(
            R.string.q1, 'B', intArrayOf(R.string.q1a, R.string.q1b, R.string.q1c, R.string.q1d)
        ), QuizModel(
            R.string.q2, 'A', intArrayOf(R.string.q2a, R.string.q2b, R.string.q2c, R.string.q2d)
        ), QuizModel(
            R.string.q3, 'C', intArrayOf(R.string.q3a, R.string.q3b, R.string.q3c, R.string.q3d)
        ), QuizModel(
            R.string.q4, 'D', intArrayOf(R.string.q4a, R.string.q4b, R.string.q4c, R.string.q4d)
        ), QuizModel(
            R.string.q5, 'A', intArrayOf(R.string.q5a, R.string.q5b, R.string.q5c, R.string.q5d)
        ), QuizModel(
            R.string.q6, 'B', intArrayOf(R.string.q6a, R.string.q6b, R.string.q6c, R.string.q6d)
        ), QuizModel(
            R.string.q7, 'A', intArrayOf(R.string.q7a, R.string.q7b, R.string.q7c, R.string.q7d)
        ), QuizModel(
            R.string.q8, 'A', intArrayOf(R.string.q8a, R.string.q8b, R.string.q8c, R.string.q8d)
        ), QuizModel(
            R.string.q9, 'B', intArrayOf(R.string.q9a, R.string.q9b, R.string.q9c, R.string.q9d)
        ), QuizModel(
            R.string.q10,
            'B',
            intArrayOf(R.string.q10a, R.string.q10b, R.string.q10c, R.string.q10d)
        )
    )


    private fun changeQuestionOnButtonClick() {
        mQuestionIndex = (mQuestionIndex + 1) % 10
        if (mQuestionIndex == 0) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Quiz is finished")
            alertDialog.setMessage("Your score is: $mQuizScore")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(
                "Finish the Quiz"
            ) { dialog, which -> finish() }
            alertDialog.show()
        } else {
            val currentQuestion: QuizModel = questionsCollection[mQuestionIndex]
            mQuizQuestionTextView.setText(currentQuestion.quizQuestion)
            option1.setText(currentQuestion.options[0])
            option2.setText(currentQuestion.options[1])
            option3.setText(currentQuestion.options[2])
            option4.setText(currentQuestion.options[3])
            quizPb.visibility = View.VISIBLE
            quizPb.incrementProgressBy(USER_PROGRESS)
        }
    }

    private fun evaluateUserAnswer(userAnswer: Char) {
        val correctAnswer: Char = questionsCollection[mQuestionIndex].isAnswer
        if (userAnswer.code.toChar() == correctAnswer) {
            mQuizScore++
            mQuizScoreTextView!!.text = mQuizScore.toString() + ""
            Toast.makeText(this@MainActivity, "Good job 👌", Toast.LENGTH_SHORT).show()
            linearLayout.setBackgroundResource(R.drawable.bg_correct_anim)
            bgAnimation = linearLayout.background as AnimationDrawable
            bgAnimation.start()
            val mp: MediaPlayer = MediaPlayer.create(this, R.raw.correct)
            mp.start()
        } else {
            Toast.makeText(this@MainActivity, "That's wrong ❌", Toast.LENGTH_SHORT).show()
            linearLayout.setBackgroundResource(R.drawable.bg_wrong_anim)
            bgAnimation = linearLayout.background as AnimationDrawable
            bgAnimation.start()
            val mp2: MediaPlayer = MediaPlayer.create(this, R.raw.wrong)
            mp2.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(QUESTION_KEY, mQuestionIndex)
        outState.putInt(SCORE_KEY, mQuizScore)
    }
}
