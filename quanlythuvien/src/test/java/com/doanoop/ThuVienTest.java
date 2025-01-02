package com.doanoop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Polyfill.PFArray;
import Polyfill.PFFileReader;
import ThuVien.Authors;
import ThuVien.Language;
import ThuVien.Languages;

public class ThuVienTest {
    @Test
    public void language() {
        Language vn = Languages.vn;
        Language vn1 = Languages.parseLang("tieng viet");
        Language vn2 = Languages.parseLang("vietnamese");
        Language vn3 = Languages.parseLang("vn");
        Language vn4 = Languages.parseLang("aoma|vayba|vn");
        Language aibiet = Languages.unknown;
        Language aibiet1 = Languages.parseLang("chuoi nay|cha biet la gi");
        assertSame(vn, vn1);
        assertSame(vn, vn2);
        assertSame(vn, vn3);
        assertSame(vn, vn4);
        assertSame(aibiet, aibiet1);
    }

    @Test
    public void authors() {
        PFFileReader authorsFile = new PFFileReader("data", "List_TacGia.csv");
        PFArray<String[]> authorsData = authorsFile.read();
        authorsData.resize(10);
        authorsData.pop_front();
        Authors authors = Authors.fromBatchBlob(authorsData);
        for (int i = 1; i < authorsData.size(); i++) {
            System.out.println(authors.getById(i));
        }
        PFArray<String[]> authorsBlob = authors.toBatchBlob();
        for (int i = 0; i < authorsData.size(); i++) {
            // first 7 is exact string
            for (int j = 0; j < 7; j++) {
                assertEquals(authorsData.at(i)[j], authorsBlob.at(i)[j]);
            }
            // 8 is language
            assertTrue(authorsBlob.at(i)[7].toLowerCase().contains(authorsData.at(i)[7].toLowerCase()));
        }
    }
}
