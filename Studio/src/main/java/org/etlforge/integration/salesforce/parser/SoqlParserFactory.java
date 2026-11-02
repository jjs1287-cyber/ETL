package org.etlforge.integration.salesforce.parser;

import org.etlforge.application.config.ApplicationConfiguration;

public final class SoqlParserFactory {
   public static SoqlParser newParser() {
      boolean var0 = ApplicationConfiguration.getBoolean("SFDC.RegExParser", false);
      return var0 ? new RegexSoqlParser() : new TokenizingSoqlParser();
   }

   private SoqlParserFactory() {
   }
}
