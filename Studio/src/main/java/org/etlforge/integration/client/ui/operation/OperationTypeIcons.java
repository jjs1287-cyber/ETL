package org.etlforge.integration.client.ui.operation;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.operation.OperationType;

public final class OperationTypeIcons {
   public static Icon smallIcon(OperationType var0) {
      return iconOfSize(var0, 16);
   }

   public static Icon largeIcon(OperationType var0) {
      return iconOfSize(var0, 32);
   }

   private static Icon iconOfSize(OperationType var0, int var1) {
      String var2 = "OpType." + var0.name() + "." + var1;
      return ClientIcons.getIcon(var2);
   }

   private OperationTypeIcons() {
   }
}
