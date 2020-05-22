package cz.harag.psi.sp.ui;

import cz.harag.psi.sp.POP3Client;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @version 2020-05-22
 * @author Patrik Harag
 */
public class AppMainUI {

    private final POP3Client client;
    private final TextArea area;

    public AppMainUI(Stage stage, POP3Client client) {
        this.client = client;

        VBox box = new VBox(
                10,
                createLabel("Mails"),
                new HBox(
                        10,
                        refresh("Refresh")
                )
        );
        box.setPadding(new Insets(10));

        this.area = new TextArea();
        area.setStyle("-fx-font-family: 'monospaced';");

        SplitPane splitPane = new SplitPane(box, area);
        splitPane.setDividerPositions(0.35);

        stage.setTitle("POP3 client");
        Scene scene = new Scene(splitPane, 700, 500, true, SceneAntialiasing.BALANCED);
        stage.setScene(scene);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold");
        return label;
    }

    private Button refresh(String label) {
        Button button = new Button(label);
        button.setOnAction(e -> {

            try {
                String list = client.sendAndExpectMultiLine("LIST", null);
                area.setText(list);

            } catch (Exception ex) {

            }

        });
        return button;
    }

}