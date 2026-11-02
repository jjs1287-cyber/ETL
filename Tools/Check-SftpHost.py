import hashlib,base64
from pathlib import Path
for name in ['id_rsa.pub','id_ecdsa.pub','id_ed25519.pub']:
 b=Path('C:/ProgramData/SFTPGo',name).read_text().split()[1];print(name,base64.b64encode(hashlib.sha256(base64.b64decode(b)).digest()).decode())
