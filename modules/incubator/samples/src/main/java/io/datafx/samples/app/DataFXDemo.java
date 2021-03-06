package io.datafx.samples.app;

import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main Class of the DataFX demo.
 * In this class a DataFX Flow is created. The flow is
 */
public class DataFXDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        //This creates the flow. The MasterView will be the start view
        Flow flow = new Flow(MasterViewController.class).

                // By using the fluent API a link is added to the flow. The link has the unique id "edit" and will link from the MasterView to the EditView
                withLink(MasterViewController.class, "edit", EditViewController.class).

                // A link is added to the flow. The link has the unique id "save" and will link from the EditView to the MasterView
                withLink(EditViewController.class, "save", MasterViewController.class).

                // An action is added to the flow. The action is registered fot the MasterView and has the unique id "remove". The action is implemented by the RemoveActionTask class
                withTaskAction(MasterViewController.class, "remove", RemoveActionTask.class).

                // An action is added to the flow. The action is registered fot the MasterView and has the unique id "load". The action is implemented by the LoadPersonsTask class
                withTaskAction(MasterViewController.class, "load", LoadPersonsTask.class);


        //This starts the Flow
        StackPane parent = flow.start();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();

    }
}
