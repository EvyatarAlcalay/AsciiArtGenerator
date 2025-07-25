package image;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Facade for the image module and an interface representing an image.
 * @author Dan Nirel
 */
public interface Image {
    Color getPixel(int x, int y);
    int getWidth();
    int getHeight();

    /**
     * Open an image from file. Each dimensions of the returned image is guaranteed
     * to be a power of 2, but the dimensions may be different.
     * @param filename a path to an image file on disk
     * @return an object implementing Image if the operation was successful,
     * null otherwise
     */
    static Image fromFile(String filename) {
        try {
            return new FileImage(filename);
        } catch(IOException ioe) {
            return null;
        }
    }

    /**
     * Allows iterating the pixels' colors by order (first row, second row and so on).
     * @return an Iterable<Color> that can be traversed with a foreach loop
     */
    default Iterable<Color> pixels() {
        return new ImageIterableProperty<>(
                this, this::getPixel);
    }

    /**
     *
     * @param numOfSubImagesInRow How many sub-images there will be in a row
     * @return Iterable arraylist of every sub image
     */
    default Iterator<Image> getSub(int numOfSubImagesInRow){
        ArrayList<Image> subImageList = new ArrayList<Image>();
        int subImageEdge = getWidth()/numOfSubImagesInRow;
        for (int row =0;row<getHeight();row+=subImageEdge){
            for (int col=0;col<getWidth();col+=subImageEdge){
                subImageList.add(createSubImage(subImageEdge,row,col));
            }
        }
        return subImageList.iterator();
    }

    /**
     * This method create a new sub-image
     * @param numOfSubImagesInRow How many sub-images there will be in a row
     * @param x                   The upper left row of the sub-image
     * @param y                   The upper left col of the sub-image
     * @return                    A sub-image
     */
    private FileImage createSubImage(int numOfSubImagesInRow, int x,int y){
        Color[][] subImagePixelArray = new Color[numOfSubImagesInRow][numOfSubImagesInRow];
        for(int row=0;row<numOfSubImagesInRow;row++){
            for(int col=0;col<numOfSubImagesInRow;col++){
                subImagePixelArray[row][col] = getPixel(x + row, y + col);
            }
        }
        FileImage subImage = new FileImage(subImagePixelArray);
        return subImage;
    }
}
