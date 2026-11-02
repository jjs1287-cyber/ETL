from pathlib import Path
import shutil
root=Path('C:/ETL')
backup=root/'Archives/global-variable-endpoint-20261029'
def write(relative,text):
 p=root/relative
 if p.exists():
  b=backup/relative;b.parent.mkdir(parents=True,exist_ok=True)
  if not b.exists():shutil.copy2(p,b)
 p.parent.mkdir(parents=True,exist_ok=True);p.write_text(text,encoding='utf-8')
def edit(relative,old,new):
 p=root/relative;s=p.read_text(encoding='utf-8');assert old in s,relative;write(relative,s.replace(old,new))
j='Studio/src/main/java/org/etlforge/integration/'
n='Server/src/native/integration/konga/'
edit(j+'data/location/DataLocationType.java','   LocalFile(16, LocalFileLocation.class),','   LocalFile(16, LocalFileLocation.class),\n   GlobalVariable(17, GlobalVariableLocation.class),')
edit(j+'data/location/DataLocationType.java','         case TemporaryStorage:\n            return "Temporary Storage";','         case GlobalVariable:\n            return "Global Variable";\n         case TemporaryStorage:\n            return "Temporary Storage";')
edit(j+'data/location/DataLocationType.java','         case FTP:\n         case LocalFile:','         case GlobalVariable:\n         case FTP:\n         case LocalFile:')
write(j+'data/location/GlobalVariableLocation.java','''package org.etlforge.integration.data.location;
import com.google.common.collect.ImmutableList;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
public final class GlobalVariableLocation extends FileLocation {
   public GlobalVariableLocation() { super(DataLocationType.GlobalVariable); }
   public static String[] getPropertyKeys() { return org.etlforge.lang.Arrays2.append(FileLocation.getPropertyKeys(), new String[]{"locator"}); }
   public String getVariableName() { return getProperty("locator", ""); }
   public void setVariableName(String value) { setStringProperty("locator", value.trim()); }
   public static boolean isValidVariableName(String value) { return value != null && value.matches("\\\\$?[A-Za-z_][A-Za-z0-9_.]{0,255}") && !value.replace("$", "").startsWith("__"); }
   @Override public void collectValidationMessages(ValidationMessageCollector messages) {
      super.collectValidationMessages(messages);
      if (!isValidVariableName(getVariableName())) messages.addMessage("Enter a global variable name, for example Payload or $Payload.", ValidationStatus.INVALID);
   }
   @Override public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(new ExportableStringProperty(this, "locator", "Global variable", false));
   }
}
''')
write(j+'client/ui/interchange/location/panel/GlobalVariableLocationPanel.java','''package org.etlforge.integration.client.ui.interchange.location.panel;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.location.local.FolderPathField;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.GlobalVariableLocation;
import org.etlforge.ui.Size;
import org.etlforge.ui.layout.OneColumnPanel;
public final class GlobalVariableLocationPanel extends DataLocationPanel {
   private final FolderPathField variable;
   public GlobalVariableLocationPanel(GlobalVariableLocation location, SourceTarget direction) {
      super(location, direction);
      variable = new FolderPathField("Global variable", "Payload or $Payload", Size.columns(40));
      variable.setRequired(true);
      addInputField(variable, variable, variable);
      reset();
   }
   @Override public void apply() { ((GlobalVariableLocation)getDisplayedObject()).setVariableName(variable.getValue()); }
   @Override public void reset() { variable.setValue(((GlobalVariableLocation)getDisplayedObject()).getVariableName()); }
   @Override public void display() {
      super.display();
      OneColumnPanel panel = new OneColumnPanel(); panel.withInsets(3,5,10,10);
      panel.add(InputFieldFactory.getCapsule(variable));
      panel.add(new JLabel(getSourceTarget() == SourceTarget.Source ? "Reads the variable's UTF-8 text. The variable must be set before use." : "Replaces the variable with the output text. Multiple output files are rejected."));
      wrap(panel);
   }
   @Override public void setInitialFocus() { variable.requestFocus(); }
}
''')
edit(j+'client/ui/interchange/location/panel/DataLocationPanelFactory.java','            case Database:','            case GlobalVariable:\n               return new GlobalVariableLocationPanel((org.etlforge.integration.data.location.GlobalVariableLocation)var1, this.type);\n            case Database:')
edit(j+'client/ui/interchange/location/search/LocationProperties.java','               case LDAP:','               case GlobalVariable:\n               case LDAP:')
edit(j+'client/ui/interchange/location/search/LocationSearchValuesCollector.java','            case FileShare:','            case GlobalVariable:\n               return Lists.newArrayList(((org.etlforge.integration.data.location.GlobalVariableLocation)var1).getVariableName());\n            case FileShare:')
edit(j+'client/ui/interchange/location/LocationConnectionTester.java','         case Database:','         case GlobalVariable:\n            return new GlobalVariableConnectionTester((org.etlforge.integration.data.location.GlobalVariableLocation)var0);\n         case Database:')
write(j+'client/ui/interchange/location/GlobalVariableConnectionTester.java','''package org.etlforge.integration.client.ui.interchange.location;
import org.etlforge.integration.data.location.GlobalVariableLocation;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.test.*;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.*;
public final class GlobalVariableConnectionTester extends LocationConnectionTester<GlobalVariableLocation> {
   public GlobalVariableConnectionTester(GlobalVariableLocation location) { super(location); }
   private final class Result extends ResultProducingServerCallback<TestConnectionResult> {
      Result(IntegrationEntity entity) {
         if (GlobalVariableLocation.isValidVariableName(location.getVariableName())) set(TestConnectionResult.success(entity));
         else caught(new IllegalArgumentException("Invalid global variable name. Runtime values can only be checked inside an operation."));
      }
   }
   @Override protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Source entity, TestSourceConnectionApi api) { return new Result(entity); }
   @Override protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Target entity, TestTargetConnectionApi api) { return new Result(entity); }
}
''')
edit(n+'DbRelations/SourceTypeIdConstants.h','const int LocalFileSourceTypeId = 16;','const int LocalFileSourceTypeId = 16;\n\tconst int GlobalVariableSourceTypeId = 17;')
for f in ['OperationObjectCreatorKeyValues.cpp','LocationCreatorDb.cpp']:
 edit(n+'OperationObjects/'+f,'#include "TemporaryStorageSource.h"','#include "TemporaryStorageSource.h"\n#include "GlobalVariableLocation.h"')
edit(n+'OperationObjects/LocationCreatorDb.cpp','\t\tcase TemporaryStorageSourceTypeId:\n\t\tcase LocalFileSourceTypeId:\n\t\t\tpSourceDataLocation.reset', '\t\tcase GlobalVariableSourceTypeId:\n\t\t\tpSourceDataLocation.reset(new GlobalVariableSource());\n\t\t\tbreak;\n\t\tcase TemporaryStorageSourceTypeId:\n\t\tcase LocalFileSourceTypeId:\n\t\t\tpSourceDataLocation.reset')
edit(n+'OperationObjects/LocationCreatorDb.cpp','\t\tcase TemporaryStorageSourceTypeId:\n\t\tcase LocalFileSourceTypeId:\n\t\t\tpTargetDataLocation.reset', '\t\tcase GlobalVariableSourceTypeId:\n\t\t\tpTargetDataLocation.reset(new GlobalVariableTarget());\n\t\t\tbreak;\n\t\tcase TemporaryStorageSourceTypeId:\n\t\tcase LocalFileSourceTypeId:\n\t\t\tpTargetDataLocation.reset')
for direction,base in [('Source','Source'),('Target','Target')]:
 marker='\t\tcase TemporaryStorageSourceTypeId:'
 p=n+'OperationObjects/OperationObjectCreatorKeyValues.cpp';s=(root/p).read_text(encoding='utf-8');start=s.index(marker) if direction=='Source' else s.index(marker,s.index(marker)+1)
 var='p'+direction+'DataLocation';adopt='adopt_p'+direction
 addition='\t\tcase GlobalVariableSourceTypeId:\n\t\t\t{\n\t\t\t\tGlobalVariable'+direction+'* p = new GlobalVariable'+direction+'();\n\t\t\t\t'+adopt+'.reset(p);\n\t\t\t\t'+var+' = p;\n\t\t\t\tpDataLocation = p;\n\t\t\t}\n\t\t\tbreak;\n\n'
 write(p,s[:start]+addition+s[start:])
