package com.example.calculadora

import android.app.SearchManager
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.calculadora.databinding.ActivityDevinfoBinding

class Ydevdata : AppCompatActivity() {
    private lateinit var binding: ActivityDevinfoBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var image: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.title = "Información del Desarrollador"

        binding = ActivityDevinfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        updateUI()
        setupIntent()
        getUserData();


    }

    private fun getUserData() {
        image = Uri.parse(sharedPreferences.getString(getString(R.string.k_image), null))
        var name = sharedPreferences.getString(getString(R.string.k_name), null)
        var email = sharedPreferences.getString(getString(R.string.k_email), null)
        var web = sharedPreferences.getString(getString(R.string.k_web), null)
        var phone = sharedPreferences.getString(getString(R.string.k_phone), null)

        updateUI(name!!, email!!, web!!, phone!!)
    }


    private fun updateUI(
        name: String = "Curso UV",
        correo: String = "zs21020216@estudiantes.uv.mx",
        web: String = "https://www.miuv.com.mx",
        phone: String = "+522293935090",

        ) {

        binding.profileTvNombre.text = name
        binding.profileTvCorreo.text = correo
        binding.profileTvWeb.text = web
        binding.profileTvPhone.text = phone
        binding.profileImg.setImageURI(image)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit-> {
                val intent = Intent(this, EditDev::class.java)

                intent.putExtra(getString(R.string.k_name), binding.profileTvNombre.text.toString())
                intent.putExtra(
                    getString(R.string.k_email),
                    binding.profileTvCorreo.text.toString()
                )
                intent.putExtra(getString(R.string.k_web), binding.profileTvWeb.text.toString())
                intent.putExtra(getString(R.string.k_phone), binding.profileTvPhone.text.toString())
                intent.putExtra(getString(R.string.k_image), image.toString())

                editResult.launch(intent)

            }

        }
        return super.onOptionsItemSelected(item)
    }



    private val editResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {

                val name = it.data?.getStringExtra(getString(R.string.k_name))
                val correo = it.data?.getStringExtra(getString(R.string.k_email))
                val phone = it.data?.getStringExtra(getString(R.string.k_phone))
                val web = it.data?.getStringExtra(getString(R.string.k_web))
                image = Uri.parse(it.data?.getStringExtra(getString(R.string.k_image)))

                saveUserData(name,correo,web,phone)
            }
        }

    private fun saveUserData(name: String?,correo: String?,web: String?,phone: String?){
        sharedPreferences.edit {
            putString(getString(R.string.k_image),image.toString())
            putString(getString(R.string.k_name),name)
            putString(getString(R.string.k_email),correo)
            putString(getString(R.string.k_web),web)
            putString(getString(R.string.k_phone),phone)
            apply()
        }
        updateUI(name!!, correo!!, web!!, phone!!)
    }

    private fun setupIntent() {
        // Buscar por web un texto
        binding.profileTvNombre.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, binding.profileTvNombre.text)
            }
            launchintent(intent)
        }

        // Envio de correo
        binding.profileTvCorreo.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:" + binding.profileTvCorreo.text.toString())
                putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.profileTvCorreo.text.toString()))
                putExtra(Intent.EXTRA_SUBJECT, "AUTOMATIC Intent")
                putExtra(Intent.EXTRA_TEXT, "SOME text here")
            }
            launchintent(intent)
        }

        // Sitio web
        binding.profileTvWeb.setOnClickListener {
            val webText = binding.profileTvWeb.text.toString().trim()
            if (webText.isNotEmpty()) {
                var formattedUrl = webText
                if (!webText.startsWith("http://") && !webText.startsWith("https://")) {
                    formattedUrl = "http://$webText"
                }
                val uri = Uri.parse(formattedUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                val browsers = packageManager.queryIntentActivities(intent, 0)
                if (browsers.size > 0) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No se encontró una aplicación para abrir este enlace", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "El enlace está vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // Teléfono
        binding.profileTvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                val phone = (it as TextView).text.toString()
                data = Uri.parse("tel:$phone")
            }
            launchintent(intent)
        }
    }

    private fun launchintent(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No se encontró aplicación compatible", Toast.LENGTH_SHORT).show()
        }
    }


}

