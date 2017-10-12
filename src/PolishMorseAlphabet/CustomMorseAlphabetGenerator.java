package PolishMorseAlphabet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class CustomMorseAlphabetGenerator {

    private Map<Character, Integer> charMap;
    private Map<Double, Character> charPercentageMap;
    private Map<Character,String> charsAndSymbolsMap;
    private List<int[]> numericCombinations;
    private List<String> symbols;

    private long numberOfChars = 0;

    public CustomMorseAlphabetGenerator(){
        charMap = new HashMap<>();
        charPercentageMap = new TreeMap<>(Collections.reverseOrder());
        charsAndSymbolsMap = new TreeMap<>();
        numericCombinations = new ArrayList<>();

        int initialNumberOfOccurences = 0;
        //populate map with common Latin chars
        for(char c = 'a'; c <= 'z'; ++c){
            charMap.put(c,initialNumberOfOccurences);
        }

        charMap.put('ą',initialNumberOfOccurences);
        charMap.put('ć',initialNumberOfOccurences);
        charMap.put('ę',initialNumberOfOccurences);
        charMap.put('ł',initialNumberOfOccurences);
        charMap.put('ń',initialNumberOfOccurences);
        charMap.put('ó',initialNumberOfOccurences);
        charMap.put('ś',initialNumberOfOccurences);
        charMap.put('ż',initialNumberOfOccurences);
        charMap.put('ź',initialNumberOfOccurences);
    }
    public void parseDictionaryFile(String fileName){

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                parseWord(word);
//                System.out.println(word);
            }
        }
        catch (Exception e){
            System.out.println( e.getMessage());
        }

        //calculate percentage of occurrence for each letter
        for(Map.Entry pair: charMap.entrySet()){
            //pair.first == letter
            //pair.second == number of letter's occurrences

            double percentageOfOccurrence = (int)pair.getValue() / (double)numberOfChars * 100;
            charPercentageMap.put(percentageOfOccurrence,(char)pair.getKey());
        }

        //print percentages of occurrence for each letter
        for(Map.Entry pair: charPercentageMap.entrySet()){
            System.out.format("%c %.2f %c%n ",(char)pair.getValue(), (double)pair.getKey(), '%');
        }
    }

    private void parseWord(String word){

        for(int i = 0; i < word.length(); i++)
        {
            char letter = word.charAt(i);

            if(letter != '\r' && letter != '\n' && letter != ' '&& letter != '\t'){
                //count occurrences of each letter
                charMap.put(letter, charMap.get(letter) + 1);

                //count whole number of letters
                ++numberOfChars;
            }
        }
    }

    public Map<Character,String> generateMorseAlphabet(){

        generateSymbolList(5);

        int i = 1; // there is whitespace at index 0, I don't know why
        for (Map.Entry pair : charPercentageMap.entrySet()){
            charsAndSymbolsMap.put((char)pair.getValue(), symbols.get(i));
            i++;
        }

        //print map
        for (Map.Entry pair : charsAndSymbolsMap.entrySet()){
            System.out.println(pair.getKey() + " " + pair.getValue());
        }
        return charsAndSymbolsMap;
    }

    private void generateSymbolList(int longestSymbol){

        int i = 0;

        while(i <= longestSymbol){
            List<int[]> list = createNumericCombinations(i,new int[] {0,1});

            numericCombinations.addAll(list);
            i++;
        }

        changeNumericCombinationsIntoSymbolic();

        //print to check symbols
        for (String s: symbols){
            System.out.println(s);
        }
    }

    private List<int[]> createNumericCombinations(int n, int[] arr){

        List<int[]> combinations = new ArrayList<>();

        // Calculate the number of arrays we should create
        int numArrays = (int)Math.pow(arr.length, n);
        // Create each array
        for(int i = 0; i < numArrays; i++) {
            combinations.add(new int[n]);
        }
        // Fill up the arrays
        for(int j = 0; j < n; j++) {
            // This is the period with which this position changes, i.e.
            // a period of 5 means the value changes every 5th array
            int period = (int) Math.pow(arr.length, n - j - 1);
            for(int i = 0; i < numArrays; i++) {
                int[] current = combinations.get(i);
                // Get the correct item and set it
                int index = i / period % arr.length;
                current[j] = arr[index];
            }
        }
        return combinations;
    }

    private void changeNumericCombinationsIntoSymbolic(){

        symbols = new ArrayList<>();

        for (int arrNumber = 0; arrNumber < numericCombinations.size(); arrNumber++){

            int[] arr = numericCombinations.get(arrNumber);
            String symbolicCombination;
            StringBuilder builder = new StringBuilder();

            for (int arrIndex = 0; arrIndex < arr.length; arrIndex++){
                if (arr[arrIndex] == 0){
                    builder.append(".");
                } else if (arr[arrIndex] == 1){
                    builder.append("-");
                }
            }
            symbolicCombination = builder.toString();
            symbols.add(symbolicCombination);
        }
    }

    public static void main(String[] args) {
        CustomMorseAlphabetGenerator generator = new CustomMorseAlphabetGenerator();
        generator.parseDictionaryFile("slowa.txt");
        generator.generateMorseAlphabet();
    }
}