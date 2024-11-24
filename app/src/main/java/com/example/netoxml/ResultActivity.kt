package com.example.netoxml

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.netoxml.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ResultActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private lateinit var netoMensual: TextView
    private lateinit var netoAnual: TextView
    private lateinit var irpf: TextView
    private lateinit var deducciones: TextView

    private var bundleMain: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //recupero el bundle traido desde la primera vista
        bundleMain = intent.extras?.getBundle("datos")
        initComponents()
        initListeners()
        initUi(bundleMain)
    }

    //se le pasa el bundle para poder trabajar con los datos dentro de la funcion
    private fun initUi(bundleMain: Bundle?) {
        //creo un usuario asignandole el bundle
        val userRec = bundleMain?.getSerializable("user") as? User
        //si el usuario no esta vacio comienzo a trabajar con los datos
        if (userRec != null) {
            //procesamiento de datos
            val dedHijos = calcHijos(userRec.numHijos.toIntOrNull())
            val dedDisc = calcDisc(userRec.discapacidad.toString())
            val dedT = dedHijos + dedDisc
            val dedSs = calcSs(userRec.salario.toDoubleOrNull(), userRec.grupo.toString())
            val calcIrpf = calcIrpf(userRec.salario.toDoubleOrNull(), dedSs, dedT)
            val resIrpf = userRec.salario.toDoubleOrNull()?.minus(dedSs)?.times(calcIrpf / 100)?.minus(dedT)
            val netoA = userRec.salario.toDoubleOrNull()?.minus(dedT)?.minus(resIrpf!!)
            //asignacio de datos a los campos de la vista
            netoMensual.text = userRec.pagas.toIntOrNull()?.let { netoA?.div(it).toString() } ?: "0"
            netoAnual.text = netoA?.toString() ?: "0"
            irpf.text = resIrpf?.toString() ?: "0"
            deducciones.text = dedT.toString()
        } else {
            //si el usuario esta vacio, muestro "0" en cada campo
            netoAnual.text = "0"
            netoMensual.text = "0"
            irpf.text = "0"
            deducciones.text = "0"
        }
    }
    //el unico boton es el fab, que te devuelve a la vista principal
    fun initListeners() {
        fab.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    //se inician los 4 campos de entrara + el boton flotante
    fun initComponents() {
        fab = findViewById(R.id.floating_action_button)
        netoMensual = findViewById(R.id.neto_mensual)
        netoAnual = findViewById(R.id.neto_anual)
        irpf = findViewById(R.id.irpf)
        deducciones = findViewById(R.id.deducciones)
    }

    //--------------> LAS CUENTAS NO SON EXACTAS <-------------
    fun calcSs(salario: Double?, contrato: String?): Double {
        if (salario == null) return 0.0
        return when (contrato) {
            "General" -> salario * 0.0635
            "Inferior a un año" -> salario * 0.064
            else -> 0.0
        }
    }
    fun calcIrpf(salario: Double?, deduccionSSSal: Double, deduccion: Int): Int {
        if (salario == null) return 0
        val resultado = salario - deduccionSSSal - deduccion
        return when (resultado) {
            in 0.0..12449.9 -> 0
            in 12450.0..20199.0 -> 19
            in 20200.0..35199.0 -> 24
            in 35200.0..59999.0 -> 30
            in 60000.0..299999.0 -> 37
            in 300000.0..Double.MAX_VALUE -> 45
            else -> 0
        }
    }
    fun calcHijos(hijos: Int?): Int {
        val deduccion: Int
        when (hijos) {
            0 -> {
                deduccion = 0
                return deduccion
            }
            1 -> {
                deduccion = 2400
                return deduccion
            }
            2 -> {
                deduccion = 2700 + 2400
                return deduccion
            }
            3 -> {
                deduccion = 4000 + 2700 + 2400
                return deduccion
            }
            else -> {
                deduccion = 4500 + 4000 + 2700 + 2400
                return deduccion
            }
        }
    }
    fun calcDisc(discapacidad: String?): Int {
        val deduccion: Int
        when (discapacidad) {
            "Sin discapacidad" -> {
                deduccion = 0
                return deduccion
            }
            "Mayor o igual al 65%" -> {
                deduccion = 9000
                return deduccion
            }
            "Menor del 65% (sin asistencia)" -> {
                deduccion = 3000
                return deduccion
            }
            "Menor del 65% (con asistencia)" -> {
                deduccion = 9000
                return deduccion
            }
        }
        return 0
    }
}