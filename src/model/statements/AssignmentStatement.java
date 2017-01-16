package model.statements;

import model.ProgramState;
import model.expressions.Expression;
import utils.SymbolTable;

public class AssignmentStatement implements Statement {
  private final String variable;
  private final Expression expression;

  public AssignmentStatement(String variable, Expression expression) {
    this.variable = variable;
    this.expression = expression;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    SymbolTable symbolTable = programState.getSymbolTable();
    symbolTable.add(variable, expression.evaluate(symbolTable, programState.getHeapTable()));
    return null;
  }

  @Override
  public String toString() {
    return variable + " = " + expression;
  }
}
