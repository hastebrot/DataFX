package io.datafx.samples.eventsystem;

import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by hendrikebbers on 21.10.14.
 */
public class EventSystemDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox box = new HBox();
        box.setSpacing(12);
        box.setPadding(new Insets(12));
        box.setFillHeight(true);
        box.setAlignment(Pos.CENTER);

        Flow senderFlow = new Flow(ProducerController.class);
        box.getChildren().add(senderFlow.start());

        Flow receiverFlow = new Flow(ReceiverController.class);
        box.getChildren().add(receiverFlow.start());

        primaryStage.setScene(new Scene(box));
        primaryStage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
