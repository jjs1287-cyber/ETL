package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentDefinitionException;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.ui.widget.table.TableColumnMetaData;

public class FixedWidthFieldTableModel extends FieldTableModel {
   private final boolean withValidation;

   public static FixedWidthFieldTableModel withValidation() {
      return new FixedWidthFieldTableModel(true);
   }

   private FixedWidthFieldTableModel(boolean var1) {
      super(
         FieldTableColumns.INDEX,
         FieldTableColumns.NAME,
         FieldTableColumns.TYPE,
         FieldTableColumns.BEGIN,
         FieldTableColumns.LENGTH,
         FieldTableColumns.END,
         FieldTableColumns.DEFAULT,
         FieldTableColumns.FORMAT,
         FieldTableColumns.VALIDATION
      );
      this.withValidation = true;
   }

   protected FixedWidthFieldTableModel() {
      super(
         FieldTableColumns.INDEX,
         FieldTableColumns.NAME,
         FieldTableColumns.TYPE,
         FieldTableColumns.BEGIN,
         FieldTableColumns.LENGTH,
         FieldTableColumns.END,
         FieldTableColumns.DEFAULT,
         FieldTableColumns.FORMAT
      );
      this.withValidation = false;
   }

   @Override
   protected final Object[] getColumnDataFromObject(Object var1) {
      if (var1 instanceof Field) {
         return this.createDataFromField((Field)var1);
      }

      this.throwIllegalRowObject(var1, Field.class);
      return null;
   }

   private Object[] createDataFromField(Field var1) {
      ArrayList var2 = Lists.newArrayList();
      Collections.addAll(
         var2,
         null,
         var1.getName(),
         var1.getDataType(),
         var1.getBeginPosition(),
         var1.getLength(),
         var1.getEndPosition(),
         var1.getDefaultValue(),
         var1.getFormat()
      );
      if (this.withValidation) {
         var2.add(var1.getValidation().orNull());
      }

      return var2.toArray();
   }

   @Override
   protected final boolean updateObjectWithColumnData(Object var1, int var2, int var3, Object var4) {
      if (this.isUpdateBlocked()) {
         return false;
      }

      boolean var5 = false;
      if (var1 instanceof Field) {
         Field var6 = (Field)var1;
         switch (var3) {
            case 1:
               var5 = this.setNewFieldName(var6, ((String)var4).trim());
               break;
            case 2:
               var5 = this.setNewDataType(var6, var2, (FieldDataType)var4);
               break;
            case 3:
               var5 = this.setNewBeginPosition(var6, var2, this.getIntValue(var4));
               break;
            case 4:
               var5 = this.setNewLength(var6, var2, this.getIntValue(var4));
               break;
            case 5:
               var5 = this.setNewEndPosition(var6, var2, this.getIntValue(var4));
               break;
            case 6:
               var5 = this.setNewDefaultValue(var6, var2, (String)var4);
               break;
            case 7:
               var5 = this.setNewFormat(var6, var2, (String)var4);
               break;
            case 8:
               var5 = this.setNewValidation(var6, (FieldValidation)var4);
         }
      }

      return var5;
   }

   private int getIntValue(Object var1) {
      return var1 instanceof Integer ? (Integer)var1 : 0;
   }

   private boolean setNewBeginPosition(Field var1, int var2, int var3) {
      var1.setBeginPosition(var3);
      this.updateWidthFields(var1, var2);
      return true;
   }

   private boolean setNewLength(Field var1, int var2, int var3) {
      var1.setLength(var3);
      this.updateWidthFields(var1, var2);
      return true;
   }

   private boolean setNewEndPosition(Field var1, int var2, int var3) {
      var1.setEndPosition(var3);
      this.updateWidthFields(var1, var2);
      return true;
   }

   private void updateWidthFields(Field var1, int var2) {
      this.setUpdateBlocked(true);

      try {
         this.setValueAt(var1.getBeginPosition(), var2, FieldTableColumns.BEGIN);
         this.setValueAt(var1.getLength(), var2, FieldTableColumns.LENGTH);
         this.setValueAt(var1.getEndPosition(), var2, FieldTableColumns.END);
      } finally {
         this.setUpdateBlocked(false);
      }
   }

   public Field createNewObject() {
      int var1 = this.getRowCount();
      return var1 == 0 ? this.createNewFirstField() : this.createNewLastField(var1);
   }

   private Field createNewFirstField() {
      return new Field(0, "", "String", "", "", 0, 1, 1, null);
   }

   private Field createNewLastField(int var1) {
      Field var2 = this.getField(var1 - 1);
      return new Field(0, "", "String", "", "", 0, 0, 1, var2);
   }

   public void recalculate() {
      int var1 = 1;

      for (int var2 = 0; var2 < this.getRowCount(); var2++) {
         this.setValueAt(var1, var2, FieldTableColumns.BEGIN);
         var1 += this.getField(var2).getLength();
      }
   }

   @Override
   public void validateModel() throws TextDocumentDefinitionException {
      int var1 = this.getRowCount();
      if (var1 != 0) {
         super.validateModel();
         new FixedWidthFieldTableModelValidator(this).validate();
      }
   }

   @Override
   public TableColumnMetaData getFormatColumn() {
      return FieldTableColumns.FORMAT;
   }
}
