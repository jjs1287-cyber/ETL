package com.etlforge.integration.client.ui.sfdc.page;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.client.ui.operation.options.ChunkSizeAndThreadCountUi;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.options.ChunkingOption;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.undo.InputPanelUndoRedoEnabler;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class ChunkingSettingsEditor {
   private final Operation op;
   private final EntityPersistor persistor;
   private final Receiver<EntityPageSaveJob> handler;
   private ChunkSizeAndThreadCountUi ui;
   private OverlayContainer overlayContainer;
   private ChunkingOption currentSettings;
   private InputPanel inputPanel;
   private InputPanelUndoRedoEnabler undoRedo;
   private InputChangeListener validationListener;

   public ChunkingSettingsEditor(Operation var1, EntityPersistor var2, Receiver<EntityPageSaveJob> var3) {
      this.op = var1;
      this.persistor = var2;
      this.handler = var3;
      this.currentSettings = var1.getOperationOptions().getChunkingOption();
   }

   public void edit() {
      this.inputPanel = new InputPanel();
      this.ui = new ChunkSizeAndThreadCountUi(this.currentSettings);
      this.ui.setChunkSizeInterval(1, 200);
      this.ui.registerAsChangeSource(this.inputPanel);
      this.inputPanel.setSendInputChangeEvents(true);
      this.inputPanel.setSendUndoEvents(true);
      if (this.overlayContainer != null) {
         this.openInOverlay();
      } else {
         this.openInDialog();
      }
   }

   public void setOverlayContainer(OverlayContainer var1) {
      this.overlayContainer = var1;
   }

   private void openInOverlay() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.ui);
      var1.setBorder(Empty.border(5));
      this.undoRedo = new InputPanelUndoRedoEnabler(this.inputPanel, var1);
      final OverlayDecorator var2 = new OverlayDecorator(var1, "Chunking Settings") {
         @Override
         protected void onOk() {
            ChunkingSettingsEditor.this.applyNewOptions();
         }

         @Override
         protected void onHidden() {
            ChunkingSettingsEditor.this.disposeInputPanel();
         }
      };
      this.validationListener = new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            boolean var2x = ChunkingSettingsEditor.this.inputPanel.isInputValid();
            var2.getOkAction().setEnabled(var2x);
         }
      };
      this.inputPanel.addInputChangeListener(this.validationListener);
      var2.setBlocking(true);
      var2.useOkAndCancel();
      var2.installOkAndCancelShortCuts();
      var2.setFocusReceiverWhenClosed(this.getCurrentFocusOwner());
      var2.show(this.overlayContainer, new OverlayCenterPositioner());
      this.ui.requestFocus();
   }

   private void disposeInputPanel() {
      this.undoRedo.uninstall();
      this.undoRedo = null;
      this.inputPanel.removeInputChangeListener(this.validationListener);
      this.inputPanel.dispose();
      this.inputPanel = null;
   }

   private JComponent getCurrentFocusOwner() {
      Component var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
      return var1 instanceof JComponent ? (JComponent)var1 : null;
   }

   private void applyNewOptions() {
      ChunkingOption var1 = this.ui.getDefinedOption();
      OperationOptions var2 = this.op.getOperationOptions();
      if (!var1.equals(var2.getChunkingOption())) {
         ChunkingOption var3 = this.currentSettings;
         this.currentSettings = var1;
         this.handler.handle(new ChunkingSettingsSaveJob(this.op, var3, var1, this.persistor, new Receiver<ChunkingOption>() {
            public void handle(ChunkingOption var1) {
               ChunkingSettingsEditor.this.currentSettings = var1;
            }
         }));
      }
   }

   private void openInDialog() {
      final KongaDialog var1 = this.createDialog();
      this.validationListener = new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1x) {
            boolean var2 = ChunkingSettingsEditor.this.inputPanel.isInputValid();
            var1.getOKButton().setEnabled(var2);
         }
      };
      this.inputPanel.addInputChangeListener(this.validationListener);
      var1.setVisible(true);
      if (var1.wasOkPressed()) {
         this.applyNewOptions();
      }

      this.disposeInputPanel();
   }

   private KongaDialog createDialog() {
      JComponent var1 = WizardStyleDialog.wrapUi(this.ui);
      WizardStyleDialog var2 = new WizardStyleDialog(ApplicationUi.getAppWinFrame(), "Chunking Settings", var1);
      var2.setFocusedComponent(this.ui);
      var2.setOkButtonAsDefault();
      var2.manageLocation(this.getClass());
      this.undoRedo = new InputPanelUndoRedoEnabler(this.inputPanel, (JComponent)var2.getContentPane());
      return var2;
   }
}
