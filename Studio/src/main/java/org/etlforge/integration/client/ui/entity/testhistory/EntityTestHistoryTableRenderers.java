package org.etlforge.integration.client.ui.entity.testhistory;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.integration.uidelegate.entity.list.IntegrationEntityListDecorator;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.KongaRowTableModel;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.renderer.ListDecorTableRenderer;

public final class EntityTestHistoryTableRenderers {
   public static void install(KongaTable var0) {
      var0.setDefaultRenderer(IntegrationEntity.class, new EntityTestHistoryTableRenderers.EntityRenderer());
      var0.setDefaultRenderer(TestResult.Outcome.class, new EntityTestHistoryTableRenderers.OutcomeRenderer());
      var0.setDefaultRenderer(String.class, new EntityTestHistoryTableRenderers.DetailsRenderer());
      var0.setDefaultRenderer(DateCell.class, new ListDecorTableRenderer());
   }

   private EntityTestHistoryTableRenderers() {
   }

   private static class DetailsRenderer extends ListDecorTableRenderer {
      private DetailsRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         if (!var3) {
            Color var7 = this.isErrorRenderingRequired(var1, var5) ? Colors.ERROR_TEXT_COLOR : Color.BLACK;
            this.setForeground(var7);
         }

         return this;
      }

      private boolean isErrorRenderingRequired(JTable var1, int var2) {
         TableModel var3 = var1.getModel();
         if (!(var3 instanceof KongaRowTableModel)) {
            return false;
         }

         int var4 = var1.convertRowIndexToModel(var2);
         Object var5 = ((KongaRowTableModel)var3).getRowObjectAt(var4);
         return var5 instanceof EntityTestResult && ((EntityTestResult)var5).getOutcome() == TestResult.Outcome.ERROR;
      }
   }

   private static final class EntityRenderer extends ListDecorTableRenderer {
      private EntityRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         if (var2 instanceof IntegrationEntity) {
            IntegrationEntityListDecorator var7 = new IntegrationEntityListDecorator((IntegrationEntity)var2);
            var7.decorate(this, var3);
         }

         return this;
      }
   }

   private static class OutcomeRenderer extends ListDecorTableRenderer {
      private OutcomeRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         this.decorate((TestResult.Outcome)var2, var3);
         return this;
      }

      private void decorate(TestResult.Outcome var1, boolean var2) {
         Pair var3 = OutcomeDecorator.getIconAndTextColor(var1);
         this.setIcon((Icon)var3.first);
         this.setToolTipText(null);
         if (!var2) {
            this.setForeground((Color)var3.second);
         }
      }
   }
}
