package view.commands;

import controller.InterpreterController;
import model.InterpreterException;

public class RunExampleCommand extends Command {
  private InterpreterController controller;

  public RunExampleCommand(int key, String description, InterpreterController controller) {
    super(key, description);
    this.controller = controller;
  }

  @Override
  public void execute() {
    try {
      controller.executeAll();
    } catch (InterpreterException e) {
      System.out.println(e.getMessage());
//            e.printStackTrace();
    }
  }
}
