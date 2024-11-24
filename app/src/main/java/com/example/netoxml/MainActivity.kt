package com.example.netoxml

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.netoxml.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    // variables desde la vista
    private lateinit var brutoAnual: TextInputEditText
    private lateinit var edad: TextInputEditText
    private lateinit var pagas: TextInputEditText
    private lateinit var radioDiscapacidad: RadioGroup
    private lateinit var discapacidad: TextInputLayout
    private lateinit var discapacidad2: AutoCompleteTextView
    private lateinit var grupo: AutoCompleteTextView
    private lateinit var estadoCivil: AutoCompleteTextView
    private lateinit var hijos: Slider
    private lateinit var fab: FloatingActionButton
    private lateinit var numHijos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // estilos a la barra de tareas
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // hace que la vista no se solape con la barra
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        //iniciar componentes
        initComponents()
        //iniciar listeners
        initListeners()
        //initUi()
    }
    fun initListeners() {
        //asignar el valor que seleccionemos en el slider a un textView
        this.hijos.addOnChangeListener { hijos, _, _ ->
            numHijos.text = hijos.value.toInt().toString()
        }
        //activa o desactiva el desplegable de discapacidad
        this.radioDiscapacidad.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.discSi -> {
                    discapacidad.isEnabled = true
                    discapacidad2.setText("")
                }

                R.id.discNo -> {
                    discapacidad.isEnabled = false
                    discapacidad2.setText(getString(R.string.discapacidad))
                }
            }
        }
        //boton flotante que te lleva a la segunda vista para mostrarte los calculos
        this.fab.setOnClickListener {
            //intent
            val intent = Intent(this, ResultActivity::class.java)
            //bundle para llevar los datos a la otra vista
            val bundle = Bundle()
            //uso un objeto "User" para no tener que pasar los datos uno a uno
            bundle.putSerializable(
                "user", User(
                    //edad y estadoCivil los paso igualmente a la segunda vista aunque no los use en los calculos
                    brutoAnual.text.toString(),
                    edad.text.toString(),
                    pagas.text.toString(),
                    discapacidad.toString(),
                    grupo.text.toString(),
                    estadoCivil.text.toString(),
                    numHijos.text.toString()
                )
            )
            //meto el bundle con los datos en el intent antes de iniciarlo
            intent.putExtra("datos", bundle)
            startActivity(intent)
        }
    }
    //iniciar cada componente dentro de su variable
    private fun initComponents() {
        brutoAnual = findViewById(R.id.bruto)
        edad = findViewById(R.id.edad)
        pagas = findViewById(R.id.pagas)
        radioDiscapacidad = findViewById(R.id.radioGroup)
        discapacidad = findViewById(R.id.discapacidad)
        discapacidad2 = findViewById(R.id.discapacidad2)
        grupo = findViewById(R.id.gpro)
        estadoCivil = findViewById(R.id.estado_civil)
        hijos = findViewById(R.id.hijos)
        fab = findViewById(R.id.fab)
        numHijos = findViewById(R.id.numHijosTv)
    }
}

