package ThuVien;

import java.util.logging.Logger;
import java.util.stream.IntStream;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Reader extends Account implements IDataProcess<Reader>, IDashboard {
    private static final Logger LOGGER = Logger.getLogger(Reader.class.getName());

    public Reader(int id, String username) {
        super(id, username);
        setCard(Card.defaultCard);
    }

    protected Reader(int id, String username, ThoiGian registration) {
        super(id, username, registration);
        setCard(Card.defaultCard);
    }

    public Card getCard() {
        return card;
    }

    protected Reader setCard(Card card) {
        this.card = card;
        return this;
    }

    public int dashboard() {
        while (true) {
            System.out.println(StringHelper.phanCach());
            System.out.println("Dang dang nhap voi tu cach Doc Gia");
            System.out.println(this.toString());
            System.out.println(StringHelper.phanCach());
            int n = StringHelper.acceptInput("Muon", "Tra", "Xem hoa don", "Dang xuat", "Tim kiem");
            if (n <= 0) {
                LOGGER.warning("Unexpected input");
                break;
            }
            switch (n) {
                case 1 -> {
                    if (this.getCard().getExpiration().compareTo(ThoiGian.now()) < 0) {
                        System.out.println("The het han, khong the muon sach, vui long gap thu ngan de gia han the");
                    } else {
                        VirtualHoaDon vhd = Global.hoadons.createVirtual(this);
                        while (true) {
                            System.out.println(StringHelper.phanCach());
                            int dn = Integer.parseInt(StringHelper.acceptLine("Nhap id tai lieu"));
                            if (dn == 0) {
                                System.out.println("Dung muon tai lieu ");
                                break;
                            }
                            dn = Global.documents.search(dn);
                            if (dn == -1) {
                                System.out.println("Khong tim thay tai lieu");
                                continue;
                            }
                            System.out.println(StringHelper.phanCach());
                            System.out.println("Tim thay tai lieu");
                            Document d = Global.documents.instance.at(dn);
                            System.out.println(d.toString());
                            System.out.println(StringHelper.phanCach());
                            if (!vhd.addBorrows(d)) {
                                System.out.println("Tai lieu khong kha dung cho muon");
                            }
                            if (StringHelper.acceptInput("Tiep tuc muon", "Dung lai") == 2) {
                                break;
                            }
                        }
                        System.out.println(StringHelper.phanCach());
                        System.out.println("Xem lai danh sach chuan bi muon");
                        System.out.println(vhd.toStringMinified());
                        if (StringHelper.acceptInput("Ok", "Bo") == 1) {
                            Global.hoadons.acceptVirtual(vhd);
                            System.out.println("Dem id hoa don ao cho thu ngan de tiep tuc");
                        }
                    }
                }
                case 2 -> {
                    System.out.println(StringHelper.phanCach());
                    System.out.println("Chua co chuc nang tu tra, hay dem id hoa don len cho thu ngan");
                }
                case 3 -> {
                    System.out.println(StringHelper.phanCach());
//                    Global.hoadons.instance.stream().filter(e -> e.getCreator().getId() == this.getId())
//                            .forEach(e -> System.out.println(e.toStringMinified()));
                    int index = IntStream.range(0, Global.hoadons.instance.size()).filter(i -> Global.hoadons.instance.at(i).getCreator().getId() ==
                            this.getId()).findAny().orElse(-1);
                    if (index != -1) {
                        System.out.println(Global.hoadons.instance.at(index).toString());
                    } else {
                        System.out.println("Khong co hoa don");
                    }
                }
                case 4 -> {
                    System.out.println(StringHelper.phanCach());
                    System.out.println("Se dang xuat");
                    return 0;
                }
                case 5 -> {
                    System.out.println(StringHelper.phanCach());
                    Global.identityLookup();
                }
            }
        }
        return 1;
    }

    public static Reader fromBlob(String[] inp) {
        int id = Integer.parseInt(inp[0]);
        String username = inp[1];
        String password = inp[2];
        String name = inp[3];
        ThoiGian regtime = ThoiGian.parseTG(inp[4]);
        ThoiGian borntime = ThoiGian.parseTG(inp[5]);
        String phone = inp[6];
        String email = inp[7];
        String address = inp[8];
        Card card = Global.cards.getById(Integer.parseInt(inp[9]));
        Reader toRet = new Reader(id, username, regtime).setCard(card == null ? Card.defaultCard : card);
        toRet.setName(name).setBirth(borntime).setPhone(phone).setEmail(email).setAddress(address);
        toRet.changePassword(null, password);
        return toRet;
    }

    public String[] toBlob() {
        return new String[]{String.valueOf(getId()), getUsername(), getPassword(), getName(),
                getRegistration().toString(), getBirth().toString(), getPhone(), getEmail(), getAddress(),
                String.valueOf(card.getId())};
    }

    @Override
    public String toString() {
        return StringHelper.phanCach() + StringHelper.liner(super.toString(), StringHelper.itemer("Card", getCard()));
    }

    public String toStringMinified() {
        return StringHelper.lv1Join(getId(), getUsername());
    }

    private Card card;
    public static final int blob_column = 10;
}
