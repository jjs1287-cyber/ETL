package org.etlforge.integration.data.entity.operation.options;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.PropertyBasedEntityLink;

public final class FileDestination {
   private final Optional<TargetId> targetId;
   private final Optional<EmailMessageId> emailId;

   public static final FileDestination none() {
      return new FileDestination(Optional.absent(), Optional.absent());
   }

   public FileDestination(Operation var1, String var2, String var3) {
      this(EntityUtils.readId(TargetId.class, var1, var2), EntityUtils.readId(EmailMessageId.class, var1, var3));
   }

   public FileDestination(Target var1, EmailMessage var2) {
      this.targetId = EntityUtils.getId(var1, TargetId.class);
      this.emailId = EntityUtils.getId(var2, EmailMessageId.class);
   }

   private FileDestination(Optional<TargetId> var1, Optional<EmailMessageId> var2) {
      this.targetId = Preconditions.checkNotNull(var1, "targetId");
      this.emailId = Preconditions.checkNotNull(var2, "emailId");
   }

   public boolean isEmpty() {
      return !this.targetId.isPresent() && !this.emailId.isPresent();
   }

   public Optional<TargetId> getTargetId() {
      return this.targetId;
   }

   public Optional<EmailMessageId> getEmailId() {
      return this.emailId;
   }

   List<EntityLink> collectLinks(Operation var1, IntegrationEntityLookup var2, String var3, String var4) {
      ArrayList var5 = Lists.newArrayList();
      Optional var6 = EntityUtils.lookupInstance(var2, this.targetId, Target.class);
      if (var6.isPresent()) {
         var5.add(new PropertyBasedEntityLink((IntegrationEntity)var6.get(), var1, var3));
      }

      Optional var7 = EntityUtils.lookupInstance(var2, this.emailId, EmailMessage.class);
      if (var7.isPresent()) {
         var5.add(new PropertyBasedEntityLink((IntegrationEntity)var7.get(), var1, var4));
      }

      return var5;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.targetId, this.emailId);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof FileDestination)) {
         return false;
      }

      FileDestination var2 = (FileDestination)var1;
      return this.targetId.equals(var2.targetId) && this.emailId.equals(var2.emailId);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("target", this.targetId).add("email", this.emailId).toString();
   }
}
