package org.etlforge.integration.client.ui.interchange.location.db;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheService;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.database.DatabaseDriverServerCache;
import org.etlforge.integration.client.database.ui.drivers.DialogDriverSelector;
import org.etlforge.integration.client.database.ui.drivers.DriverDescriptorUi;
import org.etlforge.integration.client.database.ui.drivers.DriverSelector;
import org.etlforge.integration.client.database.ui.drivers.OverlayDriverSelector;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DefaultActionHandler;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.text.TextFieldLabelAdapter;

final class DriverSelectorPart implements DatabaseLocationPanelPart {
   private static final boolean USE_OVERLAY = ApplicationConfiguration.getBoolean("DriverSelector.Overlay", false);
   private final InputPanel parent;
   private DriverDescriptor driver;
   private final DriverSelectorPart.DriverInputField driverField;
   private final Label driverFieldUi;
   private final JButton setDriverButton;
   private Receiver<DriverDescriptor> selectionListener;
   private final DriverSelectorPart.SelectDriverAction selectDriverAction;

   public DriverSelectorPart(InputPanel var1) {
      this.parent = var1;
      this.selectionListener = NoReceiver.create();
      this.selectDriverAction = new DriverSelectorPart.SelectDriverAction();
      this.driverField = this.createDriverField(var1);
      this.driverFieldUi = this.createDriverFieldUi();
      this.setDriverButton = new KongaButton(this.selectDriverAction);
      ((KongaLabel)this.driverField.getLabel()).setLabelForButton(this.setDriverButton);
   }

   public void setServerManager(ServerManager var1) {
      this.selectDriverAction.setServerManager(var1);
   }

   public void setSelectionListener(Receiver<DriverDescriptor> var1) {
      this.selectionListener = var1;
   }

   public void display() {
      this.driverField.setSendUndoEvents(false);
   }

   public void requestFocus() {
      this.setDriverButton.requestFocusInWindow();
   }

   @Override
   public void addToLayout(DatabaseLocationPanelLayout var1) {
      var1.add(this.createLayoutComponent());
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(this.driverField.getLabel(), this.driverField.getInputComponent(), this.setDriverButton);
   }

   public void applyTo(DatabaseLocation var1) {
      var1.setDriver(this.driver);
   }

   public boolean hasDriverSelection() {
      return this.driver != null;
   }

   public void reset(DatabaseLocation var1) {
      this.driver = var1.getDriver();
      this.decorateDriverField(this.driver);
   }

   @Override
   public void dispose() {
      this.driverField.dispose();
   }

   public boolean isOracleDriver() {
      return this.driver != null && this.driver.isProbablyOracle();
   }

   public boolean isJdbcDriver() {
      return this.driver != null && this.driver.getType() == DriverType.JDBC;
   }

   private DriverSelectorPart.DriverInputField createDriverField(InputPanel var1) {
      DriverSelectorPart.DriverInputField var2 = new DriverSelectorPart.DriverInputField(
         LocationResources.getString("Database.Driver.Name"), LocationResources.getString("Database.Driver.Label")
      );
      var2.getInputComponent().setEditable(false);
      var2.getInputComponent().setBackground(javax.swing.UIManager.getColor("Panel.background"));
      var1.addInputField(var2, var2, var2);
      this.configureDriverFieldContextMenu(var2);
      DefaultActionHandler var3 = new DefaultActionHandler(var2.getInputComponent(), this.selectDriverAction);
      var3.install();
      return var2;
   }

   private void configureDriverFieldContextMenu(DriverSelectorPart.DriverInputField var1) {
      KongaMenuItem var2 = new KongaMenuItem(this.selectDriverAction);
      var2.setText("&Select Driver...");
      var1.getInputComponent().addToContextMenu(var2);
      var1.getInputComponent().addToContextMenu(null);
   }

   private Label createDriverFieldUi() {
      KongaTextField var1 = this.driverField.getInputComponent();
      return new TextFieldLabelAdapter(var1);
   }

   private JComponent createLayoutComponent() {
      OneRowPanel var1 = new OneRowPanel();
      var1.withInsets(0, 0, 0, 5);
      var1.setAnchor(OneRowPanel.SOUTHWEST);
      this.layoutDriverField(var1);
      var1.add(this.setDriverButton);
      return InvisiblePanel.of(var1);
   }

   private void decorateDriverField(DriverDescriptor var1) {
      if (var1 != null) {
         DriverDescriptorUi var2 = new DriverDescriptorUi(var1);
         var2.decorate(this.driverFieldUi);
         this.driverFieldUi.setBackgroundColor(this.driverField.getInputComponent().getBackground());
      } else {
         this.driverField.setValue("");
         this.driverFieldUi.setToolTip("Must specify a driver");
      }
   }

   private void layoutDriverField(OneRowPanel var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.setFillNone();
      var2.withInsets(0, 0, 0, 2);
      var2.add(this.driverField.getLabel());
      var2.add(this.driverFieldUi.asUiProvider());
      var1.add(var2);
   }

   private void setDriver(DriverDescriptor var1, boolean var2) {
      if (!equals(this.driver, var1)) {
         DriverDescriptor var3 = this.driver;
         this.driver = var1;
         this.decorateDriverField(var1);
         this.selectionListener.handle(var1);
         if (var2) {
            DriverSelectorPart.Edit var4 = new DriverSelectorPart.Edit(var1, var3);
            this.parent.undoableEditHappened(new UndoableEditEvent(this, var4));
         }
      }
   }

   private static boolean equals(DriverDescriptor var0, DriverDescriptor var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   private static class DriverInputField extends TextInputField2 {
      public DriverInputField(String var1, String var2) {
         super(var1, createTextComponent(), KongaLabel.withMnemonic(var2));
      }

      @Override
      public boolean isInputValid(String var1) {
         return var1 != null && var1.trim().length() > 0;
      }

      @Override
      public String getInvalidMessage(String var1) {
         return var1 != null && var1.trim().length() != 0 ? "Unknown driver: " + var1 : "Must specify a driver.";
      }

      public KongaTextField getInputComponent() {
         return (KongaTextField)super.getInputComponent();
      }

      private static KongaTextField createTextComponent() {
         return new KongaTextField(20);
      }

      @Override
      public boolean isSendingUndoEvents() {
         return false;
      }
   }

   private class Edit extends AbstractUndoableEdit {
      private final DriverDescriptor newDriver;
      private final DriverDescriptor oldDriver;
      private final String presentation;

      public Edit(DriverDescriptor var2, DriverDescriptor var3) {
         this.newDriver = var2;
         this.oldDriver = var3;
         this.presentation = DriverSelectorPart.this.driverField.getUndoRedoPresentation();
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         DriverSelectorPart.this.setDriver(this.newDriver, false);
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         DriverSelectorPart.this.setDriver(this.oldDriver, false);
      }

      @Override
      public String getPresentationName() {
         return this.presentation;
      }
   }

   private class SelectDriverAction extends AbstractAction implements Receiver<DriverDescriptor> {
      private ServerManager serverManager;

      public SelectDriverAction() {
         super(LocationResources.getString("Database.SelectDriver.Name"));
         this.setEnabled(false);
      }

      public void setServerManager(ServerManager var1) {
         this.serverManager = var1;
         this.setEnabled(var1 != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SelectDriverAction.this.openSelector();
            }
         });
      }

      private void openSelector() {
         DataCacheService var1 = Application.getCacheService();
         DatabaseDriverServerCache var2 = var1.getCache(DatabaseDriverServerCache.class);
         DriverSelector var3 = null;
         if (DriverSelectorPart.USE_OVERLAY) {
            var3 = this.createOverlaySelector(var2);
         }

         if (var3 == null) {
            var3 = this.createDialogSelector(var2);
         }

         var3.selectDriver(this);
      }

      private DriverSelector createOverlaySelector(DatabaseDriverServerCache var1) {
         OverlayContainer var2 = WidgetUtils.getOverlayContainerAncestor(DriverSelectorPart.this.setDriverButton);
         if (var2 == null) {
            return null;
         }

         OverlayDriverSelector var3 = new OverlayDriverSelector(this.serverManager, var1, var2);
         var3.setFocusReceiverWhenClosed(DriverSelectorPart.this.setDriverButton);
         return var3;
      }

      private DriverSelector createDialogSelector(DatabaseDriverServerCache var1) {
         DialogDriverSelector var2 = new DialogDriverSelector(this.serverManager, var1, DriverSelectorPart.this.setDriverButton);
         var2.setFocusReceiverWhenClosed(DriverSelectorPart.this.setDriverButton);
         return var2;
      }

      public void handle(final DriverDescriptor var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               DriverSelectorPart.this.setDriver(var1, true);
               DriverSelectorPart.this.driverField.requestFocus();
            }
         });
      }
   }
}

