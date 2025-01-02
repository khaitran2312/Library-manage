package ThuVien;

import java.util.logging.Logger;
import java.util.stream.Stream;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class NativeBook extends Book {
    private static final Logger LOGGER = Logger.getLogger(NativeBook.class.getName());

    public NativeBook(int id) {
        super(id);
        setLanguage(Languages.vn);
    }

    public String[] toBlob() {
        return new String[] { String.valueOf(Documents.Type.NATIVE_BOOK), String.valueOf(getId()), getName(),
                StringHelper.lv1Join(Stream.of(getAuthors()).map(Author::getId).toArray()), getPublisher(), getLanguage().toString(),
                getPublication().toString(), String.valueOf(getCopies()), String.valueOf(getBorrowed()) };
    }

    public static NativeBook fromBlob(String[] inp) {
        int type = Integer.parseInt(inp[0]);
        if (type != Documents.Type.NATIVE_BOOK) {
            LOGGER.severe(
                    String.format("Incorrect Document Blob type, got %d, expected %d", type,
                            Documents.Type.NATIVE_BOOK));
            throw new IllegalArgumentException("Incorrect Document type");
        }
        int id = Integer.parseInt(inp[1]);
        String name = inp[2];
        Author[] authors = Stream.of(StringHelper.lv1Split(inp[3]))
                .map(e -> Global.authors.getById(Integer.parseInt(e))).toArray(Author[]::new);
        String publisher = inp[4];
        Language language = Languages.parseLang(inp[5]);
        ThoiGian publication = ThoiGian.parseTG(inp[6]);
        int copies = Integer.parseInt(inp[7]);
        int borrowed = Integer.parseInt(inp[8]);
        NativeBook toRet = new NativeBook(id);
        toRet.setPublisher(publisher).setLanguage(language);
        toRet.setName(name).setAuthors(authors).setPublication(publication).setCopies(copies).setBorrowed(borrowed);
        return toRet;
    }
    public static final int blob_column = 9;
}
