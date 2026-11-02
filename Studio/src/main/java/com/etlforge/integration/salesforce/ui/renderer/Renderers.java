package com.etlforge.integration.salesforce.ui.renderer;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import com.etlforge.integration.salesforce.ui.NameDisplayModes;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public final class Renderers {
   public static ListCellRenderer forList(NameDisplayMode var0) {
      return new SchemaEntityListRenderer(var0);
   }

   public static TableCellRenderer forTable(NameDisplayMode var0) {
      return new SchemaEntityTableRenderer(var0);
   }

   public static TreeCellRenderer forTree(NameDisplayModes var0) {
      return new SchemaEntityTreeRenderer(var0);
   }

   private Renderers() {
   }
}
