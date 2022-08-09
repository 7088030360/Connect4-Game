package com.Shaurya.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane=loader.load();

        controller= loader.getController();

        controller.createPlayground();

        MenuBar menuBar=createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        Pane menuPane=(Pane)rootGridPane.getChildren().get(0);

        menuPane.getChildren().add(menuBar);

        Scene scene=new Scene(rootGridPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public MenuBar createMenu(){
        Menu fileMenu = new Menu("File");

        MenuItem newGame=new MenuItem("New Game");

        newGame.setOnAction(event -> controller.resetGame());
        MenuItem resetGame=new MenuItem("Reset Game");

        resetGame.setOnAction(event -> controller.resetGame());
        SeparatorMenuItem seperateMenuItem=new SeparatorMenuItem();
        MenuItem exitGame=new MenuItem("Exit Game");
        exitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitGame();
            }
        });

        fileMenu.getItems().addAll(newGame,resetGame,seperateMenuItem,exitGame);

        //help menu
        Menu helpMenu = new Menu("Help");

        MenuItem aboutGame=new MenuItem("About Connect4");
        aboutGame.setOnAction(event->aboutConnect4());
        SeparatorMenuItem seperater=new SeparatorMenuItem();
        MenuItem aboutMe=new MenuItem("About Me");
        aboutMe.setOnAction(event-> aboutMe());

        helpMenu.getItems().addAll(aboutGame,seperater,aboutMe);

        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);

        return menuBar;
    }

    private void aboutMe() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Shaurya Rastogi");
        alert.setContentText("Hi! I am Shaurya, a JAVA developer. I love to play around with code and create games.");
        alert.show();
    }

    private void aboutConnect4() {

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect four");
        alert.setHeaderText("How To Play");
        alert.setContentText("This game Connect-Four is a tic-tac-toe game played by two players.\n" +
                "In this game the players take turns placing pieces on a vertical board.\n" +
                "The board  is 7 columns long and 6 rows high.\n" +
                " Each player uses pieces of a specific color, usually black and red or sometimes yellow and red.\n" +
                "The goal is to be the first to get four pieces in a horizontal, vertical, or diagonal line.\n" +
                "Since the board is vertical, parts inserted in a certain column always fall in the lowest unoccupied row in that column.\n" +
                "As soon as a column contains 6 parts, it is full and no further parts can be placed on the column.\n" +
                "Both the players begin with 21 similar pieces and the first player to reach a series of four connected pieces wins the game.\n" +
                "If all the men have played and neither player has four parts in a row, the game is a tie.\n" +
                "While Tic-Tac-Toe is the original game of getting pieces in a row, Connect 4 is another classic strategy game.\n" +
                "Connect 4 is reminiscent of checkers and requires a lot of foresight.");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //todo
    }


    public static void main(String[] args) {
        launch(args);
    }
}
