package org.etlforge.integration.client.ui.project.loading;

import java.util.Collection;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.project.ProjectLoadingIssue;
import org.etlforge.integration.client.project.ProjectLoadingIssuesHandler;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class LoadingIssuesPresenter implements ProjectLoadingIssuesHandler {
   @Override
   public void handle(IntegrationProject var1, final Collection<ProjectLoadingIssue> var2) {
      DelayedJob.createAndSchedule(500, new Runnable() {
         @Override
         public void run() {
            LoadingIssuesPresenter.Ui var1x = new LoadingIssuesPresenter.Ui(var2);
            UiProviderDialogDisplayer.display(UiUtils.getActiveFrame(), var1x, "Warnings", KongaDialog.OK_ONLY, LoadingIssuesPresenter.class);
         }
      });
   }

   private static class IssuesTableModel extends TypedAbstractKongaTableModel<ProjectLoadingIssue> {
      public static final TableColumnMetaData LEVEL = new TableColumnMetaData("", ProjectLoadingIssue.Level.class, false, 1);
      public static final TableColumnMetaData ITEM = new TableColumnMetaData("Item", String.class, false, 20);
      public static final TableColumnMetaData MESSAGE = new TableColumnMetaData("Message", String.class, false, 50);

      public IssuesTableModel(Collection<ProjectLoadingIssue> var1) {
         super(ProjectLoadingIssue.class, new TableColumnMetaData[]{LEVEL, ITEM, MESSAGE}, var1);
      }

      protected Object[] getColumnDataForTypedObject(ProjectLoadingIssue var1) {
         return new Object[]{var1.getLevel(), var1.getItemName(), var1.getMessage()};
      }
   }

   private static class LevelRenderer extends DefaultTableCellRenderer {
      private LevelRenderer() {
      }

      @Override
      protected void setValue(Object var1) {
         Icon var2 = null;
         switch ((ProjectLoadingIssue.Level)var1) {
            case WARNING:
               var2 = CommonIcons.WARNING_16;
               break;
            case FATAL:
               var2 = CommonIcons.ERROR_16;
         }

         this.setIcon(var2);
         this.setText(null);
      }
   }

   private static class Ui implements UiProvider {
      private final CaptionLayout layout;
      private final KongaTable table;

      public Ui(Collection<ProjectLoadingIssue> var1) {
         this.table = this.createTable(var1);
         this.layout = CaptionLayout.of(this.caption(), this.table);
      }

      private KongaTable createTable(Collection<ProjectLoadingIssue> var1) {
         KongaTable var2 = ComponentFactories.tableFactory().newSortedTable(new LoadingIssuesPresenter.IssuesTableModel(var1), 8);
         var2.setSortingStatus(LoadingIssuesPresenter.IssuesTableModel.MESSAGE, SortOrder.ASCENDING);
         TableColumnModel var3 = var2.getColumnModel();
         TableColumn var4 = var3.getColumn(var2.indexOfColumn(LoadingIssuesPresenter.IssuesTableModel.LEVEL));
         var4.setCellRenderer(new LoadingIssuesPresenter.LevelRenderer());
         var4.setMaxWidth(50);
         var4.setMinWidth(30);
         TableStyles.wide().makeOver(var2);
         return var2;
      }

      private JComponent caption() {
         String var1 = "There were some warnings reported when loading the project. The project may not work as intended.";
         return new JLabel(var1, AlertIcons.WARNING, 10);
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.table.requestFocus();
      }
   }
}
