/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_KEY_VALUES_H
#define KONGA_UTILS_KEY_VALUES_H

#include <map>
#include <string>

namespace konga {

	   
                                                                         
                                                           
                                 
    
	class KeyValues {
	public:
		KeyValues(void);
		                                                         
		KeyValues(const std::string& values, const std::string& sep1, const std::string& sep2);
		~KeyValues(void);

		typedef std::map<std::string, std::string>::const_iterator const_iterator;

		KeyValues::const_iterator begin() const;
		KeyValues::const_iterator end() const;

		int GetSize() const;

		void AddString(const std::string& stKey, const std::string& stValue);

		                                                                                                  
		                              
		void AddCString(const std::string& stKey, const char* value);

		void AddInt(const std::string& stKey, const int iValue);

		void AddLong(const std::string& stKey, const long lValue);

		void AddBool(const std::string& stKey, const bool bValue);

		std::string GetString(const std::string& stKey, const std::string& stDefault = "") const;

		int GetInt(const std::string& stKey, const int iDefault = -1 ) const;

		long GetLong(const std::string& stKey, const long lDefault = -1) const;

		bool GetBool(const std::string& stKey, const bool bDefault = true) const;

		bool IsKeyDefined(const std::string& stKey) const;

		void DeleteKey(const std::string& stKey);

		void Clear();

		KeyValues& operator+=(const KeyValues& rhs);

		                                                                                               
		std::string toString() const;

		                                                                                             
		                                                                                  
		                                                                                                  
		std::string toString(const std::string& sep1, const std::string& sep2) const;

	private:
		typedef std::map<std::string, std::string> ststMapT;
		typedef ststMapT::const_iterator ststMapCitT;
		ststMapT m_data;
	};

}

#endif                            

