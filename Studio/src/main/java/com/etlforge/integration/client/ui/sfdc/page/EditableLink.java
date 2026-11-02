package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.base.Objects;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.OperationSourceChanger;
import org.etlforge.integration.data.entity.operation.OperationTargetChanger;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public final class EditableLink<E extends IntegrationEntity> implements LinkToImplementingObject {
   private final EntityExplorerSupport explorer;
   private final EntityType entityType;
   private final JLabel label;
   private final TreeWidget<E> selector;

   public static <E extends IntegrationEntity> EditableLink<E> emptyLink(
      EntityExplorerSupport var0, Class<E> var1, EntityType var2, EditableLinkRestrictions var3, String var4
   ) {
      return new EditableLink<>(var0, null, var1, var2, var3, var4);
   }

   public EditableLink(EntityExplorerSupport var1, E var2, EditableLinkRestrictions var3, String var4) {
      this(var1, (E)var2, (Class<E>)var2.getClass(), var2.getEntityType(), var3, var4);
   }

   private EditableLink(EntityExplorerSupport var1, E var2, Class<E> var3, EntityType var4, EditableLinkRestrictions var5, String var6) {
      assert var4 == EntityType.Source || var4 == EntityType.Target;
      this.explorer = var1;
      this.entityType = var4;
      Icon var7 = EntityIcons.forType(var4).getDefaultIcon(EntityIcons.Size.SMALL);
      this.label = new JLabel(var6, var7, 10);
      this.selector = new TreeWidget<>(var1, var4, var3, var5.getFilter());
      this.selector.setNewEntityCallback(var5.getNewEntityCallback());
      this.selector.setSelectedEntity(var2);
   }

   @Override
   public void dispose() {
      this.selector.dispose();
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      JComponent[] var2 = this.selector.getLayoutComponents(false);
      JPanel var3 = Horizontal.box().nonOpaque().addAllWithSpace(5, var2[0], var2[1]).container();
      var1.addRow(this.label, var3);
   }

   @Override
   public void layoutIn(NColumnPanel var1) {
      JComponent[] var2 = this.selector.getLayoutComponents(false);
      var1.addRow(this.label, var2[0], var2[1]);
   }

   @Override
   public List<IntegrationEntity> applyToOperation(Operation var1) {
      IntegrationEntity var2 = this.selector.getSelectedEntity();
      if (this.entityType == EntityType.Source) {
         OperationSourceChanger var4 = new OperationSourceChanger(var1);
         return var4.setSource((Source)var2);
      } else {
         OperationTargetChanger var3 = new OperationTargetChanger(var1);
         return var3.setTarget((Target)var2);
      }
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      InputField var2 = this.selector.asInputField(this.entityType.getDisplayName());
      var1.addInputField(var2);
   }

   @Override
   public void validate() throws IntegrationDataPanelException {
      IntegrationEntity var1 = this.selector.getSelectedEntity();
      if (var1 != null) {
         Editor var2 = this.explorer.findOpenPage(var1);
         if (var2 != null && var2.isDirty()) {
            try {
               var2.save();
            } catch (SaveEditorException var4) {
               this.explorer.openPageFor(var1);
               throw new IntegrationDataPanelException(var4.getMessage());
            }
         }
      }
   }

   @Override
   public String toString() {
      IntegrationEntity var1 = this.selector.getSelectedEntity();
      return Objects.toStringHelper(this).add("type", this.entityType).add("selected", var1 == null ? "[none]" : var1.getName()).toString();
   }
}
