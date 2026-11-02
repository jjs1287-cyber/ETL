from pathlib import Path
p=Path('C:/ETL/Server/src/native/ssh/SshKeys.cpp');s=p.read_text(encoding='utf-8').replace('s+=static_cast<char>(w[i]);','s+=static_cast<char>(towlower(w[i]));');p.write_text(s,encoding='utf-8')
