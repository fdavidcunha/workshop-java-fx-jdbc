package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	private DepartmentService service;
	
	@FXML
	private TableView<Department> table;
	
	@FXML
	private TableColumn<Department, Integer> columnID;	
	
	@FXML
	private TableColumn<Department, String> columnName;	

	@FXML
	private Button btnNovo;	
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtnNovoAction() {
		System.out.println("dfsdfsdfsd");
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();		
	}

	public void initializeNodes() {
		columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// Alinhando a grid ao tamanho da tela.
		Stage stage = (Stage)Main.getMainScene().getWindow();
		table.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service is null");
		}
		
		List<Department> list = service.findAll();
		obsList = FXCollections.observableList(list);
		table.setItems(obsList);
	}
	
}
