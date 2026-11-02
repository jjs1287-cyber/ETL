package org.etlforge.integration.client.ui.admin;

import javax.swing.Action;
import org.etlforge.application.Application;
import org.etlforge.application.ExitListener;
import org.etlforge.application.ui.view.DefaultEditorViewBase;
import org.etlforge.application.ui.window.AddContentWaitIndicator;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.admin.component.controlpanel.ControlPanel;
import org.etlforge.integration.client.ui.admin.component.controlpanel.ControlPanelFactory;

public final class AdministrationView extends DefaultEditorViewBase {
   public static final String VIEW_ID = "administration";
   private final ControlPanel controlPanel;
   private final ExitListener exitListener = new AdministrationView.ExitListenerImpl();
   private final Object lock = new Object();

   public AdministrationView(ApplicationWindow var1, Iterable<Action> var2, Iterable<Action> var3) {
      super(var1);
      this.controlPanel = new ControlPanelFactory(this, var2, var3).createPanel();
   }

   @Override
   public String getID() {
      return "administration";
   }

   @Override
   public void setActive(boolean var1) {
      synchronized (this.lock) {
         if (var1 != this.isActive()) {
            super.setActive(var1);
            if (var1) {
               this.getWindow().setTitleAppendix(PackageResources.TITLE_APPENDIX);
               this.getWindow().getWindowSection(ApplicationWindow.WEST).setActiveContent(this.controlPanel);
            }
         }
      }
   }

   @Override
   public boolean prepareForDisplay() {
      synchronized (this.lock) {
         this.initializeControlPanel();
         Application.addExitListener(this.exitListener);
         return true;
      }
   }

   private void initializeControlPanel() {
      WindowSection var1 = this.getWindow().getWindowSection(ApplicationWindow.WEST);
      this.controlPanel.setWindowSection(var1);
      var1.addContent(this.controlPanel, new AddContentWaitIndicator(this.getWindow()));
   }

   public void displayEditor(Editor var1) {
      this.getEditorService().openEditor(var1, "Administration");
   }

   @Override
   public boolean allowsRemoval() {
      synchronized (this.lock) {
         return this.closeAllPages();
      }
   }

   @Override
   public void prepareForRemoval() {
      synchronized (this.lock) {
         Application.removeExitListener(this.exitListener);
      }
   }

   private boolean closeAllPages() {
      return this.getEditorService().closeEditors("Administration");
   }

   private class ExitListenerImpl implements ExitListener {
      private ExitListenerImpl() {
      }

      @Override
      public boolean isExitAllowed() {
         synchronized (AdministrationView.this.lock) {
            return AdministrationView.this.closeAllPages();
         }
      }

      @Override
      public void performExitWork() {
      }
   }
}
