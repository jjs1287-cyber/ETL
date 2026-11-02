from pathlib import Path
import re
p=Path('C:/ETL/Server/tests/EndpointArchiveTests.cpp');s=p.read_text(encoding='utf-8');s=re.sub(r'u8"input_[^"]+"',lambda _:r'u8"input_\u4e16\u754c.bin"',s);s=re.sub(r'u8"folder/[^"]+"',lambda _:r'u8"folder/\u4e16\u754c.bin"',s)
s=s.replace('check(files.size()==1&&read(files[0].path)==content,','check(std::string(format)=="gz"||files[0].name==u8"folder/\\u4e16\\u754c.bin","Unicode member name preserved");check(files.size()==1&&read(files[0].path)==content,')
p.write_text(s,encoding='utf-8')
