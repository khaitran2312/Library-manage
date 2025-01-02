package ThuVien;

import Polyfill.ThoiGian;

public abstract class ForeignBook extends Book {
    public ForeignBook(int id) {
        super(id);
    }

    public ThoiGian getOriginPublication() {
        return originPublication;
    }

    protected ForeignBook setOriginPublication(ThoiGian originPublication) {
        this.originPublication = originPublication;
        return this;
    }

    public Language getOriginLanguage() {
        return getLanguage();
    }

    protected ForeignBook setOriginLanguage(Language language) {
        setLanguage(language);
        return this;
    }
    
    public abstract String[] toBlob();

    private ThoiGian originPublication;

}
