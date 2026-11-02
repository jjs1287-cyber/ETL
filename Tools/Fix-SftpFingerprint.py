from pathlib import Path
p=Path('C:/ETL/Server/src/native/common/konga/FileTransfer/CurlTransferLibWrapper.cpp');s=p.read_text(encoding='utf-8').replace('CURLOPT_SSH_HOST_PUBLIC_KEY_SHA256,fingerprint.c_str()','CURLOPT_SSH_HOST_PUBLIC_KEY_SHA256,(fingerprint+"=").c_str()');p.write_text(s,encoding='utf-8')
p=Path('C:/ETL/Server/tests/integration/SftpKeysLiveTest.java');s=p.read_text(encoding='utf-8').replace('SFTPGo/id_ed25519.pub','SFTPGo/id_ecdsa.pub');p.write_text(s,encoding='utf-8')
