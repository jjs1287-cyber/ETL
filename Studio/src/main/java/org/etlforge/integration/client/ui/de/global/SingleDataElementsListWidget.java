package org.etlforge.integration.client.ui.de.global;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import javax.swing.Action;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.de.BuiltInDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;

public final class SingleDataElementsListWidget implements DataElementsWidget {
   private final DataElementsList list = new DataElementsList();
   private GlobalDataElements deStore;
   private boolean includeBuiltInDataElements;

   @Override
   public void setDataElements(GlobalDataElements var1) {
      this.deStore = var1;
      this.update(false);
   }

   private void update(boolean var1) {
      HashSet var2 = Sets.newHashSet();
      if (this.deStore != null) {
         var2.addAll(this.deStore.getAllDataElements());
      }

      if (this.includeBuiltInDataElements) {
         var2.addAll(BuiltInDataElements.getBuiltInDataElements());
      }

      this.list.setDataElements(var2, var1);
   }

   @Override
   public void update() {
      this.update(true);
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   @Override
   public void setIncludeBuiltInDataElements(boolean var1) {
      this.includeBuiltInDataElements = var1;
      if (this.deStore != null) {
         this.update(true);
      }
   }

   @Override
   public void setActionProvider(Supplier<List<Action>> var1) {
      this.list.setActionProvider(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.list.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.list.removeSelectionListener(var1);
   }

   public OrderedSelection getSelection() {
      return this.list.getSelection();
   }

   @Override
   public void setFilter(Predicate<String> var1) {
      this.list.setFilter(var1);
   }

   @Override
   public void selectFirst() {
      this.list.selectFirst();
   }

   @Override
   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   @Override
   public void setProcess(IntegrationEntity var1) {
   }
}
