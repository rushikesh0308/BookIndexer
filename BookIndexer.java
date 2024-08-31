
import java.io.*;
import java.util.*;

public class BookIndexer {

    public static void main(String[] args) {
        String[] pageFiles = {"C:\\Users\\Admin\\Desktop\\JavaAssignment\\Page1.txt", "C:\\Users\\Admin\\Desktop\\JavaAssignment\\Page2.txt", "C:\\Users\\Admin\\Desktop\\JavaAssignment\\Page3.txt"};
        Set<String> excludeWords = readExcludeWords("C:\\Users\\Admin\\Desktop\\JavaAssignment\\exclude-words.txt");
        Map<String, Set<Integer>> wordIndex = buildIndex(pageFiles, excludeWords);
        writeIndex(wordIndex, "C:\\Users\\Admin\\Desktop\\JavaAssignment\\index.txt");
    }
    
    
    private static Set<String> readExcludeWords(String excludeWordsFile) {
        Set<String> excludeWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(excludeWordsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                excludeWords.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading exclude words file: " + e.getMessage());
        }
        return excludeWords;
    }
    
    
    private static Map<String, Set<Integer>> buildIndex(String[] pageFiles, Set<String> excludeWords) {
        Map<String, Set<Integer>> wordIndex = new HashMap<>();
        for (int i = 0; i < pageFiles.length; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(pageFiles[i]))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split("\\W+"); 
                    for (String word : words) {
                        word = word.toLowerCase();
                        if (!excludeWords.contains(word)) {
                            Set<Integer> pages = wordIndex.getOrDefault(word, new HashSet<>());
                            pages.add(i+1); 
                            wordIndex.put(word, pages);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading page file " + pageFiles[i] + ": " + e.getMessage());
            }
        }
        return wordIndex;
    }
    
    
    private static void writeIndex(Map<String, Set<Integer>> wordIndex, String indexFile) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(indexFile))) {
            List<String> sortedWords = new ArrayList<>(wordIndex.keySet());
            Collections.sort(sortedWords);
            pw.println("Word : Page Numbers");
            pw.println("-------------------");
            for (String word : sortedWords) {
                if(word.length()>1 && checkdigits(word)==false){
                Set<Integer> pages = wordIndex.get(word);
                
                pw.println(word + " : " + pages.toString().replaceAll("[\\[\\]]", ""));
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing index file: " + e.getMessage());
        }
        
    }
    private static boolean checkdigits(String str){
        int i,len=str.length();
        for(i=0;i<len;i++){
            if(str.charAt(i)<'0' || str.charAt(i)>'9'){
                return false;
            }
        }
        return true;
    }

}
