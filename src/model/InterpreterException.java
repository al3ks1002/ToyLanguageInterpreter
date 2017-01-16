package model;

public class InterpreterException extends RuntimeException {
  public InterpreterException() {
    super();
  }

  public InterpreterException(String message) {
    super(message);
  }

  public InterpreterException(String message, Throwable cause) {
    super(message, cause);
  }

  public InterpreterException(Throwable cause) {
    super(cause);
  }
}
