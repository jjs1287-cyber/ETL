package com.etlforge.integration.client.ui.admin.scheduler;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.scheduler.ScheduledRun;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.OverlayMessagePainter;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.util.Property;

public class ScheduledRunsTable implements UiProvider {
   private final ScheduledRunsModel model;
   private final KongaRowTable table;

   public ScheduledRunsTable(ScheduledRunsModel var1) {
      this.model = var1;
      this.table = this.createTable();
      this.createModelBindings();
   }

   private void createModelBindings() {
      this.bindScheduledRuns();
      this.bindNoRuns();
   }

   private void bindScheduledRuns() {
      Property var1 = this.model.getProperty("scheduledRuns");
      ScheduledRunsTable.ScheduledRunsBinding var2 = new ScheduledRunsTable.ScheduledRunsBinding(var1);
      var2.syncUi();
   }

   private void bindNoRuns() {
      Property var1 = this.model.getProperty("empty");
      ScheduledRunsTable.NoRunsBinding var2 = new ScheduledRunsTable.NoRunsBinding(var1);
      var2.syncUi();
   }

   public SelectionSource asSelectionSource() {
      return this.table;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private KongaRowTable createTable() {
      ScheduledRunsTableModel var1 = new ScheduledRunsTableModel();
      KongaTable var2 = ComponentFactories.tableFactory().newSortedTable(var1, 8);
      this.setRenderers(var2);
      TableStyles.normal().makeOver(var2);
      var2.setSortingStatus(0, SortOrder.ASCENDING);
      return new KongaRowTable(var2);
   }

   private void setRenderers(KongaTable var1) {
      var1.setDefaultRenderer(Date.class, new ScheduledRunsTable.TimeRenderer());
      ScheduledRunsTable.EntityRenderer var2 = new ScheduledRunsTable.EntityRenderer();
      var1.setDefaultRenderer(Operation.class, var2);
      var1.setDefaultRenderer(Schedule.class, var2);
   }

   private static class EntityRenderer extends DefaultTableCellRenderer {
      private EntityRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         IntegrationEntity var7 = (IntegrationEntity)var2;
         Icon var8 = EntityIcons.forType(var7.getEntityType()).getDefaultIcon(EntityIcons.Size.SMALL);
         this.setIcon(var8);
         return this;
      }

      @Override
      protected void setValue(Object var1) {
         super.setValue(((IntegrationEntity)var1).getName());
      }
   }

   private class NoRunsBinding extends AbstractBinding<Boolean> {
      public NoRunsBinding(Property<Boolean> var2) {
         super(var2);
      }

      protected void updateUiWithValue(Boolean var1) {
         ComponentPainter var2 = this.getPainter(var1);
         ScheduledRunsTable.this.table.getAdaptee().setPainter(var2);
      }

      private ComponentPainter getPainter(boolean var1) {
         if (!var1) {
            return null;
         }

         OverlayMessagePainter var2 = new OverlayMessagePainter(EnterpriseAdminResources.getString("ScheduledRuns.NoRuns"));
         var2.setTextStyle(TextStyles.AdminHintBold);
         return var2;
      }
   }

   private class ScheduledRunsBinding extends AbstractBinding<List<ScheduledRun>> {
      public ScheduledRunsBinding(Property<List<ScheduledRun>> var2) {
         super(var2);
      }

      protected void updateUiWithValue(List<ScheduledRun> var1) {
         if (var1 == null) {
            var1 = Lists.newArrayList();
         }

         ScheduledRunsTableModel var2 = new ScheduledRunsTableModel(var1);
         ScheduledRunsTable.this.table.setModel(var2);
      }
   }

   private static class TimeRenderer extends DefaultTableCellRenderer {
      private final DateFormat format = DateFormat.getTimeInstance(3);

      private TimeRenderer() {
      }

      @Override
      protected void setValue(Object var1) {
         super.setValue(this.format.format((Date)var1));
      }
   }
}
