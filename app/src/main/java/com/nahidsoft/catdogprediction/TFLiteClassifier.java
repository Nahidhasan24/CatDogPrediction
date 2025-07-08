package com.nahidsoft.catdogprediction;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TFLiteClassifier {
    private Interpreter interpreter;
    private static final int IMAGE_SIZE = 224;
    private static final String[] CLASS_NAMES = {"cat", "dog"};

    public TFLiteClassifier(AssetManager assetManager) throws IOException {
        interpreter = new Interpreter(loadModelFile(assetManager, "model_unquant.tflite"));
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private Bitmap cropCenterAndResize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newEdge = Math.min(width, height);
        int x = (width - newEdge) / 2;
        int y = (height - newEdge) / 2;
        Bitmap cropped = Bitmap.createBitmap(bitmap, x, y, newEdge, newEdge);
        return Bitmap.createScaledBitmap(cropped, IMAGE_SIZE, IMAGE_SIZE, true);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * IMAGE_SIZE * IMAGE_SIZE * 3);
        buffer.order(ByteOrder.nativeOrder());

        int[] pixels = new int[IMAGE_SIZE * IMAGE_SIZE];
        bitmap.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE);

        for (int pixel : pixels) {
            buffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f); // R
            buffer.putFloat(((pixel >> 8) & 0xFF) / 255.0f);  // G
            buffer.putFloat((pixel & 0xFF) / 255.0f);         // B
        }
        buffer.rewind();
        return buffer;
    }

    public String classify(Bitmap bitmap) {
        Bitmap resized = cropCenterAndResize(bitmap);
        ByteBuffer inputBuffer = convertBitmapToByteBuffer(resized);

        float[][] output = new float[1][CLASS_NAMES.length];
        interpreter.run(inputBuffer, output);

        int predictedIndex = 0;
        float maxConfidence = output[0][0];
        for (int i = 1; i < CLASS_NAMES.length; i++) {
            if (output[0][i] > maxConfidence) {
                maxConfidence = output[0][i];
                predictedIndex = i;
            }
        }

        return CLASS_NAMES[predictedIndex] + " (" + String.format("%.2f", maxConfidence * 100) + "%)";
    }
}