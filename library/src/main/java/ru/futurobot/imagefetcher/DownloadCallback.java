package ru.futurobot.imagefetcher;

import java.io.File;

/**
 * Created by Alexey on 23.11.2017.
 * Futurobot
 * Callback for download result
 */

public interface DownloadCallback {
  void onSuccess(File targetFile);

  void onError(Exception e);
}
