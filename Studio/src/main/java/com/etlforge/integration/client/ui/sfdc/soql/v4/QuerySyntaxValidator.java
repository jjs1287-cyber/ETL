package com.etlforge.integration.client.ui.sfdc.soql.v4;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;

public final class QuerySyntaxValidator implements QueryValidator {
   private final SoqlParser parser = SoqlParserFactory.newParser();
   private String objectName;

   @Override
   public String validate(String var1) {
      this.objectName = null;
      if (StringUtils.isBlank(var1)) {
         return "The query string cannot be empty.";
      }

      this.objectName = this.parser.extractObjectName(var1);
      return this.objectName == null ? "The name of the queried object cannot be found. Is this a valid query string?" : null;
   }

   public String getObjectName() {
      return this.objectName;
   }
}
