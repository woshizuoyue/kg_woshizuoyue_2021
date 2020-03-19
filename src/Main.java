import java.util.*;
public class Main {

    public static void main(String[] args) {

        System.out.println(helper(args[0], args[1]));
    }

    public static boolean helper(String args1, String args2){

        if(args1.length() != args2.length()) return false;

        Map<Character, Character> map = new HashMap<>();

        Set<Character> set = new HashSet<>();

        char[] chars1 = args1.toCharArray();
        char[] chars2 = args2.toCharArray();

        Arrays.sort(chars1);
        Arrays.sort(chars2);

        for(char c : chars1){

            if(set.contains(c)) return false;
        }

        for(int i = 0; i<chars1.length; i++){

            if(map.containsKey(chars1[i])){

                if(map.get(chars1[i]) != chars2[i]) return false;
            }

            else{

                map.put(chars1[i], chars2[i]);
            }
        }

        return true;
    }
}


