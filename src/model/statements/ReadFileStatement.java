package model.statements;

import model.InterpreterException;
import model.ProgramState;
import model.expressions.Expression;
import utils.FileTable;
import utils.HeapTable;
import utils.SymbolTable;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements Statement {
  private final Expression fileDescriptorExpression;
  private final String variable;

  public ReadFileStatement(Expression fileDescriptorExpression, String variable) {
    this.fileDescriptorExpression = fileDescriptorExpression;
    this.variable = variable;
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
      String stringValue = reader.readLine();
      int value = (stringValue == null) ? 0 : Integer.parseInt(stringValue);
      symbolTable.add(variable, value);
    } catch (IOException e) {
      throw new InterpreterException("Could not read from file!", e);
    }

    return null;
  }

  @Override
  public String toString() {
    return "readFile(" + fileDescriptorExpression + ", " + variable + ")";
  }
}
