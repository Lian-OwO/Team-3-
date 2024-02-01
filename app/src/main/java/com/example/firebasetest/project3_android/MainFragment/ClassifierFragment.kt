package com.example.firebasetest.project3_android.MainFragment

import Classifier
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.firebasetest.project3_android.R
    class ClassifierFragment : Fragment() {

        private lateinit var imageView: ImageView
        private lateinit var textViewResult: TextView
        private lateinit var buttonSelectImage: Button
        private lateinit var classifier: Classifier

        private val requestImageCapture = 1
        private val readExternalStoragePermission = 101

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_classifier, container, false)
            imageView = view.findViewById(R.id.imageView)
            textViewResult = view.findViewById(R.id.textViewResult)
            buttonSelectImage = view.findViewById(R.id.buttonSelectImage)

            classifier = Classifier(requireContext().assets)

            buttonSelectImage.setOnClickListener {
                Log.d("버튼", "클릭됨")
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("버튼", "조건문 실행")
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        readExternalStoragePermission
                    )
                } else {
                    Log.d("버튼", "else 문 실행")
                    selectImageFromGallery()
                }
            }

            return view
        }

        private fun selectImageFromGallery() {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, requestImageCapture)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray
        ) {
            when (requestCode) {
                readExternalStoragePermission -> {
                    if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        selectImageFromGallery()
                    }
                    return
                }
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == requestImageCapture && resultCode == Activity.RESULT_OK) {
                data?.data?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    imageView.setImageBitmap(bitmap)
                    classifyImage(bitmap)
                }
            }
        }

        private fun classifyImage(bitmap: Bitmap) {
            val results = classifier.classifyImage(bitmap)
            textViewResult.text = results.joinToString(separator = "\n") { "Class: ${it.first}, Confidence: ${it.second}" }
        }
    }