/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <vector>

#include "StringUtils.h"
#include "MapUtils.h"

#include "KeyValues.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	KeyValues fromString(const std::string& values, const string& sep1, const string& sep2) {
		KeyValues retVal;
		vector<string> v1 = konga::SplitString(values, sep2);
		for( vector<string>::const_iterator cit = v1.begin(); cit != v1.end(); ++cit ) {
			vector<string> v2 = konga::SplitString(*cit, sep1);
			if ( v2.size() != 2 )
				continue;

			const string& key = *v2.begin();
			const string& value = *(v2.end()-1);
			if ( key.empty() )
				continue;

			retVal.AddString(key, value);
		}
		return retVal;
	}
}

KeyValues::KeyValues(void) {}

KeyValues::KeyValues(const std::string& values, const string& sep1, const string& sep2) {
	*this = fromString(values, sep1, sep2);
}

KeyValues::~KeyValues(void) {}

KeyValues::const_iterator KeyValues::begin() const {
	return m_data.begin();
}

KeyValues::const_iterator KeyValues::end() const {
	return m_data.end();
}

void KeyValues::AddString(const std::string& stKey, const std::string& stValue) {
	AddOrUpdate<ststMapT, string, string>(m_data, stKey, stValue);
}

void KeyValues::AddCString(const std::string& stKey, const char* value) {
	if ( value == 0 ) {
		this->DeleteKey(stKey);
	}
	AddOrUpdate<ststMapT, string, string>(m_data, stKey, string(value));
}

void KeyValues::AddInt(const std::string& stKey, const int iValue) {
	AddString(stKey, konga::toString(iValue));
}

void KeyValues::AddLong(const std::string& stKey, const long lValue) {
	AddString(stKey, konga::toString(lValue));
}

void KeyValues::AddBool(const std::string& stKey, const bool bValue) {
	AddCString(stKey, bValue ? "1" : "0");
}

std::string KeyValues::GetString(const std::string& stKey, const std::string& stDefault) const {
	ststMapCitT cit = m_data.find(stKey);
	return ( cit == m_data.end() ) ? stDefault : cit->second;		
}

int KeyValues::GetInt(const std::string& stKey, const int iDefaultValue) const {
	ststMapCitT cit = m_data.find(stKey);
	return ( cit == m_data.end() ) ? iDefaultValue : atoi(cit->second.c_str());
}

long KeyValues::GetLong(const std::string& stKey, const long lDefaultValue) const {
	ststMapCitT cit = m_data.find(stKey);
	return ( cit == m_data.end() ) ? lDefaultValue : atol(cit->second.c_str());
}

namespace {
	bool isValueTrue(const std::string& s) {
		return s == "1" || EqualsIgnoreCase(s, "true");
	}
}

bool KeyValues::GetBool(const std::string& stKey, const bool bDefaultValue) const {
	ststMapCitT cit = m_data.find(stKey);
	return ( cit == m_data.end() ) ? bDefaultValue : isValueTrue(cit->second);
}

bool KeyValues::IsKeyDefined(const std::string& stKey) const {
	return ( m_data.find(stKey) != m_data.end() );
}

void KeyValues::DeleteKey(const std::string& stKey) {
	m_data.erase(stKey);
}

void KeyValues::Clear() {
	m_data.clear();
}

int KeyValues::GetSize() const {
	return static_cast<int>(m_data.size());
}

KeyValues& KeyValues::operator+=(const KeyValues& rhs) {
	KeyValues::const_iterator cit = rhs.begin();
	for( ; cit != rhs.end(); ++cit ) {
		this->AddString(cit->first, cit->second);
	}
	return *this;
}

string KeyValues::toString() const {
	return toString("=", ";");
}

string KeyValues::toString(const string& sep1, const string& sep2) const {
	ststMapCitT cit = m_data.begin();
	string s;
	for(; cit != m_data.end(); ++cit) {
		if ( !s.empty() ) s += sep2;
		s += cit->first + sep1 + cit->second;
	}
	return s;
}

