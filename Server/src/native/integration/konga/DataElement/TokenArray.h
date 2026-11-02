/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_TOKEN_ARRAY_H
#define KONGA_TOKEN_ARRAY_H

#include <string>
#include <vector>
#include <set>
#include <map>
#include <stack>

namespace konga {

	class TokenArray {
	public:
		int m_start_pos,m_end_pos;
		konga::Token**m_ts;
		TokenArray(const char*buf, int start_pos, int end_pos, const std::set<std::string>*source_de_set, bool isFlatSource, int& err_code);
		~TokenArray();
	};

	class TokenArrayList {
	public:
		long m_lScriptId;
		std::string m_sScriptName;
		std::string m_sScript;
		std::vector<TokenArray*>* m_tokenArrayList;
		TokenArrayList(const std::string& templateString, const std::set<std::string>*source_de_set, bool isFlatSource, int&err_code);
		TokenArrayList(long lScriptId, const std::string& sScriptName, const std::string& sScript, int&err_code);
		~TokenArrayList();
		static int find_END_KEY_position(const char*chars,int start);
		void collect_IDE_tokens(std::vector<konga::Token*>& ide_tokens);
		bool useInstanceResolvingFunction();
		std::string getLastError(){return "";}
	};

}                   

#endif                       

