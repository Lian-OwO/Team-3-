import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class TensorFlowLiteInference(private val assetManager: AssetManager) {

    private lateinit var interpreter: Interpreter
    private var inputSize: Int = 0
    private var channels: Int = 0
    private var outputSize: Int = 0

    init {
        // 모델 초기화
        initializeInterpreter()
    }

    private fun initializeInterpreter() {
        val modelPath = "model.tflite"

        // 모델 로드
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength

        val modelByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        interpreter = Interpreter(modelByteBuffer)

        // 입력 노드의 크기 가져오기
        val inputTensor = interpreter.getInputTensor(0)
        inputSize = inputTensor.shape()[1] // 예시: 두 번째 차원의 크기를 가져옴
        channels = inputTensor.shape()[3] // 예시: 네 번째 차원은 채널 수

        // 출력 노드의 크기 가져오기
        val outputTensor = interpreter.getOutputTensor(0)
        outputSize = outputTensor.shape()[1] // 예시: 두 번째 차원의 크기를 가져옴
    }

    fun getInputSize(): Int {
        return inputSize
    }

    fun getChannels(): Int {
        return channels
    }

    fun getOutputSize(): Int {
        return outputSize
    }

    // 이미지 데이터를 모델에 전달하기 위해 ByteBuffer 반환
    fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * channels)
        byteBuffer.order(ByteOrder.nativeOrder())

        // 이미지를 정규화된 픽셀 값으로 변환하여 ByteBuffer에 채우기
        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {
                val pixelValue = bitmap.getPixel(x, y)

                byteBuffer.putFloat(((pixelValue shr 16 and 0xFF) - 127.5f) / 127.5f)
                byteBuffer.putFloat(((pixelValue shr 8 and 0xFF) - 127.5f) / 127.5f)
                byteBuffer.putFloat(((pixelValue and 0xFF) - 127.5f) / 127.5f)
            }
        }

        return byteBuffer
    }

    // 추론 예제
    fun performInference(inputData: ByteBuffer): FloatArray {
        val outputSize = getOutputSize()

        // 모델 입력에 이미지 데이터 설정
        interpreter.run(inputData, imageOutputBuffer)

        // 모델 출력 데이터를 FloatArray로 변환하여 반환
        val result = FloatArray(outputSize)
        imageOutputBuffer.rewind()
        imageOutputBuffer.asFloatBuffer().get(result)
        return result
    }

    // 모델의 출력 크기에 따라 설정
    private val imageOutputBuffer = ByteBuffer.allocateDirect(outputSize * 4).apply {
        order(ByteOrder.nativeOrder())
    }
}

