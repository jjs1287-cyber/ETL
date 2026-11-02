package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.layout;

import com.google.common.collect.Maps;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.util.persist.Persistor;

final class EmailNodeLayoutCache {
   private static final String EMAIL = "Email";
   private static final String EMAIL_ID = "emailId";
   private final Map<EmailMessageId, Rectangle> nodeBounds = Maps.newHashMap();

   public EmailNodeLayoutCache() {
   }

   public EmailNodeLayoutCache(Persistor var1) {
      this();

      try {
         HashMap var2 = Maps.newHashMap();

         for (Persistor var4 : var1.getChildren("Email")) {
            EmailMessageId var5 = new EmailMessageId(var4.getString("emailId"));
            Rectangle var6 = PersistUtils.readBounds(var4);
            var2.put(var5, var6);
         }

         this.nodeBounds.putAll(var2);
      } catch (RuntimeException var7) {
         ErrorLog.log(EmailNodeLayoutCache.class, "Failed to restore the EmailNodeLayoutCache", var7);
      }
   }

   public Rectangle getBounds(EmailMessageId var1) {
      Rectangle var2 = this.nodeBounds.get(var1);
      return var2 != null ? new Rectangle(var2) : null;
   }

   public void put(EmailMessageId var1, Rectangle var2) {
      this.nodeBounds.put(var1, new Rectangle(var2));
   }

   public void writeTo(Persistor var1) {
      for (Entry var3 : this.nodeBounds.entrySet()) {
         Persistor var4 = var1.createChild("Email");
         var4.putString("emailId", ((EmailMessageId)var3.getKey()).toString());
         PersistUtils.writeBounds(var4, (Rectangle)var3.getValue());
      }
   }
}
