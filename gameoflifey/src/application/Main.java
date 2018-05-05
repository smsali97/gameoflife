package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

	Rectangle[][] matrix;
	private static final int DIM = 1000;
	private int N = 100;

	@Override
	public void start(Stage primaryStage) {
		try {
			HBox root = new HBox();
			Grid grid = new Grid(N);

			Canvas canvas = new Canvas(DIM, DIM);
			root.getChildren().add(canvas);

			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.setFill(Color.BLACK);
			gc.rect(0, 0, DIM, DIM);

			// Spawning
//			grid.spawnExplorer(N / 2, N / 2);
			grid.spawnExplorer(N / 4, N / 4);
			grid.spawnBeeHive(N/8, N/8);
			grid.spawnBlinker(N/2, N/2);
//			grid.spawnBlinker(N/4, N/4);
			
			consolePrint(grid.grid);
			
			int width = DIM/N;
			
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, DIM, DIM);
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {

					if (grid.grid[i][j].isPopulated()) {
						gc.setFill(Color.BLUE);
						//gc.fillRect(i * width, j * width, width, width);
						gc.fillText("■", i * width, j * width, width);
						
					} else {
						gc.setFill(Color.RED);
						//gc.fillRect(i * width, j * width, width, width);
						gc.fillText("■", i * width, j * width, width);
					}
				}
			
			}
			
			

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			AnimationTimer timer = new AnimationTimer() {
				long lastUpdate = 0;
				@Override
				public void handle(long now) {
					if (now - lastUpdate > 1_000_000_000) {
						lastUpdate = now ;
						
						
						gc.setFill(Color.BLACK);
						gc.fillRect(0, 0, DIM, DIM);
						int ctr = 0;
						
						
						for (int i = 1; i < N-1; i++) {
							for (int j = 1; j < N-1; j++) {
								if (grid.grid[i][j].isPopulated()) {
									gc.setFill(Color.BLUE);
									//gc.fillRect(i * width, j * width, width, width);
									gc.fillText("■", i * width, j * width, width);
									
								} else {
									gc.setFill(Color.DARKRED);
									//gc.fillRect(i * width, j * width, width, width);
									gc.fillText("■", i * width, j * width, width);
								}
							}
						}
						grid.checkRules();
						
					}
				}
			};

			timer.start(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void consolePrint(Cell[][] grid) {
		int N = grid.length;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				int x = grid[i][j].isPopulated() ? 1 : 0;
				System.out.print(x + " ");
			}
			System.out.println();
		}
	}

	public static Rectangle[][] computeGrid(int N) {
		Rectangle[][] grid = new Rectangle[N][N];

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = new Rectangle(DIM, DIM);
				grid[i][j].setFill((Color.BLACK));
			}
		}

		return grid;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
