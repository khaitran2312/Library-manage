package ThuVien;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public abstract class StaffImpl extends Account {
    public StaffImpl(int id, String username) {
        super(id, username);
    }

    protected StaffImpl(int id, String username, ThoiGian registration) {
        super(id, username, registration);
    }

    public long getPureLuong() {
        return luong.getHienTai();
    }

    public CaTruc getTruc() {
        return truc;
    }

    protected StaffImpl setTruc(CaTruc truc) {
        this.truc = truc;
        return this;
    }

    public Luong getLuong() {
        return luong;
    }

    protected StaffImpl setLuong(Luong luong) {
        this.luong = luong;
        return this;
    }

    @Override
    public String toString() {
        return StringHelper.phanCach() + StringHelper.liner(super.toString(),
                StringHelper.itemer("Ca truc", truc.toScreen()),
                StringHelper.itemer("Luong", luong.toScreen()),
                StringHelper.itemer("Social credit", calcSocialCredit()));
    }

    protected abstract long calcSocialCredit();

    private CaTruc truc;
    private Luong luong;
}
