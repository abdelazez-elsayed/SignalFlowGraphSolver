import java.util.ArrayList;
import java.util.Collections;

public class Utility {
    private static ArrayList<Path> combination;
    private static ArrayList<ArrayList<Path>> combinations;
    public static ArrayList<ArrayList<Path>> getAllCombinations(ArrayList<Path> loops, int length){

        combination = new ArrayList<>();
        combinations = new ArrayList<>();
        Collections.sort();
        getAllCombinations(0, loops, length);
        return combinations;
    }
    private static void getAllCombinations(int index, ArrayList<Path> comb, int length){

        if(combination.size() == length){

            ArrayList<Path> nCombination = new ArrayList<>();
            nCombination.addAll(combination);
            combinations.add(nCombination);
            return;
        }

        if(index == comb.size())
            return;

        combination.add(comb.get(index));
        getAllCombinations(index+1, comb, length);

        combination.remove(comb.get(index));
        getAllCombinations(index+1, comb, length);
    }
}
