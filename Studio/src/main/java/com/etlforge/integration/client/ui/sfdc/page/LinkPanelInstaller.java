package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.page.v4.AbstractSalesforceItemPageV4;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class LinkPanelInstaller {
   private final EntityPersistor persistor;
   private final EntityExplorerSupport explorer;

   public LinkPanelInstaller(EntityPersistor var1, EntityExplorerSupport var2) {
      this.persistor = var1;
      this.explorer = var2;
   }

   public <P extends AbstractSalesforceItemPage<?>> void installLinks(P var1, List<LinkType> var2) {
      WizardItemBrowser var3 = new WizardItemBrowser((SalesforceWizardEntity)var1.getObject());
      Operation var4 = var3.getImplementingOperation();
      if (var4 != null) {
         ImmutableList var5 = this.createLinks(var2, var3);
         var1.setLinks(new ImplementingObjectsLinkPanel(var5));
         var1.addSaveConsequence(new LinkSaveConsequence(var5, var4, this.persistor));
      }
   }

   private ImmutableList<LinkToImplementingObject> createLinks(List<LinkType> var1, WizardItemBrowser var2) {
      ArrayList var3 = Lists.newArrayList();

      for (LinkType var5 : var1) {
         var3.add(var5.getLink(this.explorer, var2));
      }

      return ImmutableList.copyOf(var3);
   }

   public <P extends AbstractSalesforceItemPageV4<?>> void installLinksV4(P var1, List<LinkType> var2) {
      WizardItemBrowser var3 = new WizardItemBrowser((SalesforceWizardEntity)var1.getObject());
      Operation var4 = var3.getImplementingOperation();
      if (var4 != null) {
         ImmutableList var5 = this.createLinks(var2, var3);
         var1.setLinks(new ImplementingObjectsLinkPanel(var5));
         var1.addSaveConsequence(new LinkSaveConsequence(var5, var4, this.persistor));
      }
   }
}
