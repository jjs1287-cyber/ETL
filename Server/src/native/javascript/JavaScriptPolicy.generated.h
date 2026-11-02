#pragma once
namespace etlforge { namespace javascript { namespace policy {
struct HostDefinition {const char* name;bool api;int minimum,maximum;};
static const HostDefinition Hosts[]={
  {"ReadFile",true,1,2},
  {"WriteFile",true,2,3},
  {"DbExecute",true,2,128},
  {"DbLookup",true,2,2},
  {"GetVar",true,1,1},
  {"SetVar",true,2,2},
  {"WriteToOperationLog",false,1,1},
  {"SetScriptResult",false,1,1},
  {"SetScriptOutput",false,1,1},
  {"GetSource",true,1,1},
};
static constexpr unsigned heapBytes=67108864;
static constexpr unsigned stackBytes=262144;
static constexpr unsigned valueBytes=16777216;
static constexpr unsigned deadlineMs=5000;
} } }
