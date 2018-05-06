package application;

public class Grid {
	public Cell[][] grid;

	public Grid(int N) {
		grid = new Cell[N][N];

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = new Cell();
			}
		}
	}

	public void spawnBlinker(int r, int c) {
		grid[r - 1][c].populate();
		grid[r + 1][c].populate();
		grid[r][c].populate();
	}

	public void spawnBeeHive(int r, int c) {
		if (r + 3 >= grid.length || c + 3 >= grid.length)
			return;
		
		grid[r][c + 1].populate();
		grid[r][c + 2].populate();
		
		grid[r + 1][c].populate();
		grid[r + 1][c+3].populate();
		
		grid[r + 2][c + 1].populate();
		grid[r + 2][c + 2].populate();
		
		grid[r + 1][c + 2].die();
		grid[r + 1][c + 1].die();
	}

	public int getCellCount(int r, int c) {

		int aliveNeighbours = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (grid[r + i][c + j].isPopulated() || !(i == r && j == c))
					aliveNeighbours++;
			}
		}
		return -aliveNeighbours;

	}

	public void spawnExplorer(int r, int c) {
		if (r + 3 >= grid.length || c + 3 >= grid.length)
			return;

		grid[r - 1][c].populate();
		grid[r][c + 1].populate();
		grid[r + 1][c].populate();
		grid[r + 1][c - 1].populate();
		grid[r + 1][c + 1].populate();

	}

	public void checkRules() {

		Cell[][] new_grid = new Cell[grid.length][grid.length];
		// copy old grid to new grid
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				new_grid[i][j] = new Cell();
				if (grid[i][j].isPopulated())
					new_grid[i][j].populate();
			}
		}

		for (int r = 1; r < grid.length -1; r++) {
			for (int c = 1; c < grid.length - 1; c++) {
				int neighbors = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if (grid[r + i][c + j].isPopulated())
							neighbors++;
					}
				}
				if (grid[r][c].isPopulated()) neighbors--;

				// For a space that is 'populated':
				if (grid[r][c].isPopulated()) {
					new_grid[r][c].active = false;

					// Any live cell with fewer than two live neighbors dies, as if caused by under
					// population.
					if (neighbors < 2)
						new_grid[r][c].die();
					// Any live cell with two or three live neighbors lives on to the next
					// generation.
					else if (neighbors <= 3)
						new_grid[r][c].populate();
					// Any live cell with more than three live neighbors dies, as if by
					// overpopulation.
					else
						new_grid[r][c].die();

				}

				// For a space that is 'empty' or 'unpopulated'
				else {
					// Any dead cell with exactly three live neighbors becomes a live cell, as if by
					// reproduction.
					if (neighbors == 3) {
						new_grid[r][c].populate();
					
						new_grid[r][c].active = true;
					}
				}
			}
		}

		grid = new_grid;
	}
}

class Cell {
	private boolean populated = false;
	public boolean active = false;
	
	public Cell() {
		populated = false;
	}

	public void populate() {
		populated = true;
	}

	public void die() {
		populated = false;
	}

	public boolean isPopulated() {
		return populated;
	}
}
