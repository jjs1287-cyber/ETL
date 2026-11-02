import csv
import json
from pathlib import Path
csv.field_size_limit(2 * 1024 * 1024)
checks = 0
expected = {row['instance']: row['message'] for row in (json.loads(line) for line in Path(r'C:\ETL\Reports\cloud-logging-expected-messages.jsonl').read_text(encoding='utf-8').splitlines())}
def clipped(message):
    return message.encode('utf-8')[:1048576].decode('utf-8', errors='ignore')
required = {'Operation Name', 'Project', 'Environment', 'Status', 'Started', 'Finished', 'Server ip', 'Server Name', 'Operation messages', 'Upload ID'}
files = list(Path(r'C:\ETL\Samples\CloudLogging\sftp-fixture\CloudLogging').glob('*.csv')) + list(Path(r'C:\SFTP\endowance\CloudLoggingSMB').glob('*.csv'))
large = False
unicode_seen = False
for file in files:
    with file.open(encoding='utf-8', newline='') as stream:
        rows = list(csv.DictReader(stream))
    assert len(rows) == 1
    row = rows[0]
    assert required <= row.keys()
    message = row['Operation messages']
    assert len(message.encode('utf-8')) <= 1048576
    assert message == clipped(expected[row['Operation Instance ID']])
    unicode_seen |= 'café € 中文 😀' in message
    assert row['Operation Name'] == 'Cloud logging example operation'
    assert row['Project'] == 'Cloud Logging Examples'
    assert row['Upload ID'].lower() in file.name.lower()
    assert file.name.split('_')[0] == row['Operation ID'].zfill(20)
    if row['Messages Truncated'] == 'true':
        assert len(message.encode('utf-8')) > 1048568
        large = True
    checks += 8
assert large
assert unicode_seen
checks += 1
payloads = []
for file in Path(r'C:\ETL\Samples\CloudLogging\received').glob('*.json'):
    data = json.loads(file.read_text(encoding='utf-8'))
    if 'Upload ID' in data:
        payloads.append(data)
        assert required <= data.keys()
        assert data['Operation messages'] == clipped(expected[data['Operation Instance ID']])
        assert len(data['Operation messages'].encode('utf-8')) <= 1048576
        checks += 3
assert payloads
metrics = json.loads(Path(r'C:\ETL\Reports\cloud-sftp-fixture-state.json').read_text())
assert metrics['reads'] == 0
checks += 2
assert not list(Path(r'C:\ETL\Samples\CloudLogging\sftp-fixture\CloudLogging').glob('*.part'))
assert not list(Path(r'C:\SFTP\endowance\CloudLoggingSMB').glob('*.part'))
checks += 2
print(f'PASS: {checks} payload checks; CSV/JSON Unicode, filenames, 1 MiB cap, truncation flag, and no remote file reads.')
