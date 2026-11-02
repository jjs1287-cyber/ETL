package org.etlforge.integration.client.ui.de.global;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.ui.de.resources.DataElementResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.preference.KongaPreferences;

public final class GlobalDataElementsViewImpl extends AbstractWindowSectionContent implements GlobalDataElementsView {
   private static final String INCLUDE_ETLFORGE_VARIABLES_PREF = "IncludeETLForgeVars";
   private final GlobalDataElementsBrowser browser = new GlobalDataElementsBrowser();

   public GlobalDataElementsViewImpl(WindowSection var1) {
      super(CONTENT_ID, var1);
      this.addResource(new DisposableWindowElementResource(this.browser));
   }

   @Override
   public void decorate(JComponent var1) {
      this.doDefaultDecoration(var1, this.browser);
   }

   @Override
   public String getTitle() {
      return DataElementResources.getString("DataElement.View.Title");
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.GLOBAL_DATA_ELEMENT_16;
   }

   @Override
   public void setProject(IntegrationProject var1) {
      GlobalDataElements var2 = var1 != null ? var1.getGlobalDataElements() : null;
      this.browser.setGlobalDataElements(var2);
   }

   public void setExpressionSourceDisplayer(ExpressionSourceDisplayer var1) {
      this.browser.setExpressionSourceDisplayer(var1);
   }

   @Override
   protected JComponent getAdditionalTitleComponent() {
      GlobalDataElementsViewImpl.IncludeBuiltInDataElementsToggle var1 = new GlobalDataElementsViewImpl.IncludeBuiltInDataElementsToggle();
      JPanel var2 = InvisiblePanel.of(var1.checkBox);
      var2.setBorder(Empty.border(2, 2, 2, 0));
      return var2;
   }

   private class IncludeBuiltInDataElementsToggle {
      public final KongaCheckBox checkBox = new KongaCheckBox(DataElementResources.getString("DataElement.View.ETLForgeVars.Name"));

      public IncludeBuiltInDataElementsToggle() {
         this.checkBox.addSelectionChangeListener(new Receiver<Boolean>() {
            public void handle(Boolean var1) {
               GlobalDataElementsViewImpl.this.browser.setIncludeBuiltInDataElements(var1);
               IncludeBuiltInDataElementsToggle.this.updatePreference(var1);
            }
         });
         this.setInitialState();
      }

      private void updatePreference(boolean var1) {
         KongaPreferences var2 = this.getPreferences();
         var2.putBoolean("IncludeETLForgeVars", var1);
      }

      private KongaPreferences getPreferences() {
         return KongaPreferences.getPreferences(GlobalDataElementsView.class);
      }

      private void setInitialState() {
         boolean var1 = this.getPreferenceValue();
         this.checkBox.setSelected(var1);
      }

      private boolean getPreferenceValue() {
         KongaPreferences var1 = this.getPreferences();
         return var1.getBoolean("IncludeETLForgeVars", false);
      }
   }
}
