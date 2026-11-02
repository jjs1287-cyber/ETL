package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.border.Border;
import org.etlforge.application.Application;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.job.JobMessageListener;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.option.InputOptionPanel;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.SelectedOptionListener;

public final class WsdlSelectorPanel implements UiProvider, Disposable {
   public static final String INPUT_CHANGED = "WsdlSelector#InputChanged";
   private final WsdlSelector[] definers;
   private final ExistingWsdlFileSelector existingWsdls;
   private final InputOptionPanel<WsdlSelector> inputPanel;
   private final List<ActionListener> actionListeners;
   private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);

   public WsdlSelectorPanel(FileStore var1) {
      this.actionListeners = new CopyOnWriteArrayList<>();
      this.existingWsdls = new ExistingWsdlFileSelector(var1);
      this.definers = new WsdlSelector[]{this.existingWsdls, new RemoteWsdlFileSelector(), new LocalWsdlFileSelector(var1)};
      this.inputPanel = this.createOptionsPanel();
      this.registerListeners();
   }

   private InputOptionPanel<WsdlSelector> createOptionsPanel() {
      WsdlOption[] var1 = new WsdlOption[]{
         new WsdlOption(this.definers[0], PackageResources.WsdlSelectorPanel.Choices.EXISTING),
         new WsdlOption(this.definers[1], PackageResources.WsdlSelectorPanel.Choices.URL),
         new WsdlOption(this.definers[2], PackageResources.WsdlSelectorPanel.Choices.LOCAL)
      };
      InputOptionPanel var2 = new InputOptionPanel<WsdlSelector>(var1) {
         @Override
         protected double getOptionWeightY(int var1) {
            return var1 == 0 ? 1.0 : 0.0;
         }
      };
      var2.setSelectedOption(var1[0]);
      return var2;
   }

   private void registerListeners() {
      WsdlSelectorPanel.Listener var1 = new WsdlSelectorPanel.Listener();

      for (WsdlSelector var5 : this.definers) {
         var5.addChangeListener(var1);
         var5.addActionListener(var1);
      }

      this.inputPanel.addSelectionListener(var1);
   }

   public void downloadExistingWsdls() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            WsdlSelectorPanel.this.existingWsdls.populate(true);
         }
      });
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      for (WsdlSelector var5 : this.definers) {
         var5.installUndoRedo(var1);
      }
   }

   public void addActionListener(ActionListener var1) {
      this.actionListeners.add(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.actionListeners.remove(var1);
   }

   public boolean inputIsValid() {
      WsdlSelector var1 = this.inputPanel.getValue();
      return var1 != null && var1.isValid();
   }

   public void addChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.addPropertyChangeListener("WsdlSelector#InputChanged", var1);
   }

   public void removeChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.removePropertyChangeListener("WsdlSelector#InputChanged", var1);
   }

   private void fireInputChanged() {
      this.propChangeSupport.firePropertyChange("WsdlSelector#InputChanged", "0", "1");
   }

   @Override
   public void dispose() {
      this.inputPanel.dispose();
   }

   public void processSelectedWsdlFile(WsdlProcessingCallback var1, JobMessageListener var2) {
      WsdlSelector var3 = this.inputPanel.getValue();
      WsdlProcessingJob var4 = var3 == null ? null : var3.getProcessingJob();
      if (var4 == null) {
         var1.processingFailed(new Exception("Illegal input."));
      } else {
         var4.setCallback(var1);
         var4.addJobMessageListener(var2);
         if (EventQueue.isDispatchThread()) {
            Application.getWorker().submitForParallel(var4);
         } else {
            var4.run();
         }
      }
   }

   public void setBorder(Border var1) {
      this.inputPanel.setBorder(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.inputPanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.inputPanel.requestFocus();
   }

   private final class Listener implements ActionListener, PropertyChangeListener, SelectedOptionListener {
      private Listener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         for (ActionListener var3 : WsdlSelectorPanel.this.actionListeners) {
            var3.actionPerformed(var1);
         }
      }

      @Override
      public void selectionChanged(Option[] var1) {
         WsdlSelectorPanel.this.fireInputChanged();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         WsdlSelectorPanel.this.fireInputChanged();
      }
   }
}
