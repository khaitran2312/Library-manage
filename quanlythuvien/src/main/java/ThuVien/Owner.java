package ThuVien;

import java.time.LocalDateTime;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Owner extends Account implements ILogin, IDashboard {
    public Owner() {
        super(Integer.MIN_VALUE, "admin", new ThoiGian(LocalDateTime.MIN));
        setBirth(new ThoiGian(LocalDateTime.MIN));
        changePassword(null, "admin");
    }

    public int dashboard() {
        while (true) {
            System.out.println(StringHelper.phanCach());
            System.out.println("Dang dang nhap voi tu cach CEO");
            System.out.println(this.toString());
            int n = StringHelper.acceptInput("Thue them quan ly", "Duoi viec quan ly", "Chinh sua nhan vien",
                    "Chuyen doi tu cach", "Tim kiem", "Dang xuat");
            if (n <= 0) {
                System.out.println("Unexpected input");
                break;
            }
            switch (n) {
                case 1 -> {
                    Global.managers.add();
                }
                case 2 -> {
                    Global.managers.remove();
                }
                case 3 -> {
                    Global.managers.edit();
                }
                case 4 -> {
                    String suf = String.format("%09dv", Global.random.nextInt());
                    System.out.println("Chon 1 tu cach (se tao tai khoan ao)");
                    switch (StringHelper.acceptInput("Doc gia", "Thu ngan", "Quan ly")) {
                        case 1 -> {
                            Reader virtual = new Reader(Integer.MAX_VALUE, "docgia" + suf);
                            virtual.setBirth(new ThoiGian(LocalDateTime.MIN));
                            virtual.changePassword(null, "");
                            virtual.dashboard();
                        }
                        case 2 -> {
                            Cashier virtual = new Cashier(Integer.MAX_VALUE, "thungan" + suf);
                            virtual.setBirth(new ThoiGian(LocalDateTime.MIN));
                            virtual.changePassword(null, "");
                            virtual.dashboard();
                        }
                        case 3 -> {
                            Manager virtual = new Manager(Integer.MAX_VALUE, "quanli" + suf);
                            virtual.setBirth(new ThoiGian(LocalDateTime.MIN));
                            virtual.changePassword(null, "");
                            virtual.dashboard();
                        }
                    }
                }
                case 5 -> {
                    Global.identityLookup();
                }
                case 6 -> {
                    System.out.println("Se dang xuat");
                    return 0;
                }
            }
        }
        return 1;
    }

    public int login() {
        String username = StringHelper.acceptLine("Nhap ten tai khoan (CEO)");
        String password = StringHelper.acceptLine("Nhap mat khau");
        if (username.equalsIgnoreCase(getUsername()) && checkPassword(password)) {
            System.out.println("Ta la quan dai lao gia");
            dashboard();
            return 1;
        }
        System.out.println("Mua lai cua hang gia uu dai ib");
        return 0;
    }

    public String toStringMinified() {
        return "Ta la admin";
    }
}
