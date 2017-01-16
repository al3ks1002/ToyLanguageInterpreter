package repository;

import model.FileData;
import model.InterpreterException;
import model.ProgramState;
import model.statements.Statement;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramStateRepositoryImpl implements ProgramStateRepository {
  private final String logFilepath;
  private final List<ProgramState> programStates;

  public ProgramStateRepositoryImpl(String logFilePath) {
    this.logFilepath = logFilePath;
    programStates = new ArrayList<>();

    // Clear the log file.
    PrintWriter writer;
    try {
      writer = new PrintWriter(logFilePath);
      writer.print("");
      writer.close();
    } catch (FileNotFoundException e) {
      System.out.println("Could not open the log file!");
      e.printStackTrace();
    }
  }

  @Override
  public void addProgramState(ProgramState programState) {
    programStates.add(programState);
  }

  @Override
  public List<ProgramState> getProgramStates() {
    return programStates;
  }

  @Override
  public void logProgramState(ProgramState programState) {
    try (PrintWriter logFileWriter = new PrintWriter(new FileWriter(logFilepath, true))) {
      logFileWriter.printf("Program state id: %d\n", programState.getId());

      logFileWriter.println("Execution stack:");
      Iterable<Statement> stack = programState.getExecutionStack().getAll();
      for (Statement statement : stack) {
        logFileWriter.println(statement);
      }
      logFileWriter.println();

      logFileWriter.println("Symbol table:");
      Iterable<Map.Entry<String, Integer>> symbolTable = programState.getSymbolTable().getAll();
      for (Map.Entry<String, Integer> variable : symbolTable) {
        logFileWriter.format("%s = %d\n", variable.getKey(), variable.getValue());
      }
      logFileWriter.println();

      logFileWriter.println("File table:");
      Iterable<Map.Entry<Integer, FileData>> fileTable = programState.getFileTable().getAll();
      for (Map.Entry<Integer, FileData> variable : fileTable) {
        logFileWriter.format("%s = %s\n", variable.getKey(), variable.getValue().getFileName());
      }
      logFileWriter.println();

      logFileWriter.println("Heap table:");
      Iterable<Map.Entry<Integer, Integer>> heapTable = programState.getHeapTable().getAll();
      for (Map.Entry<Integer, Integer> variable : heapTable) {
        logFileWriter.format("%s = %s\n", variable.getKey(), variable.getValue());
      }
      logFileWriter.println();

      logFileWriter.println("Output:");
      Iterable<Integer> output = programState.getOutput().getAll();
      for (Integer value : output) {
        logFileWriter.println(value);
      }

      logFileWriter.println();
      logFileWriter.println("--------------------");
      logFileWriter.println();
    } catch (IOException e) {
      System.out.println("Could not open the log file!");
      e.printStackTrace();
    }
  }

  private Map<Integer, Integer> executeConservativeGarbageCollector(
      Collection<Integer> symbolTableValues, Map<Integer, Integer> heap) {
    return heap.entrySet().stream()
        .filter(e -> symbolTableValues.contains(e.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public void garbageCollectProgramState(ProgramState programState) {
    programState.getHeapTable().setContent(
        executeConservativeGarbageCollector(programState.getSymbolTable().getContent().values(),
            programState.getHeapTable().getContent()));
  }

  @Override
  public void serializeProgramState(ProgramState programState, String fileName) {
    try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(fileName))) {
      logProgramState(programState);
      writer.writeObject(programState);
    } catch (IOException e) {
      throw new InterpreterException("Could not serialize!", e);
    }
  }

  @Override
  public void deserializeProgramState(String fileName) {
    try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(fileName))) {
      ProgramState programState = (ProgramState) reader.readObject();
      logProgramState(programState);
    } catch (IOException | ClassNotFoundException e) {
      throw new InterpreterException("Could not deserialize!", e);
    }
  }
}
