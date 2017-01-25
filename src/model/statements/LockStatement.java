package model.statements;

import model.InterpreterException;
import model.ProgramState;
import utils.ExecutionStack;
import utils.LockTable;
import utils.SymbolTable;

public class LockStatement implements Statement {
  private final String variable;

  public LockStatement(String variable) {
    this.variable = variable;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    SymbolTable symbolTable = programState.getSymbolTable();
    LockTable lockTable = programState.getLockTable();
    ExecutionStack<Statement> executionStack = programState.getExecutionStack();

    try {
      lockTable.acquireLock();
      if (!symbolTable.contains(variable)) {
        throw new InterpreterException("Variable is not in symbol table!");
      }

      int lockId = symbolTable.getValue(variable);
      if (!lockTable.contains(lockId)) {
        throw new InterpreterException("Lock id is not in lock table!");
      }

      int currentProgramStateId = lockTable.getValue(lockId);
      if (currentProgramStateId == -1) {
        lockTable.add(lockId, programState.getId());
      } else {
        executionStack.push(this);
      }
    } finally {
      lockTable.releaseLock();
    }

    return null;
  }

  @Override
  public String toString() {
    return "lock(" + variable + ")";
  }
}
