package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Preconditions;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.DelimitedFieldTable;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.DelimitedFieldTableModel;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.DocDefTableUi;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FieldTable;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FieldTableModel;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FixedWidthFieldTable;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FixedWidthFieldTableModel;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

public final class SegmentFieldDialog extends KongaDialog {
   private static final ResourceBundle RESOURCES = TextDocumentPage.getResources();
   private final Segment segment;
   private FieldTable table;
   private FieldTableModel tableModel;
   private final boolean isFixedwidth;
   private boolean hasChanged;

   private SegmentFieldDialog(Window var1, Segment var2, boolean var3, String var4) {
      super(var1, var4);
      this.segment = var2.alias == null ? var2 : var2.alias;
      this.isFixedwidth = var3;
      this.createTableModel(var2, var3);
      this.standardLayout((UiProvider)this.getTablePanel(), OK_CANCEL);
      ActionKeyBinding.install(this.table.getAddNewAction(), this.getRootPane(), 1);
      this.setOkButtonAsDefault();
      this.manageLocation();
   }

   public static boolean show(Segment var0, boolean var1) {
      Preconditions.checkNotNull(var0, "segment");
      Field var2 = var0.findIdentField();
      if (var2 != null && var2.defaultValue.isEmpty() && var2.format.isEmpty()) {
         String var3 = var0.getName();
         var2.defaultValue = var2.format = var3;
         if (var1) {
            var2.setLength(var3.length());
         }
      }

      SegmentFieldDialog var4 = new SegmentFieldDialog(UiUtils.getActiveWindow(), var0, var1, getTitle(var0));
      var4.setVisible(true);
      return var4.hasChanged;
   }

   private void createTableModel(Segment var1, boolean var2) {
      this.tableModel = var2 ? new SegmentFieldDialog.FixedWidthFieldTableModel2() : new SegmentFieldDialog.DelimitedFieldTableModel2();

      for (Field var4 : var1.getFieldList()) {
         this.tableModel.addRow(new Field(var4));
      }
   }

   private FieldTable createFixedWidthTable() {
      return new FixedWidthFieldTable((FixedWidthFieldTableModel)this.tableModel) {
         @Override
         protected void setEditors(DocDefTableUi var1) {
            super.setEditors(var1);
            KongaComboBox var2 = new KongaComboBox(FieldDataType.getVisibleTypes(true));
            var1.setCellEditorAt(2, new DefaultCellEditor(var2));
         }
      };
   }

   private FieldTable createDelimitedTable() {
      return new DelimitedFieldTable((DelimitedFieldTableModel)this.tableModel) {
         @Override
         protected void setEditors(DocDefTableUi var1) {
            super.setEditors(var1);
            KongaComboBox var2 = new KongaComboBox(FieldDataType.getVisibleTypes(false));
            var1.setCellEditorAt(2, new DefaultCellEditor(var2));
         }
      };
   }

   private FieldTable createTable() {
      FieldTable var1 = this.isFixedwidth ? this.createFixedWidthTable() : this.createDelimitedTable();
      var1.addPropertyChangeListener("DocDefTable#TableContents", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            SegmentFieldDialog.this.hasChanged = true;
         }
      });
      return var1;
   }

   private UiProvider getTablePanel() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 5);
      this.addCaptions(var1);
      this.addTable(var1);
      return var1;
   }

   private void addCaptions(BorderLayoutBuilder var1) {
      KongaLabel var2 = TextStyles.InfoTextHeader.makeLabel(RESOURCES.getString("Caption.Dialog.Main"));
      KongaLabel var3 = TextStyles.AdminHint.makeLabel(RESOURCES.getString("Caption.Dialog.Sub"));
      Grid var4 = Grid.oneColumn(3);
      var4.addAll(var2, var3);
      var1.north(var4);
   }

   private void addTable(BorderLayoutBuilder var1) {
      this.table = this.createTable();
      JComponent var2 = this.table.getDisplayer();
      var1.center(var2);
   }

   @Override
   protected void onOk() {
      try {
         this.tableModel.validateModel();
         this.segment.fieldList = this.tableModel.getFields();
         super.onOk();
      } catch (TextDocumentDefinitionException var2) {
         ErrorLog.attention(SegmentFieldDialog.class, var2.getMessage(), var2);
      } catch (ValidationAbortedException var3) {
      }
   }

   @Override
   protected void onCancel() {
      this.hasChanged = false;
      super.onCancel();
   }

   private static String getTitle(Segment var0) {
      String var1 = RESOURCES.getString("Segment") + ": ";
      String var2 = var0.getName();
      if (var2 != null && var2.length() > 0) {
         var1 = var1 + var2;
      } else {
         var1 = var1 + RESOURCES.getString("NoName");
      }

      return var1;
   }

   @Override
   protected void onDialogOpened() {
      super.onDialogOpened();
      this.table.selectFirstRow();
      this.table.requestFocus();
   }

   private static void checkIdentifier(FieldTableModel var0) throws TextDocumentDefinitionException {
      Field var1 = getIdentifier(var0);
      if (var1 == null) {
         throw new TextDocumentDefinitionException("Must specify an identifier field.");
      }

      checkIdentifierValue(var1);
   }

   private static Field getIdentifier(FieldTableModel var0) throws TextDocumentDefinitionException {
      Field var1 = null;
      int var2 = 0;

      for (int var3 = var0.getRowCount(); var2 < var3; var2++) {
         Field var4 = var0.getField(var2);
         if (var4.getDataType() == FieldDataType.Identifier) {
            if (var1 != null) {
               throw new TextDocumentDefinitionException("There can only be one identifier field.");
            }

            var1 = var4;
         }
      }

      return var1;
   }

   private static void checkIdentifierValue(Field var0) throws TextDocumentDefinitionException {
      String var1 = var0.getDefaultValue();
      if (var1 == null || var1.trim().length() == 0) {
         String var2 = var0.getFormat();
         if (var2 == null || var2.length() == 0) {
            throw new TextDocumentDefinitionException("The identifier value must be defined in the Default of Format column.");
         }
      }
   }

   private static class DelimitedFieldTableModel2 extends DelimitedFieldTableModel {
      private DelimitedFieldTableModel2() {
      }

      @Override
      public Field createNewObject() {
         Field var1 = super.createNewObject();
         if (this.getRowCount() == 0) {
            var1.setDataType(FieldDataType.Identifier);
         }

         return var1;
      }

      @Override
      public void validateModel() throws TextDocumentDefinitionException {
         int var1 = this.getRowCount();
         if (var1 != 0) {
            SegmentFieldDialog.checkIdentifier(this);
            super.validateModel();
         }
      }
   }

   private static class FixedWidthFieldTableModel2 extends FixedWidthFieldTableModel {
      private FixedWidthFieldTableModel2() {
      }

      @Override
      public Field createNewObject() {
         Field var1 = super.createNewObject();
         if (this.getRowCount() == 0) {
            var1.setDataType(FieldDataType.Identifier);
         }

         return var1;
      }

      @Override
      public void validateModel() throws TextDocumentDefinitionException {
         int var1 = this.getRowCount();
         if (var1 != 0) {
            SegmentFieldDialog.checkIdentifier(this);
            super.validateModel();
         }
      }
   }
}
