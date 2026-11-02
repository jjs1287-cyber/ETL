package org.etlforge.integration.client.ui.interchange.component.tree;

import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.string.StringMatcher;

public final class ProjectPathRule implements StringSearchRule {
   @Override
   public String getName() {
      return "&Path";
   }

   @Override
   public String getDescription() {
      return null;
   }

   @Override
   public StringMatcher getMatcher() {
      return StringSearchRule.CONTAINS_IGNORE_CASE.getMatcher();
   }
}
