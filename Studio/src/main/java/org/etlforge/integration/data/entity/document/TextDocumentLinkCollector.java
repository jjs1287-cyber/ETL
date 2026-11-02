package org.etlforge.integration.data.entity.document;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.ScriptArgument;
import org.etlforge.integration.data.structure.text.validation.ScriptValidationFilter;
import org.etlforge.integration.data.structure.text.validation.ValidationRule;

public final class TextDocumentLinkCollector {
   private final TextDocument doc;
   private final IntegrationEntityLookup lookup;
   private List<EntityLink> links;

   public TextDocumentLinkCollector(TextDocument var1, IntegrationEntityLookup var2) {
      this.doc = var1;
      this.lookup = var2;
   }

   public List<? extends EntityLink> collect() {
      this.links = Lists.newArrayList();
      TextStructure var1 = this.doc.getDataStructure();
      if (var1 instanceof SimpleTextStructure) {
         this.collectValidationScripts((SimpleTextStructure)var1);
      }

      return this.links;
   }

   private void collectValidationScripts(SimpleTextStructure var1) {
      for (Pair var3 : var1.getFieldsWithValidation(new ScriptValidationFilter())) {
         Optional var4 = ((FieldValidation)var3.second).getRule();
         OperatorArgument var5 = ((ValidationRule)var4.get()).getArgument();
         Script var6 = this.lookup.getEntity(((ScriptArgument)var5).getId(), Script.class);
         if (var6 != null) {
            this.links.add(new FieldValidationScript(var6, this.doc));
         }
      }
   }
}
