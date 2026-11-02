package org.etlforge.application.ui.groovy;

import java.util.List;
import javax.swing.Action;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.split.KongaSplitPane;

public final class ConsoleUi implements UiProvider {
   private final ScriptArea scriptArea = new ScriptArea();
   private final ImportsArea importsArea = new ImportsArea(this.scriptArea);
   private Action executeAction;

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(10, 10);
      UiProvider var3 = this.createToolbar();
      final KongaSplitPane var4 = this.createSplitPane();
      var2.north(var3).center(var4).withBorder(Empty.border(10));
      var2.decorate(var1);
      DelayedJob.createAndSchedule(25, new Runnable() {
         @Override
         public void run() {
            var4.setDividerLocation(0.7);
         }
      });
   }

   private UiProvider createToolbar() {
      BoxBuilder var1 = BoxBuilder.horizontal().withBorder(new BorderBuilder().etched().empty(3));
      var1.add(this.executeAction);
      return var1;
   }

   private KongaSplitPane createSplitPane() {
      KongaSplitPane var1 = new KongaSplitPane(1, this.scriptArea, this.importsArea);
      var1.setOneTouchExpandable(true);
      var1.setResizeWeight(1.0);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.scriptArea.requestFocus();
   }

   public String getScript() {
      return this.scriptArea.getScript();
   }

   public void setExecuteAction(Action var1) {
      this.executeAction = var1;
      this.scriptArea.setExecuteAction(var1);
   }

   public void setEnabled(boolean var1) {
      this.scriptArea.setEnabled(var1);
      this.importsArea.setEnabled(var1);
      if (this.executeAction != null) {
         this.executeAction.setEnabled(var1);
      }
   }

   public void setCommonImports(List<CommonImport> var1) {
      this.importsArea.setCommonImports(var1);
   }
}
