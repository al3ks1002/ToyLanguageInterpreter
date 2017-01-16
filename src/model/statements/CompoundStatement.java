package model.statements;

import model.ProgramState;
import utils.ExecutionStack;

public class CompoundStatement implements Statement {
  private final Statement firstStatement;
  private final Statement secondStatement;

  public CompoundStatement(Statement firstStatement, Statement secondStatement) {
    this.firstStatement = firstStatement;
    this.secondStatement = secondStatement;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    ExecutionStack<Statement> executionStack = programState.getExecutionStack();
    executionStack.push(secondStatement);
    executionStack.push(firstStatement);
    return null;
  }

  @Override
  public String toString() {
    return firstStatement + " | " + secondStatement;
  }
}
