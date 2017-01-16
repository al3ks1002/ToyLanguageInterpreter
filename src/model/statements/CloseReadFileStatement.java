package model.statements;

import model.InterpreterException;
import model.ProgramState;
import model.expressions.Expression;
import utils.FileTable;
import utils.HeapTable;
import utils.SymbolTable;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFileStatement implements Statement {
  private final Expression fileDescriptorExpression;

  public CloseReadFileStatement(Expression fileDescriptorExpression) {
    this.fileDescriptorExpression = fileDescriptorExpression;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    SymbolTable symbolTable = programState.getSymbolTable();
    HeapTable heapTable = programState.getHeapTable();
    int fileDescriptor = fileDescriptorExpression.evaluate(symbolTable, heapTable);

    FileTable fileTable = programState.getFileTable();
    if (!fileTable.contains(fileDescriptor)) {
      throw new InterpreterException("File not opened!");
    }

    BufferedReader reader = fileTable.getValue(fileDescriptor).getReader();
    try {
      reader.close();
      fileTable.remove(fileDescriptor);
    } catch (IOException e) {
      throw new InterpreterException("Could not close the file!", e);
    }

    return null;
  }

  @Override
  public String toString() {
    return "closeReadFile(" + fileDescriptorExpression + ")";
  }
}
