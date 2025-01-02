# gets list of dates and outputs descending order
# 16/03/2006 10:26:12	30/09/1981 13:35:32
# 21/11/1991 11:13:21	10/12/1998 02:31:06
# 30/03/1993 09:29:37	19/03/1983 01:52:07
# 10/12/2007 02:53:27	21/08/2009 10:43:14
# ---->
# 16/03/2006 10:26:12	30/09/1981 13:35:32
# 10/12/1998 02:31:06   21/11/1991 11:13:21 <----
# 30/03/1993 09:29:37	19/03/1983 01:52:07
# 21/08/2009 10:43:14	10/12/2007 02:53:27 <----

import datetime, pyperclip

format = "%d/%m/%Y %H:%M:%S"

inp = (open("regtime_borntime_reorder_input.txt", "r", encoding="utf-8").read() if int(input("1.file, 2.clipboard")) == 1 else pyperclip.paste()).replace("\r", "").strip().split("\n")

def swapper(line: str):
    part = line.split("\t")
    if datetime.datetime.strptime(part[0], format) < datetime.datetime.strptime(part[1], format):
        return part[1] + "\t" + part[0]
    return line
out = "\n".join(map(swapper, inp))

if int(input("1.file, 2.clipboard")) == 1:
    open("regtime_borntime_reorder_output.txt", "w", encoding="utf-8").write(out)
else:
    pyperclip.copy(out)