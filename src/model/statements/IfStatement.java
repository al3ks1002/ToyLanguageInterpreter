package model.statements;

import model.ProgramState;
import model.expressions.Expression;
import utils.ExecutionStack;

public class IfStatement implements Statement {
  private final Expression expression;
  private final Statement ifStatement;
  private final Statement thenStatement;

  public IfStatement(Expression expression, Statement ifStatement, Statement thenStatement) {
    this.expression = expression;
    this.ifStatement = ifStatement;
    this.thenStatement = thenStatement;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    ExecutionStack<Statement> executionStack = programState.getExecutionStack();
    if (expression.evaluate(programState.getSymbolTable(), programState.getHeapTable()) != 0) {
      executionStack.push(ifStatement);
    } else {
      executionStack.push(thenStatement);
    }
    return null;
  }

  @Override
  public String toString() {
    return "if (" + expression + ") then " + ifStatement + "; else " + thenStatement;
  }
}
