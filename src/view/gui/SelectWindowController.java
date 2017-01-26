package view.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import controller.InterpreterController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.ProgramState;
import model.expressions.ArithmeticExpression;
import model.expressions.BooleanExpression;
import model.expressions.ConstantExpression;
import model.expressions.HeapReadExpression;
import model.expressions.VariableExpression;
import model.statements.AssignmentStatement;
import model.statements.CloseReadFileStatement;
import model.statements.CompoundStatement;
import model.statements.ForStatement;
import model.statements.ForkStatement;
import model.statements.HeapAllocationStatement;
import model.statements.HeapWriteStatement;
import model.statements.IfStatement;
import model.statements.LockStatement;
import model.statements.NewLockStatement;
import model.statements.OpenReadFileStatement;
import model.statements.PrintStatement;
import model.statements.ReadFileStatement;
import model.statements.Statement;
import model.statements.UnlockStatement;
import model.statements.WhileStatement;
import repository.ProgramStateRepository;
import repository.ProgramStateRepositoryImpl;

public class SelectWindowController implements Initializable {
  private List<Statement> programStatements;
  private MainWindowController mainWindowController;

  @FXML
  private ListView<String> programListView;

  @FXML
  private Button executeButton;

  public void setMainWindowController(MainWindowController mainWindowController) {
    this.mainWindowController = mainWindowController;
  }

  private void buildProgramStatements() {
        /*
        * a = 2 + 3 * 5;
        * b = a + 1;
        * print(b);
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

    ///////////

    Statement a = new HeapAllocationStatement("v1", new ConstantExpression(20));
    Statement b = new HeapAllocationStatement("v2", new ConstantExpression(30));
    Statement c = new NewLockStatement("x");

    Statement a1 = new LockStatement("x");
    Statement
        b1 =
        new HeapWriteStatement("v1",
            new ArithmeticExpression('-', new HeapReadExpression("v1"), new ConstantExpression(1)));
    Statement c1 = new UnlockStatement("x");

    Statement a2 = new LockStatement("x");
    Statement
        b2 =
        new HeapWriteStatement("v1",
            new ArithmeticExpression('+', new HeapReadExpression("v1"), new ConstantExpression(1)));
    Statement c2 = new UnlockStatement("x");
    Statement
        d =
        new ForkStatement(new CompoundStatement(
            new ForkStatement(new CompoundStatement(a1, new CompoundStatement(b1, c1))),
            new CompoundStatement(a2, new CompoundStatement(b2, c2))));

    Statement e = new NewLockStatement("q");

    Statement a3 = new LockStatement("q");
    Statement
        b3 =
        new HeapWriteStatement("v2",
            new ArithmeticExpression('+', new HeapReadExpression("v2"), new ConstantExpression(5)));
    Statement c3 = new UnlockStatement("q");
    Statement x = new ForkStatement(new CompoundStatement(a3, new CompoundStatement(b3, c3)));

    Statement a4 = new AssignmentStatement("m", new ConstantExpression(100));
    Statement b4 = new LockStatement("q");
    Statement c4 = new HeapWriteStatement("v2",
        new ArithmeticExpression('+', new HeapReadExpression("v2"), new ConstantExpression(1)));
    Statement d4 = new UnlockStatement("q");
    Statement
        y =
        new CompoundStatement(a4, new CompoundStatement(b4, new CompoundStatement(c4, d4)));

    Statement f = new ForkStatement(new CompoundStatement(x, y));

    Statement a5 = new AssignmentStatement("z", new ConstantExpression(200));
    Statement b5 = new AssignmentStatement("z", new ConstantExpression(300));
    Statement c5 = new AssignmentStatement("z", new ConstantExpression(400));
    Statement d5 = new AssignmentStatement("z", new ConstantExpression(500));
    Statement
        g =
        new CompoundStatement(a5, new CompoundStatement(b5, new CompoundStatement(c5, d5)));

    Statement a6 = new LockStatement("x");
    Statement b6 = new PrintStatement(new HeapReadExpression("v1"));
    Statement c6 = new UnlockStatement("x");
    Statement h = new CompoundStatement(a6, new CompoundStatement(b6, c6));

    Statement a7 = new LockStatement("q");
    Statement b7 = new PrintStatement(new HeapReadExpression("v2"));
    Statement c7 = new UnlockStatement("q");
    Statement i = new CompoundStatement(a7, new CompoundStatement(b7, c7));

    Statement
        ex14 =
        new CompoundStatement(a, new CompoundStatement(b, new CompoundStatement(c,
            new CompoundStatement(d, new CompoundStatement(e, new CompoundStatement(f,
                new CompoundStatement(g, new CompoundStatement(h, i))))))));

    ///////////

    Statement
        ex15 =
        new CompoundStatement(new NewLockStatement("q"), new CompoundStatement(new ForkStatement(
            new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(1)),
                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                    new LockStatement("q")))), new LockStatement("q")));

    Statement ex16 = new CompoundStatement(new NewLockStatement("x"), new UnlockStatement("q"));
    Statement ex17 = new CompoundStatement(new NewLockStatement("x"), new LockStatement("q"));

    Statement forS = new ForStatement("v", new ConstantExpression(0), new ConstantExpression(3),
        new ArithmeticExpression('+', new VariableExpression("v"), new ConstantExpression(1)),
        new ForkStatement(
            new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                new AssignmentStatement("v", new ArithmeticExpression('+',
                    new VariableExpression("v"), new ConstantExpression(1))))));

    Statement printS = new PrintStatement(new ArithmeticExpression('*',
        new VariableExpression("v"), new ConstantExpression(10)));
    Statement comp = new CompoundStatement(forS, printS);
    Statement ex18 =
        new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(20)), comp);

    programStatements =
        new ArrayList<>(
            Arrays
                .asList(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10, ex11, ex12, ex13, ex14,
                    ex15, ex16, ex17, ex18));
  }

  private List<String> getStringRepresentations() {
    return programStatements.stream().map(Statement::toString).collect(Collectors.toList());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    buildProgramStatements();
    programListView.setItems(FXCollections.observableArrayList(getStringRepresentations()));

    executeButton.setOnAction(actionEvent -> {
      int index = programListView.getSelectionModel().getSelectedIndex();

      if (index < 0) {
        return;
      }

      ProgramState initialProgramState = new ProgramState(programStatements.get(index));
      ProgramStateRepository repository = new ProgramStateRepositoryImpl("log" + index + ".txt");
      repository.addProgramState(initialProgramState);
      InterpreterController interpreterController = new InterpreterController(repository);

      mainWindowController.setInterpreterController(interpreterController);
    });
  }
}
