package org.etlforge.integration.client.database.ui.dnd;

import com.google.common.base.Functions;
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
import org.etlforge.integration.database.info.DatabaseColumn;

public final class TransferableDatabaseColumns implements Transferable {
   public static final DataFlavor FLAVOR = createFlavor();
   private static final DataFlavor[] supportedFlavors = new DataFlavor[]{FLAVOR, DataFlavor.stringFlavor};
   private final List<DatabaseColumn> tables;

   public TransferableDatabaseColumns(List<DatabaseColumn> var1) {
      this.tables = new TransferableDatabaseColumns.TransferType(var1);
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
         return Lists.newArrayList(this.tables);
      } else if (var1.equals(DataFlavor.stringFlavor)) {
         return Collections2.join(this.tables, Functions.toStringFunction(), SystemUtils.LINE_SEPARATOR);
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   private static DataFlavor createFlavor() {
      try {
         DataFlavor var0 = new DataFlavor("application/x-java-jvm-local-objectref;class=" + TransferableDatabaseColumns.TransferType.class.getName());
         var0.setHumanPresentableName("List of DatabaseColumns");
         return var0;
      } catch (ClassNotFoundException var1) {
         throw new RuntimeException(var1);
      }
   }

   private static final class TransferType extends ArrayList<DatabaseColumn> {
      public TransferType(Collection<? extends DatabaseColumn> var1) {
         super(var1);
      }
   }
}
