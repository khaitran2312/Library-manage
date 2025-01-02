package Polyfill;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KhoangThoiGian {
    public KhoangThoiGian(Duration instance) {
        this.instance = instance;
        formatterList = new formatter[] {
                // new formatter("thap ki", () -> instance.toDaysPart() / 3600),
                // new formatter("nam", () -> instance.toDaysPart() % 3600 / 360),
                // new formatter("thang", () -> instance.toDaysPart() % 360 / 30),
                new formatter("ngay", () -> instance.toDaysPart()),
                // https://stackoverflow.com/questions/52130659/java-8-java-util-function-supplier
                // Type mismatch (Long and int), and it doesn't auto convert so we have to do
                // lambdas.
                new formatter("gio", () -> Long.valueOf(instance.toHoursPart())),
                new formatter("phut", () -> Long.valueOf(instance.toMinutesPart())),
                new formatter("giay", () -> Long.valueOf(instance.toSecondsPart())),
        };
    }

    public static KhoangThoiGian between(ThoiGian tg1, ThoiGian tg2) {
        return new KhoangThoiGian(Duration.between(tg1.getInstance(), tg2.getInstance()));
    }

    public static KhoangThoiGian toNow(ThoiGian tg) {
        return KhoangThoiGian.between(tg, ThoiGian.now());
    }

    public boolean isNegative() {
        return instance.isNegative();
    }

    public static long toDays(ThoiGian thoiGian) {
        Duration duration = Duration.between(thoiGian.getInstance(), ThoiGian.now().getInstance());
        return duration.toDays();
    }

    @Override
    public String toString() {
        return Stream.of(formatterList).filter(e -> e.value.get() != 0)
                .map(e -> String.join(" ", String.valueOf(Math.abs(e.value.get())), e.unit))
                .collect(Collectors.collectingAndThen(Collectors.joining(", "),
                        str -> StringHelper.isNullOrBlank(str)
                                ? "no difference (" + formatterList[formatterList.length - 1].unit + ')'
                                : instance.isNegative() ? "in " + str : str + " ago"));
    }

    protected Duration getInstance() {
        return instance;
    }

    private Duration instance;

    private record formatter(String unit, Supplier<Long> value) {
    }

    private final formatter[] formatterList;//Lưu lại tất cả khoảng thời gian
}
