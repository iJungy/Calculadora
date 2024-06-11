package com.example.calculadora

import android.app.SearchManager
import android.content.Intent
import android.content.SharedPreferences
import android.location.SettingInjectorService
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.core.net.toUri
import com.example.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var image: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI()
        setupIntent()
        getUserData()
    }


    private fun updateUI(
        name: String = "Curso UV",
        correo: String = "zs21027321@estudiantes.uv.mx",
        web: String = "https://www.miuv.com.mx",
        phone: String = "+522291766421",

        ) {

        binding.profileTvNombre.text = name
        binding.profileTvCorreo.text = correo
        binding.profileTvWeb.text = web
        binding.profileTvPhone.text = phone

    }

    private fun getUserData(){
        //image = Uri.parse(sharedPreferences.getString(getString(R.string.k_image),""))
        var name = sharedPreferences.getString(getString(R.string.k_name),null)
        var email = sharedPreferences.getString(getString(R.string.k_email),null)
        var web = sharedPreferences.getString(getString(R.string.k_web),null)
        var phone = sharedPreferences.getString(getString(R.string.k_phone),null)
        var lat = sharedPreferences.getString(getString(R.string.k_lat),null)?.toDouble() ?:0.0
        var lon = sharedPreferences.getString(getString(R.string.k_lon),null)?.toDouble() ?:0.0

        updateUI(name!!,email!!,web!!,phone!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.show_perfil->{
                val intent = Intent(this, EditActivity::class.java)

                intent.putExtra(getString(R.string.k_name), binding.profileTvNombre.text.toString())
                intent.putExtra(getString(R.string.k_email), binding.profileTvCorreo.text.toString())
                intent.putExtra(getString(R.string.k_web), binding.profileTvWeb.text.toString())
                intent.putExtra(getString(R.string.k_phone), binding.profileTvPhone.text.toString())
                intent.putExtra(getString(R.string.k_image), binding.profileImg.toString().toUri())


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
                image = it.data?.getStringExtra(getString(R.string.k_image))?.toUri()

                binding.profileImg.setImageURI(image)
                //updateUI(name!!, correo!!, web!!, phone!!)
                saveUserData(name,correo,phone,web)
            }
        }

    private fun saveUserData(name:String?,correo:String?,phone:String?,web:String?){
        sharedPreferences.edit {
            putString(getString(R.string.k_image),image.toString())
            putString(getString(R.string.k_name),name)
            putString(getString(R.string.k_email),correo)
            putString(getString(R.string.k_phone),phone)
            putString(getString(R.string.k_web),web)
            apply()
        }

        updateUI(name!!,correo!!,web!!,phone!!)
    }

    private fun launchintent(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No se encontro aplicacion compatible", Toast.LENGTH_SHORT).show()
        }

    }


    private fun setupIntent() {
        //Buscar por web un texto
        binding.profileTvNombre.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, binding.profileTvNombre.text)
            }
            launchintent(intent)
        }

        //Envio de correo
        binding.profileTvCorreo.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto")
                putExtra(Intent.EXTRA_EMAIL, binding.profileTvCorreo.text.toString())
                putExtra(Intent.EXTRA_SUBJECT, "AUTOMATIC Intent")
                putExtra(Intent.EXTRA_TEXT, "SOME text here")
            }
            launchintent(intent)
        }

        //Sitio web
        binding.profileTvWeb.setOnClickListener {
            val url = binding.profileTvWeb.text.toString()

            launchintent(intent)
        }

        //Telefono
        binding.profileTvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                val phone = (it as TextView).text
                data = Uri.parse("tel: $phone")
            }
            launchintent(intent)
        }
    }
}

