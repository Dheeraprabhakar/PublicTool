package com.publicdotcom;


import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;

public class TextProcessor {
    public static int pageLineWidth = 75;
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the input text:");

        String input = validateStringInput(sc);

        String[] splitStrings = input.split(" ");
        int largestStr = Integer.MIN_VALUE;
        for(String s: splitStrings){
            largestStr= Math.max(largestStr, s.length());
        }

        System.out.println("Enter a number between "+largestStr  + " (length of the longest word in the input) and "+pageLineWidth);

        int width = validateIntInput(sc,"width", input, largestStr);

        System.out.println("Enter the alignment from below \n1. CENTER \n2. LEFT \n3. RIGHT \n4. HARD");

        int alignment = validateIntInput(sc,"alignment", input, largestStr);


        CustomFormatter util = new CustomFormatter(width, alignment);
        System.out.println("You've chosen " + util.align+" alignment");
        try {
            System.out.println(util.format(input, new StringBuffer(), null));
        } catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Try again with a different input");
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
        List<String> strings = null;

        try {
            strings = splitInputStrings(s, align);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Iterator<String> itr = strings.iterator();

        while (itr.hasNext()) {
            String currWord = itr.next();

            switch (align) {
                case RIGHT:
                    padSpaces(buffer, width - currWord.length());
                    buffer.append(currWord);
                    break;

                case CENTER:
                    int toAdd = width - currWord.length();
                    padSpaces(buffer, toAdd / 2);
                    buffer.append(currWord);
                    padSpaces(buffer, toAdd - toAdd / 2);
                    break;

                case LEFT:

                case HARD:
                    buffer.append(currWord);
                    padSpaces(buffer, width - currWord.length());
                    break;
            }
            buffer.append("\n");
        }
        return buffer;
    }

    protected final void padSpaces(StringBuffer buffer, int numberOfSpaces) {
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

    private List<String> splitInputStrings(String str) throws Exception {

        List<String> list = new ArrayList<String>();
        if (str == null)
            return list;
        while ( str.length()>0) {
            String[] splitStrings = str.split(" ");
            StringBuffer s = new StringBuffer();
            int linewidth = width;
            for(String subs: splitStrings){
                if(subs.length()>width){
                    throw new Exception("Some of the words are longer than specified line width.");
                }
                linewidth = linewidth-subs.length();
                if(linewidth>=0) {
                    s.append(subs).append(" ");
                    linewidth--;
                } else{
                    break;
                }
            }
            String line = s.substring(0,s.length()-1).toString();
            list.add(line);
            str = str.substring(line.length()).trim();
        }
        return list;
    }
    private List<String> splitInputStrings(String str,  Alignment align) throws Exception {
        List<String> list = new ArrayList<String>();
        if(align!=Alignment.HARD) {
            list = splitInputStrings(str);
        } else {
            if (str == null)
                return list;
            for (int i = 0; i < str.length(); i = i + width)
            {
                int endindex = Math.min(i + width, str.length());
                list.add(str.substring(i, endindex));
            }
        }
        return list;
    }
}