package ekud.ui;
// DialogBox.java is taken from https://se-education.org/guides/tutorials/javaFxPart4.html
import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * An example of a custom control using FXML.
 * This control represents a dialog box consisting of an ImageView to represent the speaker's face and a label
 * containing text from the speaker.
 * (https://se-education.org/guides/tutorials/javaFxPart4.html)
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;
    private static final String USER_DIALOG_BG_COLOR = "#D5FFC9";
    private static final String DUKE_DIALOG_BG_COLOR = "#DCFFFD";


    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);

        // Clip display picture to a circle, credits to
        // https://stackoverflow.com/questions/20708295/put-a-image-in-a-circle-view
        Circle circleClip = new Circle(50, 50, 48);
        displayPicture.setClip(circleClip);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.setStyle(String.format("-fx-background-color: %s", USER_DIALOG_BG_COLOR));
        return db;
    }

    public static DialogBox getDukeDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
<<<<<<< Updated upstream
        db.setStyle(String.format("-fx-background-color: %s", DUKE_DIALOG_BG_COLOR));
=======
        db.dialog.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 20 20 20 20;"
                + "-fx-padding: 12 12 12 12; -fx-margin: 0 100 0 0",
                DUKE_DIALOG_BG_COLOR));
        // Align the ekud dialog to the bottom left w.r.t the dialog box
        db.setAlignment(Pos.CENTER_LEFT);
        // Set right padding in dialog box so ekud dialog has some space from the right
        db.setStyle("-fx-padding: 5 50 5 5;");
>>>>>>> Stashed changes
        return db;
    }
}

