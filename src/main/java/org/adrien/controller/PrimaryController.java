package org.adrien.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.adrien.App;
import org.adrien.model.Client;
import org.adrien.model.ClientDAO;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    private TableView<Client> lst_clients;
    @FXML
    private TableColumn<Client, String> col_prenom;
    @FXML
    private TableColumn<Client, String> col_nom;
    @FXML
    private TableColumn<Client, String> col_ville;
    @FXML
    private Button btn_sauver;
    @FXML
    private Button btn_annuler;
    @FXML
    private Button btn_supprimer;
    @FXML
    private Button btn_update;
    @FXML
    private TextField text_prenom;
    @FXML
    private TextField text_nom;
    @FXML
    private TextField text_ville;
    @FXML
    private Button btn_export;

    ObservableList<Client> model = FXCollections.observableArrayList();

    /**
     * Initialize TableView and data before the launch.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ClientDAO clientDAO = new ClientDAO();
        ArrayList client = clientDAO.List();
        for (int i =0;i<client.size();i++){
            model.add((Client) client.get(i));
        }
        lst_clients.setEditable(false);
        // Jonction du tableau avec les données
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_ville.setCellValueFactory(new PropertyValueFactory<>("ville"));
        // Indique au TableView le model à observer pour trouver les données
        lst_clients.setItems(model);
        getDataModelSelected();
    }

    /**
     * Save event to save data.
     * @param event
     * @throws SQLException
     */
    @FXML
    private void save(ActionEvent event) throws SQLException {
        Client client = new Client();
        ClientDAO repo = new ClientDAO();
        client.setPrenom(text_prenom.getText());
        client.setNom(text_nom.getText());
        client.setVille(text_ville.getText());
        repo.Insert(client);
        model.add(client);
    }

    /**
     * Update event to Update data.
     * @param event
     * @throws SQLException
     */
    @FXML
    private void update(ActionEvent event) throws SQLException {
        ClientDAO repo = new ClientDAO();
        Client client = new Client(lst_clients.getSelectionModel().getSelectedIndex(),text_nom.getText(),text_prenom.getText(),text_ville.getText());
        client.setId(lst_clients.getSelectionModel().getSelectedItem().getId());
        client.setPrenom(text_prenom.getText());
        client.setNom(text_nom.getText());
        client.setVille(text_ville.getText());
        repo.Update(client);
        model.clear();
        ArrayList clientList = repo.List();
        model.addAll(clientList);
    }

    /**
     * Delete event to Delete data.
     * @param event
     * @throws SQLException
     */
    @FXML
    private void delete(ActionEvent event) throws SQLException {
        ClientDAO repo = new ClientDAO();
        Client client = new Client(lst_clients.getSelectionModel().getSelectedIndex());
        client.setId(lst_clients.getSelectionModel().getSelectedItem().getId());
        System.out.println(client.getId());
        repo.Delete(client);
        model.clear();
        ArrayList clientList = repo.List();
        model.addAll(clientList);
    }

    /**
     * Cancel the user's input.
     * @param event
     */
    @FXML
    private void annuler(ActionEvent event) {
    text_prenom.clear();
    text_nom.clear();
    text_ville.clear();
    }

    /**
     * Get the information of the selected row in the textFields areas.
     */
    @FXML
    private void getDataModelSelected(){
        lst_clients.setOnMouseClicked(event1 -> {
            Client client2 = new Client(lst_clients.getSelectionModel().getSelectedIndex());
            client2.setId(lst_clients.getSelectionModel().getSelectedItem().getId());
            client2.setNom(lst_clients.getSelectionModel().getSelectedItem().getNom());
            client2.setPrenom(lst_clients.getSelectionModel().getSelectedItem().getPrenom());
            client2.setVille(lst_clients.getSelectionModel().getSelectedItem().getVille());
            text_nom.setText(client2.getNom());
            text_prenom.setText(client2.getPrenom());
            text_ville.setText(client2.getVille());
        });
    }

    /**
     * Export the selected client to Json file.
     * @param event
     */
    @FXML
    private void sauvHandler(ActionEvent event) {

        JSONObject sauvegarde = new JSONObject();
        sauvegarde.put("prenom",text_prenom.getText());
        sauvegarde.put("nom",text_nom.getText());
        sauvegarde.put("ville",text_ville.getText());
        // Déclaration du FileChoser
        FileChooser fileChooser = new FileChooser();
        // Ajout d'un filtre d'extention
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Json file (*.json", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);
        //Affichage du popup
        File file = fileChooser.showSaveDialog(App.getStage());
        if (file != null) {
            //Si l'extension n'est pas bien renseignée on l'ajoute
            if (!file.getPath().endsWith(".json")) file = new File(file.getPath() + ".json");
            //On écrit le fichier avec un try..catch en cas d'erreur
            try (FileWriter fichier = new FileWriter(file)) {
                fichier.write(sauvegarde.toJSONString());
                fichier.flush();
            }catch (IOException e){
                //On affiche l'erreur en console
                System.out.println(e.getMessage());
            }
        }
    }
}
