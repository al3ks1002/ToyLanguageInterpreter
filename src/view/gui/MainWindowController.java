package view.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;
import controller.InterpreterController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.FileData;
import model.InterpreterException;
import model.ProgramState;
import model.statements.Statement;
import utils.ExecutionStack;
import utils.FileTable;
import utils.HeapTable;
import utils.LockTable;
import utils.Output;
import utils.SymbolTable;

public class MainWindowController implements Initializable {
  private InterpreterController interpreterController;

  @FXML
  private TableView<Map.Entry<Integer, Integer>> heapTableView;

  @FXML
  private TableColumn<Map.Entry<Integer, Integer>, Integer> heapAddressColumn;

  @FXML
  private TableColumn<Map.Entry<Integer, Integer>, Integer> heapValueColumn;

  @FXML
  private TableView<Map.Entry<Integer, String>> fileTableView;

  @FXML
  private TableColumn<Map.Entry<Integer, String>, Integer> fileIdentifierColumn;

  @FXML
  private TableColumn<Map.Entry<Integer, String>, String> fileNameColumn;

  @FXML
  private TableView<Map.Entry<String, Integer>> symbolTableView;

  @FXML
  private TableColumn<Map.Entry<String, Integer>, String> symbolTableVariableColumn;

  @FXML
  private TableColumn<Map.Entry<String, Integer>, Integer> symbolTableValueColumn;

  @FXML
  private TableView<Map.Entry<Integer, Integer>> lockTableView;

  @FXML
  private TableColumn<Map.Entry<Integer, Integer>, Integer> lockIdColumn;

  @FXML
  private TableColumn<Map.Entry<Integer, Integer>, Integer> programStateIdColumn;

  @FXML
  private ListView<Integer> outputListView;

  @FXML
  private ListView<Integer> programStateListView;

  @FXML
  private ListView<String> executionStackListView;

  @FXML
  private TextField numberOfProgramStatesTextField;

  @FXML
  private Button executeOneStepButton;

  public void setInterpreterController(InterpreterController interpreterController) {
    this.interpreterController = interpreterController;
    populateProgramStateIdentifiers();
  }

  private ProgramState getCurrentProgramState() {
    if (programStateListView.getSelectionModel().getSelectedIndex() == -1) {
      return null;
    }

    int currentId = programStateListView.getSelectionModel().getSelectedItem();
    return interpreterController.getRepository().getProgramStateWithId(currentId);
  }

  private List<Integer> getProgramStateIds(List<ProgramState> programStateList) {
    return programStateList.stream().map(ProgramState::getId).collect(Collectors.toList());
  }

  private void populateProgramStateIdentifiers() {
    List<ProgramState> programStateList = interpreterController.getRepository().getProgramStates();

    programStateListView
        .setItems(FXCollections.observableList(getProgramStateIds(programStateList)));

    numberOfProgramStatesTextField.setText("" + programStateList.size());
  }

  private void populateExecutionStack(ProgramState programState) {
    ExecutionStack<Statement> executionStack = programState.getExecutionStack();
    List<String> executionStackList = new ArrayList<>();
    for (Statement statement : executionStack.getAll()) {
      executionStackList.add(statement.toString());
    }
    executionStackListView.setItems(FXCollections.observableList(executionStackList));
    executionStackListView.refresh();
  }

  private void populateSymbolTable(ProgramState programState) {
    SymbolTable symbolTable = programState.getSymbolTable();
    List<Map.Entry<String, Integer>> symbolTableList = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : symbolTable.getAll()) {
      symbolTableList.add(entry);
    }
    symbolTableView.setItems(FXCollections.observableList(symbolTableList));
    symbolTableView.refresh();
  }

  private void populateOutput(ProgramState programState) {
    Output<Integer> output = programState.getOutput();
    List<Integer> outputList = new ArrayList<>();
    for (Integer number : output.getAll()) {
      outputList.add(number);
    }
    outputListView.setItems(FXCollections.observableList(outputList));
    outputListView.refresh();
  }

  private void populateFileTable(ProgramState programState) {
    FileTable fileTable = programState.getFileTable();
    Map<Integer, String> fileTableMap = new HashMap<>();
    for (Map.Entry<Integer, FileData> entry : fileTable.getAll()) {
      fileTableMap.put(entry.getKey(), entry.getValue().getFileName());
    }
    List<Map.Entry<Integer, String>> fileTableList = new ArrayList<>(fileTableMap.entrySet());
    fileTableView.setItems(FXCollections.observableList(fileTableList));
    fileTableView.refresh();
  }

  private void populateHeapTable(ProgramState programState) {
    HeapTable heapTable = programState.getHeapTable();
    List<Map.Entry<Integer, Integer>> heapTableList = new ArrayList<>();
    for (Map.Entry<Integer, Integer> entry : heapTable.getAll()) {
      heapTableList.add(entry);
    }
    heapTableView.setItems(FXCollections.observableList(heapTableList));
    heapTableView.refresh();
  }

  private void populateLockTable(ProgramState programState) {
    LockTable lockTable = programState.getLockTable();
    List<Map.Entry<Integer, Integer>> lockTableList = new ArrayList<>();
    for (Map.Entry<Integer, Integer> entry : lockTable.getAll()) {
      lockTableList.add(entry);
    }
    lockTableView.setItems(FXCollections.observableList(lockTableList));
    lockTableView.refresh();
  }

  private void changeProgramState(ProgramState currentProgramState) {
    if (currentProgramState == null) {
      return;
    }

    populateExecutionStack(currentProgramState);
    populateSymbolTable(currentProgramState);
    populateOutput(currentProgramState);
    populateFileTable(currentProgramState);
    populateHeapTable(currentProgramState);
    populateLockTable(currentProgramState);
  }

  private void executeOneStep() {
    if (interpreterController == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected", ButtonType.OK);
      alert.showAndWait();
      return;
    }

    int programStatesLeft = interpreterController.getRepository().getProgramStates().size();
    if (programStatesLeft == 0) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing left to execute", ButtonType.OK);
      alert.showAndWait();
      return;
    }

    try {
      interpreterController.executeOneStepGUI();
    } catch (InterpreterException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR, e.getCause().getMessage(), ButtonType.OK);
      alert.showAndWait();
      return;
    }

    changeProgramState(getCurrentProgramState());
    populateProgramStateIdentifiers();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    heapAddressColumn
        .setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
    heapValueColumn
        .setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue()).asObject());
    fileIdentifierColumn
        .setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
    fileNameColumn
        .setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue() + ""));
    symbolTableVariableColumn
        .setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey() + ""));
    symbolTableValueColumn
        .setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue()).asObject());
    lockIdColumn
        .setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
    programStateIdColumn
        .setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue()).asObject());

    programStateListView.setOnMouseClicked(mouseEvent -> {
      changeProgramState(getCurrentProgramState());
    });

    executeOneStepButton.setOnAction(actionEvent -> {
      executeOneStep();
    });
  }
}
