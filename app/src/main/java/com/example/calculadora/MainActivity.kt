package com.example.calculadora

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    // Variables para realizar cálculos
    private var num1: Double? = null
    private var num2: Double? = null
    private var operator: String = ""
    private lateinit var txtCounter: TextView
    private lateinit var txtCounter2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar los TextViews
        txtCounter = findViewById(R.id.txt_counter)
        txtCounter2 = findViewById(R.id.txt_counter2)

        // Inicializar los botones
        val btnC: Button = findViewById(R.id.btnC)
        val btnCA: Button = findViewById(R.id.btnCA)
        val btnDiv: Button = findViewById(R.id.btndiv)
        val btn7: Button = findViewById(R.id.btn7)
        val btn8: Button = findViewById(R.id.btn8)
        val btn9: Button = findViewById(R.id.btn9)
        val btnMult: Button = findViewById(R.id.btnmult)
        val btn4: Button = findViewById(R.id.btn4)
        val btn5: Button = findViewById(R.id.btn5)
        val btn6: Button = findViewById(R.id.btn6)
        val btnRest: Button = findViewById(R.id.btnrest)
        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btnSum: Button = findViewById(R.id.btnsum)
        val btnMM: Button = findViewById(R.id.btnmm)
        val btn0: Button = findViewById(R.id.btn0)
        val btnDot: Button = findViewById(R.id.btnpun)
        val btnEqual: Button = findViewById(R.id.btnigual)
        val btnPor: Button = findViewById(R.id.btnPor)

        // Definir OnClickListener para los botones
        btnC.setOnClickListener { clear() }
        btnCA.setOnClickListener { clearone() }
        btnPor.setOnClickListener { setOperator("%") }
        btnDiv.setOnClickListener { setOperator("/") }
        btn7.setOnClickListener { appendNumber("7") }
        btn8.setOnClickListener { appendNumber("8") }
        btn9.setOnClickListener { appendNumber("9") }
        btnMult.setOnClickListener { setOperator("*") }
        btn4.setOnClickListener { appendNumber("4") }
        btn5.setOnClickListener { appendNumber("5") }
        btn6.setOnClickListener { appendNumber("6") }
        btnRest.setOnClickListener { setOperator("-") }
        btn1.setOnClickListener { appendNumber("1") }
        btn2.setOnClickListener { appendNumber("2") }
        btn3.setOnClickListener { appendNumber("3") }
        btnSum.setOnClickListener { setOperator("+") }
        btnMM.setOnClickListener { changeSign() }
        btn0.setOnClickListener { appendNumber("0") }
        btnDot.setOnClickListener { appendDecimal() }
        btnEqual.setOnClickListener { calculate() }
    }


    // Método para limpiar los TextViews y las variables
    private fun clear() {
        txtCounter.text = "0"
        txtCounter2.text = "0"
        num1 = null
        num2 = null
        operator = ""
    }

    // Método para limpiar el TextView de ingreso y las variables relacionadas
    private fun clearone() {
        txtCounter2.text = "0"
        num2 = null
    }

    // Método para añadir números al TextView de ingreso
    private fun appendNumber(number: String) {
        val currentText = txtCounter2.text.toString()
        if (currentText.length < 10) { // Verificar la longitud del texto actual
            if (currentText == "0") {
                txtCounter2.text = number
            } else {
                txtCounter2.append(number)
            }
        } else {
            Toast.makeText(this, "Máximo 10 dígitos permitidos", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para añadir el punto decimal al TextView de ingreso
    private fun appendDecimal() {
        val currentText = txtCounter2.text.toString()
        if (!currentText.contains(".")) {
            txtCounter2.append(".")
        }
    }

    // Método para cambiar el signo del número en el TextView de ingreso
    private fun changeSign() {
        val currentText = txtCounter2.text.toString()
        if (currentText != "0") {
            val value = currentText.toDouble()
            txtCounter2.text = (-value).toString()
        }
    }

    // Método para establecer el operador y calcular el resultado intermedio
    private fun setOperator(op: String) {
        if (num1 == null) {
            num1 = txtCounter2.text.toString().toDouble()
        } else {
            num2 = txtCounter2.text.toString().toDouble()
            calculateIntermediateResult()
        }
        operator = op
        txtCounter2.text = "0"
    }

    // Método para calcular el resultado intermedio
    private fun calculateIntermediateResult() {
        num2?.let {
            when (operator) {
                "+" -> num1 = num1?.plus(it)
                "-" -> num1 = num1?.minus(it)
                "*" -> num1 = num1?.times(it)
                "/" -> num1 = num1?.div(it)
                "%" -> num1 = num1!! * (num2!! / 100.0)
            }
            val formattedResult = formatNumber(num1)
            txtCounter.text = formattedResult
            num2 = null
        }
    }

    // Método para calcular el porcentaje
    private fun percentage() {
        val value = txtCounter2.text.toString().toDouble()
        if (num1 != null) {
            val percent = num1!! * (value / 100.0)
            txtCounter2.text = percent.toString()
        }
    }

    // Método para realizar el cálculo final
    private fun calculate() {
        num2 = txtCounter2.text.toString().toDouble()
        calculateIntermediateResult()
        val formattedResult = formatNumber(num1)
        txtCounter.text = formattedResult
        txtCounter2.text = "0"
        num1 = null
        num2 = null
        operator = ""
    }

    // Método para formatear el número
    private fun formatNumber(number: Double?): String {
        return if (number != null) {
            if (number.absoluteValue >= 100000000) {
                String.format(Locale.getDefault(), "%.2e", number)
            } else {
                val formatter: NumberFormat = DecimalFormat("#,###.########")
                formatter.format(number)
            }
        } else {
            ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show -> {
                startActivity(Intent(this, DevData::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}