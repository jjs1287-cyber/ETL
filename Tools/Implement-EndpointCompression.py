from pathlib import Path
import shutil
root=Path('C:/ETL'); backup=root/'Archives/endpoint-compression-20261029';backup.mkdir(exist_ok=True)
files=['Server/src/native/integration/konga/OperationObjects/SourceDataLocation.cpp','Server/src/native/integration/konga/OperationObjects/SourceDataLocation.h','Server/src/native/integration/konga/OperationObjects/TargetDataLocation.cpp','Server/src/native/integration/konga/OperationObjects/DataLocation.h','Server/src/native/integration/konga/OperationObjects/ProcessedFiles.h','Server/src/native/integration/konga/OperationObjects/FileTypeTargetLocation.cpp','Server/src/native/integration/konga/OperationEngine/OperationEngine.cpp','Server/cmake/RemainingCore.cmake','Studio/src/main/java/org/etlforge/integration/data/location/FileLocation.java','Studio/src/main/java/org/etlforge/integration/client/ui/interchange/location/panel/FileLocationPanel.java']
for name in files:
 dest=backup/name;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(root/name,dest)
print('Archived',len(files),'sources')
