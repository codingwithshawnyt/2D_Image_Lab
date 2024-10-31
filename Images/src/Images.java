/*
 * Note: these methods are public in order for them to be used by other files
 * in this assignment; DO NOT change them to private.  You may add additional
 * private methods to implement required functionality if you would like.
 *
 * You should remove the stub lines from each method and replace them with your
 * implementation that returns an updated image.
 */

import acm.graphics.*;

public class Images implements ImageConversions {

    public GImage flipHorizontal(GImage source) {
        int [][] photo = source.getPixelArray(); //Obtaining matrix that has the pixels
        int temp; //variable to store rgb value for swap
        int n = photo[0].length - 1; //represents last element in column
        for(int x = 0; x < photo.length; x++) //these two help us iterate through the matrix
        {
        	for(int y = 0; y <= n/2; y++)
        	{
        		temp = photo[x][y]; //using temp to store a value while we swap
        		photo[x][y] = photo[x][n-y]; //doing the swapping across the horizontal
        		photo[x][n-y] = temp; //finishing the swap by using the temp value representing the inital pre-swap 
        	}
        }
        
        source.setPixelArray(photo);
        return source;
    }

    public GImage rotateLeft(GImage source) { 
    	int[][] photo = source.getPixelArray(); //Obtaining matrix that represents the different pixels
    	int n = photo.length; //getting length of rows
    	int m = photo[0].length; //getting length of columns
    	int[][] output = new int[m][n]; //initializing new array with length of rows and columns swapped
    	for (int x = 0; x < n; x++) //these two lines iterate through the rows and columns respectively and aid us in going through the matrix
    	{
    	    for (int y = 0; y < m; y++) 
    	    {
    	        output[m - 1 - y][x] = photo[x][y]; //This is the mathematical portion where the actual changes are being made. In this case, we are finding the position of a pixel in the matrix photo, which is actually being iterated as usual for a 2x2 matrix, and updating the relevant pixel in our updated matrix. We're using m as the end of the matrix's row, removing one because of the .length command being zero index, and then remove the current iteration of the value of y (column iteration). The columns don't need to be changed beyond a swapout here.
    	    }
    	}
    	source.setPixelArray(output); //updating the source matrix
    	return source;

    }

    public GImage rotateRight(GImage source) {
    	int[][] photo = source.getPixelArray(); //Obtaining matrix that represents the different pixels
        int n = photo.length; //getting length of rows
        int m = photo[0].length; //getting length of columns
        int [][] output = new int[m][n]; //initializing new array with length of rows and columns swapped
        for (int x = 0; x < n; x++) //these two lines iterate through the rows and columns respectively and aid us in going through the matrix
        {
        	for (int y = 0; y < m; y++)
        	{
        		output [y][n-1-x] = photo[x][y]; //If you got the one for rotateLeft, this should make conceptual sense as well. In this case, we are finding the position of a pixel in the matrix photo, which is actually being iterated as usual for a 2x2 matrix, and updating the relevant pixel in our updated matrix. We're using n as the end of the matrix's column, removing one because of the .length command being zero index, and then remove the current iteration of the value of x (row iteration). The columns don't need to be changed beyond a swapout here.
        	}
        }
        source.setPixelArray(output); //updating the source matrix
    	return source;
    }

    public GImage greenScreen(GImage source) {
        int[][] photo = source.getPixelArray(); //Obtaining the actual matrix that represents each pixel
        for(int x = 0; x < photo.length; x++) //These two for loops are how we iterate through each pixel in the matrix
        {
        	for(int y = 0; y < photo[x].length; y++)
        	{
        		if((GImage.getGreen(photo[x][y]) >= (GImage.getBlue(photo[x][y]) * 2))) //checking if the green value is more than 2 times the blue value
        		{
        				if((GImage.getGreen(photo[x][y]) >= (GImage.getRed(photo[x][y]) * 2))) //checking if the green value is more than 2 times the red value
        				{
        					photo[x][y] = GImage.createRGBPixel(1,1,1,0);	//if both of the above are true, we can replace the pixel with a completely transparent one
        				}	
        		}
        		
        	}
        }
        source.setPixelArray(photo);
        return source;
    }

    public GImage equalize(GImage source) {
        int[][] photo = source.getPixelArray(); //Obtaining array that has matrix of pixels
        int[] lums = new int[256]; //initializing array that will contain lumination count (i'm too lazy to describe exactly what it is also it's in the document anyway)
        int[] cumlums = new int[256]; //initializing array that will contain cumulative lumination count (see above line)
        int totalPixels = photo.length * photo[0].length; //Obtaining area of matrix (aka total number of pixels)

        // Computing the luminosity histogram
        for (int x = 0; x < photo.length; x++) { //these two loops help us iterate through the matrix
            for (int y = 0; y < photo[x].length; y++) {
                int luminosity = computeLuminosity(GImage.getRed(photo[x][y]), GImage.getGreen(photo[x][y]), GImage.getBlue(photo[x][y])); //obtaining luminosity value for a given pixel
                lums[luminosity]++; //adding a number to whatever corresponding luminosity value it represents in the array
            }
        }

        // Computing the cumulative luminosity histogram
        int cumulativeSum = 0; //initial cumsum = 0
        for (int i = 0; i < 256; i++) { //iterating through array of luminosities
            cumulativeSum += lums[i]; //this value, cumulativeSum, will contain the total value and will aggregate as we iterate through lums
            cumlums[i] = cumulativeSum; //this is how we actually are changing our cumsums array and are making it so that as we get further in the value will increase with the integer variable cumulativeSum
        } //ngl this was easier than it looks, and it looks pretty easy as is

        // Modifying each pixel to increase contrast
        for (int x = 0; x < photo.length; x++) {
            for (int y = 0; y < photo[x].length; y++) {
                int luminosity = computeLuminosity(GImage.getRed(photo[x][y]), GImage.getGreen(photo[x][y]), GImage.getBlue(photo[x][y]));
                int newLuminosity = (255 * cumlums[luminosity]) / totalPixels;
                int newPixel = GImage.createRGBPixel(newLuminosity, newLuminosity, newLuminosity);
                photo[x][y] = newPixel;
            }
        }

        // Create a new GImage from the modified pixel array
        source.setPixelArray((photo));
        return source;
    }

    public GImage negative(GImage source) {
        int[][] photo = source.getPixelArray(); // Obtains the matrix to display each pixel
        int numRows = photo.length; //getting the number of rows
        int numCols = photo[0].length; //getting number of columns (rectangle matrix guaranteed so we know the first row will have equal number of columns as all others)

        for (int r = 0; r < numRows; r++) { //iterating through matrix
            for (int c = 0; c < numCols; c++) {
                int red = GImage.getRed(photo[r][c]); //getting red value of pixel
                int blue = GImage.getBlue(photo[r][c]); //blue value of pixel
                int green = GImage.getGreen(photo[r][c]); //red value of pixel
                photo[r][c] = GImage.createRGBPixel(255 - red, 255 - green, 255 - blue); //literally the same thing it says to do in the doc, aka inverse colors
            }
        }

        // set the modified pixel array
        source.setPixelArray(photo);

        return source;
    }

    public GImage translate(GImage source, int dx, int dy) {
        int[][] mat = source.getPixelArray(); // Obtaining matrix with pixels
        int width = mat[0].length; // column length of array
        int height = mat.length; // row length of array
        int[][] translatedMat = new int[height][width]; // new matrix with translated values

        for (int r = 0; r < height; r++) { // iterating through our array
            for (int c = 0; c < width; c++) { // see above
                int newRow = (r + dy + height) % height; // Wrap around both edges
                int newCol = (c + dx + width) % width; // Wrap around both edges

                // Ensure the result is non-negative
                if (newRow < 0) {
                    newRow += height;
                }
                if (newCol < 0) {
                    newCol += width;
                }

                translatedMat[newRow][newCol] = mat[r][c]; // setting the translated value into our matrix
            }
        }

        return new GImage(translatedMat);
    }

    public GImage blur(GImage source) {
        int[][] photo = source.getPixelArray(); //bruh literally every other function has this i'm not re-explaining it
        int[][] blurredPhoto = new int[photo.length][photo[0].length]; //this is going to be the array that stores the blurred pixels. we're taking the doc's advice and not doing an in-place

        for (int r = 0; r < photo.length; r++) { //same as always, iterating through the array 
            for (int c = 0; c < photo[r].length; c++) { //columns iteration
                int totalRed = 0, totalGreen = 0, totalBlue = 0, count = 0; //these will be used to calculate average color values for the current pixel & total count

                for (int dr = -1; dr <= 1; dr++) { //these two loops consider the surrounding pixels of the current pixel (incl. the current pixel)
                    for (int dc = -1; dc <= 1; dc++) {
                        int newRow = r + dr; //these two calculate row/column indices of the surrounding pixels based on the currrent pixel and the offsets dr and dc
                        int newCol = c + dc; //see prev. line

                        if (newRow >= 0 && newRow < photo.length && newCol >= 0 && newCol < photo[r].length) { //checking in bounds
                            totalRed += GImage.getRed(photo[newRow][newCol]); //these three are to add the respective color value to the main one we're using
                            totalGreen += GImage.getGreen(photo[newRow][newCol]); //see previous line
                            totalBlue += GImage.getBlue(photo[newRow][newCol]); //see line 159 (2 lines above)
                            count++; //need to add to the count (num of pixels we've iterated through in this run for this current pixel)
                        }
                    }
                }

                int avgRed = totalRed / count, avgGreen= totalGreen/count, avgBlue = totalBlue/count; //these three are the averages using which we will actually add in the new and updated pixel

                blurredPhoto[r][c] = GImage.createRGBPixel(avgRed, avgGreen, avgBlue); //what i said we'd do in line 167; add the updated relevant pixel to the new matrix
            }
        }

        return new GImage(blurredPhoto);
    }

}
