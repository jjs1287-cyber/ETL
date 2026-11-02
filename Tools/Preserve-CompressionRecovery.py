from pathlib import Path
p=Path('C:/ETL/Server/src/native/integration/konga/OperationObjects/DataLocation.cpp');s=p.read_text(encoding='utf-8').split('std::string DataLocation::compressionPassword() const {')[0]
for line in ['#include "OperationEngineException.h"\n','#include <openssl/evp.h>\n','#include <memory>\n','#include <vector>\n','#include <windows.h>\n']:s=s.replace(line,'')
b=Path('C:/ETL/Archives/endpoint-compression-20261029/Server/src/native/integration/konga/OperationObjects/DataLocation.cpp');b.parent.mkdir(parents=True,exist_ok=True)
if not b.exists():b.write_text(s.rstrip()+'\n',encoding='utf-8')
