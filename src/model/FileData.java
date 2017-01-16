package model;

import java.io.BufferedReader;

public class FileData {
  private final String fileName;
  private final BufferedReader reader;

  public FileData(String fileName, BufferedReader reader) {
    this.fileName = fileName;
    this.reader = reader;
  }

  public String getFileName() {
    return fileName;
  }

  public BufferedReader getReader() {
    return reader;
  }
}
