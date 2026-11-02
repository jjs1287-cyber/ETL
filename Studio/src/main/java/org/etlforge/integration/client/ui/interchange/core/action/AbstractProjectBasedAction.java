package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.CurrentProjectServiceListener;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.Disposable;

public abstract class AbstractProjectBasedAction extends AbstractApplicationAction implements Disposable {
   private final CurrentProjectService currentProjectService;
   private IntegrationProject project;
   private final AbstractProjectBasedAction.Listener listener = new AbstractProjectBasedAction.Listener();
   private final Object lock = new Object();

   public AbstractProjectBasedAction(CurrentProjectService var1, String var2) {
      this(var1, var2, InterchangeActionDictionary.DICTIONARY);
   }

   public AbstractProjectBasedAction(CurrentProjectService var1, String var2, ActionDictionary var3) {
      super(var2, var3);
      this.currentProjectService = var1;
      var1.addListener(this.listener);
      this.setProject(var1.getProject());
   }

   @Override
   public void dispose() {
      super.dispose();
      this.currentProjectService.removeListener(this.listener);
   }

   protected final CurrentProjectService getCurrentProjectService() {
      return this.currentProjectService;
   }

   protected final IntegrationProject getProject() {
      synchronized (this.lock) {
         return this.project;
      }
   }

   protected final ManagedProject getManagedProject() {
      return this.currentProjectService.getManagedProject();
   }

   @Override
   public boolean isEnabled() {
      return super.isEnabled() && this.getProject() != null;
   }

   @Override
   public void setEnabled(boolean var1) {
      super.setEnabled(var1 && this.getProject() != null);
   }

   protected void updateEnabledState(IntegrationProject var1) {
      this.setEnabled(true);
   }

   protected void onProjectClosed(IntegrationProject var1) {
   }

   private void setProject(final IntegrationProject var1) {
      synchronized (this.lock) {
         this.project = var1;
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (var1 == null) {
               AbstractProjectBasedAction.this.setEnabled(false);
            } else {
               AbstractProjectBasedAction.this.updateEnabledState(var1);
            }
         }
      });
   }

   private class Listener extends CurrentProjectServiceListener.Adapter {
      private Listener() {
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         AbstractProjectBasedAction.this.setProject(var1.getProject());
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         AbstractProjectBasedAction.this.setProject(null);
         AbstractProjectBasedAction.this.onProjectClosed(var1.getProject());
      }
   }
}
