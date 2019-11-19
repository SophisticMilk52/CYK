package application;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Modelo;
import model.Produccion;

public class SampleController implements Initializable {
	private Modelo modelo;
	@FXML
	private TextField campo;

	@FXML
	private Button btnCYK;

	@FXML

	private ScrollPane scroll;

	@FXML
	private GridPane grid;

	@FXML
	private Button agregar;
	private ArrayList<ComboBox> arreglo = new ArrayList();
	private ObservableList<String> options = FXCollections.observableArrayList("S", "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K"

	);
	private ArrayList<String> disponibles = new ArrayList(options);

	private ArrayList<TextField> texts = new ArrayList();

	@FXML
	void agregarfila(ActionEvent event) {

		ComboBox comboBox = new ComboBox(options);
		TextField text = new TextField();
		comboBox.setCellFactory(lv -> new ListCell<String>() {

			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					setText(item.toString());
					setDisable(!disponibles.contains(item.toString()));
				}
			}
		});

		comboBox.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue ov, String viejo, String nuevo) {
				// comboBox.getItems().remove(viejo);
				try {

					disponibles.add(viejo);
					disponibles.remove(nuevo);
					for (int i = 0; i < arreglo.size(); i++) {
						// arreglo.get(i).getItems().clear();
						arreglo.get(i).setCellFactory(lv -> new ListCell<String>() {

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setText(null);
								} else {
									setText(item.toString());
									setDisable(!disponibles.contains(item.toString()));
								}
							}
						});

					}

					// comboBox.setValue(nuevo);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Basuron");
					System.out.println(e.getMessage());
				}

			}

		});
		arreglo.add(comboBox);
		texts.add(text);
		grid.addRow(grid.impl_getRowCount(), comboBox, text);

	}

	@FXML
	public void validar(ActionEvent event) {
		ArrayList<Produccion> pr = new ArrayList();
		boolean cond = false;
		for (int i = 0; i < arreglo.size(); i++) {
			for (int j = 0; j < texts.size(); j++) {

				String[] all = texts.get(j).getText().split(",");
				try {
					modelo.addProduccion(arreglo.get(i).getValue().toString(), all);
				} catch (Exception e) {

					if (cond == false) {

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning Dialog");
						alert.setHeaderText("Faltan campos por llenar");
						alert.setContentText("La gramatica debe de estar incompleta");
						alert.showAndWait();
						cond = true;
					}
				}
			}
		}
		if (campo.getText().equals("")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("campo cadena vacio");
			alert.setContentText("El campo de la cadena esta vcacio");
			alert.showAndWait();
		} else {
			if (cond == false) {
				try {

					if (modelo.CYK(campo.getText()) == true && cond == false) {

						Alert alertw = new Alert(AlertType.INFORMATION);
						alertw.setTitle("Information Dialog");
						alertw.setHeaderText("Resultado CYK");
						alertw.setContentText("la cadena " + campo.getText() + " si es generada por la gramatica");
						alertw.showAndWait();

					} else if (modelo.CYK(campo.getText()) == false && cond == false) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Information Dialog");
						alert.setHeaderText("Resultado CYK");
						alert.setContentText("la cadena " + campo.getText() + " no es generada por la gramatica");

						alert.showAndWait();
					}

				} catch (Exception e) {

					Alert alertw = new Alert(AlertType.WARNING);
					alertw.setTitle("Warning Dialog");
					alertw.setHeaderText("Por favor crear la gramatica");
					alertw.setContentText("No se a creado los campos de la gramatica");
					alertw.showAndWait();
				}
			}
		}

	}

	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		Label label1 = new Label("Variables");
		Label label2 = new Label("Producciones");
		grid.setConstraints(label1, 0, 0);
		grid.setConstraints(label2, 1, 0);
		grid.getChildren().add(label1);
		grid.getChildren().add(label2);
		modelo = new Modelo();
	}

}
