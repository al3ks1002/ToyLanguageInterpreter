package model.expressions;

import utils.HeapTable;
import utils.SymbolTable;

public class ConstantExpression implements Expression {
  private final int value;

  public ConstantExpression(int value) {
    this.value = value;
  }

  @Override
  public int evaluate(SymbolTable symbolTable, HeapTable heapTable) {
    return value;
  }

  @Override
  public String toString() {
    return "" + value;
  }
}
