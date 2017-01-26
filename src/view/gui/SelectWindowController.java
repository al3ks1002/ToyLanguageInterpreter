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
import model.statements.OpenReadFileStatement;
import model.statements.PrintStatement;
import model.statements.ReadFileStatement;
import model.statements.Statement;
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

    Statement forS = new ForStatement("v", new ConstantExpression(0), new ConstantExpression(3),
        new ArithmeticExpression('+', new VariableExpression("v"), new ConstantExpression(1)),
        new ForkStatement(
            new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                new AssignmentStatement("v", new ArithmeticExpression('+',
                    new VariableExpression("v"), new ConstantExpression(1))))));

    Statement printS = new PrintStatement(new ArithmeticExpression('*',
        new VariableExpression("v"), new ConstantExpression(10)));
    Statement comp = new CompoundStatement(forS, printS);
    Statement ex14 =
        new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(20)), comp);

    programStatements =
        new ArrayList<>(
            Arrays.asList(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10, ex11, ex12, ex13, ex14));
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
