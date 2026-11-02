package org.etlforge.integration.client.ui.entity.dependencies.graph;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.Disposable;

final class BackAndForthNavigator implements Disposable {
   private final ArrayList<IntegrationEntity> stack = new ArrayList<>();
   private int currentIndex;
   private final DependencyGraph graph;
   private boolean internalChange;
   private final BackAndForthNavigator.Back back = new BackAndForthNavigator.Back();
   private final BackAndForthNavigator.Forward forward = new BackAndForthNavigator.Forward();
   private final BackAndForthNavigator.ChangeListener listener = new BackAndForthNavigator.ChangeListener();

   public BackAndForthNavigator(DependencyGraph var1) {
      this.graph = var1;
      this.stack.add(var1.getModel().getEntity());
      var1.addPropertyChangeListener("model", this.listener);
   }

   public Action getBackAction() {
      return this.back;
   }

   public Action getForwardAction() {
      return this.forward;
   }

   @Override
   public void dispose() {
      this.graph.removePropertyChangeListener("model", this.listener);
   }

   private void updateActions() {
      this.back.setEnabled(this.currentIndex > 0);
      this.forward.setEnabled(this.currentIndex < this.stack.size() - 1);
   }

   private void navigateTo(int var1) {
      this.internalChange = true;
      IntegrationEntity var2 = this.stack.get(var1);
      this.graph.showDependenciesFor(var2);
      this.currentIndex = var1;
      this.updateActions();
      this.internalChange = false;
   }

   private class Back extends AbstractAction {
      public Back() {
         super(null, ApplicationResources.getIcon("PreviousEditor.16"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               BackAndForthNavigator.this.navigateTo(BackAndForthNavigator.this.currentIndex - 1);
            }
         });
      }
   }

   private class ChangeListener implements PropertyChangeListener {
      private ChangeListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!BackAndForthNavigator.this.internalChange) {
            DependencyGraphModel var2 = (DependencyGraphModel)var1.getNewValue();
            IntegrationEntity var3 = var2.getEntity();

            for (int var4 = BackAndForthNavigator.this.stack.size() - 1; var4 > BackAndForthNavigator.this.currentIndex; var4--) {
               BackAndForthNavigator.this.stack.remove(var4);
            }

            BackAndForthNavigator.this.stack.add(var3);
            BackAndForthNavigator.this.currentIndex = BackAndForthNavigator.this.stack.size() - 1;
            BackAndForthNavigator.this.updateActions();
         }
      }
   }

   private class Forward extends AbstractAction {
      public Forward() {
         super(null, ApplicationResources.getIcon("NextEditor.16"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               BackAndForthNavigator.this.navigateTo(BackAndForthNavigator.this.currentIndex + 1);
            }
         });
      }
   }
}
