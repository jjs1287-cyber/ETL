package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.ListModel;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.ui.widget.list.KongaListModel;

final class ObjectListPopulator {
   private Object[] listItems;

   public ObjectListPopulator(DatabaseObjectContainer[] var1) {
      List var2 = this.getObjects(var1);
      this.createListObjects(var2);
   }

   private List<Object> getObjects(DatabaseObjectContainer<?>[] var1) {
      ArrayList var2 = Lists.newArrayList();

      for (DatabaseObjectContainer var6 : var1) {
         this.handleContainer(var6, var2);
      }

      return var2;
   }

   private void handleContainer(DatabaseObjectContainer<?> var1, List<Object> var2) {
      var2.add(new ObjectListPopulator.ListHeaderItem(var1.getObjectType()));

      for (DatabaseObject var4 : var1.getAllObjects()) {
         var2.add(var4);
      }

      var2.add(new ObjectListPopulator.ListSeparator());
   }

   private void createListObjects(Collection<Object> var1) {
      this.listItems = var1.toArray();
   }

   public ListModel getListModel() {
      return new KongaListModel(this.listItems);
   }

   private static class ListHeaderItem {
      public DatabaseObjectType ItemType;

      public ListHeaderItem(DatabaseObjectType var1) {
         this.ItemType = var1;
      }

      @Override
      public String toString() {
         return this.ItemType.toString() + "S";
      }
   }

   private static class ListSeparator {
      private ListSeparator() {
      }

      @Override
      public String toString() {
         return " ";
      }
   }
}
