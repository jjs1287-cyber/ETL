package com.etlforge.integration.client.ui.sfdc.soql;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.util.Property;

public final class TestQueryAction extends AbstractAction {
   private final Property<String> soqlProperty;
   private final boolean queryAll;
   private QueryTester tester;

   public TestQueryAction(Property<String> var1, boolean var2) {
      super("&Test Query", ClientIcons.VALIDATE_16);
      this.soqlProperty = var1;
      this.queryAll = var2;
      this.bindToModel();
   }

   private void bindToModel() {
      this.soqlProperty.addListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            TestQueryAction.this.updateEnabledState();
         }
      });
   }

   private void updateEnabledState() {
      boolean var1 = false;
      if (this.tester != null) {
         String var2 = this.soqlProperty.get();
         var1 = StringUtils.isNotBlank(var2);
      }

      this.setEnabled(var1);
   }

   public void setTester(QueryTester var1) {
      this.tester = Preconditions.checkNotNull(var1);
      this.updateEnabledState();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestQueryAction.this.test();
         }
      });
   }

   private void test() {
      String var1 = this.soqlProperty.get();
      if (!StringUtils.isBlank(var1)) {
         this.tester.testQuery(var1, this.queryAll);
      }
   }
}
