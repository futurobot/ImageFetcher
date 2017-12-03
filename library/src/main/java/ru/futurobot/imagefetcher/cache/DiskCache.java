package ru.futurobot.imagefetcher.cache;

import com.jakewharton.disklrucache.DiskLruCache;
import okio.Source;

/**
 * Created by Alexey on 22.11.2017.
 * Futurobot
 * Disk cache
 */

public interface DiskCache {
  /* Disk cache that can store nothing */ DiskCache NONE = new DiskCache() {

    @Override public void set(String key, Source source) {

    }

    @Override public DiskLruCache.Snapshot get(String key) {
      return null;
    }

    @Override public void setMaxSize(long maxSize) {

    }

    @Override public long size() {
      return 0;
    }

    @Override public long maxSize() {
      return 0;
    }

    @Override public void clear() {

    }

    @Override public void close() {

    }
  };

  void set(String key, Source source);

  DiskLruCache.Snapshot get(String key);

  void setMaxSize(long maxSize);

  long size();

  long maxSize();

  void clear();

  void close();
}
