package org.etlforge.integration.client.ui.script.debug;

import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.script.resources.ScriptResources;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.DebuggingParameters;
import org.etlforge.integration.debug.client.DebuggingParametersBuilder;
import org.etlforge.integration.debug.client.DebuggingState;
import org.etlforge.integration.debug.client.event.DebugServiceEvent;
import org.etlforge.integration.debug.client.event.DebugServiceListener;
import org.etlforge.integration.debug.client.event.DebugSessionEvent;
import org.etlforge.integration.debug.client.event.DebugSessionListener;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class DebugConsole implements UiProvider, Disposable {
   private final ScriptDebugModel model;
   private final Action startAction;
   private final List<JButton> buttons;
   private final DebuggeeNameLabel nameLabel = new DebuggeeNameLabel();
   private final JCheckBox disableBreakpointsCheck;
   private final DebugServiceListener debugServiceListener = new DebugConsole.ServiceListener();
   private final DebugSessionListener sessionListener = new DebugConsole.SessionListener();
   private final PropertyChangeListener modelListener = new DebugConsole.ModelListener();
   private final JPanel ui = new JPanel();
   private volatile IntegrationEntityLookup itemLookup;

   public DebugConsole(ScriptDebugModel var1, Iterable<Action> var2) {
      this.model = var1;
      this.startAction = new StartDebuggingAction(var1);
      this.buttons = this.createButtons(var2);
      this.disableBreakpointsCheck = this.createDisableBreakpoints();
      var1.addPropertyChangeListener(this.modelListener);
      this.addDebugListeners();
      this.updateStartAction(!var1.getDebugService().isDebugging());
      this.updateNameLabel();
      this.bindActionShortCuts(this.ui, var2);
   }

   private List<JButton> createButtons(Iterable<Action> var1) {
      ArrayList var2 = Lists.newArrayList(toolbarButton(this.startAction));

      for (Action var4 : var1) {
         var2.add(toolbarButton(var4));
      }

      return var2;
   }

   private static JButton toolbarButton(Action var0) {
      return KongaButton.forToolbar(var0).setUseSmallActionIcon(true);
   }

   private JCheckBox createDisableBreakpoints() {
      KongaCheckBox var1 = new KongaCheckBox(ScriptResources.getString("DebugConsole.DisableBreakpoints.Name"), this.model.getDisableBreakpoints());
      var1.addSelectionChangeListener(new DebugConsole.DisableBreakPointListener());
      return var1;
   }

   private void addDebugListeners() {
      DebugService var1 = this.model.getDebugService();
      synchronized (var1) {
         var1.addListener(this.debugServiceListener);
         DebugSession var3 = var1.getCurrentSession();
         if (var3 != null) {
            var3.addListener(this.sessionListener);
         }
      }
   }

   private void bindActionShortCuts(JComponent var1, Iterable<Action> var2) {
      KeyBindingInstaller var3 = new KeyBindingInstaller(var1, 2);

      for (Action var5 : var2) {
         if (var5.getValue("AcceleratorKey") != null) {
            var3.addBinding(null, var5.getValue("Name"), var5);
         }
      }

      var3.install();
   }

   public ScriptDebugModel getModel() {
      return this.model;
   }

   public void setItemLookup(IntegrationEntityLookup var1) {
      if (var1 != this.itemLookup) {
         this.itemLookup = var1;
         this.updateNameLabel();
      }
   }

   @Override
   public void dispose() {
      this.model.removePropertyChangeListener(this.modelListener);
      DebugService var1 = this.model.getDebugService();
      synchronized (var1) {
         var1.removeListener(this.debugServiceListener);
         DebugSession var3 = var1.getCurrentSession();
         if (var3 != null) {
            var3.removeListener(this.sessionListener);
         }
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal(this.ui);
      var2.addAllWithSpace(5, this.buttons);
      if (this.model.areBreakpointsSupported()) {
         var2.rigid(10).add(this.disableBreakpointsCheck).rigid(20);
      }

      this.nameLabel.layout(var2);
      var2.withBorder(new BorderBuilder().etched().empty(3)).decorate(var1);
   }

   public void startTest() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (DebugConsole.this.startAction.isEnabled()) {
               ActionUtils.run(DebugConsole.this.startAction, DebugConsole.this, "run");
            }
         }
      });
   }

   @Override
   public void requestFocus() {
      this.buttons.get(0).requestFocusInWindow();
   }

   private void updateStartAction(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DebugConsole.this.startAction.setEnabled(var1);
         }
      });
   }

   private void updateNameLabel() {
      this.nameLabel.update(this.model, this.itemLookup);
   }

   private void updateDisableBreakpointsEnabled(DebugSession var1) {
      final boolean var2 = var1.getState() != DebuggingState.RUNNING;
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DebugConsole.this.disableBreakpointsCheck.setEnabled(var2);
         }
      });
   }

   private class DisableBreakPointListener implements Receiver<Boolean> {
      private DisableBreakPointListener() {
      }

      public void handle(Boolean var1) {
         DebugConsole.this.model.setDisableBreakpoints(var1);
         DebugService var2 = DebugConsole.this.model.getDebugService();
         synchronized (var2) {
            if (var2.getState() == DebuggingState.PAUSED) {
               this.updateCurrentSession(var2, var1);
            }
         }
      }

      private void updateCurrentSession(DebugService var1, boolean var2) {
         DebuggingParameters var3 = var1.getCurrentSession().getParams();
         DebuggingParametersBuilder var4 = new DebuggingParametersBuilder(var3);
         var4.setObserveBreakpoints(!var2);
         var1.getCurrentSession().setParams(var4.build());
      }
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("isStartEnabled".equals(var2)) {
            ActionUtils.setEnabledSafely(DebugConsole.this.startAction, DebugConsole.this.model.isStartTestEnabled());
         }
      }
   }

   private class ServiceListener implements DebugServiceListener {
      private ServiceListener() {
      }

      @Override
      public void newDebugSessionStarted(DebugServiceEvent var1) {
         DebugConsole.this.updateStartAction(false);
         var1.getSession().addListener(DebugConsole.this.sessionListener);
         DebugConsole.this.updateNameLabel();
         DebugConsole.this.updateDisableBreakpointsEnabled(var1.getSession());
      }

      @Override
      public void debugSessionEnded(DebugServiceEvent var1) {
         DebugConsole.this.updateStartAction(true);
         var1.getSession().removeListener(DebugConsole.this.sessionListener);
         DebugConsole.this.updateNameLabel();
      }
   }

   private class SessionListener implements DebugSessionListener {
      private SessionListener() {
      }

      @Override
      public void sessionStateChanged(DebugSessionEvent var1) {
         synchronized (DebugConsole.this.model.getDebugService()) {
            DebugConsole.this.updateDisableBreakpointsEnabled(var1.getSource());
            DebugConsole.this.updateNameLabel();
         }
      }

      @Override
      public void sessionParametersChanged(DebugSessionEvent var1) {
         boolean var2 = !var1.getSource().getParams().isObservingBreakpoints();
         this.setDisableBreakpointsCheckSelected(var2);
      }

      private void setDisableBreakpointsCheckSelected(final boolean var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               DebugConsole.this.disableBreakpointsCheck.setSelected(var1);
            }
         });
      }
   }
}
