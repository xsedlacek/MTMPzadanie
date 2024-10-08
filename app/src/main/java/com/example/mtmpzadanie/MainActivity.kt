package com.example.mtmpzadanie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class MainActivity : AppCompatActivity() {

    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var submitButton: Button
    private lateinit var showGraphButton: Button
    private lateinit var showAnimationButton: Button


    val xCoords = mutableListOf<Double>()
    val yCoords = mutableListOf<Double>()
    val tCoords = mutableListOf<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()

        submitButton.setOnClickListener {
            handleButtonClick()
        }

        showGraphButton.setOnClickListener {
            showGraph()
        }

        showAnimationButton.setOnClickListener {
            showAnimation()
        }

    }

    private fun initializeViews() {
        editText1 = findViewById(R.id.editTextAngle)
        editText2 = findViewById(R.id.editTextSpeed)
        submitButton = findViewById(R.id.buttonCalculate)
        showGraphButton = findViewById(R.id.buttonGraph)
        showAnimationButton = findViewById(R.id.buttonAnimate)
    }

    private fun calculate(): ArrayList<CoordinateData>? {
        val angle = getTextFromEditText(editText1)
        val speed = getTextFromEditText(editText2)
        xCoords.clear()
        yCoords.clear()
        tCoords.clear()
        if (angle != null || speed != null){
            computeDataLocally(angle.toDouble(),speed.toDouble())

            return ArrayList(xCoords.indices.map { index ->
                CoordinateData(xCoords[index], yCoords[index], tCoords[index])
            })

            Toast.makeText(this, "Data Computed",Toast.LENGTH_LONG).show()
        }

        Toast.makeText(this, "Invalid Data",Toast.LENGTH_LONG).show()
        return null
    }

    private fun handleButtonClick() {

        val intent = Intent(this, CoordinateDataViewActivity::class.java)
        intent.putParcelableArrayListExtra("coordinates", calculate())
        startActivity(intent)
    }

    private fun showGraph(){

        val intent = Intent(this, GraphActivity::class.java)
        intent.putParcelableArrayListExtra("coordinates", calculate())
        startActivity(intent)
    }

    private fun showAnimation(){
        val intent = Intent(this, AnimationActivity::class.java)
        intent.putParcelableArrayListExtra("coordinates", calculate())
        startActivity(intent)
    }

    private fun getTextFromEditText(editText: EditText): String {
        return editText.text.toString()
    }

    private fun computeDataLocally(angle: Double, speed: Double) {
        var x = 0.0
        var y = 0.0
        var t = 0.0
        val g = 9.81

        val timeStop = (2 * speed * sin(Math.toRadians(angle))) / g

        val timeInc = 0.1

        while (t < timeStop) {
            x = speed * t * cos(Math.toRadians(angle))
            y = speed * t * sin(Math.toRadians(angle)) - (g * t.pow(2)) / 2.0

            xCoords.add(x)
            yCoords.add(y)
            tCoords.add(t)

            t += timeInc
        }

        x = speed * timeStop * cos(Math.toRadians(angle))
        y = 0.0

        xCoords.add(x)
        yCoords.add(y)
        tCoords.add(timeStop)
    }


}