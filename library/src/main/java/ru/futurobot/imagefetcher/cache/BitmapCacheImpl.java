package ru.futurobot.imagefetcher.cache;

import android.graphics.Bitmap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Alexey on 23.11.2017.
 * Futurobot
 * Lru Bitmap cache implemention
 */

public class BitmapCacheImpl implements BitmapCache {
  private final LinkedHashMap<String, Bitmap> map;
  private final int maxSize;

  private int size;
  private int putCount;
  private int evictionCount;
  private int hitCount;
  private int missCount;

  public BitmapCacheImpl(int maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("Max size must be positive.");
    }
    this.maxSize = maxSize;
    this.map = new LinkedHashMap<>(0, 0.75f, true);
  }

  @Override public Bitmap get(String key) {
    if (key == null) {
      throw new NullPointerException("key == null");
    }

    Bitmap mapValue;
    synchronized (this) {
      mapValue = map.get(key);
      if (mapValue != null) {
        hitCount++;
        return mapValue;
      }
      missCount++;
    }

    return null;
  }

  @Override public void set(String key, Bitmap bitmap) {
    if (key == null || bitmap == null) {
      throw new NullPointerException("key == null || bitmap == null");
    }

    int addedSize = Utils.getBitmapBytes(bitmap);
    if (addedSize > maxSize) {
      return;
    }

    synchronized (this) {
      putCount++;
      size += addedSize;
      Bitmap previous = map.put(key, bitmap);
      if (previous != null) {
        size -= Utils.getBitmapBytes(previous);
      }
    }

    trimToSize(maxSize);
  }

  private void trimToSize(int maxSize) {
    while (true) {
      String key;
      Bitmap value;
      synchronized (this) {
        if (size < 0 || (map.isEmpty() && size != 0)) {
          throw new IllegalStateException(
              getClass().getName() + ".sizeOf() is reporting inconsistent results!");
        }

        if (size <= maxSize || map.isEmpty()) {
          break;
        }

        Map.Entry<String, Bitmap> toEvict = map.entrySet().iterator().next();
        key = toEvict.getKey();
        value = toEvict.getValue();
        map.remove(key);
        size -= Utils.getBitmapBytes(value);
        evictionCount++;
      }
    }
  }

  /** Clear the cache. */
  public final void evictAll() {
    trimToSize(-1); // -1 will evict 0-sized elements
  }

  @Override public final synchronized int size() {
    return size;
  }

  @Override public final synchronized int maxSize() {
    return maxSize;
  }

  @Override public final synchronized void clear() {
    evictAll();
  }

  /** Returns the number of times {@link #get} returned a value. */
  public final synchronized int hitCount() {
    return hitCount;
  }

  /** Returns the number of times {@link #get} returned {@code null}. */
  public final synchronized int missCount() {
    return missCount;
  }

  /** Returns the number of times {@link #set(String, Bitmap)} was called. */
  public final synchronized int putCount() {
    return putCount;
  }

  /** Returns the number of values that have been evicted. */
  public final synchronized int evictionCount() {
    return evictionCount;
  }
}
