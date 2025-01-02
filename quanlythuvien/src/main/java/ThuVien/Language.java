package ThuVien;

import Polyfill.StringHelper;

public class Language extends AnyId {
    public Language(int id, String tname, String name, String code) {
        super(id);
        this.name = name;
        this.tname = tname;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getTname() {
        return tname;
    }

    public String getCode() {
        return code;
    }

    public String toScreen() {
        return StringHelper.liner(super.toString(),
                StringHelper.itemer("Name", StringHelper.spacer(code, name, tname)));
    }

    @Override
    public String toString() {
        return StringHelper.concater("|", code, name, tname);
    }

    public String toStringMinified() {
        return code;
    }

    private String code, name, tname;
}
