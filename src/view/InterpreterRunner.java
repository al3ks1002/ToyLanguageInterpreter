package view;

import controller.InterpreterController;
import model.ProgramState;
import model.expressions.*;
import model.statements.*;
import repository.ProgramStateRepository;
import repository.ProgramStateRepositoryImpl;
import view.commands.DeserializeCommand;
import view.commands.ExitCommand;
import view.commands.RunExampleCommand;
import view.commands.SerializeCommand;

public class InterpreterRunner {
  private static String logFilePath;

  private static ProgramStateRepository getRepository(Statement statement) {
    ProgramStateRepository repository = new ProgramStateRepositoryImpl(logFilePath);
    ProgramState initialProgramState = new ProgramState(statement);
    repository.addProgramState(initialProgramState);
    return repository;
  }

  private static InterpreterController getController(Statement statement) {
    return new InterpreterController(getRepository(statement));
  }

  public static void main(String[] args) {
    logFilePath = "log.txt";

        /*
          a = 2 + 3 * 5;
          b = a + 1;
          print(b);
         */
    Statement
        ex1 =
        new CompoundStatement(new AssignmentStatement("a",
            new ArithmeticExpression('+', new ConstantExpression(2), new
                ArithmeticExpression('*', new ConstantExpression(3), new ConstantExpression(5)))),
            new CompoundStatement(new AssignmentStatement("b",
                new ArithmeticExpression('+', new VariableExpression("a"), new
                    ConstantExpression(1))), new PrintStatement(new VariableExpression("b"))));

        /*
         * a = 2;
         * if (a) then v = 2; else v = 3;
         */
    Statement
        ex2 =
        new CompoundStatement(new AssignmentStatement("a", new ConstantExpression(0)),
            new IfStatement(new VariableExpression("a"),
                new AssignmentStatement("v", new ConstantExpression(2)), new
                AssignmentStatement("v", new ConstantExpression(3))));

        /*
         * b = 2;
         * if (a) then v = 2; else v = 3;
         */
    Statement
        ex3 =
        new CompoundStatement(new AssignmentStatement("b", new ConstantExpression(2)),
            new IfStatement(new VariableExpression("a"),
                new AssignmentStatement("v", new ConstantExpression(2)), new
                AssignmentStatement("v", new ConstantExpression(3))));

        /*
         * a = 2 + 3 * 5;
         * b = a + 2 / 0;
         * print(b);
         */
    Statement
        ex4 =
        new CompoundStatement(new AssignmentStatement("a",
            new ArithmeticExpression('+', new ConstantExpression(2), new
                ArithmeticExpression('*', new ConstantExpression(3), new ConstantExpression(5)))),
            new CompoundStatement(new AssignmentStatement("b",
                new ArithmeticExpression('+', new VariableExpression("a"), new
                    ArithmeticExpression('/', new ConstantExpression(2),
                    new ConstantExpression(0)))), new PrintStatement(new VariableExpression("b"))));

        /*
         * openReadFile(f, "test.in");
         * readFile(f, x);
         * print(x);
         * if (x) then readFile(f, x); print(x); else print(0);
         * closeReadFile(f);
         */
    Statement
        ex5 =
        new CompoundStatement(new OpenReadFileStatement("f", "test.in"),
            new CompoundStatement(new ReadFileStatement(new VariableExpression("f"), "x"),
                new CompoundStatement(new PrintStatement(new VariableExpression("x")),
                    new CompoundStatement(new IfStatement(new VariableExpression("x"),
                        new CompoundStatement(
                            new ReadFileStatement(new VariableExpression("f"), "x"),
                            new PrintStatement(new VariableExpression("x"))),
                        new PrintStatement(new ConstantExpression(0))),
                        new CloseReadFileStatement(new VariableExpression("f"))))));

        /*
         * openReadFile(f, "test.in");
         * readFile(f + 2, x);
         * print(x);
         * if (x) then readFile(f, x); print(x); else print(0);
         * closeReadFile(f);
         */
    Statement
        ex6 =
        new CompoundStatement(new OpenReadFileStatement("f", "test.in"), new CompoundStatement(
            new ReadFileStatement(new ArithmeticExpression('+', new VariableExpression("f"),
                new ConstantExpression(2)), "x"),
            new CompoundStatement(new PrintStatement(new VariableExpression("x")),
                new CompoundStatement(new IfStatement(new VariableExpression("x"),
                    new CompoundStatement(new ReadFileStatement(new VariableExpression("f"), "x"),
                        new PrintStatement(new VariableExpression("x"))),
                    new PrintStatement(new ConstantExpression(0))),
                    new CloseReadFileStatement(new VariableExpression("f"))))));

        /*
         * v = 10;
         * heapAllocation(v, 20);
         * heapAllocation(a, 22);
         * print(v);
         */
    Statement
        ex7 =
        new CompoundStatement(new AssignmentStatement("a", new ConstantExpression(10)),
            new CompoundStatement(new HeapAllocationStatement("v", new ConstantExpression(20)),
                new CompoundStatement(new HeapAllocationStatement("a", new ConstantExpression(22)),
                    new PrintStatement(new VariableExpression("v")))));

        /*
         * v = 10;
         * heapAllocation(v, 20);
         * heapAllocation(a, 22);
         * print(100 + heapRead(v));
         * print(100 + heapRead(a));
         */
    Statement
        ex8 =
        new CompoundStatement(new AssignmentStatement("a", new ConstantExpression(10)),
            new CompoundStatement(new HeapAllocationStatement("v", new ConstantExpression(20)),
                new CompoundStatement(new HeapAllocationStatement("a", new ConstantExpression(22)),
                    new CompoundStatement(new PrintStatement(
                        new ArithmeticExpression('+', new ConstantExpression(100),
                            new HeapReadExpression("v"))),
                        new PrintStatement(
                            new ArithmeticExpression('+', new ConstantExpression(100),
                                new HeapReadExpression("a")))))));

        /*
         * v = 10;
         * heapAllocation(v, 20);
         * heapAllocation(a, 22);
         * heapWrite(a, 30);
         * print(a);
         * print(readHeap(a));
         */
    Statement
        ex9 =
        new CompoundStatement(new AssignmentStatement("a", new ConstantExpression(10)),
            new CompoundStatement(new HeapAllocationStatement("v", new ConstantExpression(20)),
                new CompoundStatement(new HeapAllocationStatement("a", new ConstantExpression(22)),
                    new CompoundStatement(new HeapWriteStatement("a", new ConstantExpression(30)),
                        new CompoundStatement(new PrintStatement(new VariableExpression("a")),
                            new PrintStatement(new HeapReadExpression("a")))))));

        /*
         * v = 10;
         * heapAllocation(v, 20);
         * heapAllocation(a, 22);
         * heapWrite(a, 30);
         * print(a);
         * print(readHeap(a));
         * a = 0;
         */
    Statement
        ex10 =
        new CompoundStatement(new AssignmentStatement("a", new ConstantExpression(10)),
            new CompoundStatement(new HeapAllocationStatement("v", new ConstantExpression(20)),
                new CompoundStatement(new HeapAllocationStatement("a", new ConstantExpression(22)),
                    new CompoundStatement(new HeapWriteStatement("a", new ConstantExpression(30)),
                        new CompoundStatement(new PrintStatement(new VariableExpression("a")),
                            new CompoundStatement(new PrintStatement(new HeapReadExpression("a")),
                                new AssignmentStatement("a", new ConstantExpression(0))))))));

        /*
         * v = 6;
         * while (v - 4) {print(v); v = v - 1;};
         * print(v);
         */
    Statement ex11 = new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(6)),
        new CompoundStatement(new WhileStatement(
            new ArithmeticExpression('-', new VariableExpression("v"), new ConstantExpression(4)),
            new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                new AssignmentStatement("v",
                    new ArithmeticExpression('-', new VariableExpression("v"),
                        new ConstantExpression(1))))),
            new PrintStatement(new VariableExpression("v"))));

        /*
         * v = 6;
         * if (v >= 4); then print(v < 6); else print(v == 6);
         * print(6 != v);
         */
    Statement ex12 = new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(6)),
        new CompoundStatement(new IfStatement(
            new BooleanExpression(">=", new VariableExpression("v"), new ConstantExpression(4)),
            new PrintStatement(
                new BooleanExpression("<", new VariableExpression("v"), new ConstantExpression(6))),
            new PrintStatement(new BooleanExpression("==", new VariableExpression("v"),
                new ConstantExpression(6)))),
            new PrintStatement(new BooleanExpression("!=", new ConstantExpression(6),
                new VariableExpression("v")))));

        /*
         * v = 10;
         * heapAllocation(a, 30);
         * fork(heapWrite(a, 30); print(v); print(heapRead(a)););
         * print(v);
         * print(heapRead(a));
         */
    Statement ex13 = new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(10)),
        new CompoundStatement(new HeapAllocationStatement("a", new ConstantExpression(22)),
            new CompoundStatement(new ForkStatement(
                new CompoundStatement(new HeapWriteStatement("a", new ConstantExpression(30)),
                    new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(32)),
                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                            new PrintStatement(new HeapReadExpression("a")))))),
                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                    new PrintStatement(new HeapReadExpression("a"))))));

    TextMenu textMenu = new TextMenu();
    textMenu.addCommand(new ExitCommand(0, "Exit."));
    textMenu.addCommand(new RunExampleCommand(1, ex1.toString(), getController(ex1)));
    textMenu.addCommand(new RunExampleCommand(2, ex2.toString(), getController(ex2)));
    textMenu.addCommand(new RunExampleCommand(3, ex3.toString(), getController(ex3)));
    textMenu.addCommand(new RunExampleCommand(4, ex4.toString(), getController(ex4)));
    textMenu.addCommand(new RunExampleCommand(5, ex5.toString(), getController(ex5)));
    textMenu.addCommand(new RunExampleCommand(6, ex6.toString(), getController(ex6)));
    textMenu.addCommand(new RunExampleCommand(7, ex7.toString(), getController(ex7)));
    textMenu.addCommand(new RunExampleCommand(8, ex8.toString(), getController(ex8)));
    textMenu.addCommand(new RunExampleCommand(9, ex9.toString(), getController(ex9)));
    textMenu.addCommand(new RunExampleCommand(10, ex10.toString(), getController(ex10)));
    textMenu.addCommand(new RunExampleCommand(11, ex11.toString(), getController(ex11)));
    textMenu.addCommand(new RunExampleCommand(12, ex12.toString(), getController(ex12)));
    textMenu.addCommand(new RunExampleCommand(13, ex13.toString(), getController(ex13)));
    textMenu.addCommand(
        new SerializeCommand(14, "Serialize program: " + ex1.toString(), getRepository(ex1)));
    textMenu.addCommand(new DeserializeCommand(15, "Deserialize program.", getRepository(ex2)));

    textMenu.show();
  }
}
