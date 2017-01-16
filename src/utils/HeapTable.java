package utils;

import java.util.HashMap;
import java.util.Map;

public class HeapTable implements Table<Integer, Integer> {
  private Map<Integer, Integer> table;

  public HeapTable() {
    table = new HashMap<>();
  }

  @Override
  public void add(Integer name, Integer value) {
    table.put(name, value);
  }

  @Override
  public boolean contains(Integer name) {
    return table.containsKey(name);
  }

  @Override
  public Integer getValue(Integer name) {
    return table.get(name);
  }

  @Override
  public void remove(Integer name) {
    table.remove(name);
  }

  @Override
  public Iterable<Map.Entry<Integer, Integer>> getAll() {
    return table.entrySet();
  }

  @Override
  public Map<Integer, Integer> getContent() {
    return table;
  }

  @Override
  public void setContent(Map<Integer, Integer> content) {
    table = content;
  }

  @Override
  public String toString() {
    return "Table = " + table;
  }
}
