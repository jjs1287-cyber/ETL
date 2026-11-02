package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.ServerFile;

final class PageAfterStartFactory {
   private final XmlStructureWizardStartPage startPage;
   private final XmlStructure toEdit;
   private Map<ServerFile, XmlStructureWizardPage> nextPageCache;

   public PageAfterStartFactory(XmlStructureWizardStartPage var1, XmlStructure var2) {
      this.startPage = var1;
      this.toEdit = var2;
   }

   public XmlStructureWizardPage getNextPage() {
      assert !this.startPage.isSampleFileMode();
      ServerFile var1 = this.startPage.getSelectedFile();
      XmlStructureWizardPage var2 = this.getCachedNextPage(var1);
      if (var2 == null) {
         Collection var3 = this.startPage.getAvailableRoots();
         var2 = this.createNextPage(var3);
         this.nextPageCache.put(var1, var2);
      }

      return var2;
   }

   private XmlStructureWizardPage getCachedNextPage(ServerFile var1) {
      if (this.nextPageCache == null) {
         this.nextPageCache = Maps.newHashMap();
         return null;
      } else {
         return this.nextPageCache.get(var1);
      }
   }

   private XmlStructureWizardPage createNextPage(Collection<String> var1) {
      return var1.size() == 1 ? null : this.createRootNodePage(var1);
   }

   private XmlStructureWizardPage createRootNodePage(Collection<String> var1) {
      XmlStructureWizardRootNodePage var2 = new XmlStructureWizardRootNodePage(this.startPage.getWizard(), var1);
      if (this.toEdit != null) {
         var2.setStructureToEdit(this.toEdit);
      }

      return var2;
   }
}
