package com.publicdotcom;


import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;

public class TextProcessor {
    public static int pageLineWidth = 75;
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);

        //scan input text
        System.out.println("Enter the input text:");

        //validate input text

        String input = validateStringInput(sc);

        //find the length of the longest string, this is to make sure the width is no less than longest string length
        String[] splitStrings = input.split(" ");
        int largestStr = Integer.MIN_VALUE;
        for(String s: splitStrings){
            largestStr= Math.max(largestStr, s.length());
        }

        System.out.println("Enter a number between "+largestStr  + " (length of the longest word in the input) and "+pageLineWidth);

        //scan and validate width of text
        int width = validateIntInput(sc,"width", input, largestStr);

        System.out.println("Enter the alignment from below \n1. CENTER \n2. LEFT \n3. RIGHT \n4. HARD");

        //scan and validate alignment
        int alignment = validateIntInput(sc,"alignment", input, largestStr);

        //create a custom formatter that applies the width and alignment options on the input
        CustomFormatter util = new CustomFormatter(width, alignment);
        try {
            //print the formatted response on the standard out
            System.out.println(util.format(input, new StringBuffer(), null));
        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Re-run the program with a different input.");
        }
    }

    private static String validateStringInput(Scanner sc) {
        String res="";
        do {
            res = sc.nextLine();
            if (res.trim().isEmpty() || res.trim().length() > Integer.MAX_VALUE) {
                System.out.println("Please enter text of reasonable length between 0 and " + Integer.MAX_VALUE + " words.");
            }
        } while(res.trim().isEmpty() || res.trim().length() > Integer.MAX_VALUE);
        return res;
    }

    private static int validateIntInput(Scanner sc, String option, String input, int longestStrLen) {
        int res=0;
        switch(option) {
            case "alignment":
                //result = sc.nextInt();
                do {
                    //System.out.println("Enter a number from below \n1. CENTER \n2. LEFT \n3. RIGHT \n4. HARD");
                    while (!sc.hasNextInt()) {
                        System.out.println("Please select a valid option from below \n1. CENTER \n2. LEFT \n3. RIGHT \n4. HARD");
                        sc.next(); //keep reading input until
                    }
                    res = sc.nextInt();

                    if (res < 1 || res > 4)
                        System.out.println("Please select a valid option from below \n1. CENTER \n2. LEFT \n3. RIGHT \n4. HARD");

                } while (res < 1 || res > 4);
                break;
            case "width":
                //result = sc.nextInt();
                do {

                    while (!sc.hasNextInt()) {
                        System.out.println("Enter a number between " + longestStrLen + " (length of the longest word in the input) and " + pageLineWidth);
                        sc.next();
                    }
                    res = sc.nextInt();
                    if (res < longestStrLen || res > 75)
                        System.out.println("Enter a number between " + longestStrLen + " (length of the longest word in the input) and " + pageLineWidth);


                } while (res < longestStrLen || res > 75);
                break;
        }
        return res;
    }

}

class CustomFormatter extends Format {
    public enum Alignment {
        LEFT, CENTER, RIGHT, HARD,
    }
    Alignment align;
    private int width;
    public CustomFormatter(int width, int alignment) {
        this.width = width;
        switch (alignment) {
            case 1: align=Alignment.CENTER;break;
            case 2: align=Alignment.LEFT;break;
            case 3: align=Alignment.RIGHT; break;
            case 4: align=Alignment.HARD; break;
            default: throw new IllegalArgumentException("cannot recognize the alignment type.");
        }
    }

    @Override
    public StringBuffer format(Object input, StringBuffer buffer, FieldPosition pos) {
        String s = input.toString();
        List<String> strings;
        try {
            //process the input text to construct list of lines based on width and alignment type
            strings = splitInputStrings(s, align);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Iterator<String> textIterator = strings.iterator();
        while (textIterator.hasNext()) {
            String currWord = textIterator.next();
            switch (align) {
                case RIGHT:
                    //append spaces at the beginning of each line
                    appendSpaces(buffer, width - currWord.length());
                    buffer.append(currWord);
                    break;
                case CENTER:
                    int numberOfSpaces = width - currWord.length();
                    //pad half the spaces before the first word
                    appendSpaces(buffer, numberOfSpaces / 2);
                    buffer.append(currWord);
                    //append half the spaces after the last word
                    appendSpaces(buffer, numberOfSpaces - numberOfSpaces / 2);
                    break;
                case LEFT:
                case HARD:
                    buffer.append(currWord);
                    //append spaces at the end of each line
                    appendSpaces(buffer, width - currWord.length());
                    break;
            }
            buffer.append("\n");
        }
        return buffer;
    }

    /**
     * append a string of spaces to the string
     * @param buffer
     * @param numberOfSpaces
     */
    protected final void appendSpaces(StringBuffer buffer, int numberOfSpaces) {
        for (int i = 0; i < numberOfSpaces; i++) {
            buffer.append(' ');
        }
    }

    String format(String s) {
        return format(s, new StringBuffer(), null).toString();
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return source;
    }

    /**
     *  split input text into list of lines based on specified width based on the following constraint:
     *  words are not split if the remaining line width is smaller than the word width.
     * @param str input non empty text
     * @return list of lines
     * @throws Exception
     */
    private List<String> getNonHardAlignmentLines(String str) throws Exception {
        List<String> list = new ArrayList<String>();
        if (str == null)
            return list;
        while (str.length()>0) {
            //split words in the text
            String[] splitStrings = str.split(" ");
            StringBuffer strBuffer = new StringBuffer();
            int linewidth = width;
            for(String subs: splitStrings){
                if(subs.length()>width){
                    throw new Exception("Some of the words are longer than specified line width.");
                }
                linewidth = linewidth-subs.length();
                if(linewidth>=0) {
                    //add spaces between words
                    strBuffer.append(subs).append(" ");
                    linewidth--;
                } else{
                    break;
                }
            }
            //remove the trailing space before adding line to the list
            String line = strBuffer.substring(0,strBuffer.length()-1);
            list.add(line);
            //remove leading and trailing spaces in the constructed string
            str = str.substring(line.length()).trim();
        }
        return list;
    }

    /**
     * method splits input text into a list of lines based on
     * HARD alignment type or not
     * @param str the input text
     * @param align alignment enum type
     * @return
     * @throws Exception
     */
    private List<String> splitInputStrings(String str,  Alignment align) throws Exception {
        List<String> list = new ArrayList<String>();
        if(align!=Alignment.HARD) {
            // if alignment type not HARD, construct each line such that
            // words are not split if the remaining line width is smaller than the word width.
            list = getNonHardAlignmentLines(str);
        } else {
            //if alignment type is HARD, words will be cut when line exceeds the specified width
            if (str == null) {
                return list;
            }
            for (int i = 0; i < str.length(); i = i + width) {
                int index = Math.min(i + width, str.length());
                list.add(str.substring(i, index));
            }
        }
        return list;
    }
}