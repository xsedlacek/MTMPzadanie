package com.example.mtmpzadanie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class GraphActivity : AppCompatActivity() {

    private lateinit var graphView: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        graphView = findViewById(R.id.graph)

        val coordinates: ArrayList<CoordinateData>? = intent.getParcelableArrayListExtra("coordinates")

        if (coordinates != null) {
            val dataPoints = coordinates.map { DataPoint(it.t, it.y) }.toTypedArray()
            val series = LineGraphSeries(dataPoints)

            graphView.addSeries(series)

            graphView.title = "Trajektória"
            graphView.viewport.isScalable = true
            graphView.viewport.isScrollable = true
        }
    }
}
