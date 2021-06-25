package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	@FXML
	private TableView<Department> table;
	
	@FXML
	private TableColumn<Department, Integer> columnID;	
	
	@FXML
	private TableColumn<Department, String> columnName;	

	@FXML
	private Button btnNovo;	
	
	@FXML
	public void onBtnNovoAction() {
		System.out.println("dfsdfsdfsd");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();		
	}

	public void initializeNodes() {
		columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage)Main.getMainScene().getWindow();
		table.prefHeightProperty().bind(stage.heightProperty());
	}
	
}
