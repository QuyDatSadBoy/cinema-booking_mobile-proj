package com.example.cinema_booking_mobile.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static File getFileFromUri(Context context, Uri uri) {
        try {
            String fileName = getFileName(context, uri);
            File file = new File(context.getCacheDir(), fileName);

            copy(context, uri, file);
            return file;
        } catch (IOException e) {
            Log.e(TAG, "Error getting file from uri: " + e.getMessage());
            return null;
        }
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static void copy(Context context, Uri uri, File destination) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(destination)) {

            if (inputStream == null) {
                throw new IOException("Failed to open input stream");
            }

            byte[] buffer = new byte[4 * 1024]; // 4KB buffer
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
        }
    }
}