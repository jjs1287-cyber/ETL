package org.etlforge.integration.client.ui.operation.siblings;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.ui.operation.resources.OperationUiStrings;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityListDecorRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.SourceAndTargetList;
import org.etlforge.util.name.NameSorter;

public final class SiblingListSelector implements UiProvider {
   private final SourceAndTargetList lists = new SourceAndTargetList(ComponentFactories.newKongaListFactory(), ComponentFactories.newKongaListFactory());

   public SiblingListSelector(IntegrationProject var1, WebServiceCall var2, Operation var3, List<Operation> var4) {
      this.lists.setCellRenderer(new IntegrationEntityListDecorRenderer());
      this.lists.setSourceItems(this.getSourceItems(var1, var3), true);
      this.lists.setTargetItems(var4.toArray());
      this.lists.setSourceListFilter(new SiblingListSelector.ListFilterImpl(var2), false);
      this.lists.setTitles(getString("SiblingSelector.Available"), getString("SiblingSelector.Selected"));
      this.lists.enableTargetListReordering();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.lists.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.lists.requestFocus();
   }

   public List<Operation> getSelectedOperations() {
      ArrayList var1 = new ArrayList();

      for (Object var5 : this.lists.getTargetItems()) {
         var1.add((Operation)var5);
      }

      return var1;
   }

   private Object[] getSourceItems(IntegrationProject var1, Operation var2) {
      List var3 = Iterables2.sortedCopy(var1.getAllEntitiesOfClass(Operation.class), NameSorter.ignoreCase());
      var3.remove(var2);
      return var3.toArray();
   }

   private static String getString(String var0) {
      return OperationUiStrings.get(var0);
   }

   private static class ListFilterImpl implements ListFilter {
      private final SiblingFilter delegate;

      public ListFilterImpl(WebServiceCall var1) {
         this.delegate = new SiblingFilter(var1.getInputStructure());
      }

      @Override
      public boolean test(Object var1) {
         return var1 instanceof Operation && this.delegate.apply((Operation)var1);
      }
   }
}
