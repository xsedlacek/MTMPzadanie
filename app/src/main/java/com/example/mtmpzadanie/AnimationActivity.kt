package com.example.mtmpzadanie

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

class AnimationActivity : AppCompatActivity() {

    private lateinit var surfaceView: SurfaceView
    private lateinit var paint: Paint
    private var coordinates: ArrayList<CoordinateData>? = null
    private var ballBitmap: Bitmap? = null
    private var ballX = 0f
    private var ballY = 0f
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        surfaceView = findViewById(R.id.surfaceView)
        paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
        }

        ballBitmap = BitmapFactory.decodeResource(resources, R.drawable.lopticka)

        coordinates = intent.getParcelableArrayListExtra("coordinates")

        thread { drawThrowAnimation() }
    }

    private fun drawThrowAnimation() {
        val holder: SurfaceHolder = surfaceView.holder
        while (true) {
            val canvas: Canvas? = holder.lockCanvas()
            canvas?.let {
                it.drawColor(Color.WHITE)

                animateBall(it)

                holder.unlockCanvasAndPost(it)
            }
            Thread.sleep(16)
        }
    }

    private fun animateBall(canvas: Canvas) {
        if (coordinates != null && currentIndex < coordinates!!.size) {
            val coord = coordinates!![currentIndex]
            ballX = coord.t.toFloat() * 20
            ballY = canvas.height - (coord.y.toFloat() * 20)

            canvas.drawBitmap(ballBitmap!!, ballX - ballBitmap!!.width / 2, ballY - ballBitmap!!.height / 2, paint)

            if (currentIndex < coordinates!!.size - 1) {
                currentIndex++
            }
        }
    }
}
