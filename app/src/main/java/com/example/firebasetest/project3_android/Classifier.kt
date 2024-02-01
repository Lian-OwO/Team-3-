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
    private val labels = listOf("matjogea", "nakgi", "geabul") // 클래스 이름

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
        val byteBuffer = convertBitmapToByteBuffer(bitmap)
        val output = Array(1) { FloatArray(labels.size) } // 클래스 수에 맞게 배열 크기 조정

        interpreter?.run(byteBuffer, output)

        // 출력 결과를 클래스 이름과 함께 Pair 리스트로 변환
        return labels.indices.map { index ->
            labels[index] to output[0][index]
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)
        intValues.forEach { value ->
            byteBuffer.putFloat(((value shr 16) and 0xFF) / 255f)
            byteBuffer.putFloat(((value shr 8) and 0xFF) / 255f)
            byteBuffer.putFloat((value and 0xFF) / 255f)
        }

        return byteBuffer
    }

    companion object {
        private const val INPUT_SIZE = 224
        private const val PIXEL_SIZE = 3
    }
}
