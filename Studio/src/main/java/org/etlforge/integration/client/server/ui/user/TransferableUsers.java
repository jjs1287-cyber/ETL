package org.etlforge.integration.client.server.ui.user;

import com.google.common.collect.Lists;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.collections.Collections2;

public class TransferableUsers implements Transferable {
   public static final DataFlavor FLAVOR = createFlavor();
   private static final DataFlavor[] supportedFlavors = new DataFlavor[]{FLAVOR, DataFlavor.stringFlavor};
   private final List<UserUi> users;

   public TransferableUsers(List<UserUi> var1) {
      this.users = new TransferableUsers.TransferType(var1);
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return (DataFlavor[])supportedFlavors.clone();
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor var1) {
      return ArrayUtils.contains(supportedFlavors, var1);
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException {
      if (var1.equals(FLAVOR)) {
         return Lists.newArrayList(this.users);
      } else if (var1.equals(DataFlavor.stringFlavor)) {
         return Collections2.join(this.users, UserUi.TO_NAME, SystemUtils.LINE_SEPARATOR);
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   private static DataFlavor createFlavor() {
      try {
         DataFlavor var0 = new DataFlavor("application/x-java-jvm-local-objectref;class=" + TransferableUsers.TransferType.class.getName());
         var0.setHumanPresentableName("List of group members");
         return var0;
      } catch (ClassNotFoundException var1) {
         throw new RuntimeException(var1);
      }
   }

   private static class TransferType extends ArrayList<UserUi> {
      public TransferType(Collection<? extends UserUi> var1) {
         super(var1);
      }
   }
}
