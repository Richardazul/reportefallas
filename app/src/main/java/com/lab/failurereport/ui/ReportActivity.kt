package com.lab.failurereport.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import coil.load
import com.lab.failurereport.R
import com.lab.failurereport.data.FailureReport
import com.lab.failurereport.databinding.ActivityReportBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding
    private val viewModel: FailureViewModel by viewModels()

    private var cameraUri: Uri? = null
    private var photoPath: String? = null

    // --- Lanzadores de resultados ---

    private val launchCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                binding.ivPhotoPreview.load(cameraUri) { crossfade(true) }
                binding.tvPhotoHint.text = "Foto tomada con la cámara"
            }
        }

    private val launchGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val dest = createImageFile()
                contentResolver.openInputStream(it)?.use { input ->
                    dest.outputStream().use { output -> input.copyTo(output) }
                }
                photoPath = dest.absolutePath
                binding.ivPhotoPreview.load(dest) { crossfade(true) }
                binding.tvPhotoHint.text = "Imagen seleccionada de la galería"
            }
        }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) openCamera()
            else Toast.makeText(this, "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show()
        }

    private val requestGalleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) launchGallery.launch("image/*")
            else Toast.makeText(this, "Se necesita permiso para acceder a la galería", Toast.LENGTH_SHORT).show()
        }

    // --------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSelectPhoto.setOnClickListener { showPhotoSourceDialog() }
        binding.btnSave.setOnClickListener { saveReport() }
    }

    private fun showPhotoSourceDialog() {
        AlertDialog.Builder(this)
            .setTitle("Agregar foto")
            .setItems(arrayOf("Tomar foto con cámara", "Elegir de la galería")) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpen()
                    1 -> checkGalleryPermissionAndOpen()
                }
            }
            .show()
    }

    private fun checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun checkGalleryPermissionAndOpen() {
        // En Android 13+ se usa READ_MEDIA_IMAGES; en versiones anteriores READ_EXTERNAL_STORAGE.
        // GetContent() en API 33+ no requiere permiso explícito, pero para APIs menores sí.
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            launchGallery.launch("image/*")
        } else {
            requestGalleryPermission.launch(permission)
        }
    }

    private fun openCamera() {
        val file = createImageFile()
        photoPath = file.absolutePath
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )
        cameraUri = uri
        launchCamera.launch(uri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, "FALLA_$timestamp.jpg")
    }

    private fun saveReport() {
        val location = binding.etLocation.text.toString().trim()
        val inventory = binding.etInventory.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (location.isEmpty()) {
            binding.tilLocation.error = "Ingresa la ubicación del equipo"
            return
        }
        if (inventory.isEmpty()) {
            binding.tilInventory.error = "Ingresa el número de inventario"
            return
        }
        if (description.isEmpty()) {
            binding.tilDescription.error = "Describe la falla del equipo"
            return
        }

        binding.tilLocation.error = null
        binding.tilInventory.error = null
        binding.tilDescription.error = null

        val report = FailureReport(
            location = location,
            inventoryNumber = inventory,
            description = description,
            photoPath = photoPath
        )

        viewModel.insert(report)
        Toast.makeText(this, "Falla registrada correctamente", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
