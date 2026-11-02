package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentDefinitionException;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.ui.widget.table.TableColumnMetaData;

public abstract class FieldTableModel extends DocDefTableModel {
   private boolean updateObjectBlocked;

   public FieldTableModel(TableColumnMetaData... var1) {
      super(var1);
   }

   public final List<Field> getFields() {
      int var1 = this.getRowCount();
      ArrayList var2 = Lists.newArrayList();

      for (int var3 = 0; var3 < var1; var3++) {
         var2.add(this.getField(var3));
      }

      return var2;
   }

   public final Field getField(int var1) {
      return (Field)this.getRowObjectAt(var1);
   }

   public Iterator<Field> getFieldIterator() {
      return new FieldTableModel.IteratorImpl();
   }

   public void validateModel() throws TextDocumentDefinitionException {
      new FieldTableModelValidator(this).validate();
   }

   protected final boolean isUpdateBlocked() {
      return this.updateObjectBlocked;
   }

   protected final void setUpdateBlocked(boolean var1) {
      this.updateObjectBlocked = var1;
   }

   protected final void setIdentifierValue(int var1, TableColumnMetaData var2, String var3) {
      this.setUpdateBlocked(true);
      this.setValueAt(var3, var1, var2);
      this.setUpdateBlocked(false);
   }

   public abstract TableColumnMetaData getFormatColumn();

   protected final boolean setNewFieldName(Field var1, String var2) {
      if (!var2.equals(var1.getName())) {
         var1.setName(var2);
         return true;
      } else {
         return false;
      }
   }

   protected final boolean setNewDataType(Field var1, int var2, FieldDataType var3) {
      if (var3 == var1.getDataType()) {
         return false;
      }

      var1.setDataType(var3);
      if (this.containsColumn(FieldTableColumns.VALIDATION)) {
         this.updateValidationToMatchNewDataType(var2, var3);
      }

      return true;
   }

   private void updateValidationToMatchNewDataType(int var1, FieldDataType var2) {
      FieldValidation var3 = (FieldValidation)this.getValueAt(var1, FieldTableColumns.VALIDATION);
      if (var3 != null) {
         FieldValidation var4 = var3.transformToType(var2);
         this.setValueAt(var4, var1, FieldTableColumns.VALIDATION);
      }
   }

   protected final boolean setNewDefaultValue(Field var1, int var2, String var3) {
      if (var3.equals(var1.getDefaultValue())) {
         return false;
      }

      var1.setDefaultValue(var3);
      if (var1.isIdentifier()) {
         var1.setFormat(var3);
         this.setIdentifierValue(var2, FieldTableColumns.FORMAT, var3);
      }

      return true;
   }

   protected final boolean setNewFormat(Field var1, int var2, String var3) {
      if (var3.equals(var1.getFormat())) {
         return false;
      }

      var1.setFormat(var3);
      if (var1.isIdentifier()) {
         var1.setDefaultValue(var3);
         this.setIdentifierValue(var2, FieldTableColumns.DEFAULT, var3);
      }

      return true;
   }

   protected final boolean setNewValidation(Field var1, FieldValidation var2) {
      Optional var3 = Optional.fromNullable(var2);
      if (var3.equals(var1.getValidation())) {
         return false;
      }

      var1.setValidation(var3);
      return true;
   }

   private class IteratorImpl implements Iterator<Field> {
      private int current = 0;
      private final int rows = FieldTableModel.this.getRowCount();

      private IteratorImpl() {
      }

      @Override
      public boolean hasNext() {
         return this.current < this.rows;
      }

      public Field next() {
         return FieldTableModel.this.getField(this.current++);
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
