package org.etlforge.integration.client.ui.admin.core.action;

import com.google.common.base.Supplier;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.client.ui.admin.core.console.ConsolePageHistoryPoint;

abstract class AbstractConsoleFactory implements Supplier<Editor> {
   protected abstract Editor createPage();

   public final Editor get() {
      Editor var1 = this.createPage();
      this.installHistoryPoint(var1);
      return var1;
   }

   private void installHistoryPoint(Editor var1) {
      if (var1 instanceof AbstractConsolePage) {
         ((AbstractConsolePage)var1).setHistoryPoint(new ConsolePageHistoryPoint(var1) {
            @Override
            protected Editor recreateEditor() {
               Editor var1x = AbstractConsoleFactory.this.createPage();
               AbstractConsoleFactory.this.configurePageWhenRestoredFromHistoryPoint(var1x);
               return var1x;
            }
         });
      }
   }

   protected void configurePageWhenRestoredFromHistoryPoint(Editor var1) {
   }
}
