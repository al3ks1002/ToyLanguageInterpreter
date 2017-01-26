package model.statements;

import model.ProgramState;
import utils.LockTable;
import utils.SymbolTable;
import utils.generators.LockIdGenerator;

public class NewLockStatement implements Statement {
  private final String variable;

  public NewLockStatement(String variable) {
    this.variable = variable;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    LockTable lockTable = programState.getLockTable();
    SymbolTable symbolTable = programState.getSymbolTable();
    int lockId = LockIdGenerator.getId();

    try {
      lockTable.acquireLock();
      lockTable.add(lockId, -1);
      symbolTable.add(variable, lockId);
    } finally {
      lockTable.releaseLock();
    }

    return null;
  }

  @Override
  public String toString() {
    return "newLock(" + variable + ")";
  }
}
