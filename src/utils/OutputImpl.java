package utils;

import java.util.ArrayList;
import java.util.List;

public class OutputImpl<T> implements Output<T> {
  private final List<T> output;

  public OutputImpl() {
    output = new ArrayList<>();
  }

  @Override
  public void add(T value) {
    output.add(value);
  }

  @Override
  public Iterable<T> getAll() {
    return output;
  }

  @Override
  public String toString() {
    return "Output = " + output;
  }
}
