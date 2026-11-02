package org.etlforge.integration.client.ui.project.location;

import com.google.common.base.Predicate;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.style.TableStyle;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.DefaultComponentAction;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.SortStatusMemento;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.ListDecorTableRenderer;

public final class ProjectLocationsTable implements UiProvider {
   private final KongaTable table;
   private final KongaRowTable selectionSource;
   private TableRowSorter<ProjectLocationsTableModel> sorter;

   public ProjectLocationsTable(ProjectLocationsTableModel var1) {
      this.table = ComponentFactories.tableFactory().newSortedTable(var1);
      this.selectionSource = new KongaRowTable(this.table);
      this.installSorter(var1);
      this.customize();
   }

   private void installSorter(ProjectLocationsTableModel var1) {
      this.sorter = new TableRowSorter<>(var1);
      this.table.setRowSorter(this.sorter);
   }

   private void customize() {
      this.setVisibleRows(6);
      this.setTableStyle();
      this.setCellRenderers();
      this.setDefaultSorting();
      this.table.setSelectionMode(0);
      this.addKeyBindings();
      this.installFocusListener();
   }

   private void setDefaultSorting() {
      this.table.setSortingStatus(ProjectLocationsTableModel.ACCESSED, SortOrder.DESCENDING);
   }

   private void setCellRenderers() {
      ListDecorTableRenderer var1 = new ListDecorTableRenderer();

      for (int var2 = 0; var2 < this.table.getColumnCount(); var2++) {
         this.table.setCellRendererAt(var2, var1);
      }
   }

   private void setTableStyle() {
      TableStyle var1 = TableStyles.wide();
      var1.setGridMode(TableGridMode.HORIZONTAL);
      var1.makeOver(this.table);
   }

   private void addKeyBindings() {
      DefaultComponentAction.bind(this.table, new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
         }
      });
   }

   private void installFocusListener() {
      this.table.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent var1) {
            ProjectLocationsTable.this.table.repaint();
         }

         @Override
         public void focusLost(FocusEvent var1) {
            ProjectLocationsTable.this.table.repaint();
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new ProjectLocationsTable.TableScrollPane(this.table));
   }

   @Override
   public void requestFocus() {
      this.table.requestFocusInWindow();
   }

   public SelectionSource asSelectionSource() {
      return this.selectionSource;
   }

   public void setDefaultAction(SelectionAction var1) {
      this.selectionSource.setDefaultAction(var1);
   }

   public SelectionAction getDefaultAction() {
      return this.selectionSource.getDefaultAction();
   }

   public void setInitialSelection() {
      if (this.table.getSelectedRowCount() == 0) {
         this.table.selectFirstRow();
      }
   }

   public void setPopupMenuRequestHandler(PopupMenuRequestHandler var1) {
      PopupMenuRequestHandlers.install(this.table, var1);
   }

   public void installKeyBinding(Action var1) {
      ActionKeyBinding.install(var1, this.table);
   }

   public ProjectLocation getSelectedProject() {
      int var1 = this.table.getSelectedRow();
      if (var1 < 0) {
         return null;
      }

      var1 = this.table.convertRowIndexToModel(var1);
      return ((ProjectLocationsTableModel)this.table.getModel()).getRowObjectAt(var1);
   }

   public void addProjectLocation(ProjectLocation var1) {
      int var2 = this.table.getRowCount();
      ((ProjectLocationsTableModel)this.table.getModel()).addRowAt(var2 - 1, var1);
      this.table.setRowSelectionInterval(var2 - 1, var2 - 1);
   }

   public void setModel(ProjectLocationsTableModel var1) {
      this.table.setModel(var1);
      RowFilter var2 = this.sorter.getRowFilter();
      this.installSorter(var1);
      this.sorter.setRowFilter(var2);
      this.setDefaultSorting();
   }

   public void addSelectionListener(ListSelectionListener var1) {
      this.table.getSelectionModel().addListSelectionListener(var1);
   }

   public void setVisibleRows(int var1) {
      this.table.setVisibleRowCount(var1);
   }

   public void selectFirst() {
      this.table.selectFirstRow();
   }

   public void setSortStatusMemento(SortStatusMemento var1) {
      this.table.setSortStatusMemento(var1);
   }

   public void setFilter(Predicate<ProjectLocation> var1) {
      CompatibleProjectsFilter var2 = new CompatibleProjectsFilter(var1);
      this.sorter.setRowFilter(var2);
   }

   public void removeFilter() {
      this.sorter.setRowFilter(null);
   }

   private static class TableScrollPane extends JScrollPane {
      private final int width = ProjectLocationsTableModel.getTotalWidth();

      public TableScrollPane(KongaTable var1) {
         super(var1);
      }

      @Override
      public Dimension getPreferredSize() {
         Dimension var1 = super.getPreferredSize();
         return new Dimension(this.width, var1.height);
      }
   }
}
