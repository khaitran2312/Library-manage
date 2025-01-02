package ThuVien;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import Polyfill.StringHelper;

public class CaTruc {
    private static final Logger LOGGER = Logger.getLogger(CaTruc.class.getName());

    public CaTruc() {
    }

    public CaTruc setCaTrucNgay(int thu, int gio1, int phut1, int giay1, int gio2, int phut2, int giay2) {
        return setCaTrucNgay(thu, LocalTime.of(gio1, phut1, giay1), LocalTime.of(gio2, phut2, giay2));
    }

    public CaTruc setCaTrucNgay(int thu, LocalTime t1, LocalTime t2) {
        indexCheck(thu);
        instance[thu] = t1.compareTo(t2) < 0 ? new CaTrucNgay(t1, t2) : new CaTrucNgay(t2, t1);
        return this;
    }

    public CaTruc setCaTrucNgay(int thu, CaTrucNgay ctn) {
        indexCheck(thu);
        instance[thu] = ctn;
        return this;
    }

    public record CaTrucNgay(LocalTime gioLam, LocalTime gioVe) {
        private static final Logger LOGGER = Logger.getLogger(CaTrucNgay.class.getName());

        public static CaTrucNgay parseCaTrucNgay(String inp) {
            CaTrucNgay toRet;
            if (!StringHelper.isNullOrBlank(inp)) {
                String[] timeMoment = StringHelper.splitThenTrim("~", inp);
                try {
                    toRet = new CaTrucNgay(LocalTime.parse(timeMoment[0], formatter),
                            LocalTime.parse(timeMoment[1], formatter));
                } catch (RuntimeException e) {
                    LOGGER.log(Level.WARNING, "CaTrucNgay parsing error", e);
                    throw e;
                }
            } else {
                toRet = new CaTrucNgay(LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 0));
            }
            return toRet;
        }

        public String toScreen() {
            return String.join(" ~ ", gioLam.format(formatter), gioVe.format(formatter));
        }

        @Override
        public String toString() {
            return String.join("~", gioLam.format(formatter), gioVe.format(formatter));
        }

        public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    private void indexCheck(int index) {
        if (index < 0 || index > instance.length - 1) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    // 10:00:00~12:00:00|17:00:00~21:45:00|...
    // ^ monday | ^ tuesday ...
    public static CaTruc parseCaTruc(String inp) {
        CaTruc toRet = new CaTruc();
        if (!StringHelper.isNullOrBlank(inp)) {
            String[] caTrucPerDay = StringHelper.lv1Split(inp);
            try {
                IntStream.range(0, 7).filter(i -> !StringHelper.isNullOrBlank(caTrucPerDay[i]))
                        .forEach(i -> toRet.setCaTrucNgay(i, CaTrucNgay.parseCaTrucNgay(caTrucPerDay[i])));
            } catch (RuntimeException e) {
                LOGGER.log(Level.WARNING, "CaTruc parsing error", e);
                throw e;
            }
        }
        return toRet;
    }

    public String toScreen() {
        return IntStream.range(0, 7).filter(i -> instance[i] != null)
                .mapToObj(i -> StringHelper.itemer(names[i], instance[i].toScreen()))
                .collect(Collectors.collectingAndThen(
                        Collectors.joining(", "), toRet -> StringHelper.isNullOrBlank(toRet) ? "Khong" : toRet));
    }

    @Override
    public String toString() {
        return Stream.of(instance).map(e -> e == null ? "" : e.toString()).collect(Collectors.joining("|"));
    }

    private CaTrucNgay[] instance = new CaTrucNgay[7];
    private static final String[] names = new String[] {
            "Thu 2", "Thu 3", "Thu 4", "Thu 5", "Thu 6", "Thu 7", "Chu Nhat"
    };
    public static final int Thu2 = 0;
    public static final int Thu3 = 1;
    public static final int Thu4 = 2;
    public static final int Thu5 = 3;
    public static final int Thu6 = 4;
    public static final int Thu7 = 5;
    public static final int ChuNhat = 6;
}
