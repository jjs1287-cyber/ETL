package org.etlforge.integration.client.ui.script.debug;

import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;

final class DebuggeeNameLabel {
   private final JLabel prefix;
   private final JLabel debuggee;
   private final JLabel postfix;

   public DebuggeeNameLabel() {
      TextStyle var1 = TextStyles.DefaultText;
      this.prefix = var1.makeLabel("");
      this.debuggee = var1.makeLabel("");
      TextStyle var2 = TextStyle.adjustSize(TextStyles.DefaultBold, -1);
      this.postfix = var2.makeLabel("");
   }

   public void layout(BoxBuilder var1) {
      var1.addAllWithSpace(5, this.prefix, this.debuggee, this.postfix);
   }

   public void update(final ScriptDebugModel var1, final IntegrationEntityLookup var2) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DebuggeeNameLabel.this.clear();
            if (var2 != null) {
               DebugService var1x = var1.getDebugService();
               synchronized (var1x) {
                  DebugSession var3 = var1x.getCurrentSession();
                  if (var3 != null) {
                     this.decorateWithSessionInfo(var3);
                  }
               }
            }
         }

         private void decorateWithSessionInfo(DebugSession var1x) {
            IntegrationEntity var2x = var2.getEntity(var1x.getDebuggeeId());
            if (var2x != null) {
               DebuggeeNameLabel.this.setDebuggee(var2x);
               switch (var1x.getState()) {
                  case PAUSED:
                     this.writePausedInfo(var1x);
                     break;
                  case RUNNING:
                     this.writeRunning();
                  case DONE:
                  case IDLE:
               }
            }
         }

         private void writeRunning() {
            DebuggeeNameLabel.this.postfix.setText("[running]");
         }

         private void writePausedInfo(DebugSession var1x) {
            String var2x = "[" + this.getBreakLocationInfo(var1x) + "]";
            DebuggeeNameLabel.this.postfix.setText(var2x);
         }

         private String getBreakLocationInfo(DebugSession var1x) {
            return this.getLabelBuilder(var1x).build();
         }

         private BreakLocation getBreakLocation(DebugSession var1x) {
            TestResult var2x = var1x.getResult();
            if (var2x != null) {
               BreakLocation[] var3 = var2x.getCallStack();
               if (var3 != null && var3.length > 0) {
                  return var3[0];
               }
            }

            return null;
         }

         private ConsoleLabelBuilder getLabelBuilder(DebugSession var1x) {
            BreakLocation var2x = this.getBreakLocation(var1x);
            return ConsoleLabelBuilder.getBuilder(var2x, var2);
         }
      });
   }

   private void clear() {
      this.prefix.setText("");
      this.debuggee.setText("");
      this.debuggee.setIcon(null);
      this.postfix.setText("");
   }

   public void setDebuggee(IntegrationEntity var1) {
      this.prefix.setText("Now testing:");
      RendererUtils.decorateRenderer(this.debuggee, var1, false);
   }
}
