package org.etlforge.integration.client.ui.admin.component.controlpanel;

import com.google.common.collect.ImmutableList;
import javax.swing.Action;
import org.etlforge.integration.client.ui.admin.AdministrationView;
import org.etlforge.integration.client.ui.admin.core.console.ConsoleLauncherFactory;

public final class ControlPanelFactory {
   private final AdministrationView view;
   private final ImmutableList<Action> consoleActions;
   private final ImmutableList<Action> otherActions;

   public ControlPanelFactory(AdministrationView var1, Iterable<Action> var2, Iterable<Action> var3) {
      this.view = var1;
      this.consoleActions = ImmutableList.copyOf(var2);
      this.otherActions = ImmutableList.copyOf(var3);
   }

   public ControlPanel createPanel() {
      ConsoleLauncherFactory var1 = new ConsoleLauncherFactory(this.consoleActions);
      Iterable var2 = var1.getLaunchers();
      return new ControlPanel(this.view, var2, this.otherActions);
   }
}
