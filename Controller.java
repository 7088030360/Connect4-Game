package com.Shaurya.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable{

	public static final int COLUMNS=7;
	public static final int ROWS=6;
	public static final int CIRCLE_DIAMETER=80;
	public static final String discColor1="#24303E";
	public static final String discColor2="#4CAABB";


	private static String PLAYER_ONE="Player One";
	private static String PLAYER_TWO="Player Two";


	private boolean isPlayOneTurn=true;

	private Disc[][] insertedDiscsArray=new Disc[ROWS][COLUMNS];

	@FXML
	public Label playerNameLabel;

	private boolean isAllowedToInsert=true;

	public void createPlayground(){

		setNamesButton.setOnAction(event->
		{
			PLAYER_ONE=playerOneTextField.getText();
			PLAYER_TWO=playerTwoTextField.getText();

		});

			setNamesButton.setOnMouseClicked(newEvent->
			{


				Alert alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Player Information");
				alert.setHeaderText("Participants are "+ PLAYER_ONE  + " & "+  PLAYER_TWO);
				alert.setContentText("Press OK to Continue ");
				alert.show();
			});



		Shape rectangeWithHoles=createGameStructuralGrid();

		rootGridPane.add(rectangeWithHoles,0,1);

		List<Rectangle> rectangleLsit=createClickableColumns();
		for(Rectangle rectangle:rectangleLsit) {
			rootGridPane.add(rectangle, 0, 1);


		}
	}

	private Shape createGameStructuralGrid(){
		Shape rectangeWithHoles=new Rectangle((COLUMNS+1)*CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);

		for(int row=0;row<ROWS;row++) {
			for (int col = 0; col < COLUMNS; col++) {

				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER / 2);
				circle.setCenterX(CIRCLE_DIAMETER / 2);
				circle.setCenterY(CIRCLE_DIAMETER / 2);
				circle.setSmooth(true);

				circle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
				circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

				rectangeWithHoles = Shape.subtract(rectangeWithHoles, circle);

			}
		}

		rectangeWithHoles.setFill(Color.WHITE);
		return rectangeWithHoles;


	}

	private List<Rectangle> createClickableColumns(){
		List<Rectangle> rectangleList=new ArrayList<>();

		for(int col=0;col<COLUMNS;col++) {
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER / 4);

			rectangle.setOnMouseEntered(event->rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event->rectangle.setFill(Color.TRANSPARENT));

			final int column=col;
			rectangle.setOnMouseClicked(event->{
				if(isAllowedToInsert) {
					isAllowedToInsert=false;
					insertDisk(new Disc(isPlayOneTurn), column);
				}
			});

			rectangleList.add(rectangle);
		}

		return rectangleList;
	}
	private  void insertDisk(Disc disc,int column){

		int row=ROWS-1;
		while(row>=0){
			if(getDiscIfPresent(row,column)==null)
				break;
			row--;
		}
		if(row<0)
			return;



		insertedDiscsArray[row][column]=disc;
		insertedDiscPane.getChildren().add(disc);

		disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

		int currentRow=row;
		TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
		translateTransition.setOnFinished(event->{
			isAllowedToInsert=true;

			if(gameEnded(currentRow,column)){
				gameOver();

			}
			isPlayOneTurn=!isPlayOneTurn;

			playerNameLabel.setText(isPlayOneTurn?PLAYER_ONE:PLAYER_TWO);
		});
		translateTransition.play();

	}
	private boolean gameEnded(int row,int column){
		List<Point2D> verticalPoints= IntStream.rangeClosed(row - 3,row + 3)
				.mapToObj(r->new Point2D(r,column))
				.collect(Collectors.toList());



		List<Point2D> horizontalPoints= IntStream.rangeClosed(column - 3,column + 3)
				.mapToObj(col->new Point2D(row,col))
				.collect(Collectors.toList());

		Point2D startPoint1=new Point2D(row-3,column+3);
		List<Point2D> diagonal1points=IntStream.rangeClosed(0,6).mapToObj(i->startPoint1.add(i,-i)).collect(Collectors.toList());


		Point2D startPoint2=new Point2D(row-3,column-3);
		List<Point2D> diagonal2points=IntStream.rangeClosed(0,6).mapToObj(i->startPoint2.add(i,i)).collect(Collectors.toList());


		boolean isEnded=checkCombinations(verticalPoints) ||
				checkCombinations(horizontalPoints) ||
				checkCombinations(diagonal1points) ||
				checkCombinations(diagonal2points);

		return isEnded;

	}

	private boolean checkCombinations(List<Point2D> points) {

		int chain=0;

		for(Point2D point: points){



			int rowIndexForArray=(int) point.getX();
			int columnIndexForArray=(int) point.getY();
			Disc disc=getDiscIfPresent(rowIndexForArray,columnIndexForArray);
			if(disc!=null && disc.isPlayerOneMove==isPlayOneTurn){
				chain++;
				if(chain==4){
					return true;
				}
			}else{
				chain=0;
			}
		}
		return false;
	}
	private Disc getDiscIfPresent(int row,int column){
		if(row>=ROWS || row<0 || column>=COLUMNS || column<0)
			return null;
		return insertedDiscsArray[row][column];
	}

	private void gameOver(){
		String winner=isPlayOneTurn?PLAYER_ONE:PLAYER_TWO;
		System.out.println("Winner is : "+winner);

		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect Four");
		alert.setHeaderText("The Winner is " +winner);
		alert.setContentText("Want to play again ? ");

		ButtonType yesBtn=new ButtonType("Yes");
		ButtonType noBtn=new ButtonType("No, Exit");
		alert.getButtonTypes().setAll(yesBtn,noBtn);

		Platform.runLater(()->{
			Optional<ButtonType> btnClicked=alert.showAndWait();
			if(btnClicked.isPresent() &&btnClicked.get()==yesBtn){
				resetGame();

			}else{
				Platform.exit();
				System.exit(0);

			}
		});



	}

	public void resetGame() {
		insertedDiscPane.getChildren().clear();
		for(int row=0;row<insertedDiscsArray.length;row++){
			for(int col=0;col<insertedDiscsArray[row].length;col++){
				insertedDiscsArray[row][col]=null;
			}
		}
		isPlayOneTurn=true;
		playerNameLabel.setText(PLAYER_ONE);

		createPlayground();
	}

	private static class Disc extends Circle{
		private final boolean isPlayerOneMove;

		public Disc(boolean isPlayerOneMove){
			this.isPlayerOneMove=isPlayerOneMove;
			setRadius(CIRCLE_DIAMETER/2);
			setFill(isPlayerOneMove?Color.valueOf(discColor1):Color.valueOf(discColor2));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
		}
	}
	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscPane;

	@FXML
	public TextField playerOneTextField,playerTwoTextField;

	@FXML
	public Button setNamesButton;


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
