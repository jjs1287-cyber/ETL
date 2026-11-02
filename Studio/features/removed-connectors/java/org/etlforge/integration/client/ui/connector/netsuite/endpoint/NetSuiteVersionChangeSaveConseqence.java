package org.etlforge.integration.client.ui.connector.netsuite.endpoint;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ChoiceMessage;
import org.etlforge.integration.client.ui.connector.netsuite.function.RefreshNetSuiteFunctionJobFactory;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.label.HtmlLabel;

public class NetSuiteVersionChangeSaveConseqence implements SaveConsequence {
   private final NetSuiteEndpointPageWrapper endpointPage;
   private final RefreshNetSuiteFunctionJobFactory refreshJobFactory;
   private ImmutableList<NetSuiteFunction> functions;
   private NetSuiteVersionChangeSaveConseqence.Choice usersDecision;

   public NetSuiteVersionChangeSaveConseqence(NetSuiteEndpointPageWrapper var1, RefreshNetSuiteFunctionJobFactory var2) {
      this.endpointPage = var1;
      this.refreshJobFactory = var2;
      this.functions = null;
   }

   @Override
   public boolean isAllowed() {
      this.usersDecision = NetSuiteVersionChangeSaveConseqence.Choice.JUST_SAVE;
      if (!this.endpointPage.isWsdlVersionChange()) {
         return true;
      }

      this.functions = this.endpointPage.collectFunctions();
      if (this.functions.isEmpty()) {
         return true;
      }

      this.usersDecision = this.getUsersDecision();
      return this.usersDecision != NetSuiteVersionChangeSaveConseqence.Choice.CANCEL;
   }

   @Override
   public void apply() {
      if (this.usersDecision == NetSuiteVersionChangeSaveConseqence.Choice.SAVE_AND_REFRESH) {
         this.refreshFunctions();
      }
   }

   private NetSuiteVersionChangeSaveConseqence.Choice getUsersDecision() {
      return UiThreadSupplier.call(new NetSuiteVersionChangeSaveConseqence.UserPrompt(this.endpointPage, this.functions));
   }

   private void refreshFunctions() {
      for (NetSuiteFunction var2 : this.functions) {
         Runnable var3 = this.refreshJobFactory.createRefreshJob(var2);
         Application.getWorker().submitForParallel(var3);
      }
   }

   private enum Choice {
      SAVE_AND_REFRESH("Save and Refresh"),
      JUST_SAVE("Save and Refresh Manually"),
      CANCEL("Cancel");

      private final String display;

      Choice(String var3) {
         this.display = var3;
      }

      @Override
      public String toString() {
         return this.display;
      }
   }

   private static class UserPrompt implements Supplier<NetSuiteVersionChangeSaveConseqence.Choice> {
      private final NetSuiteEndpointPageWrapper page;
      private final ImmutableList<NetSuiteFunction> functions;

      public UserPrompt(NetSuiteEndpointPageWrapper var1, ImmutableList<NetSuiteFunction> var2) {
         this.page = var1;
         this.functions = var2;
      }

      public NetSuiteVersionChangeSaveConseqence.Choice get() {
         UiProvider var1 = this.createLayout();
         ChoiceMessage var2 = new ChoiceMessage<>(var1, "Refresh NetSuite Functions?", NetSuiteVersionChangeSaveConseqence.Choice.values());
         var2.setType(Alert.Type.WARNING);
         var2.setDefaultChoice(NetSuiteVersionChangeSaveConseqence.Choice.SAVE_AND_REFRESH);
         var2.setChoiceIfCancelled(NetSuiteVersionChangeSaveConseqence.Choice.CANCEL);
         return (NetSuiteVersionChangeSaveConseqence.Choice)var2.show(this.page.getWindow().getFrame());
      }

      private UiProvider createLayout() {
         IntegrationEntityList var1 = new IntegrationEntityList(this.functions);
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
         String var3 = "You are about to change the structure of netsuite functions and their dependent transformations. The following functions must be refreshed in order for the new structure to be applied.";
         var2.north(new HtmlLabel(var3, 400)).center(var1);
         return var2;
      }
   }
}
