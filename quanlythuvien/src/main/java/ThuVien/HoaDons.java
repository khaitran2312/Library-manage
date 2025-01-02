package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class HoaDons extends Management<HoaDon> {
    private static final Logger LOGGER = Logger.getLogger(HoaDons.class.getName());

    public HoaDons() {
        super();
    }

    public HoaDons(HoaDon[] r) {
        super(r);
    }

    public HoaDons(PFArray<String[]> blob) {
        this();
        blob.stream().forEach(e -> instance.push_back(HoaDon.fromBlob(e)));
        updateCounter();
    }

    @Override
    public HoaDon add() {
        try {
            int id = StringHelper.acceptKey("Nhap id hoa don ao (duoc tao boi doc gia)");
            if (id == -1) {
                return null;
            }
            int pos = IntStream.range(0, virtuals.size()).filter(i -> virtuals.at(i).getId() == id).findAny()
                    .orElse(-1);
            if (pos == -1) {
                System.out.println("Khong tim thay hoa don ao");
                return null;
            }
            System.out.println("Tim thay hoa don ao");
            System.out.println(virtuals.at(pos).toStringMinified());
            HoaDon toAdd = new HoaDon(genNextId(), virtuals.at(pos));
            String ngayTraStr = StringHelper.acceptLine("Nhap ngay hoan tra (mac dinh 7 ngay)");
            int ngayTra = StringHelper.isNullOrBlank(ngayTraStr) ? 7 : Integer.parseInt(ngayTraStr);
            System.out.println(StringHelper.itemer("Thong bao le phi", toAdd.calcBorrowingFee(ngayTra)));
            System.out.println("Xac nhan thanh toan:");
            int m = StringHelper.acceptInput("Da thu tien", "Chay tron roi");
            if (m == 1) {
                toAdd.setDeadline(ThoiGian.now().modNgay(ngayTra));
                instance.push_back(toAdd);
                System.out.println(StringHelper.itemer("Da them thanh cong hoa don", toAdd.toString()));
                virtuals.erase(pos);
                System.out.println("Thanh toan da xac nhan");
                return toAdd;
            }
            System.out.println("Thanh toan huy bo");
            return toAdd;
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Likely input parse error in HoaDons::add", e);
            LOGGER.info("The editing operation is cancelled");
        }
        System.out.println(StringHelper.phanCach());
        return null;
    }

    @Override
    public HoaDon remove() {
        throw new UnsupportedOperationException("Khong cho phep xoa hoa don");
    }

    @Override
    public HoaDon edit() {
        HoaDon toEdit = null;
        try {
            int[] pos = search();
            if (pos.length == 0) {
                System.out.println("Khong co hoa don");
            }
            else {
                HoaDon[] found = IntStream.of(pos).mapToObj(i -> instance.at(i)).toArray(HoaDon[]::new);
                int selectedPos = 1;
                if (found.length > 1) {
                    System.out.println("Chon trong cac hoa don");
                    selectedPos = StringHelper.acceptInput(StringHelper.arr2str(found));
                }
                System.out.println("Dang thuc hien edit hoa don");
                System.out.println(found[selectedPos - 1].toString());
                toEdit = found[selectedPos - 1].edit();
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Likely input parse error in HoaDons::edit", e);
            LOGGER.info("The editing operation is cancelled");
        }
        return toEdit;

    }

    public int[] search() {
        try {
            int index;
            int id;
            switch (StringHelper.acceptInput("ID doc gia", "ID hoa don")) {
                case 1 -> {
                    index = Global.readers.promptSearch();//Tìm kiếm độc giả
                    id = Global.readers.instance.at(index).getId();
                    return IntStream.range(0, instance.size()).filter(i -> instance.at(i).getCreator().getId() == id)
                            .toArray();//Mỗi độc giả có nhiều hóa đơn
                }
                case 2 -> {
                    id = Integer.parseInt(StringHelper.acceptLine("Nhap ID hoa don"));
                    return IntStream.range(0, instance.size()).filter(i -> instance.at(i).getId() == id).toArray();
                }
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Likely input parse error in HoaDons::search", e);
            LOGGER.info("Will yield no result");
        }
        return new int[0];
    }

    public VirtualHoaDon createVirtual(Reader r) {
        return new VirtualHoaDon(++id_virtuals_counter, r);
    }

    public HoaDons acceptVirtual(VirtualHoaDon vhd) {
        virtuals.push_back(vhd);
        return this;
    }

    protected VirtualHoaDon removeVirtualById(int id) {
        int n = virtuals.stream().mapToInt(e -> e.getId()).filter(e -> e == id).findAny().orElse(-1);
        if (n == -1) {
            return null;
        } else {
            return virtuals.erase(n);
        }
    }

    public HoaDon confirmVirtual(int virtual_id, ThoiGian deadline) {
        int vhdn = IntStream.range(0, virtuals.size()).filter(e -> virtuals.at(e).getId() == virtual_id).findAny()
                .orElse(-1);
        if (vhdn == -1) {
            LOGGER.warning("Virtual HoaDon id not found");
            return null;
        }
        HoaDon hd = new HoaDon(genNextId(), virtuals.at(vhdn));
        hd.setDeadline(deadline);
        instance.push_back(hd);
        virtuals.erase(vhdn);
        return hd;
    }

    public HoaDon confirmVirtual(int virtual_id) {
        return confirmVirtual(virtual_id, ThoiGian.now());
    }

    public static HoaDons fromBatchBlob(PFArray<String[]> inp) {
        if (inp.size() < 1) {
            LOGGER.warning("No entries");
        } else {
            LOGGER.info(String.format("Batching %d x %d blob", inp.size(), HoaDon.blob_column));
        }
        return new HoaDons(inp);
    }

    private PFArray<VirtualHoaDon> virtuals = new PFArray<>();
    private int id_virtuals_counter = 0;
}
