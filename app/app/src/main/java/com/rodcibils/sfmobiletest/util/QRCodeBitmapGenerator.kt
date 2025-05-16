package com.rodcibils.sfmobiletest.util

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeBitmapGenerator {
    /**
     * Generates a QR code bitmap from the given content string.
     *
     * This method uses ZXing's [QRCodeWriter] to encode the input string as a QR code,
     * and produces a [Bitmap] of the specified size. The resulting QR code is monochromatic,
     * using black for the data modules and white for the background.
     *
     * @param content The string to encode into a QR code.
     * @param size The width and height (in pixels) of the generated bitmap. Default is 512.
     * @return A [Bitmap] containing the rendered QR code.
     *
     * @throws com.google.zxing.WriterException if the content cannot be encoded.
     */
    fun generate(
        content: String,
        size: Int = 512,
    ): Bitmap {
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val bmp = createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp[x, y] =
                    if (bits[x, y]) {
                        android.graphics.Color.BLACK
                    } else {
                        android.graphics.Color.WHITE
                    }
            }
        }
        return bmp
    }
}
