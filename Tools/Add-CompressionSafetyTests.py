from pathlib import Path
import binascii
r=Path('C:/ETL/Samples/EndpointCompression/native');f=Path('C:/ETL/Tools/vcpkg/buildtrees/libarchive/src/v3.8.9-cd3f8d85b2.clean/libarchive/test/test_read_format_rar5_compressed.rar.uu')
lines=f.read_bytes().splitlines();active=False;raw=bytearray()
for line in lines:
 if line.startswith(b'begin '):active=True;continue
 if not active:continue
 if line==b'end':break
 if line:raw.extend(binascii.a2b_uu(line))
(r/'reference.rar').write_bytes(raw)
p=Path('C:/ETL/Server/tests/EndpointArchiveTests.cpp');s=p.read_text().replace('#include <stdexcept>','#include <stdexcept>\n#include <clocale>');s=s.replace(' using namespace etlforge::compression;',' using namespace etlforge::compression;\n std::string initialLocale=setlocale(LC_CTYPE,nullptr);');s=s.replace(' auto encrypted=(root/"encrypted.zip").u8string();',' check(initialLocale==setlocale(LC_CTYPE,nullptr),"caller locale unchanged");\n auto rar=extract((root/"reference.rar").u8string(),(root/"rar-out").u8string(),"");check(rar.size()==1&&rar[0].name=="test.bin"&&read(rar[0].path).size()==1200,"RAR5 compressed input auto-detected");\n auto encrypted=(root/"encrypted.zip").u8string();');s=s.replace(' reject([&]{extract(encrypted,(root/"encrypted-bad")',' reject([&]{extract(encrypted,(root/"byte-limit").u8string(),"test-password","",1,10000);},"extraction byte budget enforced");\n reject([&]{extract(encrypted,(root/"entry-limit").u8string(),"test-password","",1024*1024,1);},"extraction entry budget enforced");\n reject([&]{extract(encrypted,(root/"encrypted-bad")');p.write_text(s,encoding='utf-8')
