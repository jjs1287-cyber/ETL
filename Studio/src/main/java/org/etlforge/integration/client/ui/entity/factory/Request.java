package org.etlforge.integration.client.ui.entity.factory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;

public final class Request {
   private final EntityType entityType;
   private final Request.Source source;
   private final boolean openEditor;
   private final Optional<Folder> parent;

   public static Request defaultRequest(EntityType var0) {
      return new Request(var0, Request.Source.OTHER, true, null);
   }

   public static Request.RequestBuilder builder(EntityType var0) {
      return new Request.RequestBuilder(var0);
   }

   public Request(EntityType var1, Request.Source var2, boolean var3, @Nullable Folder var4) {
      this.entityType = Preconditions.checkNotNull(var1, "entityType");
      this.source = Preconditions.checkNotNull(var2, "source");
      this.openEditor = Preconditions.checkNotNull(var3, "openEditor");
      this.parent = Optional.fromNullable(var4);
   }

   public EntityType getEntityType() {
      return this.entityType;
   }

   public Request.Source getSource() {
      return this.source;
   }

   public boolean openEditor() {
      return this.openEditor;
   }

   public Optional<Folder> getParent() {
      return this.parent;
   }

   public Request forType(EntityType var1) {
      return var1 == this.entityType ? this : new Request(var1, this.source, this.openEditor, this.parent.orNull());
   }

   public Request withParent(Folder var1) {
      return var1 == this.parent.orNull() ? this : new Request(this.entityType, this.source, this.openEditor, var1);
   }

   public static final class RequestBuilder {
      private final EntityType entityType;
      private Request.Source source = Request.Source.OTHER;
      private boolean openEditor = true;
      private Folder parent;

      public RequestBuilder(EntityType var1) {
         this.entityType = Preconditions.checkNotNull(var1, "entityType");
      }

      public Request.RequestBuilder from(Request.Source var1) {
         this.source = Preconditions.checkNotNull(var1, "source");
         return this;
      }

      public Request.RequestBuilder openEditor(boolean var1) {
         this.openEditor = var1;
         return this;
      }

      public Request.RequestBuilder withParent(Folder var1) {
         this.parent = var1;
         return this;
      }

      public Request build() {
         return new Request(this.entityType, this.source, this.openEditor, this.parent);
      }
   }

   public enum Source {
      WIZARD,
      OTHER;
   }
}
