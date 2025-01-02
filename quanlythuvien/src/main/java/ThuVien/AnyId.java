package ThuVien;

import Polyfill.StringHelper;

public abstract class AnyId implements Comparable<AnyId> {
    public int getId() {
        return id;
    }

    public AnyId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(AnyId e) {
        return Integer.compare(id, e.getId());
    }

    @Override
    public String toString() {
        return StringHelper.itemer("ID", id);
    }

    public abstract String toStringMinified();

    private int id;
}
