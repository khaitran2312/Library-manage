package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Authors extends Management<Author> {
    private static final Logger LOGGER = Logger.getLogger(Authors.class.getName());

    public Authors() {
        super();
    }

    public Authors(Author[] r) {
        super(r);
    }

    public Authors(PFArray<String[]> blob) {
        this();
        blob.stream().forEach(e -> instance.push_back(Author.fromBlob(e)));
        updateCounter();
    }
    //thêm vào thông tin của tác giả
    public Author add() {
        try {
            String name = StringHelper.acceptLine("Nhap ten tac gia");
            Language language = Languages.parseLang(StringHelper.acceptLine("Nhap ngon ngu"));
            String website = StringHelper.acceptLine("Nhap trang web");
            ThoiGian birth = ThoiGian.parseTG(StringHelper.acceptLine("Nhap ngay sinh"));
            String address = StringHelper.acceptLine("Nhap dia chi");
            String email = StringHelper.acceptLine("Nhap email");
            String phone = StringHelper.acceptLine("Nhap so dien thoai");
            Author author = new Author(genNextId());
            author.setName(name);
            author.setLanguage(language);
            author.setWebsite(website);
            author.setBirth(birth);
            author.setAddress(address);
            author.setEmail(email);
            author.setPhone(phone);
            instance.push_back(author);
            System.out.println(StringHelper.itemer("Da them thanh cong tac gia", author.toString()));
            return author;
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Likely input parse error in Authors::add", e);
            LOGGER.info("The adding operation is cancelled");
            LOGGER.fine(String.format("Id counter is %d", currentIdCount()));
            return null;
        }
    }
    //tìm kiếm tác giả dựa trên id sau đó có thể thực hiện thao tác xóa hoặc edit lại
    public int promptSearch() {
        int id = StringHelper.acceptKey("Nhap id tac gia");
        if (id == -1) {
            return -1;
        }
        int pos = search(id);
        if (pos == -1) {
            System.out.println("Tim kiem khong co ket qua: ");
        } else {
            System.out.println("Tim thay tac gia: ");
            System.out.println(instance.at(pos).toStringMinified());
        }
        return pos;
    }

    public Author remove() {
        Author author = null;
        int n;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, remove tac gia that bai");
            } else {
                System.out.println("Xac nhan xoa tac gia: ");
                System.out.println(instance.at(n).toString());
                int m = StringHelper.acceptInput("Co", "Suy nghi lai");
                if (m == 1) {
                    author = instance.erase(n);
                    System.out.println("Da xoa tac gia: ");
                    System.out.println(author.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, remove tac gia khong thanh cong", e);
            throw e;
        }
        return author;
    }

    public int menuEdit() {
        return StringHelper.acceptInput("Ten tac gia", "Ngon ngu", "Website", "Ngay sinh", "email", "Dia chi",
                "Dien thoai");
    }

    public Author edit() {
        Author author = null;
        int n;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, edit tac gia that bai");
            } else {
                int m;
                do {
                    author = instance.at(n);
                    System.out.println("Dang thao tac tac gia: ");
                    System.out.println(author.toString());
                    System.out.println("Chon thao tac");
                    switch (m = menuEdit()) {
                        case 1 -> author.setName(Global.scanner.nextLine());
                        case 2 -> author.setLanguage(Languages.parseLang(Global.scanner.nextLine()));
                        case 3 -> author.setWebsite(Global.scanner.nextLine());
                        case 4 -> author.setBirth(ThoiGian.parseTG(Global.scanner.nextLine()));
                        case 5 -> author.setEmail(Global.scanner.nextLine());
                        case 6 -> author.setAddress(Global.scanner.nextLine());
                        case 7 -> author.setPhone(Global.scanner.nextLine());
                        default -> {
                            m = -1;
                            System.out.println("Ket thuc edit tac gia");
                        }
                    }
                } while (m > 0);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, edit doc gia that bai", e);
            throw e;
        }
        return author;
    }

    public int[] search() {
        String query = StringHelper.acceptLine("Nhap ten tac gia");
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
    // public PFArray<String[]> toBatchBlob() {}; already implemented

    public static Authors fromBatchBlob(PFArray<String[]> inp) {
        if (inp.size() < 1) {
            LOGGER.warning("No entries");
        } else {
            LOGGER.info(String.format("Batching %d x %d blob", inp.size(), Author.blob_column));
        }
        return new Authors(inp);
    }
}
