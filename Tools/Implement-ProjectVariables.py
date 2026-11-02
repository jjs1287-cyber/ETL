from pathlib import Path
import shutil
root=Path('C:/ETL')
archive=root/'Archives/project-variables-20261029'
def write(rel,text):
 p=root/rel
 if p.exists():
  b=archive/rel
  if not b.exists():
   b.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,b)
 p.parent.mkdir(parents=True,exist_ok=True);p.write_text(text,encoding='utf-8')
def edit(rel,a,b):
 p=root/rel;s=p.read_text(encoding='utf-8-sig')
 if a not in s:raise RuntimeError('Missing anchor '+rel+' '+a[:60])
 write(rel,s.replace(a,b))
j='Studio/src/main/java/'
d=j+'org/etlforge/integration/data/'
n='Server/src/native/integration/konga/'
write(d+'entity/ProjectVariable.java','''package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.entity.id.ProjectVariableId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class ProjectVariable extends IntegrationEntity {
    @IdProperty public static final String ID = "projectvariable_id";
    @NameProperty public static final String NAME = "projectvariable_name";
    public ProjectVariable(String name) { this(null, name); }
    public ProjectVariable(ProjectVariableId id, String name) {
        super(EntityType.ProjectVariable, id, name);
        setDefaultValue("");
    }
    @Override public ProjectVariableId getID() { return (ProjectVariableId)super.getID(); }
    @Override public String[] getPropertyKeys() { return new String[]{ID, NAME, "default_value"}; }
    public String getDefaultValue() { return getProperty("default_value", ""); }
    public void setDefaultValue(String value) { setStringProperty("default_value", value == null ? "" : value); }
    @Override public boolean isDeepCopyable() { return true; }
    public static void validateName(String name) {
        if (name == null || !name.matches("[A-Za-z_][A-Za-z0-9_.]{0,255}")
            || name.startsWith("__") || name.toLowerCase(java.util.Locale.ROOT).startsWith("etlforge.")
            || name.toLowerCase(java.util.Locale.ROOT).startsWith("jitterbit."))
            throw new IllegalArgumentException("Use a variable name without $, spaces or reserved system prefixes; for example ApiBaseUrl.");
    }
    public static void validateValue(String value) {
        if (value.indexOf(0) >= 0) throw new IllegalArgumentException("Project variable values cannot contain NUL.");
        try {
            java.nio.ByteBuffer bytes = java.nio.charset.StandardCharsets.UTF_8.newEncoder()
                .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT).encode(java.nio.CharBuffer.wrap(value));
            if (bytes.remaining() > 1048576) throw new IllegalArgumentException("Project variable values are limited to 1 MiB.");
        } catch (java.nio.charset.CharacterCodingException error) { throw new IllegalArgumentException("Invalid Unicode in project variable value.", error); }
    }
    @Override protected void validateOtherPropertiesThanTheName(ValidationMessageCollector messages) {
        try { validateName(getName()); validateValue(getDefaultValue()); }
        catch (IllegalArgumentException error) { messages.addMessage(error.getMessage(), ValidationStatus.INVALID); }
    }
}
''')
write(d+'entity/id/ProjectVariableId.java','''package org.etlforge.integration.data.entity.id;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
public final class ProjectVariableId extends IntegrationEntityId {
    public ProjectVariableId() {}
    public ProjectVariableId(String value) { super(value); }
    @Override public EntityType getEntityType() { return EntityType.ProjectVariable; }
}
''')
config=(root/(j+'org/etlforge/integration/client/ui/interchange/entity/xslt/XsltTransformConfig.java')).read_text()
write(j+'org/etlforge/integration/client/ui/interchange/entity/projectvariable/ProjectVariableConfig.java',config.replace('entity.xslt','entity.projectvariable').replace('XsltTransform','ProjectVariable'))
write(j+'org/etlforge/integration/client/ui/interchange/entity/projectvariable/ProjectVariablePage.java','''package org.etlforge.integration.client.ui.interchange.entity.projectvariable;
import javax.swing.*;
import org.etlforge.integration.client.ui.interchange.component.page.entity.*;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.ProjectVariable;
import org.etlforge.ui.input.TextInputField2;
public final class ProjectVariablePage extends IntegrationEntityPage {
    public ProjectVariablePage(ProjectVariable variable) { super(new IntegrationEntityPageModel(variable)); setUseScroll(false); }
    @Override protected IntegrationEntityPanel createDataPanel() {
        final ProjectVariable variable = (ProjectVariable)getObject();
        return new IntegrationEntityPanel(getWindow(), variable) {
            private final JTextArea value = new JTextArea(variable.getDefaultValue(), 10, 70);
            {
                TextInputField2 field = new TextInputField2("Preset value (text)", value);
                addInputField(field, field, field);
                getDefaultLayout().add(new JLabel("Use $" + variable.getName() + " in scripts and mappings. Runtime changes do not change this preset."), false);
                getDefaultLayout().add(new JScrollPane(value), true);
            }
            @Override protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
                try { ProjectVariable.validateName(variable.getName()); ProjectVariable.validateValue(value.getText()); }
                catch (IllegalArgumentException error) { throw new IntegrationDataPanelException(error); }
                variable.setDefaultValue(value.getText());
            }
        };
    }
}
''')
edit(d+'entity/EntityType.java','   XsltTransform(34,','   ProjectVariable(35, "org.etlforge.integration.data.entity.ProjectVariable", "Project Variable", "Project Variables", "Project &Variable"),\n   XsltTransform(34,')
edit(d+'entity/EntityType.java','         case XsltTransform: return var1','         case ProjectVariable: return var1 == null ? new org.etlforge.integration.data.entity.id.ProjectVariableId() : new org.etlforge.integration.data.entity.id.ProjectVariableId(var1);\n         case XsltTransform: return var1')
edit(d+'entity/EntityType.java','         case XsltTransform: return new RootFolderId','         case ProjectVariable: return new RootFolderId("d0273500-e7f0-4000-8000-000000000035");\n         case XsltTransform: return new RootFolderId')
edit(d+'entity/EntityTypeDependencyOrder.java','EntityType.XsltTransform, EntityType.Script,','EntityType.ProjectVariable, EntityType.XsltTransform, EntityType.Script,')
r='Studio/src/main/resources/etlforge-client-config/'
edit(r+'ApplicationContext.Core.Common.xml','<value>Script</value><value>XsltTransform</value>','<value>Script</value><value>XsltTransform</value><value>ProjectVariable</value>')
edit(r+'ApplicationContext.Interchange.Common.xml','<constructor-arg value="XsltTransform" />','<constructor-arg value="XsltTransform" /></bean><bean class="org.etlforge.integration.client.ui.interchange.SingleTypeEntityCategory"><constructor-arg value="ProjectVariable" />')
edit(r+'ApplicationContext.Interchange.Entity.xml','<bean id="ic.XsltTransformConfig"','<bean id="ic.ProjectVariableConfig" parent="ic.EntityConfig.Base" class="org.etlforge.integration.client.ui.interchange.entity.projectvariable.ProjectVariableConfig" lazy-init="true" />\n\t<bean id="ic.XsltTransformConfig"')
deploy=j+'org/etlforge/integration/server/implementation/webservice/interchange/deploy/impl/DeployInterchangeModelWsImplWrapper.java'
edit(deploy,'   private void updateEntity(IntegrationEntity var1) throws DeployException {','''   private void updateEntity(IntegrationEntity var1) throws DeployException {
      if (var1 instanceof org.etlforge.integration.data.entity.ProjectVariable) {
         if (var1.isDeployDirty()) {
            org.etlforge.integration.data.entity.ProjectVariable variable = (org.etlforge.integration.data.entity.ProjectVariable)var1;
            for (String key : new String[]{"projectvariable_name", "default_value"}) {
               this.m_extendedEntityProperties.add(getInterchangeDataRow(new String[]{"35", variable.getID().toString(), key,
                  "default_value".equals(key) ? variable.getDefaultValue() : variable.getName()}));
            }
         }
         return;
      }''')
edit(deploy,'         for (IntegrationId deletedXslt :','''         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.ProjectVariable));
         for (IntegrationId deletedVariable : this.deployData.getDeletedItemsOfType(EntityType.ProjectVariable)) {
            DeployInterchangeModelWsImplWrapper.this.m_extendedEntityProperties.add(getInterchangeDataRow(new String[]{"35", deletedVariable.toString(), "__deleted", "1"}));
         }
         for (IntegrationId deletedXslt :''')
edit(d+'script/de/DefaultGlobalDataElements.java','      this.collect(new DefaultGlobalDataElements.DeCollector(), var1);','''      this.collect(new DefaultGlobalDataElements.DeCollector(), var1);
      synchronized (this.lock) {
         if (this.project != null) for (org.etlforge.integration.data.entity.ProjectVariable variable :
               this.project.getAllEntitiesOfClass(org.etlforge.integration.data.entity.ProjectVariable.class)) var1.add(variable.getName());
      }''')
for module,line in [('etlforge-integration-client-data','org/etlforge/integration/data/entity/ProjectVariable*.class'),('etlforge-integration-entity','org/etlforge/integration/data/entity/id/ProjectVariableId*.class'),('etlforge-integration-client-interchange','org/etlforge/integration/client/ui/interchange/entity/projectvariable/*.class')]:
 path='Studio/metadata/includes/'+module+'.includes';s=(root/path).read_text();write(path,s+'\n'+line+'\n')
edit(n+'DbRelations/EntityIdConstants.h','const int MaxEntityId = XsltTransformEntityId;','const int ProjectVariableEntityId = 35; const int MaxEntityId = ProjectVariableEntityId;')
edit(n+'DbRelations/DatabaseTableNameMappings.h','extern const std::string nameXsltTransforms;','extern const std::string nameProjectVariables; extern const std::string nameXsltTransforms;')
edit(n+'DbRelations/DatabaseTableNameMappings.cpp','const string nameXsltTransforms =','const string nameProjectVariables = "ProjectVariables"; const string nameXsltTransforms =')
edit(n+'DbRelations/DatabaseTableNameMappings.cpp','case XsltTransformEntityId: return nameXsltTransforms;','case ProjectVariableEntityId: return nameProjectVariables; case XsltTransformEntityId: return nameXsltTransforms;')
edit(n+'DbRelations/DatabaseTableNameMappings.cpp','if ( stName == nameXsltTransforms )','if ( stName == nameProjectVariables ) return ProjectVariableEntityId; if ( stName == nameXsltTransforms )')
edit(n+'DbRelations/DbRelations.cpp','    pimpl->m_nameColumnMap[getTableName(nameXsltTransforms)] = "xslttransform_name";','''    pimpl->m_nameColumnMap[getTableName(nameXsltTransforms)] = "xslttransform_name";
    {
        vector<KeyInfo> keys;
        keys.push_back(KeyInfo(getIdName(nameProjectVariables), GetEntityIdByName(nameProjectVariables)));
        pimpl->m_tableKeyInfoMap[getTableName(nameProjectVariables)] = new TableKeyInfo(keys);
        pimpl->m_nameColumnMap[getTableName(nameProjectVariables)] = "projectvariable_name";
        keys.clear();
        keys.push_back(KeyInfo(getIdName(nameProjectVariables), getTableName(nameProjectVariables), true));
        pimpl->m_tableKeyInfoMap[GetPropertyTableName(ProjectVariableEntityId)] = new TableKeyInfo(keys);
    }''')
path=n+'UiBackend/WebServices/DeployInterchangeData/Impl/HandleChangedDataImpl.cpp'
edit(path,'\t    InterchangeDataTableStruct xsltAssets;','''    InterchangeDataTableStruct variables;
    variables.m_vColumnNames.push_back("projectvariable_id");
    variables.m_vColumnNames.push_back("projectvariable_name");
    variables.m_vColumnNames.push_back("default_value");
    for (std::map<std::string, ExtendedItemProperties*>::const_iterator it = itemPropertiesCollection.m_PropertiesMap.begin();
         it != itemPropertiesCollection.m_PropertiesMap.end(); ++it) {
        if (it->second->GetEntityTypeId() != ProjectVariableEntityId) continue;
        if (it->second->m_properties.GetString("__deleted", "") == "1") {
            const long id = m_pGuidToIdMapper->GetId(it->first, ProjectVariableEntityId);
            if (id != GuidToIdMapper::invalid_id) DeleteFromTable(nameProjectVariables, std::vector<long>(1, id));
            continue;
        }
        const std::string name = it->second->m_properties.GetString("projectvariable_name", "");
        const std::string value = it->second->m_properties.GetString("default_value", "");
        etlforge::validateProjectVariable(name, value);
        InterchangeDataRowStruct row;
        row.m_vFields.push_back(ClientField(it->first.c_str()));
        row.m_vFields.push_back(ClientField(name.c_str()));
        row.m_vFields.push_back(ClientField(value.c_str()));
        variables.m_vRows.push_back(row);
    }
    if (!variables.m_vRows.empty()) {
        tableHandler.HandleTable(nameProjectVariables, variables, &defaultInsertSpecialHandling, &defaultUpdateSpecialHandling);
        etlforge::validateProjectVariableNames(*m_pTranDb, lInterchangeProjectId);
    }
    InterchangeDataTableStruct xsltAssets;''')
edit(path,'#include "ExtendedItemProperties.h"','#include <konga/OperationContext/ProjectVariableDefaults.h>\n#include "ExtendedItemProperties.h"')
edit(n+'UiBackend/WebServices/DeployInterchangeData/Impl/HandleExtendedProperties.cpp','properties->GetEntityTypeId() == XsltTransformEntityId &&','(properties->GetEntityTypeId() == XsltTransformEntityId || properties->GetEntityTypeId() == ProjectVariableEntityId) &&')
write(n+'OperationContext/ProjectVariableDefaults.h','''#ifndef ETLFORGE_PROJECT_VARIABLE_DEFAULTS_H
#define ETLFORGE_PROJECT_VARIABLE_DEFAULTS_H
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/DataElement/DataElement.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <regex>
#include <set>
namespace etlforge {
inline void validateProjectVariable(const std::string& name, const std::string& value) {
    if (!std::regex_match(name, std::regex("[A-Za-z_][A-Za-z0-9_.]{0,255}")) || name.find("__")==0
        || konga::EqualsIgnoreCase(name.substr(0,9), "etlforge.") || konga::EqualsIgnoreCase(name.substr(0,9), "jitterbit."))
        throw konga::CrException("Invalid project variable name: " + name);
    if (value.size() > 1048576 || value.find('\\0') != std::string::npos)
        throw konga::CrException("Project variable values must be text of at most 1 MiB without NUL.");
}
inline void validateProjectVariableNames(konga::DbConnection& db, long project) {
    konga::SmartDbPtr<konga::DynamicDbRecordSet> rows(konga::DynamicDbRecordSet::MakeInstance(&db,
        "SELECT v.projectvariable_name FROM projectvariablestab v JOIN interchangeprojectprojectvariablestab p ON p.item_id=v.projectvariable_id WHERE p.interchangeproject_id="
        + konga::toString(project) + " GROUP BY v.projectvariable_name HAVING count(*)>1"));
    rows->Open();
    if (rows->Next()) throw konga::CrException("Duplicate project variable name: " + rows->GetFieldValue(0));
}
inline void loadProjectVariableDefaults(konga::DbConnection& db, long operation, konga::OperationDataElementHolder& holder) {
    konga::SmartDbPtr<konga::DynamicDbRecordSet> rows(konga::DynamicDbRecordSet::MakeInstance(&db,
        "SELECT DISTINCT v.projectvariable_name,v.default_value FROM projectvariablestab v JOIN interchangeprojectprojectvariablestab p ON p.item_id=v.projectvariable_id "
        "JOIN interchangeprojectoperationstab o ON o.interchangeproject_id=p.interchangeproject_id WHERE o.item_id=" + konga::toString(operation) + " ORDER BY v.projectvariable_name"));
    rows->Open();
    std::set<std::string> seen;
    while (rows->Next()) {
        const std::string name=rows->GetFieldValue(0), value=rows->GetFieldValue(1);
        validateProjectVariable(name,value);
        if (!seen.insert(name).second) throw konga::CrException("Ambiguous project variable name: " + name);
        if (holder.isOperationDataElementDefined(name)) continue;
        konga::DataElement* variable=holder.getOperationDataElement(name);
        variable->setType(konga::DataElement::t_string);
        variable->setValue(value.c_str());
    }
}
}
#endif
''')
edit(n+'OperationEngine/OperationEngine.cpp','#include "OperationScriptRunner.h"','#include <konga/OperationContext/ProjectVariableDefaults.h>\n#include "OperationScriptRunner.h"')
edit(n+'OperationEngine/OperationEngine.cpp','\tinitContext(pDeHolder);\n\tGetOperationDataElements(lSessionId);','\tinitContext(pDeHolder);\n\tetlforge::loadProjectVariableDefaults(GetDbConnection(), lOperationId, *m_pDeHolder);\n\tGetOperationDataElements(lSessionId);')
edit(n+'OperationEngine/OperationEngine.cpp','\tinitContext(0);\n\n\tauto_ptr<Operation>','\tinitContext(0);\n\tetlforge::loadProjectVariableDefaults(GetDbConnection(), lOperationId, *m_pDeHolder);\n\n\tauto_ptr<Operation>')
schema=(root/'Server/database/migrations/004-xslt-assets.sql').read_text().replace('xslttransform','projectvariable').replace('xslt text NOT NULL','default_value text NOT NULL DEFAULT \'\'')
write('Server/database/migrations/010-project-variables.sql',schema)
edit('Server/src/native/audit/AuditDeployment.cpp','plan(34,"XSLT","xslttransform","xslttransforms")','plan(34,"XSLT","xslttransform","xslttransforms"),plan(35,"Project Variable","projectvariable","projectvariables")')
print('Project variables implementation written; originals archived at '+str(archive))
