package io.datafx.samples.app;

import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * The controller of the master view. The @FXMLController annotation defines the FXML file that describes the view.
 */
@ViewController(value="listView.fxml", title = "User overview")
public class MasterViewController {

    /**
     * The edit button. This instance will be injected by JavaFX. This is defined by the @FXML annotation.
     * In addition a Flow action will be bound to the button. The action is defined by the unique id "edit"
     */
    @FXML
    @ActionTrigger("edit")
    private Button editButton;

    /**
     * The remove button. This instance will be injected by JavaFX. This is defined by the @FXML annotation.
     * In addition a Flow action will be bound to the button. The action is defined by the unique id "remove"
     */
    @FXML
    @ActionTrigger("remove")
    private Button removeButton;

    /**
     * The load button. This instance will be injected by JavaFX. This is defined by the @FXML annotation.
     * In addition a Flow action will be bound to the button. The action is defined by the unique id "load"
     */
    @FXML
    @ActionTrigger("load")
    private Button loadButton;

    /**
     * The list that shows the data
     */
    @FXML
    @ActionTrigger("edit")
    private ListView dataList;

    /**
     * The data model of the flow. This model will be injected by DataFX CDI.
     * Because the @FlowScoped annotation is defined in the class of the model there will be only one instance in each flow.
     */
    @Inject
    private DataModel model;

    /**
     * Any method that has the @PostConstruct annotation will be called when the controller is initialized.
     * This means that the method will be called after all data is injected
     */
    @PostConstruct
    public void init() {
        //This binds the model of the ListView to the data in the datamodel
        dataList.itemsProperty().bind(model.getPersons());

        model.selectedPersonIndexProperty().bind(dataList.getSelectionModel().selectedIndexProperty());
    }
}
