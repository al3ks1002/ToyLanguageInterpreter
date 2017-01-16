package view.commands;

import model.InterpreterException;
import repository.ProgramStateRepository;

import java.util.Scanner;

public class SerializeCommand extends Command {
  private final ProgramStateRepository repository;

  public SerializeCommand(int key, String description, ProgramStateRepository repository) {
    super(key, description);
    this.repository = repository;
  }

  @Override
  public void execute() {
    System.out.print("Enter the path for serializing the file: ");
    Scanner scanner = new Scanner(System.in);
    String serializeFilePath = scanner.nextLine();
    try {
      repository.serializeProgramState(repository.getProgramStates().get(0), serializeFilePath);
    } catch (InterpreterException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
