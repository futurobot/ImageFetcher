package ru.futurobot.imagefetcher.cache;

import android.graphics.Bitmap;

/**
 * Created by Alexey on 22.11.2017.
 * Futurobot
 * Bitmap cache
 */

public interface BitmapCache {
  /* A bitmap cache that hold nothing*/ BitmapCache NONE = new BitmapCache() {
    @Override public Bitmap get(String key) {
      return null;
    }

    @Override public void set(String key, Bitmap bitmap) {

    }

    @Override public int size() {
      return 0;
    }

    @Override public int maxSize() {
      return 0;
    }

    @Override public void clear() {

    }
  };

  /** Retrieve an image for the specified {@code key} or {@code null}. */
  Bitmap get(String key);

  /** Store an image in the cache for the specified {@code key}. */
  void set(String key, Bitmap bitmap);

  /** Returns the current size of the cache in bytes. */
  int size();

  /** Returns the maximum size in bytes that the cache can hold. */
  int maxSize();

  /** Clears the cache. */
  void clear();
}
