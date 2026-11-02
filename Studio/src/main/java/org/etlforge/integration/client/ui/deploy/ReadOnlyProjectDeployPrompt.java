package org.etlforge.integration.client.ui.deploy;

import com.google.common.collect.Lists;
import java.util.Collection;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.deploy.DeployPrompt;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.label.HtmlLabel;

public final class ReadOnlyProjectDeployPrompt implements DeployPrompt {
   @Override
   public Collection<? extends IntegrationEntity> getDirtyItemsToDeploy() {
      return Lists.newArrayList();
   }

   @Override
   public boolean prompt(Collection<? extends IntegrationEntity> var1, Collection<? extends IntegrationEntity> var2) {
      if (this.isNotEmpty(var1)) {
         this.denyRequiredItems(var1);
         return false;
      } else {
         return this.isNotEmpty(var2) ? this.askIfContinueWithoutDeplyingChanges(var2) : true;
      }
   }

   private boolean isNotEmpty(Collection<?> var1) {
      return var1 != null && !var1.isEmpty();
   }

   private void denyRequiredItems(Collection<? extends IntegrationEntity> var1) {
      ReadOnlyProjectDeployPrompt.ItemUi var2 = new ReadOnlyProjectDeployPrompt.ItemUi(
         var1, "This action requires new items to be deployed, but this project is read-only and does not support deploy.", null
      );
      Alert.error(var2, "Cannot Deploy");
   }

   private boolean askIfContinueWithoutDeplyingChanges(Collection<? extends IntegrationEntity> var1) {
      ReadOnlyProjectDeployPrompt.ItemUi var2 = new ReadOnlyProjectDeployPrompt.ItemUi(
         var1,
         "The following items have changed, but this project is read-only so the changes cannot be deployed.",
         "Do you want to continue without deploying?"
      );
      Answer var3 = Ask.yesOrNo(null, var2, "Changes Cannot Be Deployed", Alert.Option.YES, Alert.Type.WARNING);
      return var3.isYes();
   }

   private static final class ItemUi implements UiProvider {
      private final IntegrationEntityList list;
      private final HtmlLabel top;
      private final HtmlLabel bottom;

      public ItemUi(Collection<? extends IntegrationEntity> var1, String var2, String var3) {
         this.list = new IntegrationEntityList(var1);
         this.top = new HtmlLabel(var2, 400);
         this.bottom = var3 != null ? new HtmlLabel(var3, 400) : null;
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.northToSouth(this.top, this.list, this.bottom);
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.list.requestFocus();
      }
   }
}
