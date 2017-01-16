package view.commands;

public class ExitCommand extends Command {
  public ExitCommand(int key, String description) {
    super(key, description);
  }

  @Override
  public void execute() {
    System.exit(0);
  }
}
