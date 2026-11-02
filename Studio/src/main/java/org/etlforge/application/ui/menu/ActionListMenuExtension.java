package org.etlforge.application.ui.menu;

import java.util.List;
import javax.swing.Action;

public class ActionListMenuExtension extends AbstractMenuExtension {
   private final List<? extends Action> actions;
   private final MenuExtensionPoint extensionPoint;
   private final String contributionId;

   public ActionListMenuExtension(String var1, List<? extends Action> var2, MenuExtensionPoint var3, String var4) {
      super(var1);
      this.actions = var2;
      this.extensionPoint = var3;
      this.contributionId = var4;
   }

   @Override
   public final MenuContribution createExtension(MenuExtensionPoint var1) {
      if (var1 != this.extensionPoint) {
         return null;
      }

      MenuContribution var2 = new MenuContribution(this.contributionId);
      this.addActions(var2, this.actions);
      return var2;
   }
}
