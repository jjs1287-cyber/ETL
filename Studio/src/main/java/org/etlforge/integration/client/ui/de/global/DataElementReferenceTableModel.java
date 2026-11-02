package org.etlforge.integration.client.ui.de.global;

import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.List;
import javax.swing.event.TableModelEvent;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.ValidationListener;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.transformation.TransformationExpressionSource;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.data.script.de.DataElementReference;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.system.Disposable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class DataElementReferenceTableModel extends TypedAbstractKongaTableModel<DataElementReference> implements Disposable {
   static final TableColumnMetaData SOURCE = new TableColumnMetaData("Referenced From", IntegrationEntity.class, false, 150);
   static final TableColumnMetaData MAPPING = new TableColumnMetaData("Mapping", NodePath.class, false, 100);
   static final TableColumnMetaData ACCESS = new TableColumnMetaData("Access", EnumSet.class, false, 50);
   private final DataElementReferenceTableModel.EntityListener entityListener = new DataElementReferenceTableModel.EntityListener();
   private final DataElementReferenceTableModel.DataGenerator dataGenerator = new DataElementReferenceTableModel.DataGenerator();

   public DataElementReferenceTableModel() {
      super(DataElementReference.class, SOURCE, MAPPING, ACCESS);
   }

   @Override
   public void dispose() {
      int var1 = 0;

      for (int var2 = this.getRowCount(); var1 < var2; var1++) {
         IntegrationEntity var3 = (IntegrationEntity)this.getValueAt(var1, 0);
         var3.removePropertyChangeListener("Name", this.entityListener);
         var3.removeValidationListener(this.entityListener);
      }
   }

   protected Object[] getColumnDataForTypedObject(DataElementReference var1) {
      ExpressionSource var2 = var1.getSource();
      List var3 = this.dataGenerator.getData(var2);
      var3.add(var1.getAccessType());
      return var3.toArray();
   }

   private final class DataGenerator {
      private DataGenerator() {
      }

      public List<Object> getData(ExpressionSource var1) {
         if (var1 instanceof Script) {
            return this.visit((Script)var1);
         } else if (var1 instanceof TransformationExpressionSource) {
            return this.visit((TransformationExpressionSource)var1);
         } else {
            throw new RuntimeException("Unexpected ExpressionSource: " + var1);
         }
      }

      private List<Object> visit(Script var1) {
         this.process(var1);
         return Lists.newArrayList(var1, null);
      }

      private void process(IntegrationEntity var1) {
         var1.addPropertyChangeListener("Name", DataElementReferenceTableModel.this.entityListener);
         var1.addValidationListener(DataElementReferenceTableModel.this.entityListener);
      }

      private List<Object> visit(TransformationExpressionSource var1) {
         this.process(var1.getTransformation());
         return Lists.newArrayList(var1.getTransformation(), var1.getTargetNode());
      }
   }

   private final class EntityListener implements PropertyChangeListener, ValidationListener {
      private EntityListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.fireChange((IntegrationEntity)var1.getSource());
      }

      @Override
      public void validationComplete(IntegrationData var1, ValidationResult var2) {
         this.fireChange((IntegrationEntity)var1);
      }

      private void fireChange(IntegrationEntity var1) {
         int var2 = 0;

         for (int var3 = DataElementReferenceTableModel.this.getRowCount(); var2 < var3; var2++) {
            if (var1 == DataElementReferenceTableModel.this.getValueAt(var2, 0)) {
               DataElementReferenceTableModel.this.fireTableChanged(new TableModelEvent(DataElementReferenceTableModel.this, var2, var2, 0));
               return;
            }
         }
      }
   }
}
