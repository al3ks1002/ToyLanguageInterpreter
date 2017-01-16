package view.commands;

public abstract class Command {
  private final int key;
  private final String description;

  Command(int key, String description) {
    this.key = key;
    this.description = description;
  }

  public abstract void execute();

  public int getKey() {
    return key;
  }

  public String getDescription() {
    return description;
  }
}
