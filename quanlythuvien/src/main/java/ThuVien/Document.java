package ThuVien;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public abstract class Document extends AnyId implements IDataProcess<Document> {
    private static final Logger LOGGER = Logger.getLogger(Document.class.getName());

    public Document(int id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public Document setName(String name) {
        this.name = name;
        return this;
    }

    public ThoiGian getPublication() {
        return publication;
    }

    public Document setPublication(ThoiGian publication) {
        this.publication = publication;
        return this;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public Document setAuthors(Author[] authors) {
        this.authors = authors;
        return this;
    }

    public int getCopies() {
        return copies;
    }

    protected Document setCopies(int copies) {
        this.copies = copies;
        return this;
    }

    public int getBorrowed() {
        return borrowed;
    }

    protected Document setBorrowed(int borrowed) {
        this.borrowed = borrowed;
        return this;
    }

    public Document changeCopies(int copies_offset) {
        if ((copies += copies_offset) < 0) {
            LOGGER.info(StringHelper.spacer("Document", getId(), "has been purged"));
            copies = 0;
        }
        if (borrowed > copies) {
            LOGGER.info(StringHelper.spacer("Document", getId(), "has borrowings larger than copies", borrowed, ">",
                    copies));
        }
        return this;
    }

    public boolean borrowable() {
        return borrowed < copies;
    }

    public boolean borrows() {
        if (!borrowable())
            return false;
        borrowed++;
        return true;
    }

    public void returns() {
        if (--borrowed < 0) {
            borrowed = 0;
        }
    }

    // :'(
    public void lost() {
        returns();
        changeCopies(-1);
    }

    public abstract String[] toBlob();

    @Override
    public String toString() {
        return StringHelper.phanCach() + StringHelper.phanCach() + StringHelper.liner(super.toString(),
                StringHelper.itemer("Name", name),
                StringHelper.itemer("Authors",
                        StringHelper.arr2str(Stream.of(authors).map(Author::toStringMinified).toArray())),
                StringHelper.itemer("Publication", publication.toScreen()),
                StringHelper.itemer("Copies", StringHelper.concater(" / ", copies - borrowed, copies)));
    }

    public String toStringMinified() {
        return StringHelper.concater(" / ", StringHelper.lv1Join(getId(), getName()),
                Stream.of(getAuthors()).map(Author::toStringMinified).collect(Collectors.joining(", ")));
    }

    private Author[] authors;
    private int copies = 0;
    private int borrowed = 0;
    private ThoiGian publication;
    private String name;
    public static final int blob_column = 12;
}