package org.etlforge.integration.server.implementation.webservice.project.info.impl;

import java.rmi.RemoteException;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.project.info.DownloadProjectInfoCallback;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl.CheckoutInterchangeProjectWsImplWrapper;

public final class ProjectInfoProviderImpl extends CppWebServiceServerCall implements ProjectInfoProvider {
   private final Iterable<EntityType> supportedEntityTypes;

   public ProjectInfoProviderImpl(CppWebServiceConfiguration var1, Iterable<EntityType> var2) {
      super(var1);
      this.supportedEntityTypes = var2;
   }

   @Override
   public void downloadProjectInfo(DownloadProjectInfoCallback var1) {
      try {
         CheckoutInterchangeProjectWsImplWrapper var2 = new CheckoutInterchangeProjectWsImplWrapper(this.getConfiguration(), this.supportedEntityTypes);
         List var3 = var2.getProjectTree(var1);
         if (var3 != null) {
            var1.projectInfoDownloaded(var3);
         }
      } catch (RemoteException var4) {
         var1.caught(this.convert(var4));
      } catch (ServiceException var5) {
         var1.caught(var5);
      }
   }
}
