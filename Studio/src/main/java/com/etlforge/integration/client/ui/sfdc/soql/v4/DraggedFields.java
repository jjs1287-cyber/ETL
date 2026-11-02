package com.etlforge.integration.client.ui.sfdc.soql.v4;

import com.google.common.base.Function;
import java.util.Collections;
import java.util.List;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.salesforce.SObject;

public final class DraggedFields {
   public final SObject object;
   public final List<SObject.Field> fields;

   public DraggedFields(SObject var1, SObject.Field var2) {
      this(var1, Collections.singletonList(var2));
   }

   public DraggedFields(SObject var1, List<SObject.Field> var2) {
      this.object = var1;
      this.fields = var2;
   }

   public String getListOfFieldNames() {
      Function var1 = new Function<SObject.Field, String>() {
         public String apply(SObject.Field var1) {
            return var1.getName();
         }
      };
      return Collections2.join(this.fields, var1, ", ");
   }
}
