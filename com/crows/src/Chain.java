package com.crows.src;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @authorFelipe Custódio, Gabriel Scalici
 */
public class Chain {

    // image properties
    int h; // height
    int w; // width

    // shape properties
    int height;
    int width;

    // bitmaps
    int pixels[][]; // image with threshold 1 or 0
    int visited[][]; // stores visited pixels

    // initial coordinates of the shape
    int begin[];

    // final coordinates of the shape
    int end[];

    // perimeter
    int points;
    double perimeter;

    // path
	String path = "com/crows/images/";

	// for the results
	ChainVO chainVO;
	CoordinatesVO coordinatesVO;

    public Chain() throws IOException {

        // read input file
        System.out.print("Filename: ");
		StringBuilder filepath = new StringBuilder();
		filepath.append(path);
		filepath.append(Input.readString());
		File shape = new File(filepath.toString());
        System.out.println(shape.getAbsolutePath());
        BufferedImage image = ImageIO.read(shape);

        // setar propriedades da image para uso posterior
        h = image.getHeight(); // height
        w = image.getWidth(); // width

        // initialize coordinates vectors
        begin = new int[2];
        end = new int[2];
        points = 0;
        perimeter = 0;

        // threshold image
        pixels = new int[h][w];
        visited = new int [h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels[i][j] = image.getRGB(j, i);
                if (pixels[i][j] != -1) {
                     // shades of gray -> black
                    pixels[i][j] = 1;
                } else {
                     // background -> white
                    pixels[i][j] = 0;
                }
                // set pixel as unvisited
                visited[i][j] = 0;
            }
        }   
    }

    public void firstPixel() {
        boolean flag = false;
        // locate first black pixel
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (pixels[i][j] == 1 && !(flag)) {
                    // get coordinates
                    begin[0] = i;
                    begin[1] = j;
                    flag = true;
                }
            }
        }
    }

    public void lastPixel() {
        boolean flag = false;
        // find first pixel from down-up
        for (int i = h - 1; i >= 0; i--) {
            for (int j = w - 1; j >= 0; j--) {
                if (pixels[i][j] == 1 && !(flag)) {
                    // get coordinates
                    end[0] = i;
                    end[1] = j;
                    flag = true;
                }
            }
        }
    }

    public void setHeight() {
        // y of last pixel - y of first pixel
        height = (end[0] - begin[0] + 1);                    
    }

    public void setWidth() {

        // get x coordinates of first and final pixels
        int aux[] = new int[2];
        boolean flag = false;
        // find first pixel to the left
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (pixels[j][i] == 1 && !(flag)) {
                    // get x coordinate
                    aux[0] = i;
                    flag = true;
                }
            }
        }

        flag = false;
        // find first pixel to the right
        for (int i = w - 1; i >= 0; i--) {
            for (int j = h - 1; j >= 0; j--) {
                if (pixels[j][i] == 1 && !(flag)) {
                    // get x coordinate
                    aux[1] = i;
                    flag = true;
                }
            }
        }

        // x of last pixel - x of first pixel
        width = (aux[1] - aux[0] + 1);

    }

    public void border() {

        for (int i = 0; i < h; i++) {
        	for (int j = 0; j < w; j++) {
        		if (pixels[i][j] == 1) {
        			// if a neighbor of a pixel is empty, that pixel
                    // is on the border of the shape 
        			if (borderPixel(i, j)) points++;
        		}
        	}
        }
    }

    public boolean isHangingConnection(int i, int j) {

        for (int row = i - 1; row <= i + 1; row++) {
            for (int column = j - 1; column <= j + 1; column++) {
                if (row == i && column == j) continue;
                if (pixels[row][column] == 0) continue;
                if (visited[row][column] == 0) return false;
            }
        }

        return true;
    }

    public boolean borderPixel(int i, int j) {

        // only check black pixels
    	if (pixels[i][j] == 0) return false;
    	
        // check left
    	if (j == 0) return true; // image border = shape border
    	if (j > 0) {
    		if (pixels[i][j - 1] == 0) {
    			return true;
    		}
    	}

    	// check up
    	if (i == 0) return true;
    	if (i > 0) {
    		if (pixels[i - 1][j] == 0) {
    			return true;
    		}
    	}

    	// check right
    	if (j == w) return true;
    	if (j < w) {
    		if (pixels[i][j + 1] == 0) {
    			return true;
    		}
    	}

    	// check down
    	if (i == h) return true;
    	if (i < h) {
    		if (pixels[i + 1][j] == 0) {
    			return true;
    		}
    	}

    	// no empty pixel around = not border pixel
    	return false;
    }

    public ChainVO borderNeighbors(int i, int j, ChainVO chains) {
    	
    	boolean flag = false;

    	// check around pixel for unvisited border pixels
        // calculates chain codes distance

        if (isHangingConnection(i, j)) {
            chains.setReturning(true);
        }

    	// check east
    	if (borderPixel(i, j+1) && !flag && (visited[i][j+1] == 0 || chains.isReturning())) {
    		j = j + 1;
    		System.out.print("0 ");
    		chains.addChain(0);
    		perimeter += 1;
    		flag = true;
    		chains.setI(i);
    		chains.setJ(j);
    		return chains;
    	}
    	// check southeast
    	if (borderPixel(i+1, j+1) && !flag && (visited[i+1][j+1] == 0 || chains.isReturning())) {
    		i = i + 1;
    		j = j + 1;
    		System.out.print("1 ");
			chains.addChain(1);
    		perimeter += Math.sqrt(2);
    		flag = true;
			chains.setI(i);
			chains.setJ(j);
			return chains;
    	}
    	// check south
    	if (borderPixel(i+1, j) && !flag && (visited[i+1][j] == 0 || chains.isReturning())) {
    		i = i + 1;
    		System.out.print("2 ");
			chains.addChain(2);
    		perimeter += 1;
    		flag = true;
			chains.setI(i);
			chains.setJ(j);
			return chains;
    	}
    	// check southwest
    	if (borderPixel(i+1, j-1) && !flag && (visited[i+1][j-1] == 0 || chains.isReturning())) {
    		i = i + 1;
    		j = j - 1;
    		System.out.print("3 ");
			chains.addChain(3);
    		perimeter += Math.sqrt(2);
    		flag = true;
			chains.setI(i);
			chains.setJ(j);
			return chains;
    	}
    	// check west
    	if (borderPixel(i, j-1) && !flag && (visited[i][j-1] == 0 || chains.isReturning())) {
    		j = j - 1;
    		System.out.print("4 ");
			chains.addChain(4);
    		perimeter += 1;
    		flag = true;
			chains.setI(i);
			chains.setJ(j);
			return chains;
    	}
    	// check northwest
    	if (borderPixel(i-1, j-1) && !flag && (visited[i-1][j-1] == 0 || chains.isReturning())) {
    		i = i - 1;
    		j = j - 1;
    		System.out.print("5 ");
			chains.addChain(5);
    		perimeter += Math.sqrt(2);
    		flag = true;
			chains.setI(i);
			chains.setJ(j);
			return chains;
    	}
    	// check north
    	if (borderPixel(i-1, j) && !flag && (visited[i-1][j] == 0 || chains.isReturning())) {
    		i = i - 1;
    		System.out.print("6 ");
			chains.addChain(6);
    		perimeter += 1;
    		flag = true;
			chains.setI(i);
			chains.setJ(j);
			return chains;
    	}
    	// check northeast
    	if (borderPixel(i-1, j+1) && !flag && (visited[i-1][j+1] == 0 || chains.isReturning())) { // 여기 수정한 부분
    		i = i - 1;
    		j = j + 1;
    		System.out.print("7 ");
			chains.addChain(7);
    		perimeter += Math.sqrt(2);
    		flag = true;
			chains.setI(i);
			chains.setJ(j);
			return chains;
    	}

        // no neighbor border pixels 
		chains.setI(i);
		chains.setJ(j);
		return chains;
    }

    public void chainCodes(int[] begin, int i, int j, ChainVO chains) {

    	/*
    	i e j = index of current pixel
    	index[0], index[1] = next border pixel (if exists)
    	*/

    	// coordinates of current pixel
		int[] index = new int[2];
		index[0] = chains.getI();
		index[1] = chains.getJ();

    	// check for border pixels around
		chains = borderNeighbors(i, j, chains);

    	// set pixel as visited
    	visited[index[0]][index[1]] = 1;

        if (chains.getI() == begin[0] && chains.getJ() == begin[1]) {
            System.out.println();
            return;
        }

        if (visited[chains.getI()][chains.getJ()] == 0) {
            chainCodes(begin, chains.getI(), chains.getJ(), chains);
        } else if (chains.isReturning()) {
            chains.setReturning(false);
            chainCodes(begin, chains.getI(), chains.getJ(), chains);
        } else {
            System.out.println();
        }
    }

    public Chain calculateChains(Chain c) {
		// get key coordinates
		c.firstPixel();
		c.lastPixel();

        System.out.println("begin_x: " + c.begin[0]);
        System.out.println("begin_y: " + c.begin[1]);

		// calculate shape properties
		c.setHeight();
		c.setWidth();
		System.out.println("Shape width: " + c.width);
		System.out.println("Shape height: " + c.height);

		// setting chain variable
		c.chainVO = new ChainVO();

		// generate chain codes
		// get coordinates of first border pixel after initial
		System.out.print("Chain Codes: ");
		c.chainVO = c.borderNeighbors(c.begin[0], c.begin[1], c.chainVO);
		c.chainCodes(c.begin, c.chainVO.getI(), c.chainVO.getJ(), c.chainVO);

		// get perimeter size
		c.border();
		System.out.println("Border pixels: " + c.points + " pixels");
		System.out.println("Shape perimeter: " + c.perimeter);

		return c;
	}

	public CoordinatesVO calculateCoordinate(CoordinatesVO coordinatesVO, ChainVO chains) {
    	int x = coordinatesVO.getX();
    	int y = coordinatesVO.getY();

		for (int i = 0; i < chains.getChains().size(); i++) {
			switch (chains.getChains().get(i)) {
				case 0:
					x++;
					break;
				case 1:
					x++;
					y--;
					break;
				case 2:
					y--;
					break;
				case 3:
					x--;
					y--;
					break;
				case 4:
					x--;
					break;
				case 5:
					x--;
					y++;
					break;
				case 6:
					y++;
					break;
				case 7:
					x++;
					y++;
					break;
				default:
			}

			if (x < 0) {
				coordinatesVO.setX(coordinatesVO.getX() + 50);
				return calculateCoordinate(coordinatesVO, chains);
			}

			if (y < 0)
			{
				coordinatesVO.setY(coordinatesVO.getY() + 50);
				return calculateCoordinate(coordinatesVO, chains);
			}

			coordinatesVO.addX(x);
			coordinatesVO.addY(y);
		}

		return coordinatesVO;
	}

	public void printCoordinates(CoordinatesVO coordinatesVO) {

		for (int i = 0; i < coordinatesVO.getxList().size(); i++) {
			System.out.print(coordinatesVO.getxList().get(i) + " ");
		}
		System.out.println();

		for (int i = 0; i < coordinatesVO.getyList().size(); i++) {
			System.out.print(coordinatesVO.getyList().get(i) + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) throws IOException {

		Chain c = new Chain();
		c = c.calculateChains(c);

		// calculate coordinates
		c.coordinatesVO = new CoordinatesVO();
		c.coordinatesVO = c.calculateCoordinate(c.coordinatesVO, c.chainVO);

		c.printCoordinates(c.coordinatesVO);
	}
}