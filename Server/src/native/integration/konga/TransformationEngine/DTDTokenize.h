/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef DTD_TOKENIZE_H
#define DTD_TOKENIZE_H

#include <stack>

class TokenizeBucket
{
	friend class DTDTokenize;
protected:
	int m_k;
	const char*m_name;
	const char*m_str;
	TokenizeBucket*m_link;

public:
	TokenizeBucket(const char*name,const char*str,int k,TokenizeBucket*link){
		m_k=k;
		m_name=name;
		m_str=str;
		m_link=link;
	}
};

                                                                                  
                                                                
                                               
                                                            

class DTDTokenize
{
private:
	int m_k;
	TokenizeBucket*m_link;
	const char*m_str,*m_terminators;
	std::stack<const char*>string_saved;
	const char*m_name;
	int m_nmax;
	int m_TokenLen;
	int m_NextTokenPos;
	bool m_ExtractFlag;
	std::string str0;
	char m_c[2];
public:
	DTDTokenize(const char*str,const char*terminators);
	void init(const char*str, const char*terminators);
	~DTDTokenize();
	bool NextToken();
	int get_current_token_position(){
		return m_k-(int)strlen(token);
	}
	void restart(int pos){m_k=pos; token[0]=0;}
	const char*get_current_str(){return &m_str[m_k];}
	std::string get_tokenized_str(){return std::string(m_str,m_k);}
	void push_macro(const char*name,const char*str,const char*next_str);
	const char*get_definition_name(){return m_name?m_name:"";}
	void SetExtract(bool ExtractFlag, int TokenLen, int TokenPos);
	char*token;
};

#endif                  
