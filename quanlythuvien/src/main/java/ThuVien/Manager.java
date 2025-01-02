package ThuVien;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Manager extends StaffImpl implements IDataProcess<Manager>, IDashboard {
    public Manager(int id, String username) {
        this(id, username, ThoiGian.now());
    }

    protected Manager(int id, String username, ThoiGian regtime) {
        super(id, username, regtime);
    }

    public long calcSocialCredit() {
        return getPureLuong() / 500 + Global.cashiers.size() * cashierBonus;
    }

    public int dashboard() {
        while (true) {
            System.out.println(StringHelper.phanCach());
            System.out.println("Dang dang nhap voi tu cach Quan Ly");
            System.out.println(this.toString());
            System.out.println(StringHelper.phanCach());
            int n = StringHelper.acceptInput("Thue them nhan vien", "Duoi viec nhan vien", "Chinh sua nhan vien",
                    "Dang xuat", "Tim kiem");
            if (n <= 0) {
                System.out.println("Unexpected input");
                break;
            }
            System.out.println(StringHelper.phanCach());
            switch (n) {
                case 1 -> {
                    Global.cashiers.add();
                }
                case 2 -> {
                    Global.cashiers.remove();
                }
                case 3 -> {
                    Global.cashiers.edit();
                }
                case 4 -> {
                    System.out.println("Se dang xuat");
                    return 0;
                }
                case 5 -> {
                    Global.identityLookup();
                }
            }
        }
        return 1;
    }

    public String[] toBlob() {
        return new String[] { String.valueOf(getId()), getUsername(), getPassword(), getName(),
                getRegistration().toString(), getBirth().toString(), getPhone(), getEmail(), getAddress(),
                getTruc().toString(), getLuong().toString() };
    }

    public static Manager fromBlob(String[] inp) {
        int id = Integer.parseInt(inp[0]);
        String username = inp[1];
        String password = inp[2];
        String name = inp[3];
        ThoiGian regtime = ThoiGian.parseTG(inp[4]);
        ThoiGian borntime = ThoiGian.parseTG(inp[5]);
        String phone = inp[6];
        String email = inp[7];
        String address = inp[8];
        CaTruc catruc = CaTruc.parseCaTruc(inp[9]);
        Luong luong = Luong.parseLuong(inp[10]);
        Manager toRet = new Manager(id, username, regtime);
        toRet.changePassword(null, password);
        toRet.setName(name).setBirth(borntime).setPhone(phone).setEmail(email).setAddress(address);
        toRet.setTruc(catruc).setLuong(luong);
        return toRet;
    }

    private static final long cashierBonus = 500;
    public static final int blob_column = 11;
}
