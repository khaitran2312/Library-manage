package ThuVien;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Author extends People implements IDataProcess<Author> {
    public Author(int id) {
        super(id);
    }

    public String getWebsite() {
        return website;
    }

    public Author setWebsite(String website) {
        this.website = website;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public Author setLanguage(Language language) {
        this.language = language;
        return this;
    }
    //lấy thông tin của tác giả
    public static Author fromBlob(String[] inp) {
        int id = Integer.parseInt(inp[0]);
        String name = inp[1];
        ThoiGian borntime = ThoiGian.parseTG(inp[2]);
        String phone = inp[3];
        String email = inp[4];
        String address = inp[5];
        String website = inp[6];
        Language language = Languages.parseLang(inp[7]);
        Author toRet = new Author(id);
        toRet.setName(name).setBirth(borntime).setPhone(phone).setEmail(email).setAddress(address);
        toRet.setWebsite(website).setLanguage(language);
        return toRet;
    }

    public String[] toBlob() {
        return new String[] { String.valueOf(getId()), getName(), getBirth().toString(), getPhone(), getEmail(),
                getAddress(), getWebsite(), getLanguage().toString() };
    }

    @Override
    public String toString() {
        return StringHelper.phanCach() + StringHelper.phanCach() + StringHelper.liner(super.toString(),
                StringHelper.itemer("Language", language),
                StringHelper.itemer("Website", website));
    }

    private String website;
    private Language language;

    public static final int blob_column = 8; 
}
