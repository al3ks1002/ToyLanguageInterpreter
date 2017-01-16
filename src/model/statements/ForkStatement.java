package model.statements;

import model.ProgramState;
import utils.ExecutionStackImpl;
import utils.SymbolTable;

public class ForkStatement implements Statement {
  private final Statement statement;

  public ForkStatement(Statement statement) {
    this.statement = statement;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    return new ProgramState(statement, new ExecutionStackImpl<>(),
        new SymbolTable(programState.getSymbolTable()), programState.getFileTable(),
        programState.getHeapTable(), programState.getOutput());
  }

  @Override
  public String toString() {
    return "fork(" + statement + ")";
  }
}
