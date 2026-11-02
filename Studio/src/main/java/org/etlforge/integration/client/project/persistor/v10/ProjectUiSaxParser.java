package org.etlforge.integration.client.project.persistor.v10;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;

final class ProjectUiSaxParser {
   private List<EntityDescriptor> openPages;
   private List<EntityDescriptor> expanded;
   private EntityDescriptor active;

   public void loadUiData(File var1) throws XmlLoadingException {
      this.loadUiData(Files.newInputStreamSupplier(var1));
   }

   @VisibleForTesting
   void loadUiData(InputSupplier<? extends InputStream> var1) throws XmlLoadingException {
      this.openPages = Lists.newArrayList();
      this.expanded = Lists.newArrayList();
      this.active = null;

      try {
         KongaSaxParser var2 = new KongaSaxParser(new ProjectUiSaxParser.Handler());
         var2.parseInput(var1);
      } catch (Exception var3) {
         throw new XmlLoadingException(var3);
      }
   }

   public List<EntityDescriptor> getOpenPages() {
      return this.openPages;
   }

   public List<EntityDescriptor> getExpandedItems() {
      return this.expanded;
   }

   public EntityDescriptor getActiveOnStartup() {
      return this.active;
   }

   private class Handler {
      private Handler() {
      }

      @StartElementHandler("UiState/OpenPages/Page")
      public void page(Attributes var1) {
         EntityDescriptor var2 = this.toEntityDescriptor(var1);
         ProjectUiSaxParser.this.openPages.add(var2);
         if (KongaXmlUtils.isTrue(var1, "active")) {
            ProjectUiSaxParser.this.active = var2;
         }
      }

      private EntityDescriptor toEntityDescriptor(Attributes var1) {
         return new EntityDescriptor(var1.getValue("type"), var1.getValue("id_val"));
      }

      @StartElementHandler("UiState/ExpandedNodes/Node")
      public void node(Attributes var1) {
         EntityDescriptor var2 = this.toEntityDescriptor(var1);
         ProjectUiSaxParser.this.expanded.add(var2);
      }
   }
}
