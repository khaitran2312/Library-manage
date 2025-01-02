package ThuVien;

import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public abstract class People extends AnyId {
    // Set it like this:
    // People p = new
    // People().setName(name).setEmail(email).setPhone(phone).setAddress(address)
    // ...
    public People(int id) {
        super(id);
    }

    protected String getName() {
        return name;
    }

    protected People setName(String name) {
        this.name = name;
        return this;
    }

    protected String getEmail() {
        return email;
    }

    protected People setEmail(String email) {
        this.email = email;
        return this;
    }

    protected String getPhone() {
        return phone;
    }

    protected People setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    protected String getAddress() {
        return address;
    }

    protected People setAddress(String address) {
        this.address = address;
        return this;
    }

    protected ThoiGian getBirth() {
        return birth;
    }

    protected People setBirth(ThoiGian birth) {
        this.birth = birth;
        return this;
    }

    @Override
    public String toString() {
        return StringHelper.liner(super.toString(),
                StringHelper.itemer("Name", name),
                StringHelper.itemer("Phone", phone),
                StringHelper.itemer("Email", email),
                StringHelper.itemer("Address", address),
                StringHelper.itemer("Birthdate", birth.toScreen()));
    }

    public String toStringMinified() {
        return StringHelper.lv1Join(getId(), getName());
    }

    private String name, email, phone, address;
    private ThoiGian birth;
}
