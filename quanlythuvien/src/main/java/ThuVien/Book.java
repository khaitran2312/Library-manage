package ThuVien;

public abstract class Book extends Document {
    public Book(int id) {
        super(id);
    }

    public Language getLanguage() {
        return language;
    }

    public Book setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public Book setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public abstract String[] toBlob();

    private Language language = Languages.unknown;
    private String publisher;

}
