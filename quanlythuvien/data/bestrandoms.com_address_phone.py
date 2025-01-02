# get addresses and phones from bestrandoms.com vietnam address generator
# https://www.bestrandoms.com/random-address-in-vn?quantity=20
# Street:  43 Mac Thi Buoi Street, Ben Nghe Ward, District 1

# City:   Ho Chi Minh City

# State/province/area:    Ho Chi Minh City

# Phone number  (84-8) 3829 9028

# Country calling code  +94

# Country  Vietnam

# Output:
# Address: 43 Mac Thi Buoi Street, Ben Nghe Ward, District 1, Ho Chi Minh City, Ho Chi Minh City
# Phone: (84-8) 3829 9028
import re, pyperclip

street = re.compile(r"(?<=Street:\s\s).*$", re.I | re.M)
city = re.compile(r"(?<=City:\s\s\s).*$", re.I | re.M)
state = re.compile(r"(?<=State\/province\/area:\s\s\s\s).*$", re.I | re.M)
phone = re.compile(r"(?<=Phone\snumber\s\s).*$", re.I | re.M)

inp = (open("bestrandoms.com_address_phone_input.txt","r", encoding="utf-8").read() if int(input("1.file, 2.clipboard")) == 1 else pyperclip.paste()).replace("\r","")

_street = re.findall(street, inp)
_city = re.findall(city, inp)
_state = re.findall(state, inp)
_phone = re.findall(phone, inp)

def line():
    for street, city, state, phone in zip(_street, _city, _state, _phone):
        yield "|".join([", ".join([street, city, state]), phone])
out = "\n".join(line())
if int(input("1.file, 2.clipboard")) == 1:
    open("bestrandoms.com_address_phone_output.txt","w",encoding="utf-8").write(out)
else:
    pyperclip.copy(out)