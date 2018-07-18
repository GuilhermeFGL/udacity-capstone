package com.guilhermefgl.rolling.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CompressBitmap {

    private final static Integer MAX_WIDTH = 1024;
    private final static Integer MAX_HEIGHT = 1024;
    private final static Integer QUALITY = 50;

    public static Bitmap compress(Bitmap original) {

        // resize
        int width = original.getWidth();
        int height = original.getHeight();
        if (width > MAX_WIDTH || height > MAX_HEIGHT) {
            if (width > height) { // landscape
                float ratio = (float) width / MAX_WIDTH;
                width = MAX_WIDTH;
                height = (int)(height / ratio);
            } else if (height > width) { // portrait

                float ratio = (float) height / MAX_HEIGHT;
                height = MAX_HEIGHT;
                width = (int)(width / ratio);
            } else { // square
                height = MAX_HEIGHT;
                width = MAX_WIDTH;
            }
            original = Bitmap.createScaledBitmap(original, width, height, true);
        }

        // compress
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, QUALITY, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

}
