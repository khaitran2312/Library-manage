package ThuVien;

import java.util.stream.IntStream;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Cashier extends StaffImpl implements IDataProcess<Cashier>, IDashboard {
    public Cashier(int id, String username) {
        super(id, username);
    }

    protected Cashier(int id, String username, ThoiGian registration) {
        super(id, username, registration);
    }

    protected long calcSocialCredit() {
        return getPureLuong() / 1000 + completionCount * completionBonus;
    }

    protected Cashier resetCompletionCount() {
        completionCount = 0;
        return this;
    }

    private void cashDone() {
        ++completionCount;
    }

    public int getCompletionCount() {
        return completionCount;
    }

    protected Cashier setCompletionCount(int completionCount) {
        this.completionCount = completionCount;
        return this;
    }

    private void menuDocGia() {
        System.out.println(StringHelper.phanCach());
        int choice = StringHelper.acceptInput("Them doc gia", "Xoa doc gia thanh vien", "Chinh sua doc gia",
                "Xuat danh sach", "Xem doc gia het han", "Chinh sua the cua doc gia");
        System.out.println(StringHelper.phanCach());
        switch (choice) {
            case 1 -> {
                Global.readers.add();
            }
            case 2 -> {
                Global.readers.remove();
            }
            case 3 -> {
                Global.readers.edit();
            }
            case 4 -> {
                Global.readers.instance.stream().map(a -> a.toString()).forEach(System.out::println);
            }
            case 5 -> {
                Global.readers.instance.stream().filter(e -> e.getCard().getExpiration().compareTo(ThoiGian.now()) < 0)
                        .map(e -> e.toStringMinified()).forEach(System.out::println);
            }
            case 6 -> {
                Global.cards.edit();
            }
        }
    }

    private void menuTacGia() {
        System.out.println(StringHelper.phanCach());
        int choice = StringHelper.acceptInput("Them tac gia", "Xoa tac gia", "Chinh sua tac gia", "Xuat danh sach");
        System.out.println(StringHelper.phanCach());
        switch (choice) {
            case 1 -> {
                Global.authors.add();
            }
            case 2 -> {
                Global.authors.remove();
            }
            case 3 -> {
                Global.authors.edit();
            }
            case 4 -> {
                Global.authors.instance.stream().map(a -> a.toString()).forEach(System.out::println);
            }
        }
    }

    private void menuTaiLieu() {
        System.out.println(StringHelper.phanCach());
        int choice = StringHelper.acceptInput("Them tai lieu", "Xoa tai lieu", "Sua tai lieu", "Xuat danh sach");
        System.out.println(StringHelper.phanCach());
        switch (choice) {
            case 1 -> {
                Global.documents.add();
            }
            case 2 -> {
                Global.documents.remove();
            }
            case 3 -> {
                Global.documents.edit();
            }
            case 4 -> {
                Global.documents.instance.stream().map(a -> a.toString()).forEach(System.out::println);
            }
        }
    }

    private void menuHoaDon() {
        System.out.println(StringHelper.phanCach());
        int choice = StringHelper.acceptInput("Xuat danh sach", "Danh sach hoa don chưa tra");
        System.out.println(StringHelper.phanCach());
        switch (choice) {
            case 1 -> {
                Global.hoadons.instance.stream().map(e -> e.toString()).forEach(System.out::println);
            }
            case 2 -> {
                int pos[] = IntStream.range(0, Global.hoadons.instance.size()).filter(e -> {
                    if (Global.hoadons.instance.at(e).getDeadline().compareTo(ThoiGian.now()) < 0) {
                        System.out.println(Global.hoadons.instance.at(e).toString());
                        return true;
                    } else {
                        return false;
                    }
                }).toArray();
                if (pos.length == 0) {
                    System.out.println("Khong co hoa don qua han");
                }
            }
        }
    }

    public int dashboard() {
        while (true) {
            System.out.println("Dang nhap voi tu canh Thu Ngan");
            System.out.println(this.toString());
            System.out.println(StringHelper.phanCach());
            System.out.println("Thao tac voi: ");
            int n = StringHelper.acceptInput("Doc gia", "Tac gia", "Hoa don", "Tai lieu", "Cho muon sach",
                    "Nhan tra sach",
                    "Dang xuat", "Tim kiem");
            if (n <= 0) {
                System.out.println("Unexpected input");
                break;
            }
            switch (n) {
                case 1 -> {
                    System.out.println(StringHelper.phanCach());
                    menuDocGia();
                }
                case 2 -> {
                    System.out.println(StringHelper.phanCach());
                    menuTacGia();
                }
                case 3 -> {
                    System.out.println(StringHelper.phanCach());
                    menuHoaDon();
                }
                case 4 -> {
                    System.out.println(StringHelper.phanCach());
                    menuTaiLieu();
                }
                case 5 -> {
                    System.out.println(StringHelper.phanCach());
                    if (Global.hoadons.add() != null) {
                        cashDone();
                    }
                }
                case 6 -> {
                    System.out.println(StringHelper.phanCach());
                    if (Global.hoadons.edit() != null) {
                        System.out.println("Da hoan thanh hoa don");
                    } else {
                        System.out.println("Chua hoan thanh hoa don");
                    }
                }
                case 7 -> {
                    System.out.println(StringHelper.phanCach());
                    System.out.println("Se dang xuat");
                    return 0;
                }
                case 8 -> {
                    System.out.println(StringHelper.phanCach());
                    Global.identityLookup();
                }
            }
        }
        return 1;
    }

    public static Cashier fromBlob(String[] inp) {
        int id = Integer.parseInt(inp[0]);
        String username = inp[1];
        String password = inp[2];
        String name = inp[3];
        ThoiGian regtime = ThoiGian.parseTG(inp[4]);
        ThoiGian borntime = ThoiGian.parseTG(inp[5]);
        String phone = inp[6];
        String email = inp[7];
        String address = inp[8];
        CaTruc catruc = CaTruc.parseCaTruc(inp[9]); // someone worked very hard so this could happen
        Luong luong = Luong.parseLuong(inp[10]);
        int completion = Integer.parseInt(inp[11]);
        Cashier toRet = new Cashier(id, username, regtime);
        toRet.changePassword(null, password);
        toRet.setName(name).setBirth(borntime).setPhone(phone).setEmail(email).setAddress(address);
        toRet.setTruc(catruc).setLuong(luong);
        toRet.setCompletionCount(completion);
        return toRet;
    }

    public String[] toBlob() {
        return new String[] { String.valueOf(getId()), getUsername(), getPassword(), getName(),
                getRegistration().toString(), getBirth().toString(), getPhone(), getEmail(), getAddress(),
                getTruc().toString(), getLuong().toString(), String.valueOf(getCompletionCount()) };
    }

    public String toString() {
        return StringHelper.phanCach() + StringHelper.liner(super.toString(),
                StringHelper.itemer("Completition count", getCompletionCount()));
    }

    private int completionCount = 0;
    // moi hoa don thanh toan dc them 5 tram
    private static final long completionBonus = 500;
    public static final int blob_column = 12;
}
