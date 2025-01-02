package ThuVien;

import java.util.stream.Stream;

import Polyfill.StringHelper;

public class Languages {
    public static Language parseLang(String inp) {
        String[] names = StringHelper.lv1Split(inp);
        return Stream.of(list).filter(l -> {
            for (String n : names) {
                if (n.equalsIgnoreCase(l.getCode()) || n.equalsIgnoreCase(l.getName())
                        || n.equalsIgnoreCase(l.getTname())) {
                    return true;
                }
            }
            return false;
        }).findAny().orElse(unknown);
    }

    public static final Language unknown = new Language(0, "Unknown", "Tieng Thanh Hoa", "TH");
    public static final Language vn = new Language(1, "Vietnamese", "Tieng Viet", "VN");
    public static final Language en = new Language(2, "English", "Tieng Anh", "EN");
    public static final Language es = new Language(4, "Spanish", "Tieng Tay Ban Nha", "ES");
    public static final Language jp = new Language(5, "Japanese", "Tieng Nhat", "JP");
    public static final Language fr = new Language(6, "French", "Tieng Phap", "FR");
    public static final Language kr = new Language(7, "Korean", "Tieng Nam Han", "KR");
    public static final Language cn = new Language(8, "Chinese", "Tieng Trung Quoc", "CN");
    public static final Language ru = new Language(9, "Russian", "Tieng Nga", "RU");
    public static final Language ch = new Language(10, "Cheems", "Chmua Hme", "CH");
    public static final Language[] list = new Language[] { vn, en, es, jp, fr, kr, cn, ru, ch };
}
