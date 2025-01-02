package ThuVien;

import Polyfill.PFArray;

public interface IBatchDataProcess<T> {
    public PFArray<String[]> toBatchBlob();
}
