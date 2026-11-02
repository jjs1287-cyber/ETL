package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentDefinitionException;
import org.etlforge.integration.data.structure.text.TextStructureValidator;
import org.etlforge.integration.data.structure.text.ValidationIssue;
import org.etlforge.io.KongaIoUtils;

abstract class AbstractModelValidator {
   protected abstract TextStructureValidator getValidator();

   protected final void validate() throws TextDocumentDefinitionException {
      List var1 = this.getFatalIssues();
      if (var1 != null) {
         this.throwException(var1);
      }
   }

   private List<ValidationIssue> getFatalIssues() {
      ArrayList var1 = null;

      for (ValidationIssue var4 : this.getValidator().validate()) {
         if (var4.preventsSave()) {
            if (var1 == null) {
               var1 = Lists.newArrayList();
            }

            var1.add(var4);
         }
      }

      return var1;
   }

   private void throwException(List<ValidationIssue> var1) throws TextDocumentDefinitionException {
      StringBuilder var2 = new StringBuilder();

      for (ValidationIssue var4 : var1) {
         if (var4.preventsSave()) {
            if (var2.length() > 0) {
               var2.append(KongaIoUtils.LINE_BREAK);
            }

            var2.append(var4.getDescription());
         }
      }

      throw new TextDocumentDefinitionException(var2.toString());
   }
}
