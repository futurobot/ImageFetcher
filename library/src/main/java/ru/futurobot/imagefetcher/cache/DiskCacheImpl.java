package ru.futurobot.imagefetcher.cache;

import com.jakewharton.disklrucache.DiskLruCache;
import java.io.File;
import java.io.IOException;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Source;
import timber.log.Timber;

/**
 * Created by Alexey on 22.11.2017.
 * Futurobot
 * Disk cache implementation based on Jake Wharton LRU disk cache
 */

public class DiskCacheImpl implements DiskCache {

  private final File cacheDir;
  private final int version = 1;
  private final int valueCount = 1;
  private long maxSize;
  private DiskLruCache diskLruCache;

  public DiskCacheImpl(File cacheDir, long maxSize) throws IOException {
    this.cacheDir = cacheDir;
    this.maxSize = maxSize;
    diskLruCache = DiskLruCache.open(cacheDir, version, valueCount, maxSize);
  }

  private DiskLruCache openDiskCache(File cacheDir, int version, int valueCount, long maxSize)
      throws IOException {
    return DiskLruCache.open(cacheDir, version, valueCount, maxSize);
  }

  @Override public void set(String key, Source source) {
    DiskLruCache.Editor editor;
    try {
      editor = diskLruCache.edit(key);
    } catch (IOException e) {
      Timber.e(e, "Could not open disk cache with error. Key: %s", key);
      return;
    }
    Sink sink;
    try {
      sink = Okio.sink(editor.newOutputStream(0));
    } catch (IOException e) {
      Timber.e(e, "Could not open new output stream. Key: %s", key);
      editor.abortUnlessCommitted();
      return;
    }
    BufferedSink bufferedSink = Okio.buffer(sink);
    try {
      bufferedSink.writeAll(source);
    } catch (IOException e) {
      Timber.e(e, "Error while copying source into sink. Key: %s", key);
      editor.abortUnlessCommitted();
      return;
    }
    try {
      editor.commit();
    } catch (IOException e) {
      Timber.e(e, "Error committing changes. Key: %s", key);
      editor.abortUnlessCommitted();
    }
  }

  @Override public DiskLruCache.Snapshot get(String key) {
    return null;
  }

  @Override public long size() {
    return diskLruCache.size();
  }

  @Override public long maxSize() {
    return diskLruCache.getMaxSize();
  }

  @Override public void setMaxSize(long maxSize) {
    this.maxSize = maxSize;
    diskLruCache.setMaxSize(maxSize);
  }

  @Override public void clear() {
    try {
      diskLruCache.delete();
    } catch (IOException e) {
      Timber.e(e, "Unable to delete disk cache");
    }
    try {
      diskLruCache = openDiskCache(cacheDir, version, valueCount, maxSize);
    } catch (IOException e) {
      Timber.e(e, "Unable to create new disk cache after clear");
      throw new RuntimeException(e);
    }
  }

  @Override public void close() {
    try {
      diskLruCache.flush();
    } catch (IOException e) {
      Timber.e(e, "Flush disk cache");
    }
    try {
      diskLruCache.close();
    } catch (IOException e) {
      Timber.e(e, "Close disk cache");
    }
  }
}
