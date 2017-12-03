package ru.futurobot.imagefetcher.cache;

import android.graphics.Bitmap;
import android.os.Looper;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;

/**
 * Created by Alexey on 23.11.2017.
 * Futurobot
 */

class Utils {

  private Utils() {
  }

  public static int getBitmapBytes(Bitmap bitmap) {
    int result = SDK_INT >= KITKAT ? bitmap.getAllocationByteCount() : bitmap.getByteCount();
    if (result < 0) {
      throw new IllegalStateException("Negative size: " + bitmap);
    }
    return result;
  }

  static <T> T checkNotNull(T value, String message) {
    if (value == null) {
      throw new NullPointerException(message);
    }
    return value;
  }

  static void checkNotMain() {
    if (isMain()) {
      throw new IllegalStateException("Method call should not happen from the main thread.");
    }
  }

  static void checkMain() {
    if (!isMain()) {
      throw new IllegalStateException("Method call should happen from the main thread.");
    }
  }

  static boolean isMain() {
    return Looper.getMainLooper().getThread() == Thread.currentThread();
  }
}
