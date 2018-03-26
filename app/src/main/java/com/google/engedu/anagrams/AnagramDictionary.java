/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static int wordLength = DEFAULT_WORD_LENGTH;

    public  ArrayList<String> wordList = new ArrayList<String>();
    public HashSet<String> wordSet =new HashSet<String>();
    public HashMap<String,ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    public HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedword = sortletters(word);
            ArrayList<String> wordMap;

            if (sizeToWords.containsKey(word.length())) {
                wordMap = sizeToWords.get(word.length());
                wordMap.add(word);
                sizeToWords.put(word.length(), wordMap);
            } else {
                ArrayList<String> newWordList = new ArrayList<>();
                newWordList.add(word);
                sizeToWords.put(word.length(), newWordList);
            }



            ArrayList<String> sortedlist = new ArrayList<String>();



            if(lettersToWord.containsKey(sortedword)){
                ArrayList<String> current = lettersToWord.get(sortedword);
                current.add(word);
                lettersToWord.put(sortedword,current);
            }
            else{
                lettersToWord.put(sortedword,sortedlist);
                sortedlist.add(word);
            }

        }

    }

    public boolean isGoodWord(String word, String base) {

        if(wordSet.contains(word)){
            if(word.contains(base)){
                return false;
            }
            else
                return true;
        }
        else
            return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String targetsorted=sortletters(targetWord);
        int checklen=targetsorted.length();
        for(int i=0;i<wordList.size();i++){
            int newwordlen=wordList.get(i).length();
            if(newwordlen==checklen){
                String newwordsorted=sortletters(wordList.get(i));
                if(newwordsorted.equals(targetsorted)){
                    result.add(wordList.get(i));
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> temp;
        ArrayList<String> result = new ArrayList<String>();

        for (char a = 'a'; a <= 'z'; a++) {
            String anagram = word + a;
            String sortedAnagram = sortletters(anagram);

            if (lettersToWord.containsKey(sortedAnagram)) {
                temp = lettersToWord.get(sortedAnagram);

                for (int i = 0; i < temp.size(); i++) {
                    if ( !(temp.get(i).contains(word)) ) {
                        result.add(temp.get(i));
                    }
                }
            }
        }

        return result;
    }

    public String sortletters(String word){
        String sortedword;
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        sortedword = new String(chars);
        return sortedword;
    }

    public String pickGoodStarterWord() {
            int randomNumber;
            String starterWord;

            do {
                randomNumber = random.nextInt(sizeToWords.get(wordLength).size());
                starterWord = sizeToWords.get(wordLength).get(randomNumber);
            } while (getAnagramsWithOneMoreLetter(starterWord).size() < MIN_NUM_ANAGRAMS);

            if (wordLength < MAX_WORD_LENGTH) {
                wordLength++;
            }

            return starterWord;
        }

}
