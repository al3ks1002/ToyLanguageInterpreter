package model.statements;

import model.InterpreterException;
import model.ProgramState;
import utils.LockTable;
import utils.SymbolTable;

public class UnlockStatement implements Statement {
  private final String variable;

  public UnlockStatement(String variable) {
    this.variable = variable;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    SymbolTable symbolTable = programState.getSymbolTable();
    LockTable lockTable = programState.getLockTable();

    try {
      lockTable.acquireLock();
      if (!symbolTable.contains(variable)) {
        throw new InterpreterException("Variable is not in symbol table!");
      }

      int lockId = symbolTable.getValue(variable);
      if (!lockTable.contains(lockId)) {
        return null;
      }

      int currentProgramStateId = lockTable.getValue(lockId);
      if (currentProgramStateId == programState.getId()) {
        lockTable.add(lockId, -1);
      }
    } finally {
      lockTable.releaseLock();
    }

    return null;
  }

  @Override
  public String toString() {
    return "unlock(" + variable + ")";
  }
}
