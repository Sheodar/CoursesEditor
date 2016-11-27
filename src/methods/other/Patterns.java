package  methods.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {

    public static boolean checkIDforValid(String qwerty) {
        Pattern p = Pattern.compile("\\d+");
        Matcher q = p.matcher(qwerty);
        return q.matches();
    }

    public static boolean check(String qwerty) {
        Pattern p = Pattern.compile("\\s*\\w+\\s*\\w*\\s*\\d*\\s*\\w*\\s*\\d*\\s*");
        Matcher q = p.matcher(qwerty);
        return q.matches();
    }

}
