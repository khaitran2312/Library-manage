package ThuVien;

import java.util.Random;
import java.util.Scanner;

import Polyfill.StringHelper;

public class Global {
    public static Authors authors;
    public static Cards cards;
    public static Readers readers;
    public static Documents documents;
    public static Cashiers cashiers;
    public static Managers managers;
    public static HoaDons hoadons;
    public static Owner trandan;
    public static Readers readers_black;
    public static final int ratePerDay = 5000;
    public static final Scanner scanner = new Scanner(System.in);
    public static final Random random = new Random();

    public static void identityLookup() {
        System.out.println("Select an identity to lookup:");
        Management<? extends IDataProcess<? extends AnyId>> identity = null;// Khởi tạo indentity
        // retricts upper-bounded wildcard (must implements IDataProcess, IDataProcess's
        // type must extends AnyId)
        int[] foundPos = switch (StringHelper.acceptInput("Authors", "Documents", "Readers", "Cashiers", "Managers",
                "HoaDons")) {
            case 1 -> {
                identity = authors;// dùng đa hình (upcasting)
                yield authors.search();// Trả về mảng vị trí
            }
            case 2 -> {
                identity = documents;
                yield documents.search();
            }
            case 3 -> {
                identity = readers;
                yield readers.search();
            }
            case 4 -> {
                identity = cashiers;
                yield cashiers.search();
            }
            case 5 -> {
                identity = managers;
                yield managers.search();
            }
            case 6 -> {
                identity = hoadons;
                yield hoadons.search();
            }
            default -> null;
        };
        if (identity != null) {// Nếu indentity đã có tham chiếu đối tượng
            if (foundPos.length == 0) {
                System.out.println("Khong tim thay");
            } else {
                for (int i : foundPos) {
                    System.out.println(identity.instance.at(i));// In ra tối tượng mà indenity đã tham chiếu tới, dựa
                                                                // vào vị trí đã tìm được
                }
            }
        }
    }
}
