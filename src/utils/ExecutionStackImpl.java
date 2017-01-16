package utils;

import java.util.Stack;

public class ExecutionStackImpl<T> implements ExecutionStack<T> {
  private final Stack<T> stack;

  public ExecutionStackImpl() {
    stack = new Stack<>();
  }

  @Override
  public void push(T statement) {
    stack.push(statement);
  }

  @Override
  public T pop() {
    return stack.pop();
  }

  @Override
  public boolean isEmpty() {
    return stack.isEmpty();
  }

  @Override
  public Iterable<T> getAll() {
    return stack;
  }

  @Override
  public String toString() {
    return "Stack: " + stack;
  }
}
