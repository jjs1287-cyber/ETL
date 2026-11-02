package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.parser.CaseCorrector;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlErrorReporter;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;

public final class InputtedQueryStringProcessor {
   private final SupportedQueryPolicy supportedQueryPolicy;
   private Optional<CaseCorrector> caseCorrector = Optional.absent();
   private SoqlErrorReporter errorReporter = SoqlErrorReporter.STDERR;

   public InputtedQueryStringProcessor(SupportedQueryPolicy var1) {
      this.supportedQueryPolicy = Preconditions.checkNotNull(var1);
   }

   public void setErrorReporter(SoqlErrorReporter var1) {
      this.errorReporter = Preconditions.checkNotNull(var1);
   }

   public void correctForCase(CaseCorrector var1) {
      this.caseCorrector = Optional.of(var1);
   }

   public ParsedQuery process(String var1) {
      SoqlParser var2 = SoqlParserFactory.newParser();
      ParsedQuery var3 = var2.parse(var1);
      if (var3.isInvalid()) {
         this.errorReporter.reportError("Invalid Query", var3.getErrorMessage());
         return var3;
      }

      if (!this.supportedQueryPolicy.isSupported(var3)) {
         String var4 = this.supportedQueryPolicy.whyNot();
         this.errorReporter.reportError("Not Supported", var4);
         return ParsedQuery.invalid(var4);
      }

      if (this.caseCorrector.isPresent()) {
         var3 = this.correctForCase(var3);
      }

      return var3;
   }

   private ParsedQuery correctForCase(ParsedQuery var1) {
      try {
         return this.caseCorrector.get().correctCase(var1);
      } catch (SalesforceException var3) {
         this.errorReporter.reportError("Error", var3.getMessage());
         return ParsedQuery.invalid(var3.getMessage());
      }
   }
}
