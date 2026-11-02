/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>		             

#include "IntegrationBreakpointLocation.h"

using namespace konga;
using std::string;

const string IntegrationBreakpointLocation::INSTANCE_STRING = "instance_string";
const string IntegrationBreakpointLocation::GENERATOR_PATH = "generator_path";

string IntegrationBreakpointLocation::toString() const {
	return string("Break at line " + konga::toString(getLineNumber()));
}

void IntegrationBreakpointLocation::wasHandled() {
	m_source_data_elements.clear();
}

long IntegrationBreakpointLocation::getOperationId() const {
	return m_lOperationId;
}

long IntegrationBreakpointLocation::getTransformationId() const {
	return m_lTransformationId;
}

long IntegrationBreakpointLocation::getScriptId() const {
	return m_lScriptId;
}

long IntegrationBreakpointLocation::getLineNumber() const {
	return m_lLineNumber;
}

void IntegrationBreakpointLocation::setLineNumber(long lineNumber) {
	m_lLineNumber = lineNumber;
}

string IntegrationBreakpointLocation::getResult() const {
	return m_stResult;
}

void IntegrationBreakpointLocation::setResult(const std::string& res) {
	m_stResult = res;
}

string IntegrationBreakpointLocation::getAttributePath() const {
	return m_stAttributePath;
}

void IntegrationBreakpointLocation::setSourceDataElements(std::set<DataElement*> source_de) {
	m_source_data_elements.clear();
	m_source_data_elements = source_de;
}

void IntegrationBreakpointLocation::getSourceDataElements(std::set<DataElement*>& source_de) const {
	source_de.clear();
	source_de = m_source_data_elements;
}

void IntegrationBreakpointLocation::setProperty(const std::string& key, const std::string& value) {
	m_properties.AddString(key, value);
}

string IntegrationBreakpointLocation::getProperty(const std::string& key) const {
	return m_properties.GetString(key);
}

void IntegrationBreakpointLocation::getProperties(KeyValues& properties) const {
	properties = m_properties;
}

