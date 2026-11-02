package org.etlforge.integration.client.ui.structure.editor.ldap;

import java.awt.EventQueue;
import org.etlforge.application.ui.search.string.StringSearchModel;
import org.etlforge.application.ui.search.string.StringSearchModelListener;
import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.SourceAndTargetList;

final class AvailableClassListFilter {
   private final SourceAndTargetList lists;
   private final StringSearchModel nameSearchModel;

   private AvailableClassListFilter(SourceAndTargetList var1, StringSearchModel var2) {
      this.lists = var1;
      this.nameSearchModel = var2;
      var2.addModelListener(new AvailableClassListFilter.NameSearchModelListener());
      var1.setSourceListFilter(new AvailableClassListFilter.FilterImpl(), true);
   }

   public static AvailableClassListFilter install(SourceAndTargetList var0, StringSearchModel var1) {
      return new AvailableClassListFilter(var0, var1);
   }

   private class FilterImpl implements ListFilter {
      private FilterImpl() {
      }

      @Override
      public boolean test(Object var1) {
         return var1 instanceof ClassListItem ? AvailableClassListFilter.this.nameSearchModel.test(var1.toString()) : true;
      }
   }

   private class NameSearchModelListener implements StringSearchModelListener {
      private NameSearchModelListener() {
      }

      @Override
      public void searchRuleChanged(StringSearchModel var1, StringSearchRule var2) {
         this.updateList();
      }

      @Override
      public void searchStringChanged(StringSearchModel var1, String var2) {
         this.updateList();
      }

      private void updateList() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AvailableClassListFilter.this.lists.setSourceListFilter(AvailableClassListFilter.this.new FilterImpl(), true);
            }
         });
      }
   }
}
