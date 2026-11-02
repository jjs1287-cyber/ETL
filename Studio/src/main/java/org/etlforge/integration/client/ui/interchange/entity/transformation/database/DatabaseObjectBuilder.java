package org.etlforge.integration.client.ui.interchange.entity.transformation.database;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeAttribute;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeElement;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNode;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperNodeVisitor;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseTable;

final class DatabaseObjectBuilder {
   private List<DatabaseObject> objects;

   public Collection<DatabaseObject> buildObjects(TreeMapperNode var1) {
      this.objects = Lists.newArrayList();
      DatabaseObjectBuilder.VisitorImpl var2 = new DatabaseObjectBuilder.VisitorImpl();
      Iterator var3 = var1.children();

      while (var3.hasNext()) {
         this.buildObjectsImpl((TreeMapperNode)var3.next(), var2);
      }

      return this.objects;
   }

   private void buildObjectsImpl(TreeMapperNode var1, TreeMapperNodeVisitor var2) {
      var1.accept(var2);
      Iterator var3 = var1.children();

      while (var3.hasNext()) {
         this.buildObjectsImpl((TreeMapperNode)var3.next(), var2);
      }
   }

   private class VisitorImpl implements TreeMapperNodeVisitor {
      private DatabaseObject current;

      private VisitorImpl() {
      }

      @Override
      public void visit(TreeAttribute var1) {
         if (this.current == null) {
            throw new IllegalStateException();
         }

         DatabaseColumn var2 = new DatabaseColumn(
            this.current, var1.getName(), var1.getDataType(), var1.getSqlDataType(), var1.getDefaultValue(), var1.isNullable(), false
         );
         this.current.addColumn(var2);
      }

      @Override
      public void visit(TreeElement var1) {
         Pair var2 = this.getSchemaAndTableName(var1);
         String var3 = (String)var2.first;
         String var4 = (String)var2.second;
         this.current = new DatabaseTable(var4, var3);
         DatabaseObjectBuilder.this.objects.add(this.current);
      }

      private Pair<String, String> getSchemaAndTableName(TreeElement var1) {
         String var2 = var1.getName();
         if (var2.contains(":")) {
            String[] var3 = var2.split(":", 2);
            return Pair.of(var3[0], var3[1]);
         } else {
            return Pair.of(null, var2);
         }
      }
   }
}
