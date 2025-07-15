package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class FileImage implements Image {
    private static final Color DEFAULT_COLOR = Color.WHITE;

    private final Color[][] pixelArray;

    /**
     * Construsct matrix of Colors with padding and add the picture.
     * @param filename the name of the image
     * @throws IOException
     */
    public FileImage(String filename) throws IOException {
        java.awt.image.BufferedImage im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();
        //achieve the new Height of the image
        int newHeight = initializeNewDimensions(origHeight);
        //achieve the new Width of the image
        int newWidth = initializeNewDimensions(origWidth);
        //build the new picture dimensions
        pixelArray = new Color[newHeight][newWidth];
        //padding the old image with white in the sides
        paddingImage(origHeight,newHeight,origWidth,newWidth,im);
    }

    /**
     * This constructor generate new image (sub image) from existing image.
     * @param pixelArray A pixel array which represent sub-image
     */
    public FileImage(Color[][] pixelArray) {
        this.pixelArray = pixelArray;
    }

    /**
     * This method will update the height and then width of the picture to be power of 2.
     * @param param the original height or width of the original image
     */
    private int initializeNewDimensions(int param){
        int newDimension = 1;
        while (newDimension < param){
            newDimension*=2;
        }
        return newDimension;
    }

    @Override
    public int getHeight() {
        return pixelArray.length;
    }

    @Override
    public int getWidth() {
        return pixelArray[0].length;
    }

    @Override
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Take an image, put it in the center of empty board and fill the edges in white
     * @param origHeight the height of the original image
     * @param newHeight  the height of the new image
     * @param origWidth  the width of the original image
     * @param newWidth   the width of the new image
     * @param im         The image
     */
    private void paddingImage(int origHeight, int newHeight,int origWidth, int newWidth,
                              java.awt.image.BufferedImage im){
        //coloring all the board in WHITE (this will be the padding)
        for (int row=0;row<newHeight;row++){
            for (int col=0;col<newWidth;col++){
                pixelArray[row][col] = Color.WHITE;
            }
        }

            //put the original image in the center of the new picture
            int diff_height = (newHeight - origHeight) / 2; //define where to put the original img (in the center)
            int diff_width = (newWidth - origWidth) / 2;    //define where to put the original img (in the center)
            for (int row = diff_height; row < (origHeight + diff_height); row++) {
                for (int col = diff_width; col < (origWidth + diff_width); col++) {
                    pixelArray[row][col] = new Color(im.getRGB((col - diff_width),(row - diff_height)));
                }
            }
    }

}
