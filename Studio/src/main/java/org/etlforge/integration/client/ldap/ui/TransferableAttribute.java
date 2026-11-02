package org.etlforge.integration.client.ldap.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.List;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;

public final class TransferableAttribute implements Transferable {
   public static final DataFlavor FLAVOR = new DataFlavor(List.class, "LdapStructureAttribute");
   private final LdapStructureAttribute attribute;

   public TransferableAttribute(LdapStructureAttribute var1) {
      this.attribute = var1;
   }

   @Override
   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException {
      if (var1 == FLAVOR) {
         return this.attribute;
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[]{FLAVOR};
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor var1) {
      return FLAVOR.equals(var1);
   }

   public LdapStructureAttribute getAttribute() {
      return this.attribute;
   }
}
