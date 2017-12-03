package ru.futurobot.imagefetcher;

/**
 * Created by Alexey on 22.11.2017.
 * Futurobot
 */

public class ImageFetcher {
  public RequestCreator load(String url) {
    return new RequestCreator(this).setUrl(url);
  }
}
