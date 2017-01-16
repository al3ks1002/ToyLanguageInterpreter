package model.statements;

import model.ProgramState;
import model.expressions.Expression;
import utils.Output;

public class PrintStatement implements Statement {
  private final Expression expression;

  public PrintStatement(Expression expression) {
    this.expression = expression;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    Output<Integer> output = programState.getOutput();
    output.add(expression.evaluate(programState.getSymbolTable(), programState.getHeapTable()));
    return null;
  }

  @Override
  public String toString() {
    return String.format("print(%s)", expression);
  }
}
