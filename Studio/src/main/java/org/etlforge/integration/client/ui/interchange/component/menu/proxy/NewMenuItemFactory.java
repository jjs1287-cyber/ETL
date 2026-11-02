package org.etlforge.integration.client.ui.interchange.component.menu.proxy;

import java.awt.EventQueue;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.etlforge.application.ui.menu.JMenuFactory;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.view.ViewManagerAdapter;
import org.etlforge.application.ui.view.ViewManagerEvent;
import org.etlforge.application.ui.view.menu.ViewMenuItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.CurrentProjectServiceListener;
import org.etlforge.integration.client.ui.interchange.InterchangeView;
import org.etlforge.integration.client.ui.interchange.component.menu.InterchangeMenuDictionary;
import org.etlforge.integration.client.ui.interchange.component.menu.NewEntityMenuFactory;
import org.etlforge.integration.data.project.IntegrationProject;

public final class NewMenuItemFactory implements ViewMenuItemFactory {
   private final CurrentProjectService currentProjectService;
   private final NewEntityMenuFactory menuFactory;

   public NewMenuItemFactory(CurrentProjectService var1, NewEntityMenuFactory var2) {
      this.currentProjectService = var1;
      this.menuFactory = var2;
   }

   @Override
   public JMenuItem createMenuItem(ApplicationWindow var1) {
      JMenu var2 = new JMenuFactory("File.New", InterchangeMenuDictionary.DICTIONARY).createEmptyMenu();
      var2.setEnabled(false);
      NewMenuItemFactory.Listener var3 = new NewMenuItemFactory.Listener(this.menuFactory, var2);
      this.currentProjectService.addListener(var3);
      var1.getViewManager().addViewManagerListener(var3);
      return var2;
   }

   private static class Listener extends ViewManagerAdapter implements CurrentProjectServiceListener {
      private final NewEntityMenuFactory menuFactory;
      private final JMenu menu;
      private boolean projectOpen;
      private boolean viewOpen;

      public Listener(NewEntityMenuFactory var1, JMenu var2) {
         this.menuFactory = var1;
         this.menu = var2;
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         this.setProjectOpen(false);
      }

      private void setProjectOpen(final boolean var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Listener.this.projectOpen = var1;
               Listener.this.update();
            }
         });
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         this.setProjectOpen(true);
      }

      private void update() {
         this.menu.setEnabled(this.viewOpen && this.projectOpen);
      }

      @Override
      public void viewIsDisplayed(ViewManagerEvent var1) {
         if (this.isInterchangeView(var1)) {
            this.setViewOpen(var1.getView(), true);
         }
      }

      private boolean isInterchangeView(ViewManagerEvent var1) {
         return "interchange".equals(var1.getView().getID());
      }

      private void setViewOpen(final View var1, final boolean var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Listener.this.viewOpen = var2;
               if (Listener.this.viewOpen && Listener.this.menu.getMenuComponentCount() == 0) {
                  Listener.this.populateMenu((InterchangeView)var1);
               }

               Listener.this.update();
            }
         });
      }

      private void populateMenu(InterchangeView var1) {
         for (JMenuItem var4 : this.menuFactory.createMenuItems(var1)) {
            if (var4 != null) {
               this.menu.add(var4);
            } else {
               this.menu.addSeparator();
            }
         }
      }

      @Override
      public void viewWasRemoved(ViewManagerEvent var1) {
         if (this.isInterchangeView(var1)) {
            this.setViewOpen(var1.getView(), false);
         }
      }

      @Override
      public void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException {
      }
   }
}
