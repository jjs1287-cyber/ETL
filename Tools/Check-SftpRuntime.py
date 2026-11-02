import ctypes,os
from pathlib import Path
os.add_dll_directory('C:/ETL/Server/dist/bin')
dll=ctypes.CDLL('C:/ETL/Server/dist/bin/libcurl.dll');dll.curl_version.restype=ctypes.c_char_p
print(dll.curl_version().decode())
print('libssh2 bytes',Path('C:/ETL/Server/dist/bin/libssh2.dll').stat().st_size)
