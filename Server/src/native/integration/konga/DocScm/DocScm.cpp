/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/TranDb/Variable_TypesTab.h>
#include <konga/Db/TranDb/DocumentsTab.h>
#include <konga/Db/TranDb/Document_DefinitionsTab.h>
#include <konga/Db/TranDb/TRANSFORMATIONSTAB_V.h>

using namespace konga;
using namespace TranDb;

int CreateScmFile(const std::string&dsn,const int docId,const std::string&fileName, std::string&lastErr, const int tranID)
{
	lastErr="";

	enum Type{flat_fix_doc=1,flat_delimited_doc=2,complex_fix_text=3,complex_del_text=4};

    FILE*out=fopen(fileName.c_str(),"w+");
	if(out==0){
        int err=errno;
		lastErr= std::string("faile to create ouput file\n")+ ErrorResolver().Resolve(err);
		return -1;
	}
	std::vector<std::string>dataTypes;
	const char*requiredStr[]={"$","+","?","*"};
	int SegmentTypeID=-1;

    DbConnection* pTranDbConn = 0;
    try
    {
        pTranDbConn = GetDbConnection(dsn);
    }
    catch( DbException& e )
    {
                                  
        lastErr = e.GetErrorString();
        return -1;
    }
    SmartDbPtr<DbConnection> adopt_dbConn(pTranDbConn);

    SmartDbPtr<Variable_TypesTab> var( Variable_TypesTab::MakeInstance(pTranDbConn) );
    var->Select();

	for(int i=1;!var->Next();i++){
		if(i!=var->GetVar_Type_ID()){
			                                               
			lastErr="Fail due to variable type id not in sequence\n";
			return -1;
		}
		dataTypes.push_back(var->GetVar_Type());
		if(var->GetVar_Type()=="Segment")SegmentTypeID=i;
	}

	if(SegmentTypeID<0){
		                                            
		lastErr="Fail to find variable type id for Segment\n";
		return -1;
	}

	std::string filter;

    SmartDbPtr<DocumentsTab> doc( DocumentsTab::MakeInstance(pTranDbConn) );
    filter=std::string("WHERE Document_ID = ")+toString(docId);
	doc->Select(filter);
    std::string delimiter, strQual, numQual;
    Type docType;
    if(doc->Next()){
        int docId=doc->GetDocument_ID();
        delimiter=doc->GetDelimiter();
        strQual=doc->GetText_QualIFier();
        numQual=doc->GetNumeric_QualIFier();
        docType=(Type)doc->GetFormat_Type_ID();
    }

    SmartDbPtr<Document_DefinitionsTab> ddt( Document_DefinitionsTab::MakeInstance(pTranDbConn) );

    std::string tmp;

	switch(docType){
	case flat_fix_doc:
	case flat_delimited_doc:
		{
			std::string pos;
			if(docType==flat_fix_doc){
				pos="<><><><>";
			} else {
				pos=std::string("<")+strQual+"><"+numQual+"><"+delimiter+"><>";
			}

			filter=std::string("WHERE Document_ID = ")+toString(docId);
			ddt->Select(filter);
			bool isStart=true;
			for(int i=0;!ddt->Next();i++){
				if(docType!=flat_fix_doc||!ddt->GetField_Name().empty()){
					if(isStart)fprintf(out,"(");
					else fprintf(out,",");
					isStart=false;
					
					if(docType==flat_fix_doc){
                        tmp=std::string("<")+toString(ddt->GetStart_Pos())+","+toString(ddt->GetEnd_Pos()-ddt->GetStart_Pos()+1)+">";
						pos+=tmp;
					}
					if(ddt->GetFormat().empty())
						fprintf(out,"%s(%s)",ddt->GetField_Name().c_str(),dataTypes[ddt->GetVar_Type_ID()-1].c_str());
					else
						fprintf(out,"%s(%s,\"%s\")",ddt->GetField_Name().c_str(),dataTypes[ddt->GetVar_Type_ID()-1].c_str(),ddt->GetFormat().c_str());
					if(!ddt->GetDefault_value().empty()){
						fprintf(out,"=\"%s\"",ddt->GetDefault_value().c_str());
					}
				}
			}

            fprintf(out,")%s\n",pos.c_str());
			if(tranID>0){
                SmartDbPtr<TRANSFORMATIONSTAB_V> tr( TRANSFORMATIONSTAB_V::MakeInstance(pTranDbConn) );
                filter=std::string("WHERE Transformation_ID = ")+toString(tranID);
				tr->Select(filter);
				if(tr->Next()){
					if(tr->GetSource_DiffKeys().empty()==false){
						std::string keys=tr->GetSource_DiffKeys();
						int len=keys.length();
						if(keys.at(len-1)==',')keys=keys.substr(0,len-1);
						fprintf(out,"[,\"%s\"]\n",keys.c_str());
					}
				}
			}

		}
		break;
	case complex_fix_text:
	case complex_del_text:
		{
			filter=std::string("WHERE (Document_ID = ")+toString(docId)+") AND (Var_Type_ID = "+toString(SegmentTypeID)+" ORDER BY Field_ID";
			ddt->Select(filter);
			std::string pos,tmp;
			while(ddt->Next()){
				if(ddt->GetParent_ID()==0){                  
					fprintf(out,"%s$(",ddt->GetField_Name().c_str());
				} else {
					fprintf(out,"%s(",ddt->GetField_Name().c_str());
				}
				if(docType==complex_fix_text){
					tmp=toString(ddt->GetStart_Pos())+","+toString(ddt->GetEnd_Pos()-ddt->GetStart_Pos()+1);
				}
                pos=std::string("<")+strQual+"><"+numQual+"><"+delimiter+"><"+tmp+">";

				int parentId=ddt->GetSequence()==0?ddt->GetField_ID():ddt->GetSequence();

                SmartDbPtr<Document_DefinitionsTab> field( Document_DefinitionsTab::MakeInstance(pTranDbConn) );
				filter=std::string("WHERE (Document_ID = ")+toString(docId)+") AND (Parent_ID = "+toString(parentId)+") AND (Var_Type_ID != "+toString(SegmentTypeID)
                    +") ORDER BY Field_ID";

				field->Select(filter);
				bool isStart=true;
				for(int i=0;field->Next();i++){
					if(docType==complex_del_text||!field->GetField_Name().empty()){
						if(!isStart){
							fprintf(out,",");
						}
						isStart=false;
                        tmp=std::string("<")+toString(field->GetStart_Pos())+","+toString(field->GetEnd_Pos()-field->GetStart_Pos()+1)+">";
						pos+=tmp;
						if(field->GetFormat().empty()){
							fprintf(out,"%s(%s)",field->GetField_Name().c_str(),dataTypes[field->GetVar_Type_ID()-1].c_str());
						} else {
							fprintf(out,"%s(%s,\"%s\")",field->GetField_Name().c_str(),dataTypes[field->GetVar_Type_ID()-1].c_str(),field->GetFormat().c_str());
						}
						if(!field->GetDefault_value().empty()){
							fprintf(out,"=\"%s\"",field->GetDefault_value().c_str());
						}
					}
				}
				fprintf(out,")%s\n",pos.c_str());
				if(ddt->GetParent_ID()>0){
                    SmartDbPtr<Document_DefinitionsTab> parentSegment( Document_DefinitionsTab::MakeInstance(pTranDbConn) );
					filter=std::string("WHERE Document_ID = ")+toString(docId)+" AND Field_ID = "+toString(ddt->GetParent_ID())+" AND Var_Type_ID = "+toString(SegmentTypeID);
					parentSegment->Select(filter);
					if(parentSegment->Next()){
						int req=ddt->GetRequired()-1;
						if(req<0)req=0;
						fprintf(out,"[%s,\"\",%s%s,\"\"]\n",parentSegment->GetField_Name().c_str(),ddt->GetField_Name().c_str(),requiredStr[req]);
					}
				}
			}
		}
		break;
	}
	fclose(out);
	return 0;
}



