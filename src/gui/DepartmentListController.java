package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

// Necess�rio implementar a interface Initializable para poder sobrescrever o m�todo initialize.
public class DepartmentListController implements Initializable {

	@FXML private TableView<Department>            table;
	@FXML private TableColumn<Department, Integer> columnID;	
	@FXML private TableColumn<Department, String>  columnName;	
	@FXML private Button                           btnNovo;	
	
	private DepartmentService service;

	// Lista que ser� exibida na grid.
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtnNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department dep = new Department();
		createDialogForm(dep, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();		
	}

	// Esse m�todo � necess�rio para inicializar os componentes declarados acima.
	public void initializeNodes() {
		// Padr�o JavaFX para inicializar o comportamento das colunas.
		columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		// Obtendo uma refer�ncia para o Stage atual.
		// Main.getMainScene() -> Acessa a cena.
		// Main.getMainScene().getWindow() -> Obtendo uma refer�ncia para a janela, 
		// que � uma superclasse do Stage, por isso deve-se fazer o downcast para Stage. 
		Stage stage = (Stage)Main.getMainScene().getWindow();

		// Alinhando a grid ao tamanho da tela.
		table.prefHeightProperty().bind(stage.heightProperty());
	}
	
	// Acessa o servi�o e carrega a lista de departamentos. 
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service is null");
		}
		
		// Carregando os departamentos obtidos dentro da lista/grid.
		List<Department> list = service.findAll();
		obsList = FXCollections.observableList(list);
		table.setItems(obsList);
	}
	
	private void createDialogForm(Department department, String absolutName, Stage parentStage) {
		try {
			// Carregando a tela (xml) que vem como par�metro para ser aberta.
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();
			
			// Obtendo uma refer�ncia para o controlador.
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(department);
			controller.updateForData();
			
			// Quando se carrega uma janela modal na frente de outra existente, � necess�rio instanciar outro Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Departamento");            // T�tulo da tela;
			dialogStage.setScene(new Scene(pane));           // Cena do Stage;
			dialogStage.setResizable(false);                 // N�o permite redimencionar a tela;
			dialogStage.initOwner(parentStage);              // Quem � o owner da tela;
			dialogStage.initModality(Modality.WINDOW_MODAL); // Informando que a tela ser� um modal;
			dialogStage.showAndWait();                       // Abre a tela em formato modal;
			
		} catch (IOException e) {
			Alerts.showAlert("IO Excepetion", "Erro carregando formul�rio", e.getMessage(), AlertType.ERROR);
		}
		
	}
	
}
