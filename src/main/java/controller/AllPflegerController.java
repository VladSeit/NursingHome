package controller;

import datastorage.DAOFactory;
import datastorage.PatientDAO;
import datastorage.PflegerDAO;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Patient;
import model.Pfleger;
import model.Treatment;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AllPflegerController {

    @FXML
    private TableView<Pfleger> tableView;
    @FXML
    private TableColumn<Pfleger, Integer> colID;
    @FXML
    private TableColumn<Pfleger, String> colFirstname;
    @FXML
    private TableColumn<Pfleger, String> colSurname;
    @FXML
    private TableColumn<Pfleger, String> colDate;
    private ObservableList<Pfleger> tableviewContent = FXCollections.observableArrayList();
    private PflegerDAO dao;

    /**
     * Initializes the corresponding fields. Is called as soon as the corresponding FXML file is to be displayed.
     *
     */
    public void initialize() {

        readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<Pfleger, Integer>("pfid"));
        this.colFirstname.setCellValueFactory(new PropertyValueFactory<Pfleger,String>("firstName"));
        this.colSurname.setCellValueFactory(new PropertyValueFactory<Pfleger,String>("surname"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<Pfleger, String>("dateOfBirth"));
        this.tableView.setItems(this.tableviewContent);

    }

    /**
     * Get all pflegern from the datenbank and show it in tableview
     */
    private void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createPflegerDAO();
        List<Pfleger> allPflegers;
        try {
            allPflegers = dao.readAll();
            for (Pfleger p : allPflegers) {
                this.tableviewContent.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * archive selected pfleger in tableview
     */
    @FXML
    public void lockData(){
        int id = this.tableView.getSelectionModel().getSelectedIndex();
        Pfleger p = this.tableviewContent.remove(id);
        PflegerDAO dao = DAOFactory.getDAOFactory().createPflegerDAO();
        p.setIsLocked("true");
        p.setDateOfLocking(LocalDate.now());
        try {
            dao.update(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
