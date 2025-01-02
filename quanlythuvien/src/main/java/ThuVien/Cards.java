package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;

import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Cards extends Management<Card> {
    private static final Logger LOGGER = Logger.getLogger(Cards.class.getName());

    public Cards() {
        super();
    }

    public Cards(Card[] r) {
        super(r);
    }

    public Cards(PFArray<String[]> blob) {
        this();
        blob.stream().forEach(e -> instance.push_back(Card.fromBlob(e)));
        updateCounter();
    }

    @Override
    public Card add() {
        int userInp = StringHelper.acceptInput("regular", "pro", "vip", "ultimate", "none");
        if (userInp <= 0) {
            LOGGER.warning("Unexpected input");
            return null;
        }
        Card card = new Card(genNextId(), switch (userInp) {
            case 1 -> Card.regular;
            case 2 -> Card.pro;
            case 3 -> Card.vip;
            case 4 -> Card.ultimate;
            default -> Card.none;
        }, ThoiGian.now());
        Global.cards.instance.push_back(card);
        System.out.println(StringHelper.itemer("Da them thanh cong the", card.toString()));
        return card;
    }

    public int promptSearch() {
        int n = search(Integer.parseInt(StringHelper.acceptLine("Nhap ma the: ")));
        if (n == -1) {
            System.out.println("Tim kiem khong co ket qua: ");
        } else {
            System.out.println("Tim thay the: ");
            System.out.println(instance.at(n).toString());
        }
        return n;
    }

    @Override
    public Card remove() {
        int n;
        Card card = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, remove the that bai");
            } else {
                System.out.println("Xac nhan xoa the: ");
                System.out.println(instance.at(n).toString());
                int m = StringHelper.acceptInput("Co", "Khong");
                if (m == 1) {
                    card = instance.erase(n);
                    System.out.println("Da xoa the: ");
                    System.out.println(card.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, remove the that bai", e);
            throw e;
        }

        return card;
    }

    @Override
    public Card edit() {
        int n;
        Card card = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, edit the khong thanh cong");
            } else {
                int m;
                do {
                    card = instance.at(n);
                    System.out.println(StringHelper.phanCach());
                    System.out.println("Dang thao tac edit the: ");
                    System.out.println(StringHelper.phanCach());
                    System.out.println(instance.at(n).toString());
                    System.out.println(StringHelper.phanCach());
                    System.out.println("Chon thao tac: ");
                    switch (m = StringHelper.acceptInput("Doi loai the", "Gia han the")) {
                        case 1 -> {
                            int a = StringHelper.acceptInput("regular", "pro", "vip", "ultimate");
                            switch (a) {
                                case 1 -> {
                                    card.setType(Card.regular);
                                }
                                case 2 -> {
                                    card.setType(Card.pro);
                                }
                                case 3 -> {
                                    card.setType(Card.vip);
                                }
                                case 4 -> {
                                    card.setType(Card.ultimate);
                                }
                            }
                            System.out.println(card.toString());
                        }
                        case 2 -> {
                            System.out.println(StringHelper.phanCach());
                            card.extendExpiration();
                            System.out.println(StringHelper.itemer("Su dung den", card.getExpiration().toString()));
                        }
                        default -> {
                            m = -1;
                            System.out.println("Ket thuc edit the");
                            System.out.println(StringHelper.phanCach());
                        }
                    }
                } while (m >= 0);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Coi loi xay ra, edit the that bai", e);
            throw e;
        }
        return card;
    }

    @Override
    public int[] search() {
        throw new UnsupportedOperationException("Chuc nang chua dc code do ko du thoi gian");
    }

    public static Cards fromBatchBlob(PFArray<String[]> inp) {
        if (inp.size() < 1) {
            LOGGER.warning("No entries");
        } else {
            LOGGER.info(String.format("Batching %d x %d blob", inp.size(), Card.blob_column));
        }
        return new Cards(inp);
    }
}
