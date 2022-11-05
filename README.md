# PublicTool
Text formatting tool for Publicdotcom

## Pre-requisites

- [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
- [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

## Build and Execute:

1. Clone the git repo on your laptop, navigate to "PublicTool" project folder

2. Run the following command from terminal/command prompt
>>dheeratallapragada@macbook-pro PublicTool % mvn package

3. This will generate `PublicTool-1.0-SNAPSHOT.jar` under `target` directory

4. Run the executable using the below command
>>`java -cp target/PublicTool-1.0-SNAPSHOT.jar com.publicdotcom.TextProcessor`

## Implementation and Assumptions:

1. I've implemented the following formatting styles:
      - left
      - right
      - center
      - hard
2. The application takes 3 inputs: 
      - input text (string)
      - width (int)
      - alignment style (int)
3. For the ease of validating input text, I've set the upper limit of number of characters in the text to be `2147483647` which is equal to `Integer.MAX_VALUE` in Java.
4. For the ease of validating `width` input, I've assumed the lower limit of page width to be the length of the longest word in the input text and the upper limit of the page width to be 75 chars (which I've assumed as the average number of characters in a line of a word doc). It's a constant that can be configured in the code in the `PAGE_LINE_WIDTH` variable. Eg: For input text, 
    ```
    Social media sites are riddled with ads, trackers, and dark patterns. As much as I'd love to avoid using them for privacy reasons or time concerns, it's just not possible to keep up to date with current events without them.
    ``` 
    The length of the longest word is 9 characters. My code will only accept width value > 9.

## Roadmap
1. Implement `full` formatting style. I understand that implementing this style needs a new way of parsing text and padding spaces, which the current implementation wouldn't support.
2. Add unit tests
