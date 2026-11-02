package org.etlforge.integration.client.ui.entity;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.ui.action.ToggleAction;

public class DecorateDeployDirtyAction extends AbstractAction implements ToggleAction {
   private final DeployDirtyIndicator indicator;
   private BooleanPreference preference;

   public DecorateDeployDirtyAction(DeployDirtyIndicator var1) {
      super(getString("DecorateDeployDirtyAction.Name"));
      Preconditions.checkNotNull(var1, "indicator");
      this.indicator = var1;
      this.putValue("ShortDescription", getString("DecorateDeployDirtyAction.ToolTip"));
   }

   @Override
   public boolean isSelected() {
      return this.indicator.isDecoratingDeployDirtyItems();
   }

   @Override
   public void setSelected(boolean var1) {
      this.indicator.setDecorateDeployDirtyItems(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      boolean var2 = !this.isSelected();
      this.setSelected(var2);
      if (this.preference != null) {
         this.preference.set(var2);
      }
   }

   public void setPreference(BooleanPreference var1) {
      this.preference = var1;
      this.setSelected(var1.get());
   }

   private static String getString(String var0) {
      return EntityResources.STRINGS.getString(var0);
   }
}
