/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef SQLSTATEMENTPARSER_H
#define SQLSTATEMENTPARSER_H

#include <string>
#include <vector>
#include <konga/Utils/StringTokenizer.h>

enum SqlTokenType { t_select=1, t_from=2, t_where=3, t_order=5, t_group=6, t_ws=7, t_comma=8, t_phrase=9, t_dot=10, t_name=11, t_value=12, t_end=13};

class SqlToken {
	friend class SqlStatementParser;
protected:
	int beginPos, endPos;
	SqlTokenType tokenType;
	SqlToken(int pos1, int pos2, const SqlTokenType& type){
		beginPos=pos1;
		endPos=pos2;
		tokenType=type;
	}
};

class SqlStatementParser {
	
	std::string m_Sql;
	std::vector<SqlToken*>m_tokens;
	SqlToken *select, *from, *where, *order, *group, *end;
	
public:
	SqlStatementParser(const std::string& sql);
	~SqlStatementParser();
	std::string insertNoneFilter();
	std::string getFilter();
	std::string replaceFilter(const std::string& newFilter);

private:
	int getInsrtFilterPosition();
	void findToken(konga::StringTokenizer& tk, std::string token);	
	void findMatchingToken(konga::StringTokenizer& tk, char c);
	void findMatchingParenthesis(konga::StringTokenizer& tk);
};

#endif                        
