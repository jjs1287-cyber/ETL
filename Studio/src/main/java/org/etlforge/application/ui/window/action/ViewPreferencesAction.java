package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.Application;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.prefs.ApplicationPreferences;
import org.etlforge.application.ui.prefs.PreferencesViewer;
import org.etlforge.application.ui.window.ApplicationWindow;

public final class ViewPreferencesAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.Preferences";
   private PreferencesViewer viewer;
   private String[] initialPath;
   private ApplicationPreferences preferences;

   public ViewPreferencesAction(ApplicationWindow var1) {
      super(var1, "action.Preferences", WindowActionDictionary.DICTIONARY);
   }

   public void setInitialPath(String[] var1) {
      this.initialPath = (String[])var1.clone();
   }

   @Override
   public void execute(ActionEvent var1) {
      this.setEnabled(false);
      (new BusyWorker(this.getWindow()) {
         @Override
         protected void doWork() {
            ViewPreferencesAction.this.displayPreferences();
         }
      }).run();
   }

   private void displayPreferences() {
      try {
         this.displayImpl();
      } finally {
         this.setEnabled(true);
      }
   }

   private synchronized void displayImpl() {
      if (this.viewer == null) {
         this.createViewer();
      }

      this.viewer.open(this.getWindow().getFrame());
   }

   private void createViewer() {
      ApplicationPreferences var1 = this.getPreferences();
      this.viewer = new PreferencesViewer(var1);
      if (this.initialPath != null) {
         this.viewer.setInitialPath(this.initialPath);
      }
   }

   private ApplicationPreferences getPreferences() {
      if (this.preferences == null) {
         this.preferences = Application.getBean("platform.Preferences", ApplicationPreferences.class);
      }

      return this.preferences;
   }
}
