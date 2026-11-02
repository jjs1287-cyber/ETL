# Compile/archive milestone only; executable linking is a separate target graph.
add_custom_target(etlforge_native_archives)
get_property(etlforge_targets DIRECTORY PROPERTY BUILDSYSTEM_TARGETS)
foreach(etlforge_target IN LISTS etlforge_targets)
 get_target_property(etlforge_type ${etlforge_target} TYPE)
 if(etlforge_type STREQUAL "STATIC_LIBRARY")
  add_dependencies(etlforge_native_archives ${etlforge_target})
 endif()
endforeach()
