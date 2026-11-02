package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.ui.widget.table.DefaultTableRowOrderControl;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableRowOrderControl;

public class DelimitedFieldTableModel extends FieldTableModel {
   private final boolean withValidation;

   public static DelimitedFieldTableModel withValidation() {
      return new DelimitedFieldTableModel(true);
   }

   private DelimitedFieldTableModel(boolean var1) {
      super(
         FieldTableColumns.INDEX,
         FieldTableColumns.NAME,
         FieldTableColumns.TYPE,
         FieldTableColumns.DEFAULT,
         FieldTableColumns.FORMAT,
         FieldTableColumns.VALIDATION
      );
      this.withValidation = true;
   }

   protected DelimitedFieldTableModel() {
      super(FieldTableColumns.INDEX, FieldTableColumns.NAME, FieldTableColumns.TYPE, FieldTableColumns.DEFAULT, FieldTableColumns.FORMAT);
      this.withValidation = false;
   }

   @Override
   protected final Object[] getColumnDataFromObject(Object var1) {
      if (var1 instanceof Field) {
         return this.createColumnDataFromField((Field)var1);
      }

      this.throwIllegalRowObject(var1, Field.class);
      return null;
   }

   private Object[] createColumnDataFromField(Field var1) {
      ArrayList var2 = Lists.newArrayList();
      Collections.addAll(var2, null, var1.getName(), var1.getDataType(), var1.getDefaultValue(), var1.getFormat());
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
               var5 = this.setNewDefaultValue(var6, var2, ((String)var4).trim());
               break;
            case 4:
               var5 = this.setNewFormat(var6, var2, ((String)var4).trim());
               break;
            case 5:
               var5 = this.setNewValidation(var6, (FieldValidation)var4);
         }
      }

      return var5;
   }

   public Field createNewObject() {
      return new Field(-1, "", "String", "", "", 0, false);
   }

   @Override
   public boolean isCellEditable(int var1, int var2) {
      if (this.isIdentifier(var1)) {
         TableColumnMetaData var3 = this.getColumnDescriptor(var2);
         return var3 != FieldTableColumns.TYPE;
      } else {
         return super.isCellEditable(var1, var2);
      }
   }

   @Override
   public TableRowOrderControl getRowOrderControl() {
      return new DefaultTableRowOrderControl(this) {
         @Override
         public boolean canRowMoveDown(int var1) {
            return DelimitedFieldTableModel.this.isIdentifier(var1) ? false : super.canRowMoveDown(var1);
         }

         @Override
         public boolean canRowMoveUp(int var1) {
            return var1 > 0 && DelimitedFieldTableModel.this.isIdentifier(var1 - 1) ? false : super.canRowMoveUp(var1);
         }
      };
   }

   private boolean isIdentifier(int var1) {
      Field var2 = this.getField(var1);
      return var2 != null && var2.isIdentifier();
   }

   @Override
   public TableColumnMetaData getFormatColumn() {
      return FieldTableColumns.FORMAT;
   }
}
