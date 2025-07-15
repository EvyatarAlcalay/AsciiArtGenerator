package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import java.util.HashSet;
import java.util.Scanner;

public class Shell {
    private final static int MIN_PIXELS_PER_CHAR = 2;
    private final static int INITIAL_CHARS_IN_ROW = 64;
    private final static String BOUND_ERR_MSG = "Did not change due to exceeding boundaries";
    private final static String WRONG_INPUT_MSG = "Did not executed due to incorrect command";
    private final static String WRONG_FORMAT = "Did not add due to incorrect format";
    private final static String PROMPT = ">>> ";
    private final static String EXIT = "exit";
    public static final String CHARS = "chars";
    public static final String ADD = "add ";
    public static final String REMOVE = "remove ";
    public static final String RES_UP = "res up";
    public static final String RES_DOWN = "res down";
    public static final String CONSOLE = "console";
    public static final String RENDER = "render";
    public static final String SPACE = "space";
    public static final String ALL = "all";
    private Image image;
    private Scanner scanner;
    private HashSet<Character> chars;
    private int minCharsInRow;
    private int maxCharsInRow;
    private int charsInRow;
    private String output;

    /**
     * This is a constructor that initialize a scanner
     * @param image the image I want to convert
     */
    public Shell(Image image){
        this.image = image;
        this.scanner = new Scanner(System.in);
        this.chars = new HashSet<Character>();
        //initialize as a default 0-9
        for (char c ='0';c<='9';c++){
            chars.add(c);
        }
        minCharsInRow = Math.max(1, image.getWidth()/image.getHeight());
        maxCharsInRow = image.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow),
                minCharsInRow);
        this.output = "html";
    }

    /**
     * This is the method which run the program logic
     */
    public void run(){
        System.out.print(PROMPT);
        String input = scanner.nextLine(); //the user press enter first of all
        while (!(input.equals(EXIT))){
            if (input.equals(CHARS)){
                for (char c : chars){
                    System.out.print(c+" ");
                }
                System.out.println();
            }
            else if (input.startsWith(ADD)){
                add(input);
            }
            else if (input.startsWith(REMOVE)){
                remove(input);
            }
            else if (input.equals(RES_UP)) {
                if (charsInRow*2 <= maxCharsInRow){
                    charsInRow *=2;
                    System.out.println("Width set to " + charsInRow);
                }
                else{
                    System.out.println(BOUND_ERR_MSG);
                }
            }
            else if (input.equals(RES_DOWN)) {
                if (charsInRow/2 >= minCharsInRow){
                    charsInRow /=2;
                    System.out.println("Width set to " + charsInRow);
                }
                else{
                    System.out.println(BOUND_ERR_MSG);
                }
            }
            else if (input.equals(CONSOLE)){
                output = CONSOLE;
            }
            else if (input.equals(RENDER)){
                render();
            }
            else{
                System.out.println(WRONG_INPUT_MSG);
            }
            System.out.print(PROMPT);
            input = scanner.nextLine();
        }
        System.exit(0);
    }

    /**
     * This method responsible to present the ASCII image
     */
    private void render() {
        AsciiOutput asciiOutput = null;
        BrightnessImgCharMatcher charMatcher = new BrightnessImgCharMatcher(image, "Courier New");

        Character[] arr = new Character[chars.size()];
        chars.toArray(arr);

        var chars = charMatcher.chooseChars(charsInRow, arr);

        //configure the output
        if (output.equals(CONSOLE)){
            asciiOutput = new ConsoleAsciiOutput();
        }
        else{
            asciiOutput = new HtmlAsciiOutput("out.html","Courier New");
        }
        asciiOutput.output(chars);
    }

    /**
     * This method responsible to add more possible chars to draw with them the ASCII image
     * @param input how much chars I want to add
     */
    private void add(String input) {
        if (input.length() == 5){
            chars.add(input.charAt(4));
        }
        else if (input.endsWith(SPACE)){
            chars.add(' ');
        }
        else if (input.endsWith(ALL)){
            for (char c=' ';c<='~';c++){
                chars.add(c);
            }
        }
        else if((input.charAt(5) == '-') && (input.length() == 7)){
            for (char c = input.charAt(4);c<=input.charAt(6);c++){
                chars.add(c);
            }
        }
        else{
            System.out.println(WRONG_FORMAT);
        }
    }

    /**
     * This method responsible to remove possible chars to draw with them the ASCII image
     * @param input how much chars I want to remove
     */
    private void remove(String input){
        //for efficient necessary
        if (chars.size() == 0){
            return;
        }
        if (input.length() == 8){
            chars.remove(input.charAt(7));
        }
        else if (input.endsWith(SPACE)){
            chars.remove(' ');
        }
        else if (input.endsWith(ALL)){
            chars.clear();
        }
        else if((input.charAt(8) == '-') && (input.length() == 10)){
            for (char c = input.charAt(7);c<=input.charAt(9);c++){
                chars.remove(c);
            }
        }
        else{
            System.out.println(WRONG_FORMAT);
        }
    }

}
