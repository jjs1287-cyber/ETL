package com.etlforge.integration.client.ui.sfdc.soql.v4;

import com.google.common.collect.Lists;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

final class FieldList implements UiProvider {
   private final SObject object;
   private final SelectionAction insertAction;
   private final SObjectSelectionListener listener;
   private final KongaList fieldList;

   public FieldList(SObject var1, SObjectSelectionListener var2) {
      this.object = var1;
      this.listener = var2;
      this.insertAction = new FieldList.InsertAction();
      this.fieldList = this.createFieldList();
   }

   private KongaList createFieldList() {
      ArrayList var1 = Lists.newArrayList();

      for (SObject.Field var3 : this.object.getFields()) {
         if (!var3.getName().equals("fieldsToNull")) {
            var1.add(var3);
         }
      }

      KongaListFactory var4 = ComponentFactories.newKongaListFactory();
      var4.setCellRenderer(new FieldList.Renderer());
      var4.setVisibleRowCount(16);
      var4.setLabel(KongaLabel.withMnemonic("&Fields in " + this.object.getLocalName() + ":"));
      var4.setTransferHandler(new FieldListTransferHandler(this.object));
      var4.setDragSource(true);
      KongaList var5 = var4.newList(var1);
      var5.setDefaultAction(this.insertAction);
      return var5;
   }

   public SObject getObject() {
      return this.object;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.center(this.fieldList);
      BoxBuilder var3 = BoxBuilder.horizontal();
      var3.glue().add(this.insertAction);
      var2.south(var3);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fieldList.requestFocus();
   }

   public void setSelectedField(SObject.Field var1) {
      Selection var2 = var1 == null ? Selection.EMPTY : SelectionFactory.newOrderedSelection(var1);
      this.fieldList.setSelection(var2);
      this.fieldList.ensureSelectedIndexVisible();
   }

   private class InsertAction extends AbstractSingleTypeSelectionAction<SObject.Field> {
      public InsertAction() {
         super(SObject.Field.class, "&Insert Selected Fields");
      }

      @Override
      protected void run(List<SObject.Field> var1) {
         FieldList.this.listener.fieldsSelected(FieldList.this.object, var1);
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof SObject.Field) {
            RendererUtils.renderField((SObject.Field)var2, var1, this, var4);
         }

         return this;
      }
   }
}
