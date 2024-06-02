package com.simplexlanguageide;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

public class SimplexLanguageIDEController {
    public TextArea assemblyTextArea;
    @FXML
    private VBox vboxWindow;

    @FXML
    private MenuItem lexicalAnalysisMenuItem;

    @FXML
    private MenuItem syntaxAnalysisMenuItem;

    @FXML
    private MenuItem semanticAnalysisMenuItem;

    @FXML
    private MenuItem codeGeneratio;

    @FXML
    private MenuItem runMenuItem;

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
    private TextArea terminalTextArea;

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

        lexicalAnalysisMenuItem.setOnAction(event -> run("analysis", "lex -verbose"));
        syntaxAnalysisMenuItem.setOnAction(event -> run("analysis","parse -verbose"));
        semanticAnalysisMenuItem.setOnAction(event -> run("analysis","check -verbose"));
        codeGeneratio.setOnAction(event -> run("code","compile"));
        runMenuItem.setOnAction(event -> run("code","run"));
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



    @FXML
    private void run(String type, String arg) {
        String codeText = textArea.getText();

        terminalTextArea.clear();
        assemblyTextArea.clear();

        try {
            // Create a temporary file to store the code
            File tempFile = File.createTempFile("temp_code", ".txt");
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(codeText);
            }

            File tempFile2 = File.createTempFile("temp_code", ".txt");
            try (FileWriter writer = new FileWriter(tempFile2)) {
                writer.write(codeText);
            }

            // Get the absolute path of the temporary file
            String tempFilePath = tempFile.getAbsolutePath();
            String tempFilePath2 = tempFile2.getAbsolutePath();

            // Specify the absolute path to the Linux executable
            String executablePath = "src/main/resources/libs/simplex-language";

            // Construct the command to execute the Linux executable
            if (Objects.equals(arg, "compile") || Objects.equals(arg, "run")) {
                arg = "compile -out:" + tempFilePath2;
            }
            String command = executablePath + " " + tempFilePath + " " + arg;

            System.out.println("Command: " + command);

            // Execute the command using ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true); // Redirect error stream to input stream

            Process process = processBuilder.start();

            // Read output from the process and append it to terminalTextArea
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                terminalTextArea.setText(output.toString());
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            // Display the content of tempFile2 in the label description
            if (Objects.equals(type, "code")) {
                String content = Files.readString(tempFile2.toPath());
                assemblyTextArea.setText(content);
            }

            if (Objects.equals(type, "code")) {
                String command2 = "java -jar src/main/resources/libs/rars.jar a " + tempFilePath2;
                System.out.println(command2);

                ProcessBuilder processBuilder2 = new ProcessBuilder(command2.split(" "));
                processBuilder2.redirectErrorStream(true); // Redirect error stream to input stream

                Process process2 = processBuilder2.start();

                // Read output from the process and append it to terminalTextArea
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process2.getInputStream()))) {
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    terminalTextArea.setText(output.toString());
                }

            }

            // Delete the temporary files after execution
            tempFile.delete();
            tempFile2.delete();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
