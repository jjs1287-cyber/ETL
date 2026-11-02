package com.etlforge.integration.license.client.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.ViewPreferencesAction;

public final class ConfigurePreferencesAction extends AbstractAction {
   private final ApplicationWindow appWin;
   private final Action impl;

   public ConfigurePreferencesAction(ApplicationWindow var1) {
      this.appWin = var1;
      this.impl = this.createImpl();
      this.putValue("Name", "License Message &Preferences...");
   }

   private Action createImpl() {
      ViewPreferencesAction var1 = new ViewPreferencesAction(this.appWin);
      var1.putValue("AcceleratorKey", null);
      var1.setInitialPath(new String[]{"License"});
      return var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.impl.actionPerformed(var1);
   }
}
