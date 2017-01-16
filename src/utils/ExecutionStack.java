package utils;

import java.io.Serializable;

public interface ExecutionStack<T> extends Serializable {
  void push(T statement);

  T pop();

  boolean isEmpty();

  Iterable<T> getAll();
}
