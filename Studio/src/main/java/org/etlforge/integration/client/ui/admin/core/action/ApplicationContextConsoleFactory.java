package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.Application;
import org.etlforge.application.ui.window.editor.Editor;

public final class ApplicationContextConsoleFactory extends AbstractConsoleFactory {
   private final String beanId;

   public ApplicationContextConsoleFactory(String var1) {
      this.beanId = var1;
   }

   @Override
   protected Editor createPage() {
      return Application.getBean(this.beanId, Editor.class);
   }
}
