package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;
import org.etlforge.ui.wait.WaitService;

final class FieldListChangeConsequence implements SaveConsequence {
   private final SalesforceQuery query;
   private final SoqlModel soqlModel;
   private ParsedQuery oldQueryInfo;
   private boolean needsProcessing;
   private WaitService waitService = WaitService.NONE;
   private QueryStructure newQueryStructure;
   private QueryStructure oldQueryStructure;

   public FieldListChangeConsequence(SalesforceQuery var1, SoqlModel var2) {
      this.query = var1;
      this.soqlModel = var2;
      this.oldQueryInfo = this.getOldQueryInfo();
   }

   private ParsedQuery getOldQueryInfo() {
      String var1 = this.query.getQueryString();
      SoqlParser var2 = SoqlParserFactory.newParser();
      return var2.parse(var1);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public boolean isAllowed() {
      this.needsProcessing = false;
      ParsedQuery var1 = this.soqlModel.getParsedQuery();
      if (var1.isInvalid()) {
         return false;
      }

      this.newQueryStructure = new QueryStructure(var1);
      this.oldQueryStructure = new QueryStructure(this.oldQueryInfo);
      if (this.newQueryStructure.equals(this.oldQueryStructure)) {
         this.oldQueryInfo = this.soqlModel.getParsedQuery();
         return true;
      }

      boolean var2 = this.askUserForPermission();
      if (var2) {
         this.oldQueryInfo = this.soqlModel.getParsedQuery();
      }

      return var2;
   }

   private boolean askUserForPermission() {
      boolean var1 = false;
      if (this.isTextTarget()) {
         UpdateResponseQuestion.Choice var2 = UpdateResponseQuestion.ask();
         var1 = var2 != UpdateResponseQuestion.Choice.CANCEL;
         this.needsProcessing = var2 == UpdateResponseQuestion.Choice.CONTINUE_AND_UPDATE;
      } else {
         var1 = this.giveResponseModificationPromptForNonTextTargets();
      }

      return var1;
   }

   private boolean isTextTarget() {
      WizardItemInfo var1 = new WizardItemInfo(this.query);
      Optional var2 = var1.getResponseStructureType();
      return var2.isPresent() && (var2.get() == DataStructureType.Text || var2.get() == DataStructureType.ComplexText);
   }

   private boolean giveResponseModificationPromptForNonTextTargets() {
      String var1 = "By changing the query string you might also have to update the response mappings.";
      Answer var2 = Ask.continueOrCancel(ApplicationUi.getAppWinFrame(), var1, "Query Change", Alert.Option.CONTINUE, Alert.Type.INFORMATION);
      return var2.isContinue();
   }

   @Override
   public void apply() {
      if (this.needsProcessing) {
         FieldListChange.MappingDiff var1 = this.createMappingDiff(this.newQueryStructure, this.oldQueryStructure);
         FieldListChange var2 = new FieldListChange(this.query, this.newQueryStructure, var1);
         KongaSwingWorker.runCallableWhileWaiting(var2, this.waitService);
      }
   }

   private FieldListChange.MappingDiff createMappingDiff(QueryStructure var1, QueryStructure var2) {
      if (var2.isFlat() && var1.isFlat()) {
         return new FlatToFlatMappingDiff(var2, var1);
      }

      if (var2.isFlat() && var1.isNested()) {
         return new FlatToNestedMappingDiff(var2, var1);
      }

      if (var2.isNested() && var1.isFlat()) {
         return new NestedToFlatMappingDiff(var2, var1);
      }

      assert var2.isNested() && var1.isNested();
      return new NestedToNestedMappingDiff(var2, var1);
   }
}
