from pathlib import Path
p=Path('C:/ETL/Tools/Install-EndpointCompression.ps1');s=p.read_text(encoding='utf-8').replace('endpoint-compression-20261029','sftp-keys-20261029').replace("@('archive.dll','liblzma.dll')","@('libcurl.dll','libssh2.dll')");Path('C:/ETL/Tools/Install-SftpKeys.ps1').write_text(s,encoding='utf-8')
