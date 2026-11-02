package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentDefinitionException;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.SegmentOccurrence;
import org.etlforge.ui.widget.table.DefaultTableRowOrderControl;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableRowOrderControl;

public final class SegmentTableModel extends DocDefTableModel {
   public static final TableColumnMetaData INDEX = new TableColumnMetaData("#", Void.class, false, 30);
   public static final TableColumnMetaData NAME = new TableColumnMetaData(RESOURCES.getString("Table.Column.SegmentName"), String.class, true, 200);
   public static final TableColumnMetaData PARENT = new TableColumnMetaData(RESOURCES.getString("Table.Column.Parent"), String.class, true, 200);
   public static final TableColumnMetaData OCCURRENCE = new TableColumnMetaData(
      RESOURCES.getString("Table.Column.Occurrence"), SegmentOccurrence.class, true, 100
   );
   private ComplexTextStructure textStructure;

   public SegmentTableModel(TextDocument var1) {
      super(INDEX, NAME, PARENT, OCCURRENCE);
      if (this.isNewDocument(var1)) {
         this.createNewTextStructure(var1);
      } else {
         this.populateModel(var1);
      }
   }

   private boolean isNewDocument(TextDocument var1) {
      return var1.isUnknownDoc() || var1.getDataStructure() == null;
   }

   private void createNewTextStructure(TextDocument var1) {
      this.textStructure = new ComplexTextStructure(var1.getName(), var1.isFixedField());
      Segment var2 = Segment.newRootSegment();
      this.addRow(var2);
   }

   private void populateModel(TextDocument var1) {
      TextStructure var2 = var1.getDataStructure();
      Preconditions.checkArgument(!(var2 instanceof SimpleTextStructure), "document is a simple text document.");
      if (var2 instanceof ComplexTextStructure) {
         this.textStructure = (ComplexTextStructure)var2;
         this.populateModel(this.textStructure);
      }
   }

   private void populateModel(ComplexTextStructure var1) {
      HashMap var2 = Maps.newHashMap();

      for (Segment var4 : var1.getSegmentList()) {
         Segment var5 = new Segment(var4);
         Segment var6 = (Segment)var2.get(var4.getName());
         if (var6 == null) {
            var2.put(var4.getName(), var5);
         } else {
            var5.alias = var6;
         }

         this.addRow(var5);
      }
   }

   public Segment getSegment(int var1) {
      return (Segment)this.getRowObjectAt(var1);
   }

   public Iterator<Segment> getSegmentIterator() {
      return new SegmentTableModel.IteratorImpl();
   }

   @Override
   protected Object[] getColumnDataFromObject(Object var1) {
      if (var1 instanceof Segment) {
         return this.createColumnData((Segment)var1);
      }

      this.throwIllegalRowObject(var1, Segment.class);
      return null;
   }

   private Object[] createColumnData(Segment var1) {
      return new Object[]{null, var1.getName(), var1.getParentName(), SegmentOccurrence.fromString(var1.getOccurrence())};
   }

   @Override
   public Object getValueAt(int var1, int var2) {
      if (var1 == 0) {
         TableColumnMetaData var3 = this.getColumnDescriptor(var2);
         if (var3 == PARENT) {
            return "N/A";
         }
      }

      return super.getValueAt(var1, var2);
   }

   @Override
   public boolean isCellEditable(int var1, int var2) {
      return var1 > 0 && super.isCellEditable(var1, var2);
   }

   @Override
   protected boolean updateObjectWithColumnData(Object var1, int var2, int var3, Object var4) {
      boolean var5 = false;
      if (var1 instanceof Segment) {
         Segment var6 = (Segment)var1;
         switch (var3) {
            case 1:
               var5 = this.renameSegment(var6, (String)var4, var2);
               break;
            case 2:
               var5 = this.setNewParentName(var6, (String)var4);
               break;
            case 3:
               var5 = this.setSegmentOccurrence(var6, (SegmentOccurrence)var4);
         }
      }

      return var5;
   }

   private boolean renameSegment(Segment var1, String var2, int var3) {
      String var4 = var1.getName();
      if (!var4.equals(var2)) {
         var1.setName(var2);
         this.segmentRenamed(var1, var4, var3);
         return true;
      } else {
         return false;
      }
   }

   private boolean setNewParentName(Segment var1, String var2) {
      if (!var2.equals(var1.getParentName())) {
         var1.setParentName(var2);
         return true;
      } else {
         return false;
      }
   }

   private boolean setSegmentOccurrence(Segment var1, SegmentOccurrence var2) {
      if (!var2.toString().equals(var1.getOccurrence())) {
         var1.setOccurrence(var2);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public Object createNewObject() {
      Segment var1 = new Segment("");
      Field var2 = new Field(-1, "ID", FieldDataType.Identifier.name(), "", "", 1, 0, 0, null, this.textStructure.isFixField());
      var1.fieldList.add(var2);
      return var1;
   }

   public List<Segment> getSegments() {
      int var1 = this.getRowCount();
      ArrayList var2 = Lists.newArrayList();

      for (int var3 = 0; var3 < var1; var3++) {
         var2.add(this.getSegment(var3));
      }

      return var2;
   }

   @Override
   public TableRowOrderControl getRowOrderControl() {
      return new DefaultTableRowOrderControl(this) {
         @Override
         public boolean canRowMoveDown(int var1) {
            return var1 > 0 && var1 < SegmentTableModel.this.getRowCount() - 1;
         }

         @Override
         public boolean canRowMoveUp(int var1) {
            return var1 > 1;
         }

         @Override
         public void moveRowUp(int var1) {
            if (this.canRowMoveUp(var1)) {
               Segment var2 = SegmentTableModel.this.getSegment(var1);
               Segment var3 = SegmentTableModel.this.getSegment(var1 - 1);
               if (var2.getParentName().equals(var3.getName())) {
                  SegmentTableModel.this.setValueAt(SegmentTableModel.this.getSegment(0).getName(), var1, SegmentTableModel.PARENT);
               }

               super.moveRowUp(var1);
            }
         }
      };
   }

   @Override
   public boolean canRowBeDeleted(int var1) {
      return var1 > 0;
   }

   @Override
   public void deleteRow(int var1) {
      String var2 = this.getSegment(var1).getName();
      super.deleteRow(var1);
      Segment var3 = this.getSegment(0);

      for (int var4 = 1; var4 < this.getRowCount(); var4++) {
         Segment var5 = this.getSegment(var4);
         if (var5.getParentName().equals(var2)) {
            this.setValueAt(var3.getName(), var4, PARENT);
         }
      }
   }

   public void validateModel() throws TextDocumentDefinitionException {
      new SegmentTableModelValidator(this).validate();
   }

   private void segmentRenamed(Segment var1, String var2, int var3) {
      Segment var4 = this.getTopSegmentWithOldName(var2, var3);
      this.processRowsUnderRenamedSegment(var1, var2, var3, var4);
      this.processRowsAboveRenamedSegment(var1, var3, var4);
   }

   private Segment getTopSegmentWithOldName(String var1, int var2) {
      for (int var3 = 0; var3 < var2; var3++) {
         Segment var4 = this.getSegment(var3);
         if (var1.equals(var4.getName())) {
            return var4;
         }
      }

      return null;
   }

   private void processRowsUnderRenamedSegment(Segment var1, String var2, int var3, Segment var4) {
      for (int var5 = var3 + 1; var5 < this.getRowCount(); var5++) {
         Segment var6 = this.getSegment(var5);
         if (var4 == null && var6.alias == var1) {
            var6.alias = null;
            this.copyFieldList(var1, var6);
         }

         String var7 = var6.getParentName();
         if (var7 != null && var7.equals(var2)) {
            this.setValueAt(var1.getName(), var5, PARENT);
         }
      }
   }

   private void processRowsAboveRenamedSegment(Segment var1, int var2, Segment var3) {
      Segment var4 = null;

      for (int var5 = 0; var5 < var2; var5++) {
         Segment var6 = this.getSegment(var5);
         if (var6.getName().equals(var1.getName())) {
            var4 = var6;
            break;
         }
      }

      if (var4 != null) {
         var1.fieldList = null;
         var1.alias = var4;
      } else if (var3 != null) {
         var1.fieldList = Lists.newArrayList();
         var1.alias = null;
      }
   }

   private void copyFieldList(Segment var1, Segment var2) {
      ArrayList var3 = Lists.newArrayList();

      for (Field var5 : var1.getFieldList()) {
         var3.add(new Field(var5));
      }

      var2.fieldList = var3;
   }

   private class IteratorImpl implements Iterator<Segment> {
      private int current = 0;
      private final int rows = SegmentTableModel.this.getRowCount();

      private IteratorImpl() {
      }

      @Override
      public boolean hasNext() {
         return this.current < this.rows;
      }

      public Segment next() {
         return SegmentTableModel.this.getSegment(this.current++);
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
