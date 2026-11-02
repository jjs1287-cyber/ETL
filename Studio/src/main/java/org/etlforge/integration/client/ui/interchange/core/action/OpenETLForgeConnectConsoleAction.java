package org.etlforge.integration.client.ui.interchange.core.action;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.TopLevelEntityCategory;
import org.etlforge.integration.client.ui.interchange.connect.LaunchConnectDialog;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;

public final class OpenETLForgeConnectConsoleAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.ETLForgeConnectConsole";
   private final ApplicationWindow appWin;
   private final EntityConfigLookup configLookup;
   private final ImmutableList<TopLevelEntityCategory> categories;

   public OpenETLForgeConnectConsoleAction(
      ApplicationWindow var1, CurrentProjectService var2, EntityConfigLookup var3, Iterable<TopLevelEntityCategory> var4, Iterable<TopLevelEntityCategory> var5
   ) {
      super(var2, "action.ic.ETLForgeConnectConsole");
      this.appWin = var1;
      this.configLookup = var3;
      this.categories = new Builder<TopLevelEntityCategory>().addAll(var4).addAll(var5).build();
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenETLForgeConnectConsoleAction.this.openConsole();
         }
      });
   }

   private void openConsole() {
      LaunchConnectDialog var1 = new LaunchConnectDialog(this.configLookup, this.categories, "ETLForge Connect\u2122", ClientIcons.ETLFORGE_CONNECT_32);
      var1.open(this.appWin.getFrame());
   }

   @Override
   protected void invokedWhenDisabled() {
      if (this.getProject() == null) {
         Alert.error("Please open a project first.", "No Project Open");
      }
   }
}
