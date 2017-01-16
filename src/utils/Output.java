package utils;

import java.io.Serializable;

public interface Output<T> extends Serializable {
  void add(T value);

  Iterable<T> getAll();
}