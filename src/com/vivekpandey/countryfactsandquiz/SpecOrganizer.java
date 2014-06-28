package com.vivekpandey.countryfactsandquiz;

import android.util.Log;
import java.util.Random;

public class SpecOrganizer {
	int numItems;
	int numRows;
	int numCols;
	public static final int MAX_Y = 200;
	public static final int MAX_X = 4;
	public static final int MAX_ITEMS = 100;
	
	public static final int NUM_TILE_TYPES = 4;
	public static final int TWO_BY_TWO = 0;
	public static final int TWO_BY_ONE = 1;
	public static final int ONE_BY_TWO = 2;
	public static final int ONE_BY_ONE = 3;
	
	boolean matrix[][] = new boolean[MAX_X][MAX_Y];
	int curX = 0, curY = 0;
	Tile item2Tile[] = new Tile[MAX_ITEMS];
	int constantSize;
	
	Random random;
	int seed = (int)System.currentTimeMillis() % 12345;
	
	public SpecOrganizer(int numItems, int constantSize) {
		this.constantSize = constantSize;
		this.numRows = numItems;
		this.numCols = 4;
		boolean possible[] = new boolean[NUM_TILE_TYPES];
		random = new Random(System.currentTimeMillis());
		
		for (int i = 0; i < MAX_X; i++) {
			for (int j = 0; j < MAX_Y; j++) {
				matrix[i][j] = false;
			}
		}

		for (int i = 0; i < MAX_ITEMS; i++) {
			int width = 0, height = 0;
			possible = getPossibleTiles();
			int tileIndex = selectTileIndex(possible);
			width = getTileWidth(tileIndex);
			height = getTileHeight(tileIndex);
			item2Tile[i] = new Tile(curX, curY, width, height);
			for (int m = curX; m < curX + width; m++) {
				for (int n = curY; n < curY + height; n++) {
					matrix[m][n] = true;
				}
			}
			/* We need first update curY and then curX. */
			updateCurY();
			updateCurX();
		}
	}
	public Tile getTile(int item) {
		return item2Tile[item];
	}
	private int selectTileIndex(boolean[] possible) {
		/* Count number of possibilities */
		int count = 0;
		for (int i = 0; i < NUM_TILE_TYPES; i++) {
			if (possible[i]) {
				count++;
			}
		}
		
		assert(count >= 1);
		/* Generate a random number between 0 and count - 1 */
		int r;
		if (constantSize == -1) {
			r = random.nextInt(count);
		} else {
			r = constantSize;
		}
		//int r = seed % count;
		//seed = (110 * seed + 12345) << 8;
		//int r = 0;
		
		/* Skip first r - 1 possibilities, and return the rth possibility */
		int index = 0;
		for (int i = 0; i < NUM_TILE_TYPES; i++) {
			if (index == r && possible[i]) {
				return i;
			}
			if (possible[i]) {
				index++;
			}
		}
		return NUM_TILE_TYPES - 1;
	}
	private void updateCurY() {
		/* There are three blocks of this code because if currently curRow is
		 * k, then it can, at max, be k + 2 after one iteration.
		 */
		for (int i = 0; i < MAX_X; i++) {
			if (matrix[i][curY] == false) {
				return;
			}
		}
		curY++;
		for (int i = 0; i < MAX_X; i++) {
			if (matrix[i][curY] == false) {
				return;
			}
		}
		curY++;
		for (int i = 0; i < MAX_X; i++) {
			if (matrix[i][curY] == false) {
				return;
			}
		}
		assert(false);
	}
	private void updateCurX() {
		for (int i = 0; i < MAX_X; i++) {
			if (matrix[i][curY] == false) {
				curX = i;
				return;
			} 
		}
		assert(false);
	}
	private boolean[] getPossibleTiles() {
		boolean[] possible = new boolean[NUM_TILE_TYPES];
		for (int i = 0; i < NUM_TILE_TYPES; i++) {
			possible[i] = false;
		}
		try {
			if (!matrix[curX][curY] && !matrix[curX][curY + 1] &&
					!matrix[curX + 1][curY] && !matrix[curX + 1][curY + 1]) {
				possible[TWO_BY_TWO] = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		
		try {
			if (!matrix[curX][curY] && !matrix[curX + 1][curY]) {
				possible[TWO_BY_ONE] = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		
		try {
			if (!matrix[curX][curY] && !matrix[curX][curY + 1]) {
				possible[ONE_BY_TWO] = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		
		try {
			if (!matrix[curX][curY]) {
				possible[ONE_BY_ONE] = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return possible;
	}
	private int getTileWidth(int tileIndex) {
		if (tileIndex == ONE_BY_TWO || tileIndex == ONE_BY_ONE) {
			return 1;
		}
		return 2;
	}
	private int getTileHeight(int tileIndex) {
		if (tileIndex == ONE_BY_ONE || tileIndex == TWO_BY_ONE) {
			return 1;
		}
		return 2;
	}
}