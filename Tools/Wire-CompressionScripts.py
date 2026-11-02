from pathlib import Path
r=Path('C:/ETL/Server/src/native/integration/konga/TransformationUtils')
b=Path('C:/ETL/Archives/endpoint-compression-20261029/Server/src/native/integration/konga/TransformationUtils');b.mkdir(parents=True,exist_ok=True)
for name in ['ReadFileImpl.cpp','FileTypeTargetCache.cpp']:
 p=r/name;s=p.read_text();(b/name).write_text(s);s='#include <konga/OperationObjects/EndpointCompression.h>\n'+s
 if name=='ReadFileImpl.cpp':s=s.replace('pFileSource->getFirstSourceFile(context, files);','pFileSource->getFirstSourceFile(context, files);\n            expandEndpointArchives(*pFileSource, files);\n            if(files.getProcessedFilesByRef().size()>1)throw OperationEngineException("ReadFile requires one archive member; use an operation source to process every member.");')
 else:s=s.replace('m_target.doTransfer(dh, files);','compressEndpointFiles(m_target,files);\n        m_target.doTransfer(dh, files);')
 p.write_text(s)
