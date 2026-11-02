package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.FieldDelimiter;

public final class SingleFieldTextDocumentProvider extends AbstractTextDocumentProvider {
   private final String documentName;
   private final String fieldName;

   public SingleFieldTextDocumentProvider(IntegrationProject var1, String var2, String var3) {
      super(var1);
      this.documentName = var2;
      this.fieldName = var3;
   }

   @Override
   protected final TextDocument createNewDocument() {
      String var1 = EntityUtils.getAvailableChildName(this.getParent(), this.documentName);
      TextDocument var2 = new TextDocument(var1);
      var2.setDocType(2);
      SimpleTextStructure var3 = new SimpleTextStructure("Dummy", false);
      Field var4 = new Field(0, this.fieldName, FieldDataType.String.name(), null, null, 1, false);
      var3.setFieldList(Lists.newArrayList(var4));
      var2.setStructure(var3);
      var2.setDelimiter(FieldDelimiter.COMMA);
      var2.setStringQualifier("\"");
      var2.setTransient(true);
      var2.setPartOfSalesforceWizard(true);
      return var2;
   }

   @Override
   protected final boolean isSuitable(TextDocument var1) {
      return var1.getName().equals(this.documentName) ? this.validateStructure(var1) : false;
   }

   private boolean validateStructure(TextDocument var1) {
      TextStructure var2 = var1.getDataStructure();
      if (!(var2 instanceof SimpleTextStructure)) {
         return false;
      }

      List var3 = ((SimpleTextStructure)var2).getFieldList();
      return var3.size() == 1 && this.fieldName.equals(((Field)var3.get(0)).getName());
   }
}
