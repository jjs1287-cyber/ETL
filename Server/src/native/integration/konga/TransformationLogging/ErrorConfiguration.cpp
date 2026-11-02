/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>

#include "ErrorConfiguration.h"

using std::string;

namespace {
	const string EHSTATUS_OK = "Ok";
	const string EHSTATUS_WARNING = "Warning";
	const string EHSTATUS_ERROR = "Error";

	bool equals(const string& optionName, const string& value, const string& default_val) {
		const string& option = konga::GetOptionAsString("ErrorHandling", optionName, default_val);
		return konga::EqualsIgnoreCase(option, value);
	}
}

bool konga::ErrorConfiguration::isOk(const string& name, bool isDefault) {
	return equals(name, EHSTATUS_OK, isDefault ? EHSTATUS_OK : "");
}

bool konga::ErrorConfiguration::isWarning(const string& name, bool isDefault) {
	return equals(name, EHSTATUS_WARNING, isDefault ? EHSTATUS_WARNING : "");
}

bool konga::ErrorConfiguration::isError(const string& name, bool isDefault) {
	return equals(name, EHSTATUS_ERROR, isDefault ? EHSTATUS_ERROR : "");
}
