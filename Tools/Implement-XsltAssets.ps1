$ErrorActionPreference='Stop'
$archive=(Get-Content C:\ETL\Reports\xslt-assets-archive.txt -Raw).Trim()
function Update-Source([string]$path,[scriptblock]$change) {
 $backup=Join-Path $archive ($path.Substring(7))
 if(!(Test-Path -LiteralPath $backup)){New-Item -ItemType Directory -Path (Split-Path $backup) -Force | Out-Null; Copy-Item -LiteralPath $path -Destination $backup}
 $old=[IO.File]::ReadAllText($path)
 $new=& $change $old
 if($new -eq $old){throw "No change in $path"}
 [IO.File]::WriteAllText($path,$new)
}
$native='C:\ETL\Server\src\native\integration\konga'
Update-Source "$native\DbRelations\EntityIdConstants.h" {param($s) $s.Replace('const int MaxEntityId = SalesforceConnectorEntityId;', 'const int XsltTransformEntityId = 34; const int MaxEntityId = XsltTransformEntityId;')}
Update-Source "$native\DbRelations\DatabaseTableNameMappings.h" {param($s) $s.Replace('extern const std::string nameScripts;', 'extern const std::string nameXsltTransforms; extern const std::string nameScripts;')}
Update-Source "$native\DbRelations\DatabaseTableNameMappings.cpp" {param($s)
 $s=$s.Replace('const string nameScripts =', 'const string nameXsltTransforms = "XsltTransforms"; const string nameScripts =')
 $s=$s.Replace('case ScriptEntityId:', 'case XsltTransformEntityId: return nameXsltTransforms; case ScriptEntityId:')
 $s=$s.Replace('if ( stName == nameScripts )', 'if ( stName == nameXsltTransforms ) return XsltTransformEntityId; if ( stName == nameScripts )')
 $s
}
Update-Source "$native\DbRelations\DbRelations.cpp" {param($s)
 $needle='pimpl->m_nameColumnMap[ getTableName(nameScripts) ] = "script_name";'
 $s.Replace($needle,$needle+@'

    vKeyInfo.clear();
    vKeyInfo.push_back(KeyInfo(getIdName(nameXsltTransforms), GetEntityIdByName(nameXsltTransforms)));
    pimpl->m_tableKeyInfoMap[getTableName(nameXsltTransforms)] = new TableKeyInfo(vKeyInfo);
    pimpl->m_nameColumnMap[getTableName(nameXsltTransforms)] = "xslttransform_name";
    vKeyInfo.clear();
    vKeyInfo.push_back(KeyInfo(getIdName(nameXsltTransforms), getTableName(nameXsltTransforms), true));
    pimpl->m_tableKeyInfoMap[GetPropertyTableName(GetEntityIdByName(nameXsltTransforms))] = new TableKeyInfo(vKeyInfo);
'@)
}
Update-Source "$native\UiBackend\WebServices\DeployInterchangeData\Impl\HandleChangedDataImpl.cpp" {param($s)
 $s=$s.Replace('#include "DeployUtils.h"', '#include "ExtendedItemProperties.h"'+[Environment]::NewLine+'#include <konga/DbRelations/EntityIdConstants.h>'+[Environment]::NewLine+'#include "DeployUtils.h"')
 $needle='tableHandler.HandleTable(nameFolders, changedData.FoldersTab,'
 $addition=@'
    InterchangeDataTableStruct xsltAssets;
    xsltAssets.m_vColumnNames.push_back("xslttransform_id");
    xsltAssets.m_vColumnNames.push_back("xslttransform_name");
    xsltAssets.m_vColumnNames.push_back("xslt");
    for (std::map<std::string, ExtendedItemProperties*>::const_iterator xslt = itemPropertiesCollection.m_PropertiesMap.begin();
         xslt != itemPropertiesCollection.m_PropertiesMap.end(); ++xslt) {
        if (xslt->second->GetEntityTypeId() != XsltTransformEntityId) continue;
        InterchangeDataRowStruct row;
        row.m_vFields.push_back(ClientField(xslt->first.c_str()));
        row.m_vFields.push_back(ClientField(xslt->second->m_properties.GetString("xslttransform_name", "").c_str()));
        row.m_vFields.push_back(ClientField(xslt->second->m_properties.GetString("xslt", "").c_str()));
        xsltAssets.m_vRows.push_back(row);
    }
    if (!xsltAssets.m_vRows.empty())
        tableHandler.HandleTable(nameXsltTransforms, xsltAssets, &defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);

'@
 $s.Replace($needle,$addition+$needle)
}
$studio='C:\ETL\Studio\src\main\java'
Update-Source "$studio\org\etlforge\integration\server\implementation\webservice\interchange\deploy\impl\DeployInterchangeModelWsImplWrapper.java" {param($s)
 $needle='   private void updateEntity(IntegrationEntity var1) throws DeployException {'
 $s=$s.Replace($needle,$needle+@'

      if (var1 instanceof org.etlforge.integration.data.entity.XsltTransform) {
         if (var1.isDeployDirty()) {
            org.etlforge.integration.data.entity.XsltTransform xslt = (org.etlforge.integration.data.entity.XsltTransform)var1;
            for (String key : new String[]{"xslttransform_name", "xslt"}) {
               this.m_extendedEntityProperties.add(getInterchangeDataRow(new String[]{"34", xslt.getID().toString(), key,
                  "xslt".equals(key) ? xslt.getXslt() : xslt.getName()}));
            }
         }
         return;
      }
'@)
 $needle='this.project.getRootFolder(EntityType.Script));'
 $s=$s.Replace($needle,$needle+[Environment]::NewLine+'         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.XsltTransform));')
 $s
}
Update-Source "$studio\org\etlforge\integration\data\entity\EntityTypeDependencyOrder.java" {param($s) $s.Replace('EntityType.Script,','EntityType.XsltTransform, EntityType.Script,')}
