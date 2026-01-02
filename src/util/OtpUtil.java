package util;

import java.security.SecureRandom;

public class OtpUtil {
    private static final SecureRandom RND = new SecureRandom();

    public static String gen6() {
        int n = 100000 + RND.nextInt(900000);
        return String.valueOf(n);
    }
}
