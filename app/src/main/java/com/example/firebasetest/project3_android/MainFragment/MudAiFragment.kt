package com.example.firebasetest.project3_android.MainFragment

import TensorFlowLiteInference
import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.firebasetest.project3_android.R
import com.example.firebasetest.project3_android.databinding.FragmentMudAiBinding
import java.nio.ByteBuffer

class MudAiFragment : Fragment() {
    private lateinit var fragmentMudAiBinding: FragmentMudAiBinding
    private lateinit var tfliteInference: TensorFlowLiteInference
    private lateinit var imageViewHost: ImageView
    private lateinit var resultTextView: TextView

    private val PICK_IMAGE_REQUEST = 1
    private val classLabels = arrayOf("개불", "낙지", "맛조개")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMudAiBinding = FragmentMudAiBinding.inflate(layoutInflater)
        return fragmentMudAiBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val assetManager = requireContext().assets
        tfliteInference = TensorFlowLiteInference(assetManager)

        imageViewHost = view.findViewById(R.id.imageViewHost)
        resultTextView = view.findViewById(R.id.resultTextView)

        fragmentMudAiBinding.uploadImageButton.setOnClickListener {
            openGallery()
        }

        fragmentMudAiBinding.buttonReserve.setOnClickListener {
            val bitmap = (imageViewHost.drawable as BitmapDrawable).bitmap
            val inputData = tfliteInference.convertBitmapToByteBuffer(bitmap)
            val result: FloatArray = tfliteInference.performInference(inputData)
            processResult(result)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
            imageViewHost.setImageBitmap(bitmap)
        }
    }

    private fun processResult(result: FloatArray) {
        // 최고 신뢰도와 해당 클래스 인덱스 찾기
        val maxConfidence = result.maxOrNull()
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1

        // 훈련한 이미지와 얼마나 비슷한지를 백분율로 계산
        val similarityPercentage = (maxConfidence!! * 100).toInt()

        // 클래스 인덱스를 클래스 라벨로 변환
        val predictedLabel = if (maxIndex >= 0 && maxIndex < classLabels.size) {
            classLabels[maxIndex]
        } else {
            "알 수 없음"
        }

        // 결과를 더 알아보기 쉽게 표시
        val resultText = "분석 결과: 가장 유사한 클래스 - $predictedLabel, 신뢰도 - $similarityPercentage%"
        resultTextView.text = resultText

        // 추가적으로 필요한 작업 수행
        // 예: 다른 UI 업데이트, 로그 출력 등
    }
}