package model.expressions;

import model.InterpreterException;
import utils.HeapTable;
import utils.SymbolTable;

public class VariableExpression implements Expression {
  private final String variable;

  public VariableExpression(String variable) {
    this.variable = variable;
  }

  @Override
  public int evaluate(SymbolTable symbolTable, HeapTable heapTable) {
    if (!symbolTable.contains(variable)) {
      throw new InterpreterException("Variable not found in symbol table!");
    }
    return symbolTable.getValue(variable);
  }

  @Override
  public String toString() {
    return variable;
  }
}
