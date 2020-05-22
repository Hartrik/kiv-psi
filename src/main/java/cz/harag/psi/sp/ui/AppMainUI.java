package cz.harag.psi.sp.ui;

import java.util.List;

import cz.harag.psi.sp.POP3Client;
import cz.harag.psi.sp.POP3ClientHelper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
    private final ListView<String> listView;

    public AppMainUI(Stage stage, POP3Client client) {
        this.client = client;

        listView = new ListView<>();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            show(newValue);
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem remove = new MenuItem("Remove");
        remove.setOnAction(event -> {
            String id = listView.getSelectionModel().getSelectedItem();
            if (id != null) {
                Thread thread = new Thread(() -> {
                    try {
                        POP3ClientHelper.delete(client, id);
                        Platform.runLater(this::refreshListView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        });
        contextMenu.getItems().add(remove);
        listView.setContextMenu(contextMenu);

        VBox box = new VBox(
                10,
                createLabel("Mails"),
                new HBox(
                        10,
                        reset("Revert modifications")
                ),
                listView
        );
        box.setPadding(new Insets(10));

        this.area = new TextArea();
        area.setStyle("-fx-font-family: 'monospaced';");

        SplitPane splitPane = new SplitPane(box, area);
        splitPane.setDividerPositions(0.35);

        stage.setTitle("POP3 client");
        Scene scene = new Scene(splitPane, 700, 500, true, SceneAntialiasing.BALANCED);
        stage.setScene(scene);

        refreshListView();
    }

    private void refreshListView() {
        listView.getItems().clear();
        Thread thread = new Thread(() -> {
            try {
                List<String> list = POP3ClientHelper.list(client);
                Platform.runLater(() -> {
                    listView.getItems().setAll(list);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void show(String id) {
        area.setText("");
        if (id != null) {
            Thread thread = new Thread(() -> {
                try {
                    String mail = POP3ClientHelper.rawMail(client, id);
                    Platform.runLater(() -> {
                        area.setText(mail);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold");
        return label;
    }

    private Button reset(String label) {
        Button button = new Button(label);
        button.setOnAction(e -> {
            Thread thread = new Thread(() -> {
                try {
                    POP3ClientHelper.reset(client);
                    Platform.runLater(this::refreshListView);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
        });
        return button;
    }

}