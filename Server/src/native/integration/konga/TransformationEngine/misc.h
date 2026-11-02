/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_ENGINE_MISC_H
#define TRANSFORMATION_ENGINE_MISC_H

#include <string>
#include <stack>
#include <vector>

#include <konga/Utils/PortDefs.h>
#include <konga/Utils/KString.h>
#include <konga/Utils/AutoPtrCleanup.h>	                
#include <konga/Xml/DOMNodePtr.h>

#define POSTGRESQL "PSQLODBC.DLL"
#define POSTGRESQL_PLUS "PSQLODBCPLUS.DLL"
#define HITSW_DB2 "HSDB232.DLL"
#define HITSW_DB2_AS400_OPTIMIZED "HSOPT32.DLL"


#define c_precondition "/PRECONDITION/"

#define WWF_DELIMITER '^'
#define WWF_ASSIGNMENT '='

#define COMPLEXT_TEXT_ROOT "_Root"

typedef std::map<KString, KString> STRMAP_T;

class TreeMapper;

int StringToVector(const char*str,std::vector<KString*>&vStr);
KString VectorToString(const std::vector<KString*>&vStr);

bool GetODBCTimeOut(long& dwTimeOut);
bool GetNameValuePair(const KString&OverRideParams, char Delimiter, char Assignment, const KString& SearchName, KString& Value);
bool GetLevelInfo(KString& sEndPath, KString &sLevelName, int &iInstanceNumber);

                                      
                                                                                                    
               
char* FGETS(konga::AutoArrayDelete<char>& buf,int&l,FILE*in);

                                                                                            
void removeEscapeCharactersFromQuotedString(KString& value);
void removeEscapeCharactersFromQuotedString(std::string& value);

void RemoveCRFromString(char* const buf,int buf_len);
void RemoveCRFromString(KString&buf);

                                                                    
enum DataTreeType {t_TargetWithHFSource, t_TargetWithXMLSource, t_TargetFlatToHF, 
				   t_TargetFlatToFlat, t_Source};

bool getXmlValidationRegistryFlag();

int updateTableNameAlias(const char*str, std::map<std::string,std::string>&tableAliases, const std::string& sBeginQuote, const std::string& sEndQuote);

void parseOwnerTableFieldString(const char*str, std::string& owner, std::string& tableName, std::string& field, const std::string& sBeginQuote, const std::string& sEndQuote);
std::string getNameFromSqlTableName(const char*str, const std::string& sBeginQuote, const std::string& sEndQuote);
void separateOwnerTableNames(const std::string& str, std::string& owner, std::string& tableName, const std::string& sBeginQuote, const std::string& sEndQuote);
std::string toSqlTableName(const std::string& owner, const std::string& table, const std::string& beginQuote, const std::string& endQuote);
std::string getDETableName(const std::string& owner, const std::string& table);
KString findTableNameFromSelectSql(const KString& csTableName);
void getBeginEndStringForTable(TreeMapper*p, const KString& sSourceSQL, std::string&sBeginStr, std::string&sEndStr);
void printDOMNode(konga::xml::DOMNodePtr node, FILE*out, const std::string&header, const std::string&dh);


#endif                                


