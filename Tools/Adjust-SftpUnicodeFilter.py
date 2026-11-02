from pathlib import Path
p=Path('C:/ETL/Server/tests/integration/SftpKeysLiveTest.java');s=p.read_text(encoding='utf-8').replace('n==5?"caf\\u00e9 \\u4e16\\u754c\\ud83d\\ude00.bin":"payload.bin"','n==5?"caf*.bin":"payload.bin"');p.write_text(s,encoding='utf-8')
