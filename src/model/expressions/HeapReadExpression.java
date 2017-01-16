package model.expressions;

import model.InterpreterException;
import utils.HeapTable;
import utils.SymbolTable;

public class HeapReadExpression implements Expression {
  private final String variable;

  public HeapReadExpression(String variable) {
    this.variable = variable;
  }

  @Override
  public int evaluate(SymbolTable symbolTable, HeapTable heapTable) {
    if (!symbolTable.contains(variable)) {
      throw new InterpreterException("Variable is not in symbol table!");
    }
    int address = symbolTable.getValue(variable);

    if (!heapTable.contains(address)) {
      throw new InterpreterException("Address is not in heap table!");
    }

    return heapTable.getValue(address);
  }

  @Override
  public String toString() {
    return "heapRead(" + variable + ")";
  }
}
