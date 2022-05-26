package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sehatin.R
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Food
import com.app.sehatin.databinding.ItemHomeFoodBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter.HorizontalFoodAdapter
import com.app.sehatin.ui.activities.objectDetection.ObjectDetectionActivity
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewHolder
import com.app.sehatin.utils.FileHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class HomeFoodHolder(
    itemView: View,
    private val parent: Fragment,
    private val bottomNavigationView: BottomNavigationView,
    private val lifecycleOwner: LifecycleOwner
    ): ViewHolder(itemView) {

    private val binding = ItemHomeFoodBinding.bind(itemView)
    private lateinit var viewModel: ContentViewModel
    private lateinit var foodAdapter: HorizontalFoodAdapter
    private lateinit var context: Context
    private var selectedImageFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.viewModel = viewModel
        this.context = context
        initVariable()
        initListener()
        getData()
        setRvFoods()
    }

    private fun initVariable() {
        foodAdapter = HorizontalFoodAdapter(foods)
    }

    private fun initListener() = with(binding) {
        CameraMLBtn.setOnClickListener {
            startIntentCamera()
        }
        otherFoodBtn.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.nav_health
        }
    }

    private fun getData() {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result.token
                idToken?.let { getGoodFoods(it) }
            } else {
                Log.e(TAG, "getIdToken: ${task.exception}")
            }
        }
    }

    private fun getGoodFoods(idToken: String) {
        viewModel.getGoodFoods(idToken).observe(lifecycleOwner) {
            when(it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    Log.e(TAG, "getGoodFoods: ${it.error}")
                }
                is Result.Success -> {
                    Log.d(TAG, "getGoodFoods: ${it.data}")
                    val data = it.data
                    if(data != null) {
                        val ok = data.ok
                        ok?.let { isOk ->
                            if(isOk) {
                                showLoading(false)
                                setRvFoods()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setRvFoods() = with(binding) {
        rvFoods.setHasFixedSize(true)
        rvFoods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFoods.adapter = foodAdapter
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if(isLoading) {
            contentGroup.visibility = View.GONE
            shimmerLoading.visibility = View.VISIBLE
        } else {
            contentGroup.visibility = View.VISIBLE
            shimmerLoading.visibility = View.GONE
        }
    }

    private fun startIntentCamera() {
        if (!allCameraPermissionsGranted()) {
            ActivityCompat.requestPermissions(parent.requireActivity(), REQUIRED_PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(parent.requireActivity().packageManager)
            FileHelper.createTempFile(context).also {
                val photoURI: Uri = FileProvider.getUriForFile(context,
                    AUTHOR, it)
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun allCameraPermissionsGranted() = REQUIRED_PERMISSIONS_CAMERA.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = parent.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            selectedImageFile = FileHelper.reduceFileImage(File(currentPhotoPath))
            if(selectedImageFile != null) {
                val result =  BitmapFactory.decodeFile(selectedImageFile!!.path)
                moveToODT(result)
            } else {
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToODT(result: Bitmap?) {
        val intent = Intent()
        intent.setClass(context, ObjectDetectionActivity::class.java)
        ObjectDetectionActivity.IMAGE = result
        parent.startActivity(intent)
    }

    private val foods = arrayListOf(
        Food(
            name = "Jahe",
            thumbnail = "https://www.tokoindonesia.co.uk/wp-content/uploads/2020/05/jaheputih.png"
        ),
        Food(
            name = "Madu",
            thumbnail = "https://d3avoj45mekucs.cloudfront.net/astrogempak/media/articleasset/2018/nov/lovoury1_2.jpg"
        ),
        Food(
            name = "Bayam",
            thumbnail = "https://d2ncjxd2rk2vpl.cloudfront.net/e-petani/product/608ca9780826280006508aa5/600x600/95/outside/382e1d02-05f7-4d26-9a06-fd9509c6db03"
        ),
        Food(
            name = "Apel",
            thumbnail = "https://1.bp.blogspot.com/-rVn4xEKKGJc/XadDLQIVloI/AAAAAAAABXM/I16Jue0pFnc6EXHD02fw38jaxnf38a-ggCLcBGAsYHQ/s1600/apple-2788616_1280.jpg"
        ),
        Food(
            name = "Kacang Hijau",
            thumbnail = "https://www.kampustani.com/wp-content/uploads/2019/01/Teknologi-Produksi-Benih-Kacang-Hijau.jpg"
        ),
    )

    private companion object {
        const val TAG = "HomeContentHolder"
        val REQUIRED_PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_CAMERA = 10
        const val AUTHOR = "com.app.sehatin"
    }
}