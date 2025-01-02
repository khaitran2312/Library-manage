package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Documents extends Management<Document> {
    private static final Logger LOGGER = Logger.getLogger(Documents.class.getName());

    public Documents() {
        super();
    }

    public Documents(Document[] r) {
        super(r);
    }
    //Chọn loại sách dựa trên type
    public Documents(PFArray<String[]> blob) {
        blob.stream().forEach(e -> instance.push_back(switch (Integer.parseInt(e[0])) {
            case Type.NEWSPAPER -> Newspaper.fromBlob(e);
            case Type.NATIVE_BOOK -> NativeBook.fromBlob(e);
            case Type.FOREIGN_NONTRANSLATED_BOOK -> ForeignNontranslatedBook.fromBlob(e);
            case Type.FOREIGN_TRANSLATED_BOOK -> ForeignTranslatedBook.fromBlob(e);
            default -> {
                LOGGER.severe(String.format("File read Document Blob type out of range"));
                yield null;
            }
        }));
        updateCounter();
    }

    private Author[] accessInpAuthor() {
        int soLuongTacGia = Integer.parseInt(StringHelper.acceptLine("Nhap so luong tac gia: "));
        Author[] authors = new Author[soLuongTacGia];
        for (int i = 0; i < authors.length; ++i) {
            boolean run = true;
            do {
                int userInp = StringHelper.acceptInput("Nhap ma tac gia da co", "Them tac gia moi");
                switch (userInp) {
                    case 1 -> {
                        int index = Global.authors.promptSearch();
                        if (index == -1) {
                            System.out.println("Khong tim thay tac gia");
                        } else {
                            System.out.println("Da tim thay tac gia: ");
                            System.out.println(Global.authors.instance.at(index).toString());
                            authors[i] = Global.authors.instance.at(index);
                            System.out.println("Them thanh cong");
                            run = false;
                        }
                    }
                    case 2 -> {
                        authors[i] = Global.authors.add();
                        System.out.println("Them thanh cong tac gia: ");
                        System.out.println(authors[i].toString());
                        run = false;
                    }
                }
            } while (run);
        }
        return authors;
    }

    @Override
    public Document add() {
        String name = StringHelper.acceptLine("Nhap ten sach");
        Author[] authors = accessInpAuthor();
        ThoiGian publication = ThoiGian.parseTG(StringHelper.acceptLine("Nhap thoi gian xuat ban"));
        int copies = Integer.parseInt(StringHelper.acceptLine("Nhap so luong ban sao"));
        int n = StringHelper.acceptInput("Bao", "Sach trong nuoc",
                "Sach dich", "Sach chua dich");
        Document toRet = switch (n) {
            case Type.NEWSPAPER -> {
                String editorial = StringHelper.acceptLine("Nhap toa soan");
                yield new Newspaper(genNextId()).setEditorial(editorial);
            }
            case Type.NATIVE_BOOK -> new NativeBook(genNextId());
            case Type.FOREIGN_TRANSLATED_BOOK -> {
                Language translatedLanguage = Languages.parseLang(StringHelper.acceptLine("Nhap ngon ngu da dich"));
                String translator = StringHelper.acceptLine("Nhap ten nguoi dich");
                yield new ForeignTranslatedBook(genNextId()).setTranslatedLanguage(translatedLanguage)
                        .setTranslator(translator);
            }
            case Type.FOREIGN_NONTRANSLATED_BOOK -> new ForeignNontranslatedBook(genNextId());
            default -> {
                LOGGER.warning("Unexpected input");
                yield null;
            }
        };
        toRet.setName(name);
        toRet.setAuthors(authors);
        toRet.setPublication(publication);
        toRet.setCopies(copies);
        System.out.println(StringHelper.itemer("Da them thanh cong tai lieu", toRet.toString()));
        instance.push_back(toRet);
        return toRet;
    }

    public int promptSearch() {
        int n = search(Integer.parseInt(StringHelper.acceptLine("Nhap ma tai lieu: ")));
        if (n == -1) {
            System.out.println("Tim kiem khong co ket qua: ");
        } else {
            System.out.println("Tim thay tai lieu: ");
            System.out.println(instance.at(n).toString());
        }
        return n;
    }

    @Override
    public Document remove() {
        int n;
        Document document = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, remove tai lieu khong thanh cong");
            } else {
                System.out.println("Xac nhan xoa tai lieu: ");
                System.out.println(instance.at(n).toString());
                int m = StringHelper.acceptInput("Co", "Suy nghi lai");
                if (m == 1) {
                    document = instance.erase(n);
                    System.out.println("Da xoa tai lieu: ");
                    System.out.println(document.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, remove tai lieu that bai", e);
        }
        return document;
    }

    @Override
    public Document edit() {
        int n;
        Document document = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, edit tai lieu that bai");
            } else {
                int m;
                do {
                    document = instance.at(n);
                    System.out.println("Dang thao tac edit tai lieu: ");
                    System.out.println(document.toString());
                    System.out.println("Chon thao tac: ");
                    switch (m = StringHelper.acceptInput("Ten", "Tac gia", "Ngay xuat ban", "So luong ban sao")) {
                        case 1 -> document.setName(StringHelper.acceptLine("Nhap ten tai lieu: "));
                        case 2 -> {
                            System.out.println("Nhap lai cac tac gia: ");
                            document.setAuthors(accessInpAuthor());
                        }
                        case 3 ->
                            document.setPublication(ThoiGian.parseTG(StringHelper.acceptLine("Nhap ngay xuat ban: ")));
                        case 4 ->
                            document.setCopies(Integer.parseInt(StringHelper.acceptLine("Nhap so luong ban sao: ")));
                        default -> {
                            m = -1;
                            System.out.println("Ket thuc edit tai lieu");
                        }
                    }
                } while (m >= 0);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, edit tai lieu khong thanh cong", e);
            throw e;
        }
        return document;
    }

    public static Documents fromBatchBlob(PFArray<String[]> inp) {
        if (inp.size() < 1) {
            LOGGER.warning("No entries");
        } else {
            LOGGER.info(String.format("Batching %d x %d blob", inp.size(), Document.blob_column));
        }
        return new Documents(inp);
    }

    @Override
    public int[] search() {
        String query = StringHelper.acceptLine("Nhap ten tai lieu");
        String[] entries = query.toLowerCase().split(" ");
        return IntStream.range(0, instance.size()).filter(i -> {
            String[] names = instance.at(i).getName().toLowerCase().split(" ");
            for (int j = 0; j < names.length; j++) {
                for (int k = 0; k < entries.length; k++) {
                    if (names[j].startsWith(entries[k])) {
                        return true;
                    }
                }
            }
            return false;
        }).toArray();
    }

    public static final class Type {
        public static final int NEWSPAPER = 1;
        public static final int NATIVE_BOOK = 2;
        public static final int FOREIGN_TRANSLATED_BOOK = 3;
        public static final int FOREIGN_NONTRANSLATED_BOOK = 4;
    }

    public static class Instance {
        public static final Newspaper newspaper = new Newspaper(0);
        public static final NativeBook nativeBook = new NativeBook(0);
        public static final ForeignNontranslatedBook foreignNontranslatedBook = new ForeignNontranslatedBook(0);
        public static final ForeignTranslatedBook foreigntranslatedBook = new ForeignTranslatedBook(0);
    }
}
