package org.etlforge.integration.client.database.ui.drivers;

import javax.swing.Icon;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverDescriptorVisitor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelDecorator;

public final class DriverDescriptorUi implements ListDecorator, LabelDecorator {
   private final DriverDescriptor driver;
   private final String text;
   private final Icon icon;

   public DriverDescriptorUi(DriverDescriptor var1) {
      this.driver = var1;
      DriverDescriptorUi.TextAndIconGenerator var2 = new DriverDescriptorUi.TextAndIconGenerator();
      var1.accept(var2);
      this.text = var2.text;
      this.icon = var2.icon;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.text);
      var1.setToolTip(this.text);
      var1.setIcon(this.icon);
   }

   @Override
   public void decorate(Label var1) {
      var1.setText(MnemonicStringFactory.NO_MNEMONIC_FACTORY.fromString(this.text));
      var1.setToolTip(this.text);
      var1.setIcon(this.icon);
   }

   public DriverDescriptor getDriver() {
      return this.driver;
   }

   @Override
   public String toString() {
      return this.driver.getName();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof DriverDescriptorUi ? this.driver.equals(((DriverDescriptorUi)var1).driver) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.driver.hashCode();
   }

   private static final class TextAndIconGenerator implements DriverDescriptorVisitor {
      private String text;
      private Icon icon;

      private TextAndIconGenerator() {
      }

      @Override
      public void visit(JdbcDriverDescriptor var1) {
         this.text = var1.getName() + " [JDBC]";
         this.icon = DatabaseUiResources.ICONS.getIcon("JavaCup.16");
      }

      @Override
      public void visit(OdbcDriverDescriptor var1) {
         this.text = var1.getName() + " [ODBC]";
         this.icon = null;
      }
   }
}
