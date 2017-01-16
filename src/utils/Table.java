package utils;

import java.io.Serializable;
import java.util.Map;

public interface Table<K, V> extends Serializable {
  void add(K name, V value);

  boolean contains(K name);

  V getValue(K name);

  void remove(K name);

  Iterable<Map.Entry<K, V>> getAll();

  Map<K, V> getContent();

  void setContent(Map<K, V> content);
}
