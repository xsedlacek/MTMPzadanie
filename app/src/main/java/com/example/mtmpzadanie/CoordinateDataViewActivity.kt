package com.example.mtmpzadanie

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CoordinateDataViewActivity : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinate_data_view)

        tableLayout = findViewById(R.id.tableLayout)

        createTableHeader()

        val coordinateList: ArrayList<CoordinateData>? = intent.getParcelableArrayListExtra("coordinates")
        if (coordinateList != null) {
            populateTable(coordinateList)
        }
    }

    private fun populateTable(coordinateList: ArrayList<CoordinateData>) {
        for (coordinate in coordinateList) {
            val tableRow = TableRow(this)

            val xTextView = createTableCell(String.format("%.2f", coordinate.x))
            val yTextView = createTableCell(String.format("%.2f", coordinate.y))
            val tTextView = createTableCell(String.format("%.2f", coordinate.t))

            tableRow.addView(xTextView)
            tableRow.addView(yTextView)
            tableRow.addView(tTextView)

            tableLayout.addView(tableRow)
        }
    }

    private fun createTableCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            setPadding(16, 16, 16, 16)
            setBackgroundColor(Color.LTGRAY)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            val padding = 4
            setPadding(padding, padding, padding, padding)
        }
    }

    private fun createTableHeader() {
        val headerRow = TableRow(this)

        val xHeader = createHeaderCell("X - súradnica")
        val yHeader = createHeaderCell("Y - súradnica")
        val tHeader = createHeaderCell("Čas")

        headerRow.addView(xHeader)
        headerRow.addView(yHeader)
        headerRow.addView(tHeader)

        tableLayout.addView(headerRow)
    }

    private fun createHeaderCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
            setPadding(16, 16, 16, 16)
            setBackgroundResource(R.drawable.table_cell_background)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            gravity = Gravity.CENTER
            setTextColor(Color.BLACK)
            textSize = 18f
        }
    }

}
