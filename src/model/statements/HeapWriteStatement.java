package model.statements;

import model.InterpreterException;
import model.ProgramState;
import model.expressions.Expression;
import utils.HeapTable;
import utils.SymbolTable;

public class HeapWriteStatement implements Statement {
  private final String variable;
  private final Expression expression;

  public HeapWriteStatement(String variable, Expression expression) {
    this.variable = variable;
    this.expression = expression;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    SymbolTable symbolTable = programState.getSymbolTable();
    HeapTable heapTable = programState.getHeapTable();

    if (!symbolTable.contains(variable)) {
      throw new InterpreterException("Variable not found in the symbol table!");
    }
    int address = symbolTable.getValue(variable);

    if (!heapTable.contains(address)) {
      throw new InterpreterException("Address not found in the heap table!");
    }

    heapTable.add(address, expression.evaluate(symbolTable, heapTable));

    return null;
  }

  @Override
  public String toString() {
    return "heapWrite(" + variable + ", " + expression + ")";
  }
}
