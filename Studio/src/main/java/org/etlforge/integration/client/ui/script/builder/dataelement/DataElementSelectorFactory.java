package org.etlforge.integration.client.ui.script.builder.dataelement;

import org.etlforge.integration.client.ui.de.global.DataElementsWidget;
import org.etlforge.integration.client.ui.de.global.GlobalDataElementsBrowser;
import org.etlforge.integration.client.ui.de.global.SingleDataElementsListWidget;
import org.etlforge.integration.client.ui.de.global.SplitDataElementsListWidget;
import org.etlforge.integration.client.ui.de.global.tree.DataElementsTreeWidget;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderModel;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSection;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSectionFactory;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreference;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DataElementSelectorFactory implements ScriptBuilderSectionFactory {
   private boolean includeBuiltInVariables = true;

   public void setIncludeBuiltInVariables(boolean var1) {
      this.includeBuiltInVariables = var1;
   }

   @Override
   public ScriptBuilderSection newSection(ScriptBuilderModel var1) {
      GlobalDataElementsBrowser var2 = this.createBrowser(var1);
      DefaultDataElementSelector var3 = new DefaultDataElementSelector(var2);
      var3.setAllowBuiltInVariablesToBeHidden(false);
      return var3;
   }

   private GlobalDataElementsBrowser createBrowser(ScriptBuilderModel var1) {
      DataElementsWidget var2 = this.createWidget();
      GlobalDataElementsBrowser var3 = new GlobalDataElementsBrowser(var2, false);
      IntegrationProject var4 = var1.getProject();
      var3.setGlobalDataElements(var4.getGlobalDataElements());
      return var3;
   }

   private DataElementsWidget createWidget() {
      LayoutType var1 = LayoutTypePreference.get();
      DataElementsWidget var2;
      if (var1 == LayoutType.TABS) {
         var2 = this.includeBuiltInVariables ? new SplitDataElementsListWidget() : new SingleDataElementsListWidget();
      } else {
         var2 = new DataElementsTreeWidget();
      }

      var2.setIncludeBuiltInDataElements(this.includeBuiltInVariables);
      return var2;
   }
}
