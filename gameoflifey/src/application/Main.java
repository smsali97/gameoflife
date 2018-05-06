package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private static final int DIM = 600;
	private static int N = 60;
	private static Grid grid;

	private Color active = Color.SKYBLUE;
	private Color inactive = Color.gray(0.2);
	private Color changed = Color.YELLOW;

	private final long time = 300_000_000;

	private static final String Beehive = "Beehive";
	private static final String Blinker = "Blinker";
	private static final String Glider = "Glider";
	private static final String Pulsar = "Pulsar";
	private static final String Pentadecathlon = "Pentadecathlon";
	private static final String Exploder = "Exploder";
	private static final String CLEAR = "CLEAR";

	@Override
	public void start(Stage primaryStage) {
		try {
			HBox root = new HBox();
			root.setAlignment(Pos.CENTER);
			root.setPadding(new Insets(5));
			grid = new Grid(N);

			Canvas canvas = new Canvas(DIM, DIM);
			VBox panel = createPanel();
			root.getChildren().addAll(panel, canvas);

			GraphicsContext gc = canvas.getGraphicsContext2D();
			gc.setFill(Color.BLACK);
			gc.rect(0, 0, DIM, DIM);

			// Spawning
			grid.spawnExplorer(N / 4, N / 4);
			grid.spawnBeeHive(N / 8, N / 8);
			grid.spawnBlinker(N / 2, N / 2);
			// grid.spawnBlinker(N/4, N/4);

			int width = DIM / N;

			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, DIM, DIM);

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {

					if (grid.grid[i][j].isPopulated()) {
						gc.setFill(active);
						gc.fillText("■", i * width, j * width, width);

					} else {
						gc.setFill(inactive);
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
					if (now - lastUpdate > time) {
						lastUpdate = now;

						gc.setFill(Color.BLACK);
						gc.fillRect(0, 0, DIM, DIM);

						for (int i = 1; i < N - 1; i++) {
							for (int j = 1; j < N - 1; j++) {
								if (grid.grid[i][j].isPopulated()) {
									gc.setFill(grid.grid[i][j].active ? changed : active);
									// gc.fillRect(i * width, j * width, width, width);
									gc.fillText("■", i * width, j * width, width);

								} else {
									gc.setFill(inactive);
									// gc.fillRect(+* width, j * width, width, width);
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

	public static void clear(Cell[][] grid) {
		int N = grid.length;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				grid[i][j].die();
	}

	public static VBox createPanel() {

		ComboBox<String> box = new ComboBox<>();
		box.getItems().addAll(Beehive, Blinker, Glider, Pentadecathlon, Pulsar, Exploder, CLEAR);
		box.setValue("Glider");

		TextField rowText = new TextField();
		rowText.setPromptText("Enter row number");
		TextField colText = new TextField();
		colText.setPromptText("Enter column number");

		CheckBox cb = new CheckBox("Random");
		cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean prev, Boolean curr) {
				if (curr) {
					rowText.setText("-1");
					rowText.setEditable(false);
					colText.setText("-1");
					colText.setEditable(false);
				} else {
					rowText.setText("");
					rowText.setPromptText("Enter row number");
					rowText.setEditable(true);
					colText.setText("");
					colText.setPromptText("Enter column number");
					colText.setEditable(true);
				}
				// TODO Auto-generated method stub

			}
		});

		Button button = new Button("SPAWN!");
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int r = -1;
				int c = -1;
				

				try {
					r = Integer.parseInt(rowText.getText());
					c = Integer.parseInt(colText.getText());
				} catch (NumberFormatException e) {
					
				}

				r = r == -1 ? (int) (1 + Math.random() * N) : r;
				c = c == -1 ? (int) (1 + Math.random() * N) : c;
				switch (box.getValue()) {
				case Beehive:
					System.out.println("YAYY");
					grid.spawnBeeHive(r, c);
					break;
				case Blinker:
					grid.spawnBlinker(r, c);
					break;
				case Glider:
					grid.spawnExplorer(r, c);
					break;
				case Pentadecathlon:

					break;
				case Pulsar:

					break;
				case Exploder:

					break;
				case CLEAR:
					clear(grid.grid);
					break;
				default:
					break;
				}

			}
		});

		VBox panel = new VBox(10);
		panel.setAlignment(Pos.CENTER);

		panel.getChildren().addAll(getFancyLabel(), box, cb, new HBox(rowText, colText), button);

		return panel;
	}

	public static Label getFancyLabel() {
		Label l = new Label();
		l.setFont(new Font(30));
		l.setText("The Game of Life");

		return l;

	}

	public static void main(String[] args) {
		launch(args);
	}
}
