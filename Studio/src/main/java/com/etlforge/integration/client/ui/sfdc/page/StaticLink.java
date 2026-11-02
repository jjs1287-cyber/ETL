package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.base.Objects;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityListDecorRenderer;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class StaticLink implements LinkToImplementingObject {
   private static final boolean USE_FAKE_DROPDOWN_FOR_ITEM = ApplicationConfiguration.getBoolean("StaticLink.UseDropdown", false);
   private final KongaLabel label;
   private final JComponent itemField;
   private final KongaButton editButton;
   private final IntegrationEntity item;

   public static StaticLink emptyLink(EntityType var0, String var1) {
      return new StaticLink(null, null, var0, var1);
   }

   public StaticLink(EntityExplorerSupport var1, IntegrationEntity var2, String var3) {
      this(var1, var2, var2.getEntityType(), var3);
   }

   private StaticLink(EntityExplorerSupport var1, IntegrationEntity var2, EntityType var3, String var4) {
      this.item = var2;
      this.label = this.createLabel(var4, var3);
      if (var2 instanceof BuiltInIntegrationEntity) {
         this.itemField = this.createSystemItemField(var2);
      } else if (var2 != null && USE_FAKE_DROPDOWN_FOR_ITEM) {
         this.itemField = this.createItemField(var2);
      } else {
         this.itemField = null;
      }

      StaticLink.EditAction var5 = var2 == null ? new StaticLink.EditAction(var1, var3) : new StaticLink.EditAction(var1, var2);
      this.editButton = new KongaButton(var5);
   }

   private KongaLabel createLabel(String var1, EntityType var2) {
      KongaLabel var3 = KongaLabel.withMnemonic(var1);
      EntityType var4 = var2 == EntityType.SystemSource ? EntityType.Source : var2;
      Icon var5 = EntityIcons.forType(var4).getDefaultIcon(EntityIcons.Size.SMALL);
      var3.setIcon(var5);
      return var3;
   }

   private JComponent createItemField(IntegrationEntity var1) {
      KongaComboBox var2 = new KongaComboBox(new Object[]{var1});
      var2.setRenderer(new IntegrationEntityListDecorRenderer());
      return var2;
   }

   private JComponent createSystemItemField(IntegrationEntity var1) {
      assert var1 instanceof BuiltInIntegrationEntity;
      return new JLabel(var1.getName());
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      this.bindLabel();
      var1.addRow(this.label, InvisiblePanel.of(this.editButton));
   }

   private void bindLabel() {
      if (this.itemField == null) {
         this.label.setLabelForButton(this.editButton);
      } else {
         this.label.setLabelFor(this.itemField);
      }
   }

   @Override
   public void layoutIn(NColumnPanel var1) {
      this.bindLabel();
      if (this.itemField == null) {
         var1.addRow(this.label, InvisiblePanel.of(this.editButton), null);
      } else {
         var1.addRow(this.label, this.itemField, InvisiblePanel.of(this.editButton));
      }
   }

   @Override
   public void dispose() {
   }

   @Override
   public List<IntegrationEntity> applyToOperation(Operation var1) {
      return Collections.emptyList();
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
   }

   @Override
   public void validate() throws IntegrationDataPanelException {
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("type", this.item.getEntityType()).add("item", this.item.getName()).toString();
   }

   private class EditAction extends AbstractAction {
      private final EntityExplorerSupport explorer;
      private final IntegrationEntity item;

      public EditAction(EntityExplorerSupport var2, EntityType var3) {
         super("No " + var3.getDisplayName());
         this.explorer = null;
         this.item = null;
         this.setEnabled(false);
      }

      public EditAction(EntityExplorerSupport var2, IntegrationEntity var3) {
         super("Edit");
         assert var3 != null;
         this.explorer = var2;
         this.item = var3;
         this.setEnabled(var2 != null && var3.isEditable());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditAction.this.openItemEditor();
            }
         });
      }

      private void openItemEditor() {
         if (this.item != null && this.explorer != null) {
            this.explorer.openPageFor(this.item);
         }
      }
   }
}
