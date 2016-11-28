package  methods.utils;

import java.util.Random;

public class Randomaizer {
    private  static Random random = new Random();
    public static int random() {
        return random.nextInt(100)+1;
    }
}
