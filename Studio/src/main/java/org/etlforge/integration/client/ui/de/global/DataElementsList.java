package org.etlforge.integration.client.ui.de.global;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListUtils;

final class DataElementsList implements UiProvider, SelectionSource {
   private final KongaList list;
   private FilteredListModel model;
   private final ListContextMenuHandler contextMenuHandler = new ListContextMenuHandler();
   private ListFilter listFilter;

   public DataElementsList() {
      this(new HashSet<>());
   }

   public DataElementsList(Collection<String> var1) {
      this.model = new FilteredListModel(new DefaultListModel());
      this.list = this.createList();
      this.setDataElements(var1, false);
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setTransferHandler(new ListTransferHandlerImpl());
      var1.setDragSource(true);
      var1.setPopupMenuRequestHandler(this.contextMenuHandler);
      return var1.newList(this.model);
   }

   public void setActionProvider(Supplier<List<Action>> var1) {
      this.contextMenuHandler.setActionProvider(var1);
   }

   public void setDataElements(Collection<String> var1, final boolean var2) {
      this.model = this.createModel(var1);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            Object var1x = DataElementsList.this.list.getSelection().head();
            DataElementsList.this.list.setModel(DataElementsList.this.model);
            if (var2) {
               this.retainSelection(var1x);
            } else {
               DataElementsList.this.list.selectFirst();
            }
         }

         private void retainSelection(Object var1) {
            DataElementsList.this.list.setSelection(SelectionFactory.newOrderedSelection(var1));
         }
      });
   }

   private FilteredListModel createModel(Collection<String> var1) {
      TreeSet var2 = Sets.newTreeSet(String.CASE_INSENSITIVE_ORDER);
      var2.addAll(var1);
      DefaultListModel var3 = ListUtils.createModel(var2);
      FilteredListModel var4 = new FilteredListModel(var3);
      if (this.listFilter != null) {
         var4.addFilter(this.listFilter);
      }

      return var4;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
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

   public void clearSelection() {
      this.list.setSelection(Selection.EMPTY);
   }

   public void addFocusListener(FocusListener var1) {
      this.list.addFocusListener(var1);
   }

   public void removeFocusListener(FocusListener var1) {
      this.list.removeFocusListener(var1);
   }

   public void setFilter(final Predicate<String> var1) {
      this.listFilter = new ListFilter() {
         @Override
         public boolean test(Object var1x) {
            return var1.apply(var1x.toString());
         }
      };
      this.model.setFilter(this.listFilter);
   }

   public void selectFirst() {
      this.list.selectFirst();
   }

   public boolean isEmpty() {
      return this.model.getSize() == 0;
   }
}
