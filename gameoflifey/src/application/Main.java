package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

	Rectangle[][] matrix;
	private static final int DIM = 700;
	private int N = 50;

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
			grid.spawnExplorer(N / 2, N / 2);

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					int x = grid.grid[i][j].isPopulated() ? 1 : 0;
					System.out.print(x + " ");
				}
				System.out.println();
			}

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			AnimationTimer timer = new AnimationTimer() {
				long lastUpdate = 0;
				@Override
				public void handle(long now) {
					if (now - lastUpdate > 2_000_000_000) {
						lastUpdate = now ;
						
						int val = DIM / (N);
						gc.setFill(Color.BLACK);
						gc.fillRect(0, 0, DIM, DIM);
						int ctr = 0;

						for (int i = 0; i < N; i++) {
							for (int j = 0; j < N; j++) {

								grid.checkRules(i, j);
								if (grid.grid[i][j].isPopulated()) {
									gc.setFill(Color.BLUE);
									gc.fillText("1", i * val, j * val, 4);
								} else {
									gc.setFill(Color.RED);
									gc.fillText("0", i * val, j * val, 4);
								}
							}
						}
					}
				}
			};

			timer.start();

		} catch (Exception e) {
			e.printStackTrace();
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
