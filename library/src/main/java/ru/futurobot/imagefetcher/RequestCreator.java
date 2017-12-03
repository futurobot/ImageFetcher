package ru.futurobot.imagefetcher;

import android.widget.ImageView;
import java.io.File;

/**
 * Created by Alexey on 23.11.2017.
 * Futurobot
 */

public final class RequestCreator {
  private String url;
  private ImageFetcher imageFetcher;

  RequestCreator(ImageFetcher imageFetcher) {
    this.imageFetcher = imageFetcher;
  }

  public void into(BitmapCallback callback) {

  }

  public void into(ImageView imageView) {

  }

  public void download(File file, DownloadCallback callback) {

  }

  public RequestCreator setUrl(String url) {
    this.url = url;
    return this;
  }
}
