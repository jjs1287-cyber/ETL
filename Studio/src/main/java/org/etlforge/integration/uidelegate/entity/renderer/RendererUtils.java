package org.etlforge.integration.uidelegate.entity.renderer;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelAdapter;
import org.etlforge.util.string.StringLimiter;

public final class RendererUtils {
   private static final ListCellRenderer INTEGRATION_ENTITY_LIST_CELL_RENDERER = new IntegrationEntityListDecorRenderer();

   public static void decorateRenderer(JLabel var0, IntegrationEntity var1, EntityIcons.Size var2) {
      decorateRenderer(new LabelAdapter(var0), var1, var2, ValidationStatus.VALID, false);
   }

   public static void decorateRenderer(JLabel var0, IntegrationEntity var1, boolean var2) {
      decorateRenderer(new LabelAdapter(var0), var1, ValidationStatus.VALID, var2);
   }

   public static void decorateRenderer(Label var0, IntegrationEntity var1, boolean var2) {
      decorateRenderer(var0, var1, ValidationStatus.VALID, var2);
   }

   public static void decorateRenderer(JLabel var0, IntegrationEntity var1, ValidationStatus var2, boolean var3) {
      decorateRenderer(new LabelAdapter(var0), var1, var2, var3);
   }

   public static void decorateRenderer(Label var0, IntegrationEntity var1, ValidationStatus var2, boolean var3) {
      decorateRenderer(var0, var1, EntityIcons.Size.SMALL, var2, var3);
   }

   public static void decorateRenderer(Label var0, IntegrationEntity var1, EntityIcons.Size var2, ValidationStatus var3, boolean var4) {
      EntityIcons.Category var5 = EntityIcons.Category.fromValidationStatus(var3);
      Icon var6 = IconStore.getIcons(var1).getIcon(var5, var2);
      var0.setIcon(var6);
      String var7 = var1.getName();
      if (var4) {
         String var8 = new EntityPath(var1, false).toString();
         var0.setText(var7 + " [ " + var8 + "]");
      } else {
         byte var11 = 50;
         if (var7.length() <= var11) {
            var0.setText(var7);
         } else {
            StringLimiter var9 = new StringLimiter(var7);
            String var10 = var9.getLimitedString(var11);
            var0.setText(var10);
         }
      }

      var0.setToolTip(ToolTipStringFactory.getToolTip(var1));
   }

   public static void decorateRenderer(JLabel var0, EntityType var1) {
      EntityIcons var2 = EntityIcons.forType(var1);
      Icon var3 = var2.getDefaultIcon(EntityIcons.Size.SMALL);
      var0.setIcon(var3);
   }

   public static void decorateProjectRenderer(JLabel var0, IntegrationProject var1) {
      decorateRenderer(var0, EntityType.Project);
      var0.setText(var1.getName());
      var0.setToolTipText(ToolTipStringFactory.getToolTip(var1));
   }

   public static ListCellRenderer getIntegrationEntityListCellRenderer() {
      return INTEGRATION_ENTITY_LIST_CELL_RENDERER;
   }

   public static void installIntegrationEntityRenderer(JComboBox var0) {
      RendererUtils.IntegrationEntityComboRenderer var1 = new RendererUtils.IntegrationEntityComboRenderer(var0);
      var0.setRenderer(var1);
   }

   private RendererUtils() {
   }

   private static class IntegrationEntityComboRenderer implements ListCellRenderer {
      private final JComboBox comboBox;
      private final ListCellRenderer delegate;

      public IntegrationEntityComboRenderer(JComboBox var1) {
         this.comboBox = var1;
         this.delegate = RendererUtils.INTEGRATION_ENTITY_LIST_CELL_RENDERER;
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         Component var6 = this.delegate.getListCellRendererComponent(var1, var2, var3, var4, var5);
         var6.setEnabled(this.comboBox.isEnabled());
         return var6;
      }
   }
}
