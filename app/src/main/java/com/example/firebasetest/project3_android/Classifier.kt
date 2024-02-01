import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Classifier(private val assetManager: AssetManager) {

    private var interpreter: Interpreter? = null

    init {
        val model = loadModelFile()
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = assetManager.openFd("model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classifyImage(bitmap: Bitmap): List<Pair<String, Float>> {
        // 이미지 전처리: Bitmap을 ByteBuffer로 변환
        val byteBuffer = convertBitmapToByteBuffer(bitmap)

        // 모델의 출력 형식에 맞게 배열을 초기화
        // 예를 들어, 모델이 3개의 클래스에 대한 확률을 반환한다고 가정
        val output = Array(1) { FloatArray(3) }

        // 모델 실행
        interpreter?.run(byteBuffer, output)

        // 출력 결과를 Pair 리스트로 변환
        return output[0].mapIndexed { index, probability ->
            "Class $index" to probability
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        // ByteBuffer 초기화 및 설정
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Bitmap을 ByteBuffer로 변환
        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (value in intValues) {
            byteBuffer.putFloat((value shr 16 and 0xFF) / 255f)
            byteBuffer.putFloat((value shr 8 and 0xFF) / 255f)
            byteBuffer.putFloat((value and 0xFF) / 255f)
        }

        return byteBuffer
    }

    companion object {
        private const val INPUT_SIZE = 224
        private const val PIXEL_SIZE = 3
    }

}

