package ThuVien;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import Polyfill.KhoangThoiGian;
import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class HoaDon extends VirtualHoaDon implements IDataProcess<HoaDon> {
    private static final Logger LOGGER = Logger.getLogger(HoaDon.class.getName());

    public HoaDon(int id, PFArray<Document> borrows, Reader creator) {
        super(id, creator);
        setBorrows(borrows);
        setHoldings(borrows);
    }

    public HoaDon(int id, VirtualHoaDon vhd) {
        this(id, vhd.getBorrows(), vhd.getCreator());
    }

    protected HoaDon setDeadline(ThoiGian deadline) {
        this.deadline = deadline;
        return this;
    }

    public ThoiGian getDeadline() {
        return deadline;
    }

    public PFArray<Document> getHoldings() {
        return holdings;
    }

    protected HoaDon setHoldings(PFArray<Document> holdings) {
        this.holdings = holdings;
        return this;
    }

    public double calcBorrowingFee(int ngayTra) {
        return getBorrows().size() * ngayTra * Global.ratePerDay + Global.ratePerDay;
    }

    public HoaDon edit() {
        if (holdings.size() <= 0) {
            System.out.println("Hoa don nay da ket thuc");
        }
        try {
            switch (StringHelper.acceptInput("Tra tai lieu", "Mat tai lieu")) {
                case 1 -> {
                    System.out.println("Chon tai lieu tra (nhap all de tra het");
                    IntStream.range(0, holdings.size())
                            .forEach(i -> System.out.println(String.format("%d. %s", i, holdings.at(i).toString())));
                    String input = Global.scanner.nextLine().trim().toLowerCase();
                    if (input.equals("all")) {
                        holdings.stream().forEach(e -> e.returns());
                        holdings.clear();//Dọn sạch danh sách tài liệu đang giữ
                        System.out.println("Da tra het tai lieu");
                        if (ThoiGian.now().compareTo(this.deadline) > 0) {
                            System.out.println("Tra tre: \n" + KhoangThoiGian.toNow(this.deadline).toString());
                            System.out.println("Tien phat: \n" + (holdings.size() + 1) * (KhoangThoiGian.toDays(this.deadline) * 5000));
                            switch (StringHelper.acceptInput("Da nop tien phat roi", "Bo chay")) {
                                case 1 -> {
                                    return this;
                                }
                                case 2 -> {
                                    Reader creator = getCreator();
                                    System.out.println(StringHelper.liner("Thong tin doc gia vo trach nhiem:", creator.toString()));
                                    int index = Global.readers.search(creator.getId());
                                    if (index != -1) {
                                        Global.readers.instance.erase(index);
                                        Global.readers_black.instance.push_back(creator);
                                    }
                                }
                            }
                        }
                    } else {
                        Document d = holdings.erase(Integer.parseInt(StringHelper.acceptLine("Nhap stt tai lieu tra")));
                        d.returns();
                        System.out.println("Tai lieu vua tra");
                        System.out.println(d.toString());
                    }
                }
                case 2 -> {
                    System.out.println("Chon tai lieu mat (nhap all la mat het");
                    IntStream.range(0, holdings.size())
                            .forEach(i -> System.out.println(String.format("%d. %s", i, holdings.at(i).toString())));
                    String input = Global.scanner.nextLine().trim().toLowerCase();
                    if (input.equals("all")) {
                        holdings.stream().forEach(e -> e.lost());
                        holdings.clear();
                        System.out.println("Da xu ly mat het tai lieu");
                    } else {
                        Document d = holdings.erase(Integer.parseInt(StringHelper.acceptLine("Nhap stt tai lieu mat")));
                        d.lost();
                        System.out.println("Tai lieu vua mat");
                        System.out.println(d.toString());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Input error", e);
            throw e;
        }
        if (holdings.size() == 0) {//Nếu đã trả hết tài liệu thì xóa hóa đơn
            StringHelper.itemer("Da xoa hoa don", this.toStringMinified());
            int pos = IntStream.range(0, Global.hoadons.instance.size()).filter(e -> Global.hoadons.instance.at(e).getId() == this.getId()).findAny().orElse(-1);
            if (pos != -1) {
                Global.hoadons.instance.erase(pos);
            }
        }
        return null;
    }

    public String[] toBlob() {
        return new String[]{String.valueOf(getId()), String.valueOf(getCreator().getId()),
                StringHelper.lv1Join(getBorrows().stream().map(Document::getId).toArray()),
                StringHelper.lv1Join(getHoldings().stream().map(Document::getId).toArray()),
                getDeadline().toString()};
    }

    public static HoaDon fromBlob(String[] inp) {
        int id = Integer.parseInt(inp[0]);
        Reader creator = Global.readers.getById(Integer.parseInt(inp[1]));
        PFArray<Document> borrows = new PFArray<>();
        Stream.of(StringHelper.lv1Split(inp[2]))
                .forEach(e -> borrows.push_back(Global.documents.getById(Integer.parseInt(e))));
        PFArray<Document> holdings = new PFArray<>();
        Stream.of(StringHelper.lv1Split(inp[3]))
                .forEach(e -> holdings.push_back(Global.documents.getById(Integer.parseInt(e))));
        ThoiGian deadline = ThoiGian.parseTG(inp[4]);
        HoaDon toRet = new HoaDon(id, borrows, creator);
        toRet.setHoldings(holdings).setDeadline(deadline);
        return toRet;
    }

    public String toString() {
        return StringHelper.phanCach() + StringHelper.liner(super.toString(),
                StringHelper.itemer("Holdings",
                        holdings.stream().map(Document::toString).collect(Collectors.joining("\n"))),
                StringHelper.itemer("Deadline", deadline.toScreen()));
    }

    public String toStringMinified() {
        return StringHelper.lv1Join(getId(),
                holdings.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.joining(", ")));
    }

    private ThoiGian deadline;
    private PFArray<Document> holdings = new PFArray<>();
    public static final int blob_column = 5;
}
