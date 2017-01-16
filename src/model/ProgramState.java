package model;

import model.statements.Statement;
import utils.*;
import utils.generators.ProgramStateIdGenerator;

import java.io.Serializable;

public class ProgramState implements Serializable {
  private final int id;
  private final ExecutionStack<Statement> executionStack;
  private final SymbolTable symbolTable;
  private final FileTable fileTable;
  private final HeapTable heapTable;
  private final Output<Integer> output;

  public ProgramState(Statement initialProgram, ExecutionStack<Statement> executionStack,
                      SymbolTable symbolTable, FileTable fileTable, HeapTable heapTable,
                      Output<Integer> output) {
    id = ProgramStateIdGenerator.getProgramStateId();

    this.executionStack = executionStack;
    this.symbolTable = symbolTable;
    this.fileTable = fileTable;
    this.heapTable = heapTable;
    this.output = output;

    executionStack.push(initialProgram);
  }

  public ProgramState(Statement initialProgram) {
    this(initialProgram, new ExecutionStackImpl<>(), new SymbolTable(), new FileTable(),
        new HeapTable(), new OutputImpl<>());
  }

  public ProgramState executeOneStep() {
    if (executionStack.isEmpty()) {
      throw new RuntimeException("Attempt to pop from an empty stack.");
    }
    Statement currentStatement = executionStack.pop();
    return currentStatement.execute(this);
  }

  public boolean isCompleted() {
    return executionStack.isEmpty();
  }

  public int getId() {
    return id;
  }

  public ExecutionStack<Statement> getExecutionStack() {
    return executionStack;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public FileTable getFileTable() {
    return fileTable;
  }

  public HeapTable getHeapTable() {
    return heapTable;
  }

  public Output<Integer> getOutput() {
    return output;
  }

  @Override
  public String toString() {
    return "ProgramState{" +
        "id=" + id +
        ", executionStack=" + executionStack +
        ", symbolTable=" + symbolTable +
        ", fileTable=" + fileTable +
        ", heapTable=" + heapTable +
        ", output=" + output +
        '}';
  }
}
