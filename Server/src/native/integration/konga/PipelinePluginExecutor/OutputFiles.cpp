/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include "OutputFiles.h"

using namespace konga;

namespace {
	FileWarning::Severity GetSeverityFromString(const std::string& severity) {
		if ( EqualsIgnoreCase(severity, "Information") )
			return FileWarning::Severity_Information;
		else if ( EqualsIgnoreCase(severity, "Warning") )
			return FileWarning::Severity_Warning;
		else if ( EqualsIgnoreCase(severity, "Error") )
			return FileWarning::Severity_Error;
		else
			return FileWarning::Severity_Warning;
	}
}

FileWarning::FileWarning(void) : m_severity(FileWarning::Severity_Information) {}


FileWarning::FileWarning(const std::string& warning,
						 const std::string& severity) :
m_warning(warning), m_severity(GetSeverityFromString(severity))
{}
