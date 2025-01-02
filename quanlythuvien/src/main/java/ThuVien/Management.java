package ThuVien;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import Polyfill.PFArray;

public abstract class Management<T extends AnyId & IDataProcess<T>> implements IBatchDataProcess<T> {
    private static final Logger LOGGER = Logger.getLogger(Management.class.getName());

    public Management() {
        instance = new PFArray<>();
        id_counter = 0;
    }

    public Management(T[] ts) {
        instance = new PFArray<>(ts);
        updateCounter();
    }

    public abstract T add();

    public abstract T remove();

    public abstract T edit();

    public abstract int[] search();

    public int search(int id) {
        return IntStream.range(0, instance.size()).filter(i -> instance.at(i).getId() == id).findAny().orElse(-1);
    }

    public T getById(int id) {
        return instance.stream().filter(e -> e.getId() == id).findAny().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public T[] batchGetByIds(int[] ids) {
        return (T[]) IntStream.of(ids).mapToObj(e -> getById(e)).filter(Objects::nonNull).toArray();
    }

    public boolean removeById(int id) {
        int toRemoveIndex = search(id);
        if (toRemoveIndex < 0) {
            // not found
            return false;
        } else {
            instance.erase(toRemoveIndex);
            return true;
        }

    }

    public PFArray<String[]> toBatchBlob() {
        PFArray<String[]> toRet = new PFArray<>(instance.size());
        LOGGER.info(String.format("Blobing %d x %d batch", instance.size(), instance.at(0).toBlob().length));
        // instance.stream().map(T::toBlob).forEach(e -> toRet.push_back(e));
        // java.lang.BootstrapMethodError: bootstrap method initialization exception
        // at java.base/java.lang.invoke.BootstrapMethodInvoker.invoke(Unknown Source)
        // at java.base/java.lang.invoke.CallSite.makeSite(Unknown Source)
        // at java.base/java.lang.invoke.MethodHandleNatives.linkCallSiteImpl(Unknown
        // Source)
        // at java.base/java.lang.invoke.MethodHandleNatives.linkCallSite(Unknown
        // Source)
        // at ThuVien.Management.toBatchBlob(Management.java:54)
        // at com.doanoop.ThuVienTest.authors(ThuVienTest.java:19)
        // Caused by: java.lang.invoke.LambdaConversionException: Invalid receiver type
        // class ThuVien.AnyId; not a subtype of implementation type interface
        // ThuVien.IDataProcess
        // https://stackoverflow.com/questions/27394032/bootstrapmethoderror-caused-by-lambdaconversionexception-caused-by-using-methodh
        instance.stream().map(e -> e.toBlob()).forEach(e -> toRet.push_back(e));
        return toRet;
    }

    protected void updateCounter() {
        id_counter = instance.stream().mapToInt(AnyId::getId).max().orElse(0);
    }

    protected int genNextId() {
        return ++id_counter;
    }

    public int size() {
        return instance.size();
    }

    protected int currentIdCount() {
        return id_counter;
    }

    protected PFArray<T> instance;
    private int id_counter;
}
