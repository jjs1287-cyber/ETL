package org.etlforge.integration.client.project;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.xml.XmlStringBuilder;

public final class ProjectUiState {
   private final IntegrationEntityLookup dataLookup;
   private List<EntityDescriptor> displayOnStartUp;
   private EntityDescriptor activeOnStartUp;
   private List<EntityDescriptor> expandedOnStartUp;

   public ProjectUiState(IntegrationEntityLookup var1) {
      this.dataLookup = var1;
   }

   void initialize(List<EntityDescriptor> var1, EntityDescriptor var2, List<EntityDescriptor> var3) {
      this.displayOnStartUp = var1;
      this.activeOnStartUp = var2;
      this.expandedOnStartUp = var3;
   }

   public void reset() {
      if (this.displayOnStartUp != null) {
         this.displayOnStartUp.clear();
      }

      this.activeOnStartUp = null;
      if (this.expandedOnStartUp != null) {
         this.expandedOnStartUp.clear();
      }
   }

   public void setEntitiesToDisplayOnStartUp(List<IntegrationEntity> var1) {
      this.displayOnStartUp = new ArrayList<>();
      if (var1 != null) {
         for (IntegrationEntity var3 : var1) {
            this.displayOnStartUp.add(new EntityDescriptor(var3));
         }
      }
   }

   public boolean hasEntitiesToDisplayOnStartup() {
      return this.displayOnStartUp != null && !this.displayOnStartUp.isEmpty();
   }

   public List<IntegrationEntity> getEntitiesToDisplayOnStartUp() {
      return this.translateDescriptorList(this.displayOnStartUp);
   }

   private List<IntegrationEntity> translateDescriptorList(List<EntityDescriptor> var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var1 != null) {
         for (EntityDescriptor var4 : var1) {
            IntegrationEntity var5 = this.dataLookup.getEntity(var4.getID());
            if (var5 != null) {
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   public void setActiveEntityOnStartUp(IntegrationEntity var1) {
      this.activeOnStartUp = var1 == null ? null : new EntityDescriptor(var1);
   }

   public IntegrationEntity getActiveEntityOnStartUp() {
      return this.activeOnStartUp != null ? this.dataLookup.getEntity(this.activeOnStartUp.getID()) : null;
   }

   public void setExpandedOnStartUp(List<EntityDescriptor> var1) {
      this.expandedOnStartUp = var1 == null ? new ArrayList<>() : var1;
   }

   public List<IntegrationEntity> getExpandedOnStartUp() {
      return this.translateDescriptorList(this.expandedOnStartUp);
   }

   public String getXml() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      var1.addElement("UiState");
      if (this.displayOnStartUp != null) {
         this.appendOpenPagesXml(var1);
      }

      if (this.expandedOnStartUp != null) {
         this.appendExpandedNodesXml(var1);
      }

      var1.closeElement();
      return var1.getXml();
   }

   private void appendOpenPagesXml(XmlStringBuilder var1) {
      var1.addElement("OpenPages");

      for (EntityDescriptor var3 : this.displayOnStartUp) {
         var1.addElement("Page");
         var1.addAttribute("type", var3.getEntityType().toString());
         var1.addAttribute("id_val", var3.getID().toString());
         if (var3.equals(this.activeOnStartUp)) {
            var1.addAttribute("active", "true");
         }

         var1.closeElement();
      }

      var1.closeElement();
   }

   private void appendExpandedNodesXml(XmlStringBuilder var1) {
      var1.addElement("ExpandedNodes");

      for (EntityDescriptor var3 : this.expandedOnStartUp) {
         var1.addElement("Node");
         var1.addAttribute("type", var3.getEntityType().toString());
         var1.addAttribute("id_val", var3.getID().toString());
         var1.closeElement();
      }

      var1.closeElement();
   }
}
