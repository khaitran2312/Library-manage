package Polyfill;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import ThuVien.Global;

public final class StringHelper {
    private static final Logger LOGGER = Logger.getLogger(StringHelper.class.getName());

    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    // this project's general single field seperator
    public static String[] lv1Split(String line) {
        if (isNullOrBlank(line)) {
            return new String[0];
        }
        return lv1Sep.split(line);
    }

    public static String lv1Join(Object... words) {
        return concater("|", words);
    }

    public static String[] lv2Split(String line) {
        if (isNullOrBlank(line)) {
            return new String[0];
        }
        return lv2Sep.split(line);
    }

    public static String lv2Join(String[] words) {
        return String.join("\\", words);
    }

    public static String[] splitThenTrim(String sep, String line) {
        return Stream.of(line.split(sep)).map(String::trim).toArray(String[]::new);
    }

    // lol why
    public static String liner(Object... lines) {
        return concater("\n", lines);
    }

    // bruh
    public static String spacer(Object... words) {
        return concater(" ", words);
    }

    // boomer
    public static String itemer(Object key, Object value) {
        if (value == null) {
            value = "null";
        }
        return spacer(key + ":", value);
    }

    // it's kinda weird tho
    public static String concater(String delim, Object... strings) {
        try {
            if (strings instanceof String[]) {
                return String.join(delim, (String[]) strings);
            }
            return String.join(delim, vararg2str(strings));
        } catch (NullPointerException e) {
            return "";
        }
    }

    // well this one is important
    public static String[] vararg2str(Object... objs) {
        return Stream.of(objs).map(Object::toString).toArray(String[]::new);
    }

    public static String arr2str(Object[] objs) {
        return Stream.of(objs).map(Object::toString).collect(Collectors.joining("\n"));
    }

    public static String[] pfa2str(PFArray<Object> pfa) {
        return StreamSupport.stream(pfa.spliterator(), false).map(Object::toString).toArray(String[]::new);
    }

    public static int acceptInput(String... lines) {
        // flushScanner();
        IntStream.range(0, lines.length).forEach(i -> System.out.println(StringHelper.concater(". ", i + 1, lines[i])));
        int n = acceptKey("Number input");
        if (n < 1 || n > lines.length) {
            LOGGER.warning("Key out of range");
            LOGGER.info("Default key (-1) is used");
            n = -1;
        }
        return n;
    }

    public static String acceptLine(String prompt) {
        // flushScanner();
        System.out.print(prompt + ": ");
        return Global.scanner.nextLine().trim();
    }

    public static int acceptKey(String prompt) {
        try {
            return Integer.parseInt(acceptLine(prompt));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Unexpected key parse error", e);
            LOGGER.info("Default key (-1) is used");
            return -1;
        }
    }

    public static String initPhanCach() {
        String s = "";
        for (int i = 0; i < 100; i++) {
            s += "=";
        }
        if (s != "")
            s += "\n";
        return s;
    }

    private static final String PHANCACH = initPhanCach();

    public static String phanCach() {
        return PHANCACH;
    }
    // public static void flushScanner() {
    // while (Global.scanner.hasNext()) {
    // Global.scanner.nextLine();
    // }
    // }

    public static final Pattern lv1Sep = Pattern.compile(Pattern.quote("|"));
    public static final Pattern lv2Sep = Pattern.compile(Pattern.quote("\\"));
}
