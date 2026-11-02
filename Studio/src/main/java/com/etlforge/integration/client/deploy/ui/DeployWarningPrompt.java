package com.etlforge.integration.client.deploy.ui;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.DoNotShowAgainQuestion;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

abstract class DeployWarningPrompt<T> {
   private final IntegrationProject project;
   private final List<T> items;
   private final String title;
   private final String message;
   private final BooleanPreference preference;

   protected DeployWarningPrompt(IntegrationProject var1, List<T> var2, String var3, String var4, BooleanPreference var5) {
      Preconditions.checkArgument(!var2.isEmpty(), "items may not be empty");
      this.project = Preconditions.checkNotNull(var1, "project");
      this.items = Lists.newArrayList(var2);
      this.title = var3;
      this.message = var4;
      this.preference = var5;
   }

   public final boolean isGreenlighted() {
      return (Boolean)UiThreadSupplier.call(new DeployWarningPrompt.Message());
   }

   protected abstract UiProvider createUi(IntegrationProject var1, List<T> var2);

   private class Message implements Supplier<Boolean> {
      private Message() {
      }

      public Boolean get() {
         UiProvider var1 = this.doLayout();
         return new DoNotShowAgainQuestion(
               var1, DeployWarningPrompt.this.title, DeployWarningPrompt.this.preference, Alert.Option.YES_NO, Alert.Option.YES, Alert.Type.WARNING
            )
            .ask(UiUtils.getActiveFrame());
      }

      private UiProvider doLayout() {
         return new BorderLayoutBuilder(0, 5)
            .northToSouth(
               DeployWarningPrompt.this.message,
               DeployWarningPrompt.this.createUi(DeployWarningPrompt.this.project, DeployWarningPrompt.this.items),
               "Do you want to continue?"
            );
      }
   }
}
