package utils;

import model.FileData;

import java.util.HashMap;
import java.util.Map;

public class FileTable implements Table<Integer, FileData> {
  private Map<Integer, FileData> table;

  public FileTable() {
    table = new HashMap<>();
  }

  @Override
  public void add(Integer name, FileData value) {
    table.put(name, value);
  }

  @Override
  public boolean contains(Integer name) {
    return table.containsKey(name);
  }

  @Override
  public FileData getValue(Integer name) {
    return table.get(name);
  }

  @Override
  public void remove(Integer name) {
    table.remove(name);
  }

  @Override
  public Iterable<Map.Entry<Integer, FileData>> getAll() {
    return table.entrySet();
  }

  @Override
  public Map<Integer, FileData> getContent() {
    return table;
  }

  @Override
  public void setContent(Map<Integer, FileData> content) {
    table = content;
  }

  @Override
  public String toString() {
    return "Table = " + table;
  }
}
