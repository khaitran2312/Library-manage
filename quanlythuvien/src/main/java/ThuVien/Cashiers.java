package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Cashiers extends Management<Cashier> implements ILogin {
    private static final Logger LOGGER = Logger.getLogger(Cashiers.class.getName());

    public Cashiers() {
        super();
    }

    public Cashiers(Cashier[] r) {
        super(r);
    }

    public Cashiers(PFArray<String[]> blob) {
        this();
        blob.stream().forEach(e -> instance.push_back(Cashier.fromBlob(e)));
        updateCounter();
    }

    @Override
    public Cashier add() {
        try {
            String username = StringHelper.acceptLine("Ten tai khoan");
            String password = StringHelper.acceptLine("Mat khau");
            String name = StringHelper.acceptLine("Ten");
            ThoiGian birth = ThoiGian.parseTG(StringHelper.acceptLine("Ngay sinh"));
            String phone = StringHelper.acceptLine("So dien thoai");
            String email = StringHelper.acceptLine("Email");
            String address = StringHelper.acceptLine("Dia chi");
            CaTruc truc = CaTruc.parseCaTruc(StringHelper.acceptLine("Ca truc"));
            String luongStr = StringHelper.acceptLine("Luong khoi dau");
            Luong luong = StringHelper.isNullOrBlank(luongStr) ? new Luong() : new Luong(Long.parseLong(luongStr));
            Cashier toAdd = new Cashier(genNextId(), username);
            toAdd.changePassword(null, password);
            toAdd.setName(name);
            toAdd.setBirth(birth);
            toAdd.setPhone(phone);
            toAdd.setEmail(email);
            toAdd.setAddress(address);
            toAdd.setTruc(truc);
            toAdd.setLuong(luong);
            System.out.println(StringHelper.itemer("Da them thanh cong thu ngan", toAdd.toString()));
            instance.push_back(toAdd);
            return toAdd;
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Likely input parse error in Cashier::add", e);
            LOGGER.info("The adding operation is cancelled");
            LOGGER.fine(String.format("Id counter is %d", currentIdCount()));
            return null;
        }
    }

    private int promptSearch() {
        int id = StringHelper.acceptKey("Nhap id thu ngan");
        if (id == -1) {
            return -1;
        }
        int pos = search(id);
        if (pos == -1) {
            System.out.println("Tim kiem khong co ket qua");
        } else {
            System.out.println("Tim thay thu ngan:");
            instance.at(pos).toStringMinified();
        }
        return pos;
    }

    @Override
    public Cashier remove() {
        int n;
        Cashier toRemove = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, remove thu ngan that bai");
            } else {
                System.out.println("Xac nhan xoa thu ngan:");
                instance.at(n).toString();
                int m = StringHelper.acceptInput("Co", "Suy nghi lai");
                if (m == 1) {
                    toRemove = instance.erase(n);
                    System.out.println("Da xoa thu ngan");
                    System.out.println(toRemove.toString());
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, remove thu ngan that bai", e);
        }
        return toRemove;
    }

    @Override
    public Cashier edit() {
        int n;
        Cashier toEdit = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, edit thu ngan that bai");
            } else {
                int m;
                do {
                    toEdit = instance.at(n);
                    System.out.println("Dang thao tac edit thu ngan:");
                    System.out.println(toEdit.toString());
                    System.out.println("Chon thao tac");
                    switch (m = StringHelper.acceptInput("Ten", "Ngay sinh", "So dien thoai", "Email", "Dia chi",
                            "Ca truc", "Thay doi mat khau")) {
                        case 1:
                            toEdit.setName(StringHelper.acceptLine("Nhap ten"));
                            break;
                        case 2:
                            toEdit.setBirth(ThoiGian.parseTG(StringHelper.acceptLine("Nhap ngay sinh")));
                            break;
                        case 3:
                            toEdit.setPhone(StringHelper.acceptLine("Nhap sdt"));
                            break;
                        case 4:
                            toEdit.setEmail(StringHelper.acceptLine("Nhap email"));
                            break;
                        case 5:
                            toEdit.setAddress(StringHelper.acceptLine("Nhap dia chi"));
                            break;
                        case 6:
                            toEdit.setTruc(CaTruc.parseCaTruc(StringHelper.acceptLine("Nhap ca truc")));
                            break;
                        case 7:
                            String old = StringHelper.acceptLine("Nhap mat khau cu");
                            if (toEdit.changePassword(old, StringHelper.acceptLine("Nhap mat khau moi")) == true) {
                                System.out.println("Thay doi mat khau thanh cong");
                            } else {
                                System.out.println("Mat khau cu sai");
                            }
                            break;
                        case -1:
                        default:
                            m = -1;
                            System.out.println("Ket thuc edit thu ngan");
                            break;
                    }
                } while (m > 0);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, edit thu ngan that bai", e);
        }
        return toEdit;
    }

    public int login() {
        String username = StringHelper.acceptLine("Nhap ten tai khoan (thu ngan)");
        int found = IntStream.range(0, instance.size())
                .filter(e -> username.equalsIgnoreCase(instance.at(e).getUsername())).findAny().orElse(-1);
        if (found == -1) {
            System.out.println("Khong tim thay ten dang nhap (thu ngan)");
            return -1;
        }
        System.out.println(StringHelper.phanCach());
        System.out.println("Tim thay thu ngan");
        System.out.println(instance.at(found).toStringMinified());
        System.out.println(StringHelper.phanCach());
        int soLanNhapSai = 0;
        int toiDa = 5;
        while (true) {
            String password = StringHelper.acceptLine("Nhap mat khau");
            if (!instance.at(found).checkPassword(password)) {
                System.out.println("Sai mat khau");
                ++soLanNhapSai;
                if (soLanNhapSai == toiDa) {
                    System.out.println("Nhap sai qua nhieu lan");
                    break;
                }
            } else {
                System.out.println("Mat khau chinh xac");
                System.out.println(StringHelper.phanCach());
                instance.at(found).dashboard();
                return found;
            }
        }
        return -1;
    }

    @Override
    public int[] search() {
        String query = StringHelper.acceptLine("Nhap ten thu ngan");
        String[] entries = query.toLowerCase().split(" ");
        return IntStream.range(0, instance.size()).filter(i -> {
            String[] names = instance.at(i).getName().toLowerCase().split(" ");
            for (int j = 0; j < names.length; j++) {
                for (int k = 0; k < entries.length; k++) {
                    if (names[j].startsWith(entries[k])) {
                        return true;
                    }
                }
            }
            return false;
        }).toArray();
    }

    public static Cashiers fromBatchBlob(PFArray<String[]> inp) {
        if (inp.size() < 1) {
            LOGGER.warning("No entries");
        } else {
            LOGGER.info(String.format("Batching %d x %d blob", inp.size(), Cashier.blob_column));
        }
        return new Cashiers(inp);
    }
}
