package model.expressions;

import model.InterpreterException;
import utils.HeapTable;
import utils.SymbolTable;

public class ArithmeticExpression implements Expression {
  private final char operation;
  private final Expression firstOperand;
  private final Expression secondOperand;

  public ArithmeticExpression(char operation, Expression firstOperand, Expression secondOperand) {
    this.operation = operation;
    this.firstOperand = firstOperand;
    this.secondOperand = secondOperand;
  }

  @Override
  public int evaluate(SymbolTable symbolTable, HeapTable heapTable) {
    int firstValue = firstOperand.evaluate(symbolTable, heapTable);
    int secondValue = secondOperand.evaluate(symbolTable, heapTable);

    switch (operation) {
      case '+':
        return firstValue + secondValue;
      case '-':
        return firstValue - secondValue;
      case '*':
        return firstValue * secondValue;
      case '/':
        if (secondValue == 0) {
          throw new InterpreterException("Arithmetic exception: division by 0!");
        }
        return firstValue / secondValue;
      default:
        throw new InterpreterException("Invalid operation");
    }
  }

  @Override
  public String toString() {
    return "" + firstOperand + " " + operation + " " + secondOperand;
  }
}
