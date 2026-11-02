from pathlib import Path
p=Path('C:/ETL/Server/src/native/common/konga/FileTransfer/Ftp.h');p.write_text(p.read_text(encoding='utf-8').replace('../../../../ssh/','../../../ssh/'),encoding='utf-8')
p=Path('C:/ETL/Studio/src/main/java/org/etlforge/integration/client/ui/admin/ssh');p.mkdir(parents=True,exist_ok=True)
