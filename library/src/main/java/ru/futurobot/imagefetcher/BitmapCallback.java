package ru.futurobot.imagefetcher;

import android.graphics.Bitmap;

/**
 * Created by Alexey on 23.11.2017.
 * Futurobot
 */

public interface BitmapCallback {
  void onBitmapLoaded(Bitmap bitmap);

  void onError(Exception e);
}
