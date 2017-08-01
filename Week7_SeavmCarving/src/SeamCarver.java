
import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

/*
public class SeamCarver {
	private int[][] colorMatrix;

	private int height;
	private int width;
	
	public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
		height = picture.height();
		width = picture.width();
		this.colorMatrix = new int[picture.height()][picture.width()];

		for (int row = 0; row < height;row++){
			for (int col = 0; col < width;col++){
				this.colorMatrix[row][col] = picture.get(col, row).getRGB();
			}
		}
	}
	
	private double[][] energyMatrix(){
		double[][] energyMatrix = new double[this.height][this.width];
		for (int row = 0; row < height;row++){
			for (int col = 0; col < width;col++){
				energyMatrix[row][col] = energy(col, row);
			}
		}
		return energyMatrix;
	}
	
	public Picture picture(){                          // current picture
		Picture picture = new Picture(this.width,this.height);
		for(int row = 0; row < this.height;row++){
			for (int col = 0; col < this.width; col++){
				picture.set(col, row, new Color(this.colorMatrix[row][col]));
			}
		}
		return picture;
	}
	public int width(){                            // width of current picture
		return this.width;
	}
	public int height(){                           // height of current picture
		return this.height;
	}
	public double energy(int col, int row){               // energy of pixel at column x and row y
		double xEnergy;
		int xLeft,xRight,yTop,yBottom;
		if(col==0){
			xLeft = this.colorMatrix[row][this.width-1];
			xRight = this.colorMatrix[row][col+1];
		}else if(col==this.width()-1){
			xLeft = this.colorMatrix[row][col-1];
			xRight = this.colorMatrix[row][0];
		}else{
			xLeft = this.colorMatrix[row][col-1];
			xRight = this.colorMatrix[row][col+1];
		}
		
		if(row==0){
			yTop = this.colorMatrix[this.height-1][col];
			yBottom = this.colorMatrix[row+1][col];
		}else if(row == this.height()-1){
			yTop = this.colorMatrix[row-1][col];
			yBottom = this.colorMatrix[0][col];
		}else{
			yTop = this.colorMatrix[row-1][col];
			yBottom = this.colorMatrix[row+1][col];
		}
		return Math.sqrt(this.gradient(xLeft, xRight)+this.gradient(yTop, yBottom));
	}
	
	private double gradient(int fromColor,int toColor){
		Color c1 = new Color(fromColor);
		Color c2 = new Color(toColor);
		double deltaB = c1.getBlue() - c2.getBlue();
		double deltaR = c1.getRed() - c2.getRed();
		double deltaG = c1.getGreen() - c2.getGreen();
		return deltaB*deltaB + deltaR*deltaR + deltaG*deltaG;
	}
	
	public int[] findVerticalSeam(){                 // sequence of indices for vertical seam
		
		double[][] energyMatrix = this.energyMatrix();
		double[][] energyTo = new double[this.height()][this.width()]; //extra virtual top row.
		int[][] fromPoint = new int[this.height()][this.width()];
		IndexMinPQ pq = new IndexMinPQ(this.width());
		int[] seam = new int[this.height()];
		
		//initialize energyTo[][]
		for(int row = 0; row < this.height(); row++){
			for (int col = 0; col < this.width(); col++){
				if(row == 0) energyTo[row][col] = energyMatrix[row][col];//255*255*3
				else energyTo[row][col] = Double.POSITIVE_INFINITY;
			}
		}
		//relax energyTo
		for(int row = 1; row < this.height(); row++){
			for (int col = 0; col < this.width(); col++){
				for (int k = col-1; k<= col+1;k++){
					if(k >= 0 && k <=this.width()-1){
						if(energyTo[row][k] > energyTo[row-1][col] + energyMatrix[row][k]){
							energyTo[row][k] = energyTo[row-1][col] + energyMatrix[row][k];
							fromPoint[row][k] = this.rowColToV(row-1, col); 
						}
					}
				}
			}
		}
		
		//insert the last row
		for(int col = 0; col < this.width(); col++){
			pq.insert(col, energyTo[this.height()-1][col]);
		}
		seam[this.height()-1] = pq.minIndex();
		
		//back trace
		for(int row = this.height()-2; row >=0; row--){
			int preCol = seam[row+1];
			seam[row] = fromPoint[row+1][preCol]%this.width();
		}
		
		return seam;
	}

	private int rowColToV(int row, int col){
		return row*this.width()+col;
	}
	
	
	public void printEnergyMatrix(){
		double[][] energyMatrix = this.energyMatrix();
		for (int row = 0; row < this.height();row++){
			for (int col = 0; col < this.width();col++){
				StdOut.printf("%.0f ", energyMatrix[row][col]); 
			}
			StdOut.println();
		}
		
		for (int row = 0; row < this.height();row++){
			for (int col = 0; col < this.width();col++){
				Color color= new Color(this.colorMatrix[row][col]);
				StdOut.printf("(%3d，%3d, %3d) ", color.getRed(),color.getGreen(),color.getBlue()); 
			}
			StdOut.println();
		}
		
	}
	
	public void removeVerticalSeam(int[] seam){     // remove vertical seam from current picture
		int[][] newColorMatrix = new int[this.height()][this.width()-1];
		for(int row = 0; row < this.height(); row ++){
			for(int col = 0; col < this.width()-1; col++){
				if(col < seam[row]){
					newColorMatrix[row][col] = this.colorMatrix[row][col];
				}else{
					newColorMatrix[row][col] = this.colorMatrix[row][col+1];
				}
			}
		}
		this.width--;
		this.colorMatrix = newColorMatrix;
	}
	
	private Picture transpose(){
		int width = this.height();
		int height = this.width();
		Picture newPicture = new Picture(width, height);
		for (int row = 0; row < height; row++){
			for(int col = 0; col < width; col ++){
				newPicture.set(col, row, new Color(this.colorMatrix[col][row]));
			}
		}
		return newPicture;
	}
	
	public  int[] findHorizontalSeam(){               // sequence of indices for horizontal seam
		return new SeamCarver(this.transpose()).findVerticalSeam();
	}
	
	public void removeHorizontalSeam(int[] seam){   // remove horizontal seam from current picture
		int[][] newColorMatrix = new int[this.height()-1][this.width()];
		for(int col = 0; col < this.width(); col++){
			for(int row = 0; row < this.height()-1; row ++){
				if(row < seam[col]){
					newColorMatrix[row][col] = this.colorMatrix[row][col];
				}else{
					newColorMatrix[row][col] = this.colorMatrix[row+1][col];
				}
			}
		}
		this.height--;
		this.colorMatrix = newColorMatrix;
	}
	
	public static void main(String[] args) {
		Picture picture = new Picture(args[0]);
		SeamCarver seamCarver = new SeamCarver(picture);
		int rRow = 3;
		for (int row = 0; row < rRow; row++){
			StdOut.printf("remove: %dth\n",row);
			for (int p:seamCarver.findHorizontalSeam()){
				StdOut.print(p + " ");
			}
			StdOut.println("");
			seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
		}
	}
}
*/



import java.awt.Color;

public class SeamCarver {
    private int[][] colorMatrix;
    private int width, height;

    public SeamCarver(Picture picture) {
        width = picture.width();
        height = picture.height();
        colorMatrix = new int[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                colorMatrix[x][y] = picture.get(x, y).getRGB();
            }
    }
    
    // current picture
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                pic.set(x, y, new Color(colorMatrix[x][y]));
            }
        return pic;
    }
    
    // width  of current picture
    public int width() {
        return width;
    }        
    
    // height of current picture
    public int height() {
        return height;
    }
    
    // energy of pixel at column x and row y in current picture
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            throw new java.lang.IndexOutOfBoundsException();
        
        if (x == 0 || x == width()-1 || y == 0 || y == height()-1) 
            return 195075.0;
        return squareOfXGradient(x, y) + squareOfYGradient(x, y);
    }
    
    // sequence of indices for horizontal seam in current picture
    public int[] findHorizontalSeam() {  
        // construct energy matrix by H x W
        double[][] energyMatrix = toEnergyMatrix(height, width, true);
        return findSeam(energyMatrix);
    }
    
    // sequence of indices for vertical   seam in current picture
    public int[] findVerticalSeam() {
        // construct energy matrix by W x H
        double[][] energyMatrix = toEnergyMatrix(width, height, false);
        return findSeam(energyMatrix);
    }
    
    private int[] findSeam(double[][] eMatrix) {
        int W = eMatrix.length;
        int H = eMatrix[0].length;        
        
        // construst energyTo matrix
        double[][] energyTo = new double[W][H];
        for (int y = 0; y < H; y++) 
            for (int x = 0; x < W; x++) {            
                if (y == 0) energyTo[x][y] = 195075.0;
                else energyTo[x][y] = Double.POSITIVE_INFINITY;
            }        
        
        int[] seam = new int[H];
        int[][] edgeTo = new int[W][H];
        IndexMinPQ pq = new IndexMinPQ(W);
        
        // calculate energyTo by relax pixels
        for (int y = 0; y < H - 1; y++) 
            for (int x = 0; x < W; x++) 
                for (int k = x-1; k <= x+1; k++) 
                    if (k >= 0 && k < W) 
                        if (energyTo[k][y+1] > energyTo[x][y] + eMatrix[k][y+1]) {
                            energyTo[k][y+1] = energyTo[x][y] + eMatrix[k][y+1];
                            edgeTo[k][y+1] = xyTo1D(x, y, eMatrix);
                        }
        
        // find the minimum index in last row
        for (int x = 0; x < W; x++)
            pq.insert(x, energyTo[x][H-1]);        
        seam[H-1] = pq.minIndex();
        
        // back-track
        for (int y = H-1; y > 0; y--)
            seam[y-1] = edgeTo[seam[y]][y] % W;        
        return seam;
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] a) {
        if (a.length != width || height <= 1) 
            throw new java.lang.IllegalArgumentException();        
        //checkSeam(a);
        
        int[][] copy = new int[width][height-1];
        
        for (int x = 0; x < width; x++) {
            System.arraycopy(colorMatrix[x], 0, copy[x], 0, a[x]);
            System.arraycopy(colorMatrix[x], a[x]+1, copy[x], a[x], height-a[x]-1);
        }

        height--;
        colorMatrix = copy;
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] a) {
        if (a.length != height || width <= 1) 
            throw new java.lang.IllegalArgumentException();        
        //checkSeam(a);        
        
        int[][] copy = new int[width-1][height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (x < a[y]) copy[x][y] = colorMatrix[x][y];
                else if (x > a[y]) copy[x-1][y] = colorMatrix[x][y];
            }
        }

        width--;
        colorMatrix = copy;      
    }
    
    private void checkSeam(int[] a) {
        for (int i = 1; i < a.length; ++i) {
            if (Math.abs(a[i - 1] - a[i]) > 1)
                throw new IllegalArgumentException(
                        "two adjacent entries differ by more than 1");
        }
    }
        
    private double squareOfXGradient(int x, int y) {        
        Color c1 = new Color(colorMatrix[x-1][y]);
        Color c2 = new Color(colorMatrix[x+1][y]);
        double r = Math.abs(c1.getRed() - c2.getRed());
        double g = Math.abs(c1.getGreen() - c2.getGreen());
        double b = Math.abs(c1.getBlue() - c2.getBlue());
        return r*r + g*g + b*b;
    }
    
    private double squareOfYGradient(int x, int y) {
        Color c1 = new Color(colorMatrix[x][y-1]);
        Color c2 = new Color(colorMatrix[x][y+1]);
        double r = Math.abs(c1.getRed() - c2.getRed());
        double g = Math.abs(c1.getGreen() - c2.getGreen());
        double b = Math.abs(c1.getBlue() - c2.getBlue());
        return r*r + g*g + b*b;
    }
    
    private double[][] toEnergyMatrix(int W, int H, boolean isTranspose)
    {
        double[][] result = new double[W][H];
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++) {
                if (isTranspose) result[x][y] = energy(y, x);
                else result[x][y] = energy(x, y);
            }
    
        return result;        
    }
    
    private int xyTo1D(int x, int y, double[][] m) {
        return y * m.length + x;
    }   
    
	public static void main(String[] args) {
		Picture picture = new Picture(args[0]);
		SeamCarver seamCarver = new SeamCarver(picture);
		int rRow = 3;
		for (int row = 0; row < rRow; row++){
			StdOut.printf("remove: %dth\n",row);
			for (int p:seamCarver.findVerticalSeam()){
				StdOut.print(p + " ");
			}
			StdOut.println("");
			seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
		}
	}
}

