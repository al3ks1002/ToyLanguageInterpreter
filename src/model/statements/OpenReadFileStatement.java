package model.statements;

import model.FileData;
import model.InterpreterException;
import model.ProgramState;
import utils.generators.FileDescriptorGenerator;
import utils.FileTable;
import utils.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class OpenReadFileStatement implements Statement {
  private String variableFileId;
  private String fileName;

  public OpenReadFileStatement(String variableFileId, String fileName) {
    this.variableFileId = variableFileId;
    this.fileName = fileName;
  }

  @Override
  public ProgramState execute(ProgramState programState) {
    FileTable fileTable = programState.getFileTable();
    for (Map.Entry<Integer, FileData> entry : fileTable.getAll()) {
      if (entry.getValue().getFileName().equals(fileName)) {
        throw new InterpreterException("File name already in the file table!");
      }
    }

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));
      int fileDescriptor = FileDescriptorGenerator.getDescriptor();
      fileTable.add(fileDescriptor, new FileData(fileName, reader));
      SymbolTable symbolTable = programState.getSymbolTable();
      symbolTable.add(variableFileId, fileDescriptor);
    } catch (IOException e) {
      throw new InterpreterException("Error while trying to create a new buffered reader!", e);
    }

    return null;
  }

  @Override
  public String toString() {
    return "openReadFile(" + variableFileId + ", " + fileName + ")";
  }
}
