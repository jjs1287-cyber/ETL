package org.etlforge.integration.client.ui.entity;

import com.google.common.collect.Lists;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.name.Nameables;

public class TransferableIntegrationEntities implements Transferable {
   public static final DataFlavor ENTITIES_FLAVOR = createFlavor();
   private static final DataFlavor[] supportedFlavors = new DataFlavor[]{ENTITIES_FLAVOR, DataFlavor.stringFlavor};
   private final List<IntegrationEntity> entities;

   public TransferableIntegrationEntities(List<? extends IntegrationEntity> var1) {
      this.entities = new TransferableIntegrationEntities.TransferClass(var1);
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return (DataFlavor[])supportedFlavors.clone();
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor var1) {
      for (DataFlavor var5 : supportedFlavors) {
         if (var5.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException {
      if (ENTITIES_FLAVOR.equals(var1)) {
         return Lists.newArrayList(this.entities);
      } else if (DataFlavor.stringFlavor.equals(var1)) {
         return Collections2.join(this.entities, Nameables.toNameFunction(), SystemUtils.LINE_SEPARATOR);
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   private static DataFlavor createFlavor() {
      try {
         DataFlavor var0 = new DataFlavor("application/x-java-jvm-local-objectref;class=" + TransferableIntegrationEntities.TransferClass.class.getName());
         var0.setHumanPresentableName("List of project items");
         return var0;
      } catch (ClassNotFoundException var1) {
         throw new RuntimeException(var1);
      }
   }

   private static class TransferClass extends ArrayList<IntegrationEntity> {
      public TransferClass(List<? extends IntegrationEntity> var1) {
         super(var1);
      }
   }
}
