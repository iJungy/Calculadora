package com.example.calculadora

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadora.databinding.ActivityEditdevBinding

class EditDev : AppCompatActivity() {
    private lateinit var binding: ActivityEditdevBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditdevBinding.inflate(layoutInflater)
        supportActionBar?.title = "Editar Información"

        setContentView(binding.root)

        setFocusLast()

        imageUri = Uri.parse(intent.extras?.getString(getString(R.string.k_image)))
        binding.profileImg.setImageURI(imageUri)
        binding.etname.setText(intent.extras?.getString(getString(R.string.k_name)))
        binding.etcorre.setText(intent.extras?.getString(getString(R.string.k_email)))
        binding.etsitioweb.setText(intent.extras?.getString(getString(R.string.k_web)))
        binding.etphone.setText(intent.extras?.getString(getString(R.string.k_phone)))



        binding.btnimgchange.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }

            galleryResult.launch(intent)
        }


    }

    private fun setFocusLast(){
        binding.etname.setOnFocusChangeListener { view, isFocused ->
            if (isFocused){
                binding.etname.text?.let { binding.etname.setSelection(it.length) }
            }
        }

        binding.etcorre.setOnFocusChangeListener { view, isFocused ->
            if (isFocused){
                binding.etcorre.text?.let { binding.etcorre.setSelection(it.length) }
            }
        }
        binding.etsitioweb.setOnFocusChangeListener { view, isFocused ->
            if (isFocused){
                binding.etsitioweb.text?.let { binding.etsitioweb.setSelection(it.length) }
            }
        }

        binding.etphone.setOnFocusChangeListener { view, isFocused ->
            if (isFocused){
                binding.etphone.text?.let { binding.etphone.setSelection(it.length) }
            }
        }


    }

    private val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK ) {
            imageUri = it.data?.data


            val contentResolver=applicationContext.contentResolver
            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

            imageUri?.let {
                contentResolver.takePersistableUriPermission(it, takeFlags)
            }
        }
        binding.profileImg.setImageURI(imageUri)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save) {
            if (validarCampos()) {
                sendData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun sendData(){
        val intent = Intent()
        intent.putExtra(getString(R.string.k_name),binding.etname.text.toString())
        intent.putExtra(getString(R.string.k_email),binding.etcorre.text.toString())
        intent.putExtra(getString(R.string.k_web),binding.etsitioweb.text.toString())
        intent.putExtra(getString(R.string.k_phone),binding.etphone.text.toString())
        intent.putExtra(getString(R.string.k_image),imageUri.toString())

        setResult(RESULT_OK,intent)
        finish()
    }

    private fun validarCampos():Boolean {
        var isValid = true
        //Validar nombre
        if (binding.etname.text.isNullOrEmpty() || binding.etname.text.toString().trim().isEmpty()) {
            binding.tilName.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        } else if (binding.etname.length().toString().toInt()<3){
            binding.tilName.run {
                error = context.getString(R.string.msn_campo_name)
                requestFocus()
            }
            isValid = false
        }
        else{
            binding.tilName.error=null
        }
        //Validar Correo
        if(binding.etcorre.text.isNullOrEmpty() || binding.etcorre.text.toString().trim().isEmpty() ) {
            binding.tilCorre.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etcorre.text).matches()){
            binding.tilCorre.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        }else{
            binding.tilCorre.error=null
        }
        //Validar web
        if(binding.etsitioweb.text.isNullOrEmpty()|| binding.etsitioweb.text.toString().trim().isEmpty() ) {
            binding.tilSitioweb.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        }else if(!URLUtil.isValidUrl(binding.etsitioweb.text.toString())){
            binding.tilSitioweb.run {
                error = context.getString(R.string.msn_campo_web)
                requestFocus()
            }
            isValid = false
        }else{
            binding.tilSitioweb.error=null
        }
        //Validar telefono
        if (binding.etphone.text.isNullOrEmpty() || binding.etphone.text.toString().trim().isEmpty()) {
            binding.tilPhone.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        } else if (!binding.etphone.text!!.matches(Regex("\\+\\d{12}"))) {
            binding.tilPhone.run {
                error = context.getString(R.string.msn_campo_phone)
                requestFocus()
            }
            isValid = false
        } else {
            binding.tilPhone.error = null
        }
        return isValid

      //Validar Correo
        if(binding.etcorre.text.isNullOrEmpty() || binding.etcorre.text.toString().trim().isEmpty() ) {
            binding.tilCorre.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etcorre.text).matches()){
            binding.tilCorre.run {
                error = context.getString(R.string.msn_campo_name)
                requestFocus()
            }
            isValid = false
        }else{
            binding.tilCorre.error=null
        }
        //Validar web
        if(binding.etsitioweb.text.isNullOrEmpty()|| binding.etsitioweb.text.toString().trim().isEmpty() ) {
            binding.tilSitioweb.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        }else if(!URLUtil.isValidUrl(binding.etsitioweb.text.toString())){
            binding.tilSitioweb.run {
                error = context.getString(R.string.msn_campo_web)
                requestFocus()
            }
            isValid = false
        }else{
            binding.tilSitioweb.error=null
        }
        //Validar telefono
        if (binding.etphone.text.isNullOrEmpty() || binding.etphone.text.toString().trim().isEmpty()) {
            binding.tilPhone.run {
                error = context.getString(R.string.msn_campo_obligatorio)
                requestFocus()
            }
            isValid = false
        } else if (!binding.etphone.text!!.matches(Regex("\\+\\d{12}"))) {
            binding.tilPhone.run {
                error = context.getString(R.string.msn_campo_phone)
                requestFocus()
            }
            isValid = false
        } else {
            binding.tilPhone.error = null
        }
        return isValid
    }
}

