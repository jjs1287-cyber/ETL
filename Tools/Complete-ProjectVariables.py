exec(open('C:/ETL/Tools/Implement-ProjectVariables.py',encoding='utf-8').read().split("write(d+'entity/ProjectVariable.java'")[0])
edit(d+'project/PartialProjectCreator.java','      this.items = Sets.newHashSet(var2);','''      this.items = Sets.newHashSet(var2);
      if (!this.items.isEmpty()) this.items.addAll(var1.getAllEntitiesOfClass(org.etlforge.integration.data.entity.ProjectVariable.class).toList());''')
edit(d+'script/de/DefaultGlobalDataElements.java','            this.updater.addPropertyChangeListener("script",','''            this.updater.addPropertyChangeListener("projectvariable_name", event -> this.listeners.fireRebuilt());
            this.updater.addPropertyChangeListener("default_value", event -> this.listeners.fireRebuilt());
            this.updater.addPropertyChangeListener("script",''')
edit(d+'script/de/DefaultGlobalDataElements.java','      private void scanAll(IntegrationProjectEvent var1) {','''      private void scanAll(IntegrationProjectEvent var1) {
         if (!var1.filter(org.etlforge.integration.data.entity.ProjectVariable.class).isEmpty()) DefaultGlobalDataElements.this.listeners.fireRebuilt();''')
edit(j+'org/etlforge/integration/client/ui/script/debug/ScriptDebugModel.java','   private SourceDataElementContainer sourceDeContainer;','''   private SourceDataElementContainer sourceDeContainer;
   private org.etlforge.integration.data.project.IntegrationProject project;
   public void setProject(org.etlforge.integration.data.project.IntegrationProject value) { this.project = value; }''')
edit(j+'org/etlforge/integration/client/ui/script/debug/ScriptDebugModel.java','         .setDataElements(this.dataElementsModel.getDataElements());','''         .setDataElements(projectDefaults(this.dataElementsModel.getDataElements()));''')
edit(j+'org/etlforge/integration/client/ui/script/debug/ScriptDebugModel.java','   private DebugSession getSession() {','''   private java.util.Collection<org.etlforge.integration.debug.client.de.DataElement> projectDefaults(
         java.util.Collection<org.etlforge.integration.debug.client.de.DataElement> existing) {
      java.util.List<org.etlforge.integration.debug.client.de.DataElement> result = new java.util.ArrayList<>(existing);
      if (this.project == null) return result;
      for (org.etlforge.integration.data.entity.ProjectVariable variable : this.project.getAllEntitiesOfClass(org.etlforge.integration.data.entity.ProjectVariable.class)) {
         boolean present = false;
         for (org.etlforge.integration.debug.client.de.DataElement element : existing)
            if (element.getScope() == org.etlforge.integration.debug.client.de.DataElementScope.GLOBAL &&
                element.getName().replaceFirst("^\\\\$", "").equals(variable.getName()) && element.getValue() != null) present = true;
         if (present) continue;
         result.removeIf(element -> element.getScope() == org.etlforge.integration.debug.client.de.DataElementScope.GLOBAL &&
             element.getName().replaceFirst("^\\\\$", "").equals(variable.getName()));
         org.etlforge.integration.debug.client.de.DataElement element = new org.etlforge.integration.debug.client.de.DataElement(
             variable.getName(), org.etlforge.integration.debug.client.de.DataElementScope.GLOBAL, "string");
         element.setValue(variable.getDefaultValue()); result.add(element);
      }
      return result;
   }

   private DebugSession getSession() {''')
edit(j+'org/etlforge/integration/client/ui/script/builder/ScriptBuilderUi.java','   private void prepareDebugModel(final ScriptDebugModel var1) {','''   private void prepareDebugModel(final ScriptDebugModel var1) {
      var1.setProject(this.model.getProject());''')
edit(j+'org/etlforge/integration/uidelegate/entity/EntityIcons.java','      Builder var0 = ImmutableMap.builder();','      Builder var0 = ImmutableMap.builder();\n      var0.put(EntityType.ProjectVariable, "Script");')
print('Project defaults added to partial deployments, variable discovery and Studio testing.')
