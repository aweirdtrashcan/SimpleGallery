package com.aweirdtrashcan.simplegallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aweirdtrashcan.simplegallery.data.GalleryData
import com.aweirdtrashcan.simplegallery.databinding.ActivityMainBinding
import com.aweirdtrashcan.simplegallery.models.Photos
import com.aweirdtrashcan.simplegallery.recyclerview.PhotosAdapter
import com.aweirdtrashcan.simplegallery.utils.isSdk29OrAbove
import com.aweirdtrashcan.simplegallery.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var permissionsContract : ActivityResultLauncher<Array<String>>

    private var hasReadPermissions = false

    private var hasWritePermissions = false

    private var allPhotos = mutableListOf<Photos>()

    private var adapter = PhotosAdapter()

    private val viewModel : MainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionsContract = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            hasReadPermissions = it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: hasReadPermissions
            hasWritePermissions = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: hasWritePermissions
        }

        askPermissions()

        lifecycleScope.launch {
            if (hasReadPermissions) {
                allPhotos = viewModel.getGallery().toMutableList()
                adapter.submitList(allPhotos)
                binding.rvMain.adapter = adapter
                binding.rvMain.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
            }
        }

    }

    private fun askPermissions() {
        val permissions = mutableListOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val readPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val writePermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val is29SdkOrAbove = isSdk29OrAbove {
            true
        } ?: false

        hasReadPermissions = readPermission
        hasWritePermissions = writePermission || is29SdkOrAbove

        if(!hasReadPermissions) {
            permissionsContract.launch(permissions.toTypedArray())
        }
        if(!hasWritePermissions) {
            permissionsContract.launch(permissions.toTypedArray())
        }


        Intent(Intent.ACTION_SHOW_APP_INFO)

        val rationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)

        if (rationale) {
           AlertDialog.Builder(this@MainActivity).apply {
               setTitle("You need to allow access to your files to see the gallery")
           }
        }

    }
}