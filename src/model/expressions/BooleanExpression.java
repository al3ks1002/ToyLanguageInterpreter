package model.expressions;

import model.InterpreterException;
import utils.HeapTable;
import utils.SymbolTable;

public class BooleanExpression implements Expression {
  private final String booleanOperator;
  private final Expression firstOperand;
  private final Expression secondOperand;

  public BooleanExpression(String booleanOperator, Expression firstOperand,
                           Expression secondOperand) {
    this.booleanOperator = booleanOperator;
    this.firstOperand = firstOperand;
    this.secondOperand = secondOperand;
  }

  @Override
  public int evaluate(SymbolTable symbolTable, HeapTable heapTable) {
    int firstValue = firstOperand.evaluate(symbolTable, heapTable);
    int secondValue = secondOperand.evaluate(symbolTable, heapTable);

    switch (booleanOperator) {
      case "<":
        return (firstValue < secondValue) ? 1 : 0;
      case "<=":
        return (firstValue <= secondValue) ? 1 : 0;
      case "==":
        return (firstValue == secondValue) ? 1 : 0;
      case "!=":
        return (firstValue != secondValue) ? 1 : 0;
      case ">":
        return (firstValue > secondValue) ? 1 : 0;
      case ">=":
        return (firstValue >= secondValue) ? 1 : 0;
      default:
        throw new InterpreterException("Invalid operation");
    }
  }

  @Override
  public String toString() {
    return "" + firstOperand + " " + booleanOperator + " " + secondOperand;
  }
}
