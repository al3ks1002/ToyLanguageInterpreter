package model.statements;

import model.ProgramState;
import model.expressions.BooleanExpression;
import model.expressions.Expression;
import model.expressions.VariableExpression;
import utils.ExecutionStack;

public class ForStatement implements Statement {
  private final String variable;
  private final Expression initialExpression;
  private final Expression conditionExpression;
  private final Expression assignmentExpression;
  private final Statement statement;

  public ForStatement(String variable, Expression initialExpression, Expression conditionExpression,
                      Expression assignmentExpression, Statement statement) {
    this.variable = variable;
    this.initialExpression = initialExpression;
    this.conditionExpression = conditionExpression;
    this.assignmentExpression = assignmentExpression;
    this.statement = statement;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    ExecutionStack<Statement> executionStack = programState.getExecutionStack();

    Statement
        newCompoundStatement =
        new CompoundStatement(new AssignmentStatement(variable, initialExpression),
            new WhileStatement(
                new BooleanExpression("<", new VariableExpression("v"), conditionExpression),
                new CompoundStatement(statement,
                    new AssignmentStatement(variable, assignmentExpression))));
    executionStack.push(newCompoundStatement);

    return null;
  }

  @Override
  public String toString() {
    return "for (v = " + initialExpression + "; v < " + conditionExpression + "; v = "
        + assignmentExpression + ") " + "{" + statement + "}";
  }
}
