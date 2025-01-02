package com.doanoop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import Polyfill.KhoangThoiGian;
import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class PolyfillTest {

    @Test
    public void PFArrayTest() {
        PFArray<Integer> pfarray = new PFArray<Integer>();
        List<String> strs = new ArrayList<String>();
        // regular add remove test and iterable test
        pfarray.push_back(1);
        pfarray.push_back(2);
        pfarray.push_front(3);
        pfarray.push_front(4);
        pfarray.insert(1, 5);
        for (int i : pfarray) {
            strs.add(Integer.toString(i));
        }
        strs.add(Integer.toString(pfarray.size()));
        strs.add(Integer.toString(pfarray.capacity()));
        assertTrue(String.join(" ", strs).equals("4 5 3 1 2 5 8"));
        strs.clear();
        // stream test
        pfarray.stream().forEach(e -> strs.add(String.valueOf(e)));
        assertEquals("4 5 3 1 2", StringHelper.spacer(strs.toArray()));
        strs.clear();
        // regular add remove test and iterable test again
        pfarray.pop_back();
        pfarray.pop_front();
        pfarray.erase(1);
        for (int i : pfarray) {
            strs.add(Integer.toString(i));
        }
        strs.add(Integer.toString(pfarray.size()));
        strs.add(Integer.toString(pfarray.capacity()));
        assertTrue(String.join(" ", strs).equals("5 1 2 8"));
        // stream test again
        assertEquals("5 1", pfarray.stream().map(e -> e.toString()).collect(Collectors.joining(" ")));
    }

    @Test
    public void thoiGianTest() {
        ThoiGian thoigian1 = new ThoiGian(18, 11, 2022);
        ThoiGian thoigian2 = new ThoiGian(18, 11, 2022, 2, 7, 10);
        ThoiGian thoigian3 = ThoiGian.parseTG("18/11/2022 02:07:10");
        assertTrue("18/11/2022 00:00:00".equals(thoigian1.toString()));
        assertTrue("18/11/2022 02:07:10".equals(thoigian2.toString()));
        assertTrue("18/11/2022 02:07:10".equals(thoigian3.toString()));
        thoigian1 = thoigian1.modNgay(20);
        thoigian2 = thoigian2.modNgay(-20);
        assertTrue("08/12/2022 00:00:00".equals(thoigian1.toString()));
        assertTrue("29/10/2022 02:07:10".equals(thoigian2.toString()));
        assertTrue(thoigian2.compareTo(thoigian1) < 0);
        assertTrue(thoigian3.compareTo(thoigian2) > 0);
        ThoiGian thoigian4 = new ThoiGian(LocalDateTime.of(2022, 10, 29, 2, 7, 10));
        assertTrue(thoigian4.compareTo(thoigian2) == 0);
        System.out.println(ThoiGian.now().toString());
    }

    @Test
    public void khoangThoiGianTest() {
        ThoiGian tg1 = ThoiGian.parseTG("08/12/2022 21:13:49");
        ThoiGian tg2 = ThoiGian.parseTG("29/10/2022 02:07:10");
        ThoiGian tg3 = ThoiGian.parseTG("11/09/2023 11:19:19");
        KhoangThoiGian ktg1 = KhoangThoiGian.between(tg2, tg1);
        KhoangThoiGian ktg2 = KhoangThoiGian.between(tg3, tg2);
        KhoangThoiGian ktg3 = KhoangThoiGian.between(tg3, tg3);
        assertEquals(ktg1.toString(), "40 ngay, 19 gio, 6 phut, 39 giay ago");
        assertEquals(ktg2.toString(), "in 317 ngay, 9 gio, 12 phut, 9 giay");
        assertTrue(ktg3.toString().startsWith("no difference"));
    }
    // Working no need rewrite
    // @Test
    // public void PFFileTest() {
    // PFFileReader fr = new PFFileReader("data", "List_NhanVien.csv");
    // // can throws exceptions and fail
    // PFArray<String[]> datain = fr.read();
    // // REMEMBER: DELETE BOM FROM CSV FILE IF IT's SAVED WITH BOM
    // assertArrayEquals(datain.front(), new String[] { "id", "username",
    // "password", "name", "regtime", "borntime",
    // "phone", "email", "address" });
    // PFArray<String[]> dataout = new PFArray<>();
    // dataout.push_back(datain.at(0));
    // Random r = new Random();
    // for (int i = 0; i < 10; i++) {
    // dataout.push_back(datain.at(r.nextInt(datain.size() - 2) + 1));
    // }
    // PFFileWriter fw = new PFFileWriter("data", "List_NhanVien_out.csv");
    // fw.write(dataout);
    // PFFileReader fr2 = new PFFileReader("data", "List_NhanVien_out.csv");
    // PFArray<String[]> datain2 = fr2.read();
    // assertArrayEquals(datain2.front(), new String[] { "id", "username",
    // "password", "name", "regtime", "borntime",
    // "phone", "email", "address" });
    // IntStream.range(0, 9).forEach(i -> assertArrayEquals(dataout.at(i),
    // datain2.at(i)));
    // }

    @Test
    public void stringhelperTest() {
        String[] s = StringHelper.vararg2str(new ThoiGian(1, 1, 1990), new ThoiGian(1, 1, 2000));
        assertEquals("01/01/1990 00:00:00 01/01/2000 00:00:00", StringHelper.spacer((Object[]) s));
    }
}
