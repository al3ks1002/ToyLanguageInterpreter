package model.statements;

import model.ProgramState;
import model.expressions.Expression;
import utils.generators.HeapAddressGenerator;
import utils.HeapTable;
import utils.SymbolTable;

public class HeapAllocationStatement implements Statement {
  private final String variable;
  private final Expression expression;

  public HeapAllocationStatement(String variable, Expression expression) {
    this.variable = variable;
    this.expression = expression;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    SymbolTable symbolTable = programState.getSymbolTable();
    HeapTable heapTable = programState.getHeapTable();

    int value = expression.evaluate(symbolTable, heapTable);
    int address = HeapAddressGenerator.getAddress();
    heapTable.add(address, value);
    symbolTable.add(variable, address);

    return null;
  }

  @Override
  public String toString() {
    return "heapAllocation(" + variable + ", " + expression + ")";
  }
}
