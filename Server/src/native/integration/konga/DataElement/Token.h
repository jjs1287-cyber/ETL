/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_TRANSFORMATION_UTILS_H
#define ETLFORGE_TRANSFORMATION_UTILS_H

#include <string>
#include <vector>
#include <set>
#include <map>

namespace konga {

	class DataElement;
	class LocalVariables;

	class Token{
	public:
		                                                                         
		enum Type{Unknown=0, IDE, ODE, Num,				          
			StrConst, SourceDE, GlobalDE, LocalDE,		    
			Set, PSet, MSet,							    
			Add, Sub, Mul, Div,							    
			Not,										    
			NE, EQ, GT, GE, LT, LE,						    
			LP,	RP,										    
			LBRACE,	RBRACE,								    
			LBRACKET, RBRACKET,	StartArray, EndArray,	    
			Neg, PostPP, PostMM, PrePP, PreMM,			    
			POW,										    
			ODBC, And, Or,								    
			Func, Comma,								    
			BoolConst, Marker,							    
			Semicolon,									    
			Mod,										    
			WhiteSpaces,								    
			Comment										                    
		};

	public:
		struct FNC{
			const char* fnc_name;
			const char* fnc_args;
			int iFunction;
			bool isFixedArgumentCount() const {
				const size_t argn0=strlen(fnc_args);
				return argn0==0 || fnc_args[argn0-1]!='.';
			}
		};

		Token(const char*str,int&i,bool IsStart, bool hasValidLHS, const FNC* fnc, const std::set<std::string>* source_de_set, bool isFlatSource, int&err);
		Token(Type type,const char*str);
		~Token();

		static int find_func(const char* str, const FNC* fnc_array);
		static Token**Tokenize(const char*str,int&err_code);
		static std::vector<Token*>Tokenize0(const char*str,int&err_code);

		const std::string& GetName(){return m_stName;}
		static const char* GetTypeString(Type type){return OpStr[(int)type];}
		DataElement*GetDE(){return m_de;}
		int AddNumberOfData(int nComma,Type lastId, const FNC* fnc_array=Token::fnc);

		static const FNC fnc[];

		void setDataElement(DataElement* de) {
			m_de = de;
		}

		int priority() const;
		int data() const {
			return m_data;
		}

		void DefineDE(LocalVariables* local_variable_map);

		Type getType() const {
			return m_type;
		}

		void setType(Type type){
			m_type=type;
		}

		long line_number() const {return m_lineNumber;}
		const std::string& source_location() const {return m_stSource;}
		static bool isSourceDE(const std::set<std::string>*source_de_set, const std::string& name, bool isFlat);
		static std::string rmInstanceTag(const std::string& name0);

	private:
		static const int Pr[];
		static const char*OpStr[];

		Type m_type;
		long m_data;
		long m_lineNumber;
		DataElement* m_de;
	public:
		std::string m_stName, m_stSource;
        bool localWriteTarget = false;
	};

	class LocalVariables {
		std::map<std::string,konga::DataElement*>locaVar_map;
        std::set<konga::DataElement*> assigned;
	public:
		LocalVariables(){}
		~LocalVariables();
		konga::DataElement* get(const std::string& name);
		void put(konga::DataElement* de);
        bool isAssigned(konga::DataElement* de) const { return assigned.count(de)!=0; }
        void markAssigned(konga::DataElement* de);
		void loadArguments(const std::vector<konga::DataElement*>&arguments);
	};


	                 
	int scan_de_name(const char*str);

}                   

#endif                                   

