import java.util.*;
public class Main {

    public static void main(String[] args) {

        System.out.println(helper(args[0], args[1]));
    }

    public static boolean helper(String args1, String args2){

        if(args1.length() == 0 || args1.length() != args2.length()) return false;

        Set<Character> set = new HashSet<>();

        char[] chars = args1.toCharArray();

        for(char c : chars){

            if(set.contains(c)) return false;

            set.add(c);
        }

        return true;

    }
}


