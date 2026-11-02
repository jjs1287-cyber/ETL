/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_PIPELINE_PLUGIN_EXECUTOR_OUTPUT_FILES
#define ETLFORGE_PIPELINE_PLUGIN_EXECUTOR_OUTPUT_FILES

#include <string>
#include <vector>

namespace konga {	


	class FileWarning {
	public:
		enum Severity {
			Severity_Information = 0,
			Severity_Warning = 1,
			Severity_Error = 2
		};

		FileWarning(void);
		FileWarning(const std::string& warning, const std::string& severity);
		~FileWarning(void) {}

		std::string GetWarning() const {
			return m_warning;
		}

		FileWarning::Severity GetSeverity() const {
			return m_severity;
		}

	private:
		std::string m_warning;
		Severity m_severity;
	};

	struct OutputFile {
		std::string Name;
		std::vector<std::string> OriginFiles;
		FileWarning Warning;
	};
}

#endif                                                  

