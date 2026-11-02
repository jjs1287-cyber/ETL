from pathlib import Path
import re
root=Path('C:/ETL/Reference/v4/jitterbit-code-r45309-branches-v4-4.1.x/integration/install/server/files/KONGA_HOME/Db/PostgreSQL/PDb/data')
copy_sql=(root/'insert_data.sql').read_text()
keys={'ActivityRuleTab':4,'ActivityTab':3,'DocumentTab':4,'ProcessTab':2,'routetab':4,'taskdataelementstab':4,'TaskTab':3}
output=['-- Native Interchange process seed, derived from the supplied v4 process definitions.',
        '-- Upstream copyright (c) 1998-2011 Jitterbit, Inc. Retained attribution.',
        '-- Additive and repeatable: no table truncation and no changes to existing routes.']
for table,columns,file in re.findall(r'COPY\s+(\w+)\s*\((.*?)\)\s*FROM\s+\x27.*?data\\\\(\w+\.txt)\x27',copy_sql,re.S|re.I):
    columns=[value.strip() for value in columns.split(',')]
    for row in (root/file).read_text().splitlines():
        values=row.split('\t')
        if values[0]!='1':continue
        assert len(columns)==len(values),(table,len(columns),len(values))
        literals=['NULL' if value=='<NULL>' else "'"+value.replace("'","''")+"'" for value in values]
        where=' AND '.join(column+'='+value for column,value in zip(columns[:keys[table]],literals))
        output.append('INSERT INTO '+table+'('+','.join(columns)+') SELECT '+','.join(literals)+' WHERE NOT EXISTS(SELECT 1 FROM '+table+' WHERE '+where+');')
assert len(output)>5
Path('C:/ETL/Server/src/database/postgresql/PDb/NativeProcessSeed.sql').write_text('\n'.join(output)+'\n')
print('Prepared native Interchange route seed.')
