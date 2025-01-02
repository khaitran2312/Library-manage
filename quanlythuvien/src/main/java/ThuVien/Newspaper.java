package ThuVien;

import java.util.logging.Logger;
import java.util.stream.Stream;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Newspaper extends Document {
    private static final Logger LOGGER = Logger.getLogger(Newspaper.class.getName());

    public Newspaper(int id) {
        super(id);
    }

    public String getEditorial() {
        return editorial;
    }

    public Newspaper setEditorial(String editorial) {
        this.editorial = editorial;
        return this;
    }

    public String[] toBlob() {
        return new String[] { String.valueOf(Documents.Type.NEWSPAPER), String.valueOf(getId()), getName(),
                StringHelper.lv1Join(Stream.of(getAuthors()).map(Author::getId).toArray()), getEditorial(), getPublication().toString(),
                String.valueOf(getCopies()), String.valueOf(getBorrowed()) };
    }

    public static Newspaper fromBlob(String[] inp) {
        int type = Integer.parseInt(inp[0]);
        if (type != Documents.Type.NEWSPAPER) {
            LOGGER.severe(
                    String.format("Incorrect Document Blob type, got %d, expected %d", type, Documents.Type.NEWSPAPER));
            throw new IllegalArgumentException("Incorrect Document type");
        }
        int id = Integer.parseInt(inp[1]);
        String name = inp[2];
        Author[] authors = Stream.of(StringHelper.lv1Split(inp[3]))
                .map(e -> Global.authors.getById(Integer.parseInt(e))).toArray(Author[]::new);
        String editorial = inp[4];
        ThoiGian publication = ThoiGian.parseTG(inp[5]);
        int copies = Integer.parseInt(inp[6]);
        int borrowed = Integer.parseInt(inp[7]);
        Newspaper toRet = new Newspaper(id).setEditorial(editorial);
        toRet.setName(name).setAuthors(authors).setPublication(publication).setCopies(copies).setBorrowed(borrowed);
        return toRet;
    }

    private String editorial;

}
