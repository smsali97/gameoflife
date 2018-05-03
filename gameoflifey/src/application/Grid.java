package application;

public class Grid {
	public Cell[][] grid;

	public Grid(int N ) {
		grid = new Cell[N][N];
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = new Cell();
			}
		}
	}

	public void spawnBeeHive(int r, int c) {
		if ( r  + 3>= grid.length || c + 3  >= grid.length) return;
		
		grid[r][c+1].populate();
		grid[r][c+2].populate();
		grid[r+1][c].populate();
		grid[r+2][c].populate();
		grid[r+3][c+1].populate();
		grid[r+3][c+2].populate();
		
		grid[r+1][c+2].die();
		grid[r+1][c+1].die();
	}
	
	
	public int getCellCount(int r, int c) {
		int ctr = 0;
		for (int i = r -1; i <= r + 1; i++) {
			if (i == -1 || i == grid.length) continue;

			for (int j = c -1; j <= c + 1; j++) {
				if ( j == c || j == -1 || j == grid.length || (i== r && j == c)) continue;
				if (grid[i][j].isPopulated()) ctr++;
			}
		}
		return ctr;
	}
	
	public void spawnExplorer(int r, int c) {
		if ( r  + 3>= grid.length || c + 3  >= grid.length) return;
		
		grid[r-1][c].populate();
		grid[r][c+1].populate();
		grid[r+1][c].populate();
		grid[r+1][c-1].populate();
		grid[r+1][c+1].populate();

	}



	public void checkRules(int r, int c) {
		int neighbors = this.getCellCount(r,c);

		// For a space that is 'populated':
		if (grid[r][c].isPopulated()) {

			 //    Each cell with one or no neighbors dies, as if by solitude. 
			 if (neighbors <= 1) grid[r][c].die();
			 //    Each cell with four or more neighbors dies, as if by overpopulation. 
			 else if (neighbors >= 1) grid[r][c].die();
			 //    Each cell with two or three neighbors survives.
			 else  grid[r][c].populate(); 
		}

		// For a space that is 'empty' or 'unpopulated'
		else {
			//   Each cell with three neighbors becomes populated.
			if (neighbors == 3) grid[r][c].populate();
		}
	 
	}
}

class Cell {
	private boolean populated = false;

	public Cell() {
		populated = false;
	}

	public void populate() {
		populated = true;
	}

	public void die() {
		populated = false;
	}

	public boolean isPopulated() { return populated; }
}