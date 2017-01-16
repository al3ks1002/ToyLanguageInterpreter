package view.commands;

import model.InterpreterException;
import repository.ProgramStateRepository;

import java.util.Scanner;

public class DeserializeCommand extends Command {
  private final ProgramStateRepository repository;

  public DeserializeCommand(int key, String description, ProgramStateRepository repository) {
    super(key, description);
    this.repository = repository;
  }

  @Override
  public void execute() {
    System.out.print("Enter the path for the deserialized file: ");
    Scanner scanner = new Scanner(System.in);
    String serializeFilePath = scanner.nextLine();
    try {
      repository.deserializeProgramState(serializeFilePath);
    } catch (InterpreterException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
