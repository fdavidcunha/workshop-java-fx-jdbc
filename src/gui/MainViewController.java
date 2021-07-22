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

//Necessário implementar a interface Initializable para poder sobrescrever o método initialize.
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

	// synchronized -> Garante que o processamento/carregamento da tela não vai ser interrompido.
	private synchronized <T> void loadView(String absolutName, Consumer<T> initializingAction) {
		try {
			// Carregando a tela (xml) que vem como parâmetro para ser aberta.
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			VBox newVBox = loader.load();
			
			// Obtendo a referência para a cena do formulário principal, que neste caso é um VBox.
			Scene mainScene = Main.getMainScene();
			
			 // mainScene.getRoot() -> Obtém o primeiro elemento da view (que neste caso é o ScrollPane).
			 // ((ScrollPane) mainScene.getRoot()).getContent() -> Retorna o elemento <content> que está logo abaixo do ScrollPane.
			VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			
			// Obtendo uma referência para o menu principal, para que ele não seja perdido.
			// mainVBox.getChildren().get(0) -> Obtento o primeiro nó do VBox da tela principal.			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			// Limpando todos os filhos do menu.
			mainVBox.getChildren().clear();
			
			// Adicionando o menu novamente.
			mainVBox.getChildren().add(mainMenu);
			
			// Adicionando o conteúdo da nova tela.
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			// Executando a função passada como parâmetro.
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
