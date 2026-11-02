#pragma once
#include <konga/JavaBackend/JsonEngine/JsonEngineWrapper.h>
namespace konga {
JsonEngineWrapper* createNativeJsonBackend(DbConnection& connection, const UserInfo& user);
}
