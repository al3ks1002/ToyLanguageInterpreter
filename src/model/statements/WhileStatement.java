package model.statements;

import model.ProgramState;
import model.expressions.Expression;
import utils.ExecutionStack;

public class WhileStatement implements Statement {
  private final Expression expression;
  private final Statement statement;

  public WhileStatement(Expression expression, Statement statement) {
    this.expression = expression;
    this.statement = statement;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    ExecutionStack<Statement> executionStack = programState.getExecutionStack();

    if (expression.evaluate(programState.getSymbolTable(), programState.getHeapTable()) != 0) {
      executionStack.push(this);
      executionStack.push(statement);
    }

    return null;
  }

  @Override
  public String toString() {
    return "while (" + expression + ") then " + statement;
  }
}
