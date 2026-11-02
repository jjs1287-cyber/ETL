package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public class InvalidMappingsTableModel extends TypedAbstractKongaTableModel<InvalidMapping> {
   public static final TableColumnMetaData TARGET_PATH = new TableColumnMetaData(TreeStrings.get("InvalidMappings.Viewer.TargetPath"), String.class, false, 100);
   public static final TableColumnMetaData EXPRESSION = new TableColumnMetaData(TreeStrings.get("InvalidMappings.Viewer.Expression"), String.class, false, 200);
   public static final TableColumnMetaData ERROR = new TableColumnMetaData(TreeStrings.get("InvalidMappings.Viewer.Error"), String.class, false, 300);

   public InvalidMappingsTableModel(TreeMapper var1) {
      this(var1.getMappingManager().getInvalidMappings());
   }

   public InvalidMappingsTableModel(InvalidMapping[] var1) {
      super(InvalidMapping.class, new TableColumnMetaData[]{TARGET_PATH, EXPRESSION, ERROR}, var1);
   }

   public List<InvalidMapping> getMappings() {
      ArrayList var1 = Lists.newArrayList();
      int var2 = 0;

      for (int var3 = this.getRowCount(); var2 < var3; var2++) {
         var1.add(this.getRowObjectAt(var2));
      }

      return var1;
   }

   protected Object[] getColumnDataForTypedObject(InvalidMapping var1) {
      return new Object[]{var1.getTargetPath().toString(), var1.getExpression(), var1.getError()};
   }
}
