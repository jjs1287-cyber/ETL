package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;

public final class Strings extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.name("action.ic.tx.EditUpdateKeys"), "Edit &Update Keys..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.EditUpdateKeys"), "Edit the update keys used in the database target."},
      {ActionDictionaryKeys.name("action.ic.tx.EditSource"), "Edit &Source Structure..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.EditSource"), "Edit the structure that is used as source in this transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.EditTarget"), "Edit &Target Structure..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.EditTarget"), "Edit the target structure that is used as target in this transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.EditWhereClause"), "Edit &WHERE Clause..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.EditWhereClause"), "Edit the WHERE clause used when retrieving the source data from the database."},
      {ActionDictionaryKeys.name("action.ic.tx.ExportMappings"), "E&xport Mappings..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ExportMappings"), "Export the mappings to an external file"},
      {ActionDictionaryKeys.name("action.ic.tx.ExportMultipleFolders"), "Export Renamed Mapping Folders..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ExportMultipleFolders"), "Export the renamed mapping folders to an external file"},
      {ActionDictionaryKeys.name("action.ic.tx.ImportMappings"), "&Import Mappings..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ImportMappings"), "Import mappings into the displayed transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.ImportMultipleFolders"), "Import Renamed Mapping Folders..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ImportMultipleFolders"), "Import renamed mapping folders into the displayed transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.TextWizard"), "&Convert to Text Document..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.TextWizard"), "Convert the transformation's source and/or target structure to a text document."},
      {ActionDictionaryKeys.name("action.ic.tx.Print"), "&Print..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.Print"), "Print the displayed transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.LoadSource"), "&Load Source Data"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.LoadSource"), "Load sample source data into the transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.Migrate"), "&Migrate..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.Migrate"), "Migrate the displayed transformation into another transformation"},
      {ActionDictionaryKeys.name("action.ic.RefreshAllTransformations"), "Refresh All Transformations"},
      {ActionDictionaryKeys.tooltip("action.ic.RefreshAllTransformations"), "Refresh the source and target structures for all transformations in the project"},
      {ActionDictionaryKeys.name("action.ic.tx.Refresh"), "Refresh Structures"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.Refresh"), "Refresh the source and target structures in the displayed transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.ShowSourceData"), "&Source Tree"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ShowSourceData"), "Hide nodes in the source tree that do not hold data"},
      {ActionDictionaryKeys.name("action.ic.tx.ShowTargetData"), "&Target Tree"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ShowTargetData"), "Hide nodes in the target tree that do not hold data"},
      {ActionDictionaryKeys.name("action.ic.tx.TestOperation"), "Test &Operation"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.TestOperation"), "Test an operation that uses the transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.TestTransformation"), "Test &Transformation"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.TestTransformation"), "Test the transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.Search"), "&Find / Replace..."},
      {ActionDictionaryKeys.tooltip("action.ic.tx.Search"), "Find nodes in the mapping tree, and/or search and replace in the mapping scripts"},
      {ActionDictionaryKeys.name("action.ic.tx.ViewSourceFile"), "&View Source File"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ViewSourceFile"), "View the file that was loaded into the source tree"},
      {ActionDictionaryKeys.name("action.ic.tx.ViewTargetFile"), "View Target File"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ViewTargetFile"), "View the file that was loaded into the target tree"},
      {ActionDictionaryKeys.name("action.ic.tx.ViewLinkFile"), "View Link File"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ViewLinkFile"), "View the file that links the source and target data"},
      {ActionDictionaryKeys.name("action.ic.tx.InputJtr"), "&View Input JTR"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.InputJtr"), "View the JTR data from which the mapping input structure is created"},
      {ActionDictionaryKeys.name("action.ic.tx.OutputJtr"), "&View Output JTR"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.OutputJtr"), "View the JTR data from which the mapping output structure is created"},
      {ActionDictionaryKeys.name("action.ic.tx.RemoveData"), "Remove Test Data"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.RemoveData"), "Remove test data from the mapping tree"},
      {ActionDictionaryKeys.name("action.ic.tx.CreateOperation"), "Create Operation"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.CreateOperation"), "Create a new Operation based on this Transformation"},
      {ActionDictionaryKeys.name("action.ic.tx.ShowMappedSourceNodes"), "&Source Tree"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ShowMappedSourceNodes"), "Hide nodes in the source tree that are not mapped"},
      {ActionDictionaryKeys.name("action.ic.tx.ShowMappedTargetNodes"), "&Target Tree"},
      {ActionDictionaryKeys.tooltip("action.ic.tx.ShowMappedTargetNodes"), "Hide nodes in the target tree that are not mapped"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
