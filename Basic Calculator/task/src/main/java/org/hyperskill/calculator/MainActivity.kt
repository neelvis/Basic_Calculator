package org.hyperskill.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.hyperskill.calculator.R
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.roundToInt

fun Double.roundedToString() =
    if ((this - floor(this)).absoluteValue < Double.MIN_VALUE)
        this.roundToInt().toString()
    else
        this.toString()


class MainActivity : AppCompatActivity() {
    private enum class OP {ADD, SUB, DIV, MUL, NULL}
    private var operation = OP.NULL
    private var a: Double = Double.NaN
    private var b: Double = Double.NaN

    @SuppressLint("SetTextI18n")
    private fun addSymbol(c: Char) {
        val currentInput = editText.text.toString()
        editText.text.append(
            when (c) {
                '.' -> when {
                    currentInput.isEmpty() -> "0."
                    !currentInput.contains('.') -> "."
                    else -> ""
                }
                '0' -> if (currentInput != "0") "0" else ""
                in '1'..'9', '-' -> "$c"
                else -> "Type error"
            })
    }

    private fun setOperation(operation: OP) {
        if (b.isNaN()) {
            a = if (editText.text.isEmpty()) 0.0 else editText.text.toString().toDouble()
            this.operation = operation
            editText.text.clear()
            editText.hint = a.roundedToString()
        }
    }
    private fun calculate() {
        if (a.isNaN()) return
        b = if (editText.text.isEmpty()) 0.0 else editText.text.toString().toDouble()
        when (operation) {
            OP.ADD -> a += b
            OP.SUB -> a -= b
            OP.MUL -> a *= b
            OP.DIV -> a /= b
        }
        b = Double.NaN
        editText.setText(a.roundedToString())
        editText.hint = a.roundedToString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..9) {
            this.findViewById<Button>(resources.getIdentifier("button$i", "id", packageName))
                .setOnClickListener {
                    addSymbol(i.toString().first())
                }
        }
        dotButton.setOnClickListener {
            addSymbol('.')
        }
        clearButton.setOnClickListener {
            editText.text.clear()
        }
        subtractButton.setOnClickListener {
            if (editText.text.isEmpty()) addSymbol('-')
            else setOperation(OP.SUB)
        }
        addButton.setOnClickListener {
            setOperation(OP.ADD)
        }
        multiplyButton.setOnClickListener {
            setOperation(OP.MUL)
        }
        divideButton.setOnClickListener {
            setOperation(OP.DIV)
        }
        equalButton.setOnClickListener {
            calculate()
        }
    }
}