package com.simplexlanguageide;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SimplexLanguageIDEController {

    public VBox vboxWindow;
    @FXML
    private MenuItem newMenuItem;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private MenuItem saveAsMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem revertMenuItem;

    @FXML
    private MenuItem quitMenuItem;

    @FXML
    private TextArea textArea;

    private File currentFile;

    @FXML
    private MenuItem undoMenuItem;

    @FXML
    private MenuItem redoMenuItem;

    @FXML
    private MenuItem cutMenuItem;

    @FXML
    private MenuItem copyMenuItem;

    @FXML
    private MenuItem pasteMenuItem;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private MenuItem selectAllMenuItem;

    @FXML
    private MenuItem unselectAllMenuItem;

    @FXML
    public void initialize() {
        newMenuItem.setOnAction(event -> handleNew());
        openMenuItem.setOnAction(event -> handleOpen());
        saveMenuItem.setOnAction(event -> handleSave());
        saveAsMenuItem.setOnAction(event -> handleSaveAs());
        closeMenuItem.setOnAction(event -> handleClose());
        revertMenuItem.setOnAction(event -> handleRevert());
        quitMenuItem.setOnAction(event -> handleQuit());


        quitMenuItem.setOnAction(event -> handleQuit());
        undoMenuItem.setOnAction(event -> handleUndo());
        redoMenuItem.setOnAction(event -> handleRedo());
        cutMenuItem.setOnAction(event -> handleCut());
        copyMenuItem.setOnAction(event -> handleCopy());
        pasteMenuItem.setOnAction(event -> handlePaste());
        deleteMenuItem.setOnAction(event -> handleDelete());
        selectAllMenuItem.setOnAction(event -> handleSelectAll());
        unselectAllMenuItem.setOnAction(event -> handleUnselectAll());
    }

    private void handleNew() {
        textArea.clear();
        currentFile = null;
    }

    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                textArea.setText(content);
                currentFile = file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSave() {
        if (currentFile != null) {
            saveToFile(currentFile);
        } else {
            handleSaveAs();
        }
    }

    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            saveToFile(file);
            currentFile = file;
        }
    }

    private void saveToFile(File file) {
        try {
            Files.writeString(file.toPath(), textArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClose() {
        textArea.clear();
        currentFile = null;
    }

    private void handleRevert() {
        if (currentFile != null) {
            try {
                String content = Files.readString(currentFile.toPath());
                textArea.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleQuit() {
        Platform.exit();
    }


    private void handleUndo() {
        textArea.undo();
    }

    private void handleRedo() {
        textArea.redo();
    }

    private void handleCut() {
        textArea.cut();
    }

    private void handleCopy() {
        textArea.copy();
    }

    private void handlePaste() {
        textArea.paste();
    }

    private void handleDelete() {
        textArea.replaceSelection("");
    }

    private void handleSelectAll() {
        textArea.selectAll();
    }

    private void handleUnselectAll() {
        textArea.deselect();
    }
}
