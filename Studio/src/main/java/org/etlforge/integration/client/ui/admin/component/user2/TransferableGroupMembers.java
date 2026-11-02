package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.collect.Lists;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.collections.Collections2;
import org.etlforge.util.name.Nameables;

final class TransferableGroupMembers implements Transferable {
   public static final DataFlavor FLAVOR = createFlavor();
   private static final DataFlavor[] supportedFlavors = new DataFlavor[]{FLAVOR, DataFlavor.stringFlavor};
   private final List<GroupMember> groups;

   public TransferableGroupMembers(List<GroupMember> var1) {
      this.groups = new TransferableGroupMembers.TransferType(var1);
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
      if (var1.equals(FLAVOR)) {
         return Lists.newArrayList(this.groups);
      } else if (var1.equals(DataFlavor.stringFlavor)) {
         return this.createStringRepresentation();
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   private String createStringRepresentation() {
      return Collections2.join(this.groups, Nameables.toNameFunction(), SystemUtils.LINE_SEPARATOR);
   }

   private static DataFlavor createFlavor() {
      try {
         DataFlavor var0 = new DataFlavor("application/x-java-jvm-local-objectref;class=" + TransferableGroupMembers.TransferType.class.getName());
         var0.setHumanPresentableName("List of group members");
         return var0;
      } catch (ClassNotFoundException var1) {
         throw new RuntimeException(var1);
      }
   }

   private static class TransferType extends ArrayList<GroupMember> {
      public TransferType(Collection<? extends GroupMember> var1) {
         super(var1);
      }
   }
}
