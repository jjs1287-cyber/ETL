package org.etlforge.integration.client.ui.entity.selector.combo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.accessibility.Accessible;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import org.etlforge.integration.client.ui.entity.EntityPathPublisher;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.widget.combobox.KongaComboBox;

public final class IntegrationEntityComboBox implements UiProvider, SelectionSource, Disposable {
   private final KongaComboBox comboBox;
   private IntegrationEntityComboBox.InternalSelectionListener internalSelectionListener;
   private boolean disposed;

   public IntegrationEntityComboBox(IntegrationProject var1, EntityType var2, EntityFilter var3, boolean var4) {
      EntityComboBoxModel var5 = new EntityComboBoxModel(var1, var2, var3, var4);
      this.comboBox = this.createComboBox(var5);
      if (var4 || var5.getSize() == 1) {
         this.comboBox.setSelectedIndex(0);
      }

      this.registerInternalSelectionListener();
   }

   public IndexedSelection getSelection() {
      return this.comboBox.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.comboBox.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.comboBox.removeSelectionListener(var1);
   }

   public void setMnemonicLabel(JLabel var1) {
      var1.setLabelFor(this.comboBox);
   }

   public ComboBoxInputField asInputField(String var1, String var2) {
      return new ComboBoxInputField(var1, this.comboBox, new JLabel(var2));
   }

   public void setDynamic() {
      this.getModel().setDynamic();
   }

   @Override
   public void dispose() {
      if (!this.disposed) {
         this.getModel().dispose();
         this.uninstallInternalSelectionListener();
         this.disposed = true;
      }
   }

   public void setFilter(EntityFilter var1) {
      this.getModel().setFilter(var1);
   }

   public boolean accepts(IntegrationEntity var1) {
      return this.getModel().accept(var1);
   }

   public IntegrationEntity getSelectedEntity() {
      return this.getModel().getSelectedEntity();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.comboBox);
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
   }

   public void selectFirst() {
      this.comboBox.selectFirst();
   }

   public boolean isEmpty() {
      int var1 = 0;

      for (int var2 = this.comboBox.getItemCount(); var1 < var2; var1++) {
         Object var3 = this.comboBox.getItemAt(var1);
         if (var3 instanceof IntegrationEntity) {
            return false;
         }
      }

      return true;
   }

   public void installKeyBindings(KeyBindingInstaller var1) {
      var1.install(this.comboBox);
   }

   public void setSelectedEntity(IntegrationEntity var1) {
      this.setSelectionImpl(var1);
   }

   public void setSelectedEntity(IntegrationEntityId var1) {
      this.setSelectionImpl(var1);
   }

   private void setSelectionImpl(Object var1) {
      this.internalSelectionListener.setBlocked(true);

      try {
         this.getModel().setSelectedItem(var1);
         this.updateToolTip();
      } finally {
         this.internalSelectionListener.setBlocked(false);
      }
   }

   public void setEnabled(boolean var1) {
      this.comboBox.setEnabled(var1);
   }

   public boolean isEnabled() {
      return this.comboBox.isEnabled();
   }

   public void setToolTipText(String var1) {
      this.comboBox.setToolTipText(var1);
   }

   public void setActionCommand(String var1) {
      this.comboBox.setActionCommand(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.comboBox.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.comboBox.removeActionListener(var1);
   }

   public ActionListener[] getActionListeners() {
      return this.comboBox.getActionListeners();
   }

   public void addItemListener(ItemListener var1) {
      this.comboBox.addItemListener(var1);
   }

   public void removeItemListener(ItemListener var1) {
      this.comboBox.removeItemListener(var1);
   }

   public void setPrototypeValue(Object var1) {
      this.comboBox.setPrototypeDisplayValue(var1);
   }

   public void setMaximumRowCount(int var1) {
      this.comboBox.setMaximumRowCount(var1);
   }

   public void layoutIn(String var1, TwoColumnLayout var2) {
      var2.addRow(new JLabel(var1), this.comboBox);
   }

   private EntityComboBoxModel getModel() {
      return (EntityComboBoxModel)this.comboBox.getModel();
   }

   private void updateToolTip() {
      this.comboBox.setToolTipText(this.getModel().getToolTip());
   }

   private void registerInternalSelectionListener() {
      this.internalSelectionListener = new IntegrationEntityComboBox.InternalSelectionListener();
      BasicComboBoxUI var1 = (BasicComboBoxUI)this.comboBox.getUI();
      Accessible var2 = var1.getAccessibleChild(this.comboBox, 0);
      if (var2 instanceof ComboPopup) {
         JList var3 = ((ComboPopup)var2).getList();
         var3.addListSelectionListener(this.internalSelectionListener);
      }

      this.comboBox.addActionListener(this.internalSelectionListener);
   }

   private void uninstallInternalSelectionListener() {
      BasicComboBoxUI var1 = (BasicComboBoxUI)this.comboBox.getUI();
      Accessible var2 = var1.getAccessibleChild(this.comboBox, 0);
      if (var2 instanceof ComboPopup) {
         JList var3 = ((ComboPopup)var2).getList();
         var3.removeListSelectionListener(this.internalSelectionListener);
      }

      this.comboBox.removeActionListener(this.internalSelectionListener);
      this.internalSelectionListener = null;
   }

   private KongaComboBox createComboBox(EntityComboBoxModel var1) {
      KongaComboBox var2 = new KongaComboBox(var1);
      RendererUtils.installIntegrationEntityRenderer(var2);
      var2.setTransferHandler(new DropHandler(var1));
      var2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      return var2;
   }

   private final class InternalSelectionListener implements ListSelectionListener, ActionListener {
      private boolean isBlocked;
      private final EntityPathPublisher pathPublisher = new EntityPathPublisher(this);

      private InternalSelectionListener() {
         this.pathPublisher.setIncludeProject(true);
      }

      @Override
      public void valueChanged(ListSelectionEvent var1) {
         if (!var1.getValueIsAdjusting() && !this.isBlocked) {
            this.pathPublisher.publish(this.getSelectedEntity(var1));
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         IntegrationEntityComboBox.this.updateToolTip();
      }

      public void setBlocked(boolean var1) {
         this.isBlocked = var1;
      }

      private IntegrationEntity getSelectedEntity(ListSelectionEvent var1) {
         JList var2 = (JList)var1.getSource();
         Object var3 = var2.getSelectedValue();
         return var3 instanceof IntegrationEntity ? (IntegrationEntity)var3 : null;
      }
   }
}
