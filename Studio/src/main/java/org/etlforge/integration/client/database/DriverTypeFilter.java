package org.etlforge.integration.client.database;

import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverDescriptorVisitor;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;

public final class DriverTypeFilter implements DriverDescriptorFilter {
   private final DriverTypeFilter.Visitor visitor = new DriverTypeFilter.Visitor();
   private boolean acceptOdbc = true;
   private boolean acceptJdbc = true;

   @Override
   public boolean accepts(DriverDescriptor var1) {
      var1.accept(this.visitor);
      return this.visitor.accepted;
   }

   public void setAcceptsOdbcDrivers(boolean var1) {
      this.acceptOdbc = var1;
   }

   public void setAcceptsJdbcDrivers(boolean var1) {
      this.acceptJdbc = var1;
   }

   private class Visitor implements DriverDescriptorVisitor {
      public boolean accepted;

      private Visitor() {
      }

      @Override
      public void visit(JdbcDriverDescriptor var1) {
         this.accepted = DriverTypeFilter.this.acceptJdbc;
      }

      @Override
      public void visit(OdbcDriverDescriptor var1) {
         this.accepted = DriverTypeFilter.this.acceptOdbc;
      }
   }
}
