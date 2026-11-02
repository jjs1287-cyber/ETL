package org.etlforge.integration.client.ui.validation;

import java.awt.Component;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.integration.data.validation.ValidationMessage;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

final class EntityValidationResultListDisplayer implements UiProvider {
   private final KongaList list;

   public EntityValidationResultListDisplayer(EntityCollectionValidationResult var1) {
      this.list = this.createList(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   private KongaList createList(EntityCollectionValidationResult var1) {
      ListModel var2 = this.createListModel(var1);
      KongaListFactory var3 = ComponentFactories.newKongaListFactory();
      var3.setVisibleRowCount(Math.max(var2.getSize(), 3));
      var3.setCellRenderer(new EntityValidationResultListDisplayer.ListRenderer());
      return var3.newList(var2);
   }

   private ListModel createListModel(EntityCollectionValidationResult var1) {
      DefaultListModel var2 = new DefaultListModel();
      if (isValid(var1)) {
         var2.addElement(EntityValidationResultListDisplayer.ListItem.fromString(Strings.get("Validation.ListDisplayer.Valid")));
      } else {
         for (IntegrationEntity var4 : var1.getValidatedEntities()) {
            ValidationResult var5 = var1.getValidationResult(var4);
            if (!var5.isEmpty()) {
               var2.addElement(var4);

               for (ValidationMessage var7 : var5.allMessages()) {
                  var2.addElement(EntityValidationResultListDisplayer.ListItem.fromMessage(var7));
               }
            }
         }

         var2.addElement(EntityValidationResultListDisplayer.ListItem.empty());
      }

      return var2;
   }

   private static boolean isValid(EntityCollectionValidationResult var0) {
      return var0 == null || var0.isEverythingOk();
   }

   private static final class ListItem implements ListDecorator {
      private final String text;
      private final Icon icon;

      private ListItem(String var1, Icon var2) {
         this.text = var1;
         this.icon = var2;
      }

      public static Object fromMessage(ValidationMessage var0) {
         Icon var1 = ValidationIcons.Indicators.fromMessage(var0);
         return new EntityValidationResultListDisplayer.ListItem(var0.message(), var1);
      }

      public static Object fromString(String var0) {
         return new EntityValidationResultListDisplayer.ListItem(var0, null);
      }

      public static Object empty() {
         return new EntityValidationResultListDisplayer.ListItem(" ", null);
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setIcon(this.icon);
         var1.setText(this.text);
      }

      @Override
      public String toString() {
         return this.text;
      }
   }

   private static final class ListRenderer implements ListCellRenderer {
      private final ListCellRenderer entityRenderer = RendererUtils.getIntegrationEntityListCellRenderer();
      private final ListCellRenderer messageRenderer = this.createMessageRenderer();

      public ListRenderer() {
      }

      private ListCellRenderer createMessageRenderer() {
         DefaultListDecorRenderer var1 = new DefaultListDecorRenderer();
         BorderBuilder var2 = new BorderBuilder(var1.getBorder()).empty(0, 20, 0, 5);
         var1.setCustomBorder(var2);
         return var1;
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         return var2 instanceof IntegrationEntity
            ? this.entityRenderer.getListCellRendererComponent(var1, var2, var3, var4, var5)
            : this.messageRenderer.getListCellRendererComponent(var1, var2, var3, var4, var5);
      }
   }
}
