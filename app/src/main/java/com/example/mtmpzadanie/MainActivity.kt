package com.example.mtmpzadanie

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.*
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


class MainActivity : AppCompatActivity() {

    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var submitButton: Button
    private lateinit var showGraphButton: Button
    private lateinit var showAnimationButton: Button
    private lateinit var onlineSwitch: Switch

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

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
            handleButtonClick(::onSubmitButtonClicked)
        }

        showGraphButton.setOnClickListener {
            handleButtonClick(::onShowGraphButtonClicked)
        }

        showAnimationButton.setOnClickListener {
            handleButtonClick(::onShowAnimationButtonClicked)
        }

    }

    private fun initializeViews() {
        editText1 = findViewById(R.id.editTextAngle)
        editText2 = findViewById(R.id.editTextSpeed)
        submitButton = findViewById(R.id.buttonCalculate)
        showGraphButton = findViewById(R.id.buttonGraph)
        showAnimationButton = findViewById(R.id.buttonAnimate)
        onlineSwitch = findViewById(R.id.switch2)
    }

    private fun handleButtonClick(action: (List<CoordinateData>?) -> Unit) {
        val angleText = editText1.text.toString()
        val speedText = editText2.text.toString()

        val angle = angleText.toDoubleOrNull()
        val power = speedText.toDoubleOrNull()

        if (angle != null && power != null) {
            if (onlineSwitch.isChecked) {
                lifecycleScope.launch {
                    val result = sendDataToServer(angle, power)
                    action(result)
                }
            } else {
                val localResult = computeDataLocally(angle, power)
                action(localResult)
            }
        } else {
            Toast.makeText(this, "Please enter valid angle and speed", Toast.LENGTH_SHORT).show()
        }

    }


    private fun onSubmitButtonClicked(result: List<CoordinateData>?) {
        if (result != null) {
            val intent = Intent(this, CoordinateDataViewActivity::class.java)
            intent.putParcelableArrayListExtra("coordinates", ArrayList(result))
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to get data from server", Toast.LENGTH_LONG).show()
        }
    }

    private fun onShowGraphButtonClicked(result: List<CoordinateData>?) {
        if (result != null) {
            val intent = Intent(this, GraphActivity::class.java)
            intent.putParcelableArrayListExtra("coordinates", ArrayList(result))
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to get data from server", Toast.LENGTH_LONG).show()
        }
    }

    private fun onShowAnimationButtonClicked(result: List<CoordinateData>?) {
        if (result != null) {
            val intent = Intent(this, AnimationActivity::class.java)
            intent.putParcelableArrayListExtra("coordinates", ArrayList(result))
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to get data from server", Toast.LENGTH_LONG).show()
        }
    }

    private fun computeDataLocally(angle: Double, speed: Double): List<CoordinateData> {
        val localResults = mutableListOf<CoordinateData>()
        var x = 0.0
        var y = 0.0
        var t = 0.0
        val g = 9.81

        val timeStop = (2 * speed * sin(Math.toRadians(angle))) / g
        val timeInc = 0.1

        while (t < timeStop) {
            x = speed * t * cos(Math.toRadians(angle))
            y = speed * t * sin(Math.toRadians(angle)) - (g * t.pow(2)) / 2.0

            localResults.add(CoordinateData(x, y, t))
            t += timeInc
        }

        x = speed * timeStop * cos(Math.toRadians(angle))
        y = 0.0
        localResults.add(CoordinateData(x, y, timeStop))

        return localResults
    }



    private suspend fun sendDataToServer(angle: Double, power: Double): List<CoordinateData>? {
        val inputData = InputData(angle, power)

        val jsonBody = Json.encodeToString(inputData) // No need to use .serializer()
        println("Sending request with body: $jsonBody")

        return try {
            client.post("http://10.0.2.2:3000/compute") {
                contentType(io.ktor.http.ContentType.Application.Json)
                setBody(inputData)
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
