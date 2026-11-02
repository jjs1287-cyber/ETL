package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Strings;
import javax.swing.JLabel;
import org.etlforge.integration.client.ui.connector.function.ConnectorCallPage;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.util.Fonts;

public final class ReadOnlyFunctionProperty implements FunctionProperty {
   private final String name;
   private final String value;

   public ReadOnlyFunctionProperty(String var1, String var2) {
      this.name = ArgCheck.checkNotEmpty(var1, "name");
      this.value = Strings.nullToEmpty(var2);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      JLabel var2 = new JLabel(this.value);
      Fonts.convertToBold(var2);
      var1.addRow(new JLabel(this.name), var2);
   }

   @Override
   public void associateWithPage(ConnectorCallPage var1) {
   }
}
