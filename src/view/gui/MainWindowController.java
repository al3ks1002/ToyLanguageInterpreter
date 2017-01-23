package view.gui;

import java.net.URL;
import java.util.ResourceBundle;
import controller.InterpreterController;
import javafx.fxml.Initializable;

public class MainWindowController implements Initializable {
  private InterpreterController interpreterController;

  public void setInterpreterController(InterpreterController controller) {
    this.interpreterController = interpreterController;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}
