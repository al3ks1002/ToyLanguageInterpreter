package view;

import view.commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class TextMenu {
  private Map<Integer, Command> commands;

  TextMenu() {
    commands = new HashMap<>();
  }

  void addCommand(Command command) {
    commands.put(command.getKey(), command);
  }

  private void printMenu() {
    for (Command command : commands.values()) {
      String line = String.format("%d : %s", command.getKey(), command.getDescription());
      System.out.println(line);
    }
  }

  void show() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      printMenu();
      System.out.printf("Input the option: ");
      String key = scanner.nextLine();
      try {
        Command command = commands.get(Integer.parseInt(key));
        if (command == null) {
          System.out.println("Invalid Option!");
        } else {
          command.execute();
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid Option!");
      }
    }
  }
}
