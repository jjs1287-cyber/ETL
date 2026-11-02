package org.etlforge.integration.client.ui.debug;

import javax.swing.JLabel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TargetNode;
import org.etlforge.integration.debug.serverapi.TestOperationProvider;
import org.etlforge.integration.debug.serverapi.TestScriptProvider;
import org.etlforge.integration.debug.serverapi.TestTransformationProvider;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.DialogLayoutBuilder;
import org.etlforge.ui.style.TextStyle;

final class ActiveSessionDescription implements UiProvider {
   private final JLabel title;
   private final JLabel item;
   private final JLabel targetNode;

   public ActiveSessionDescription(DebugSession var1, IntegrationEntityLookup var2) {
      this.title = createTitle(var1);
      this.item = createItemLabel(var1, var2);
      this.targetNode = createDetails(var1);
   }

   private static JLabel createTitle(DebugSession var0) {
      String var2 = var0.getTestProviderId();
      String var1;
      if (var2.equals(TestScriptProvider.ID)) {
         IntegrationEntityId var3 = var0.getDebuggeeId();
         if (var3 instanceof ScriptId) {
            var1 = "Test Script";
         } else {
            if (!(var3 instanceof TransformationId)) {
               throw new RuntimeException("Unexpected ID type: " + var3.getEntityType());
            }

            var1 = "Test Mapping";
         }
      } else if (var2.equals(TestOperationProvider.ID)) {
         var1 = "Test Operation";
      } else {
         if (!var2.equals(TestTransformationProvider.ID)) {
            throw new RuntimeException("Unexpected provider: " + var2);
         }

         var1 = "Test Transformation";
      }

      return new JLabel(var1);
   }

   private static JLabel createItemLabel(DebugSession var0, IntegrationEntityLookup var1) {
      JLabel var2 = new JLabel();
      IntegrationEntityId var3 = var0.getDebuggeeId();
      IntegrationEntity var4 = var1.getEntity(var3);
      if (var4 != null) {
         RendererUtils.decorateRenderer(var2, var4, EntityIcons.Size.SMALL);
      } else {
         var2.setText("GUID: " + var3);
         RendererUtils.decorateRenderer(var2, var3.getEntityType());
      }

      return var2;
   }

   private static JLabel createDetails(DebugSession var0) {
      TargetNode var1 = var0.getTargetNode();
      return var1 == null ? null : new JLabel(var1.getPath());
   }

   @Override
   public void decorate(UiCanvas var1) {
      DialogLayoutBuilder var2 = new DialogLayoutBuilder(5, 10);
      TextStyle var3 = TextStyles.DefaultBold;
      var2.addRow(var3.makeLabel("Test:"), this.title);
      var2.addRow(var3.makeLabel("Tested item:"), this.item);
      if (this.targetNode != null) {
         var2.addRow(var3.makeLabel("Target node:"), this.targetNode);
      }

      var2.setBorder(StyleSheet.getDefaultTitleBorder("Details:"));
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }
}
