package ascii_art.img_to_char;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import image.FileImage;
import image.Image;

public class BrightnessImgCharMatcher {

    private static final double BLUE_GREYSCALE = 0.0722;

    private static final double GREEN_GREYSCALE = 0.7152;

    private static final double RED_GREYSCALE = 0.2126;

    private static final int PIXELS_PER_CHAR = 16;
    private static final int RGB_MIN = 0;

    private static final int RGB_MAX = 255;

    private FileImage img;
    private String font;
    private final Map<Character,Double> brightnessDict;


    /**
     * This is a constructor for brightnessImgCharManager.
     * its initialize the hashmap of the ASCII letters
     * @param img  The image I convert to ASCII image
     * @param font The font of the ASCII text
     */
    public BrightnessImgCharMatcher(Image img, String font) {
        this.img = (FileImage)img;
        this.font = font;
        this.brightnessDict = new HashMap<Character,Double>();
    }

    /**
     * This method is build step by step the Ascii image
     * @param numCharsInRow  Number of chars in a row
     * @param charSet        The chars with them we will draw our picture
     * @return               The Ascii image
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {

        char[][] asciiImage = new char[numCharsInRow][numCharsInRow]; //the resulting ascii image. we will add
        //here ascii chars in the next.

        updateDictionaryOfCharBrightness(charSet); //here we will add values to our dictionary if necessary
        Iterator<Image> subImageIt = img.getSub(numCharsInRow); //This row is dividing the image to sub-pictures
        int subPicRow = 0, subPicCol = 0;

        //from here I convert every sub picture to Ascii char and finally add it to the ascii image
        while (subImageIt.hasNext()) {
            Image subImage = subImageIt.next();
            //calculate the average brightness of the sub image
            double brightnessAverageOfSubImage = calcBrightnessAverageOfImage(subImage);
            //fit a char to the brightness average of the image (this with the most brightness level)
            char minChar = fittingChars(charSet,brightnessAverageOfSubImage);
            // place the char in the matrix
            asciiImage[subPicRow][subPicCol++] = minChar;
            if(subPicCol == numCharsInRow){
                subPicCol = 0;
                subPicRow++;
            }
        }
        return asciiImage;
    }

    /**
     * This method is calculating the grey scale of a specific pixel
     * @param pixel A pixel in image
     * @return      The pixel greyscale
     */
    private double colorToGreyscale(Color pixel) {
        return pixel.getRed() * RED_GREYSCALE + pixel.getGreen() * GREEN_GREYSCALE +
                pixel.getBlue() * BLUE_GREYSCALE;
    }

    /**
     * This method fit to every new char to his brightness level, after a linear stretch
     * @param charSet The chars with them, we will draw our picture
     */
    private void updateDictionaryOfCharBrightness(Character[] charSet){
        double minBrightness = RGB_MAX, maxBrightness = RGB_MIN;
        // 1+2. calculate normalized char brightness, and the minimum and maximum brightness level of each char
        for(int i=0; i < charSet.length; i++) {
            if(!(brightnessDict.containsKey(charSet[i]))){
                double brightnessOfChar = calcCharBrightness(charSet[i]);
                brightnessDict.put(charSet[i],brightnessOfChar);
            }
            if(brightnessDict.get(charSet[i]) < minBrightness) minBrightness = brightnessDict.get(charSet[i]);
            if(brightnessDict.get(charSet[i]) > maxBrightness) maxBrightness = brightnessDict.get(charSet[i]);
        }

        // 3. calculate new char brightness by a linear stretch
        for(char c: charSet){
            double x = (brightnessDict.get(c)-minBrightness) / (maxBrightness-minBrightness);
            brightnessDict.put(c,x);
        }
    }


    /**
     * Calculate the char brightness by counting how many times it appears in the boolean array of the chat
     * and divide the amount by number of the pixels.
     * @param c The char I want to calculate his brightness
     * @return  Normalized char brightness
     */
    private double calcCharBrightness(char c) {
        // convert char to boolean matrix
        boolean[][] mat = CharRenderer.getImg(c, PIXELS_PER_CHAR, font);
        // count the number of white pixels
        int whitesCount = 0;
        for(int i = 0; i < mat.length; i++) {
            for(int j = 0; j < mat[i].length; j++) {
                if(mat[i][j])
                    whitesCount++;
            }
        }
        // normalize to a value between  0 and 1
        return (double)whitesCount/PIXELS_PER_CHAR;
    }

    /**
     * This method calculates the average brightness of image
     * @param image The image I calculate her average brightness
     * @return      The average brightness
     */
    private double calcBrightnessAverageOfImage(Image image){
        // sum the greyscale values of every pixel
        double greyscaleSum = 0;
        for(int i = 0; i < image.getHeight(); i++) {
            for(int j = 0; j < image.getWidth(); j++) {
                greyscaleSum += colorToGreyscale(image.getPixel(i, j));
            }
        }
        // sum of all grey-scaled pixels divided by total number of pixels
        double brightnessAverageOfSubImage = greyscaleSum / (image.getHeight() * image.getWidth());
        brightnessAverageOfSubImage = brightnessAverageOfSubImage/RGB_MAX;
        return brightnessAverageOfSubImage;
    }

    /**
     * This method is find the char with the closest brightness to specific brightness level
     * @param charSet                  The chars with them we will draw our picture
     * @param brightnessAverageOfImage The brightness level I want to close
     * @return                         The most closet char to the char with our
     */
    private char fittingChars(Character[] charSet, double brightnessAverageOfImage){
        char minChar = charSet[0];
        double minDiff = Double.MAX_VALUE;
        for(Map.Entry<Character,Double> element : brightnessDict.entrySet()){
            double diff = Math.abs(element.getValue()-brightnessAverageOfImage);
            if(diff  < minDiff) {
                minDiff = diff;
                minChar = element.getKey();
            }
        }
        return minChar;
    }
}
