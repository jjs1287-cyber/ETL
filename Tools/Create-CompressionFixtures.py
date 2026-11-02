from pathlib import Path
p=Path('C:/ETL/Server/src/native/compression/EndpointArchive.cpp');s=p.read_text().replace('while(!relative.empty()&&relative.back()', 'while(relative.compare(0,2,"./")==0)relative.erase(0,2);\n   if((relative.empty()||relative==".")&&archive_entry_filetype(entry)==AE_IFDIR)continue;\n   while(!relative.empty()&&relative.back()');p.write_text(s)
p=Path('C:/ETL/Server/src/native/integration/konga/OperationObjects/SourceDataLocation.cpp');s=p.read_text().replace(' doPostTransformation(context,original);',' for(auto& item:original.getProcessedFilesByRef())item.archiveEntryName.clear();\n doPostTransformation(context,original);');p.write_text(s)
p=Path('C:/ETL/Server/cmake/RemainingCore.cmake');p.write_text(p.read_text()+'''
add_executable(etlforge_endpoint_archive_tests tests/EndpointArchiveTests.cpp)
target_link_libraries(etlforge_endpoint_archive_tests PRIVATE etlforge_endpoint_archive)
''')
import zipfile,tarfile,io
r=Path('C:/ETL/Samples/EndpointCompression/native');r.mkdir(parents=True,exist_ok=True)
for f,n in [('traversal.zip','../escape.txt'),('absolute.zip','C:/escape.txt'),('reserved.zip','NUL.txt')]:
 with zipfile.ZipFile(r/f,'w') as z:z.writestr(n,'bad')
with zipfile.ZipFile(r/'symlink.zip','w') as z:
 e=zipfile.ZipInfo('link');e.create_system=3;e.external_attr=0o120777<<16;z.writestr(e,'../outside')
with zipfile.ZipFile(r/'case.zip','w') as z:z.writestr('a.txt','a');z.writestr('A.txt','b')
with tarfile.open(r/'leading-dot.tar.gz','w:gz') as z:
 e=tarfile.TarInfo('./');e.type=tarfile.DIRTYPE;z.addfile(e)
 e=tarfile.TarInfo('./nested/input.txt');e.size=5;z.addfile(e,io.BytesIO(b'hello'))
e=tarfile.TarInfo('big');e.size=3*1024**3;(r/'oversize.tar').write_bytes(e.tobuf()+b'\0'*1024)
