package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.DefaultSObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public class WscPartnerConnectionSchemaProvider {
   private final PartnerConnection connection;

   public WscPartnerConnectionSchemaProvider(PartnerConnection var1) {
      this.connection = Preconditions.checkNotNull(var1, "connection");
   }

   public List<SObjectSchema> listAllObjects() throws ConnectionException {
      List var1 = this.listObjectNames();
      return this.describeObjects(var1);
   }

   private List<String> listObjectNames() throws ConnectionException {
      ArrayList var1 = Lists.newArrayList();
      DescribeGlobalResult var2 = this.connection.describeGlobal();
      DescribeGlobalSObjectResult[] var3 = var2.getSobjects();

      for (DescribeGlobalSObjectResult var7 : var3) {
         var1.add(var7.getName());
      }

      return var1;
   }

   public SObjectSchema describeObject(String var1) throws ConnectionException {
      DescribeSObjectResult var2 = this.connection.describeSObject(var1);
      return SchemaBuilder.createFlatDescription(var2);
   }

   public List<SObjectSchema> describeObjects(List<String> var1) throws ConnectionException {
      ArrayList var2 = Lists.newArrayList();

      for (List var4 : ListSplitter.split(var1, 100)) {
         DescribeSObjectResult[] var5 = this.connection.describeSObjects((String[]) var4.toArray(new String[var4.size()]));

         for (DescribeSObjectResult var9 : var5) {
            DefaultSObjectSchema var10 = SchemaBuilder.createFlatDescription(var9);
            var2.add(var10);
         }
      }

      return var2;
   }
}
