package utils;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements Table<String, Integer> {
  private Map<String, Integer> table;

  public SymbolTable() {
    table = new HashMap<>();
  }

  public SymbolTable(SymbolTable other) {
    table = new HashMap<>();
    for (Map.Entry<String, Integer> entry : other.getAll()) {
      table.put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void add(String name, Integer value) {
    table.put(name, value);
  }

  @Override
  public boolean contains(String name) {
    return table.containsKey(name);
  }

  @Override
  public Integer getValue(String name) {
    return table.get(name);
  }

  @Override
  public void remove(String name) {
    table.remove(name);
  }

  @Override
  public Iterable<Map.Entry<String, Integer>> getAll() {
    return table.entrySet();
  }

  @Override
  public Map<String, Integer> getContent() {
    return table;
  }

  @Override
  public void setContent(Map<String, Integer> content) {
    table = content;
  }

  @Override
  public String toString() {
    return "Table = " + table;
  }
}
