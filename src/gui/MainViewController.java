package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

//Necess�rio implementar a interface Initializable para poder sobrescrever o m�todo initialize.
public class MainViewController implements Initializable {

	@FXML private MenuItem menuItemVendedor;
	@FXML private MenuItem menuItemDepartamento;
	@FXML private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("Vendedor");
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemSobreAction() {
		loadView("/gui/Sobre.fxml", x -> {});
	}

	// synchronized -> Garante que o processamento/carregamento da tela n�o vai ser interrompido.
	private synchronized <T> void loadView(String absolutName, Consumer<T> initializingAction) {
		try {
			// Carregando a tela (xml) que vem como par�metro para ser aberta.
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			// Obtendo a refer�ncia para a cena do formul�rio principal, que neste caso � um VBox.
			Scene mainScene = Main.getMainScene();
			
			 // mainScene.getRoot() -> Obt�m o primeiro elemento da view (que neste caso � o ScrollPane).
			 // ((ScrollPane) mainScene.getRoot()).getContent() -> Retorna o elemento <content> que est� logo abaixo do ScrollPane.
			VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			
			// Obtendo uma refer�ncia para o menu principal, para que ele n�o seja perdido.
			// mainVBox.getChildren().get(0) -> Obtento o primeiro n� do VBox da tela principal.			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			// Limpando todos os filhos do menu.
			mainVBox.getChildren().clear();
			
			// Adicionando o menu novamente.
			mainVBox.getChildren().add(mainMenu);
			
			// Adicionando o conte�do da nova tela.
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			// Executando a fun��o passada como par�metro.
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

}
