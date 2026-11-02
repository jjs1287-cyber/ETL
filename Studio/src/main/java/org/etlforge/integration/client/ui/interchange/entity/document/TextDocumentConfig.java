package org.etlforge.integration.client.ui.interchange.entity.document;

import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class TextDocumentConfig extends AbstractEntityConfig {
   public TextDocumentConfig() {
      super(EntityType.Document);
      this.setSearchAndReplaceMetaData(new TextDocumentSearchAndReplaceProperties());
      this.setSearchValuesCollector(new TextDocumentSearchValuesCollector());
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      IntegrationProject var2 = this.getProject();
      return new TextDocumentPage(var2, (TextDocument)var1);
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new TextDocumentToolTip((TextDocument)var1);
   }
}
