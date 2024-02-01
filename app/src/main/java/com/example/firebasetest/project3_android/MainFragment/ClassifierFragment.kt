package com.example.firebasetest.project3_android.MainFragment

import Classifier
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.firebasetest.project3_android.R
import java.io.InputStream

class ClassifierFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var textViewResult: TextView
    private lateinit var selectImageLauncher: ActivityResultLauncher<String>
    private lateinit var classifier: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        classifier = Classifier(requireActivity().assets)

        // 이미지 선택 결과를 처리하기 위한 ActivityResultLauncher 초기화
        selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // 사용자가 이미지를 선택하면 이곳에서 URI 처리
            uri?.let {
                val bitmap = uriToBitmap(uri)
                imageView.setImageBitmap(bitmap)
                classifyImage(bitmap)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Fragment의 레이아웃 설정
        val view = inflater.inflate(R.layout.fragment_classifier, container, false)
        imageView = view.findViewById(R.id.imageView)
        textViewResult = view.findViewById(R.id.textViewResult)

        val buttonSelectImage: Button = view.findViewById(R.id.buttonSelectImage)
        buttonSelectImage.setOnClickListener {
            // 갤러리에서 이미지를 선택하도록 요청
            selectImageFromGallery()
        }

        return view
    }

    private fun selectImageFromGallery() {
        // "image/*" MIME 타입을 사용해 갤러리에서 모든 이미지를 볼 수 있도록 함
        selectImageLauncher.launch("image/*")
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap {
        val contentResolver = requireActivity().contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(selectedFileUri)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun classifyImage(bitmap: Bitmap) {
        val results = classifier.classifyImage(bitmap)
        textViewResult.text = results.joinToString(separator = "\n") { "${it.first}: ${it.second}" }
    }
}
