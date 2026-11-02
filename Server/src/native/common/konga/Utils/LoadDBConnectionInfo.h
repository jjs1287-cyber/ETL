/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef LOADDBCONNECTIONINFO_H
#define LOADDBCONNECTIONINFO_H

#import "DBInfo.exe" no_namespace

  
                       
  
void LoadPDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer = "");
void LoadIDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer = "");
void LoadMsgDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer = "");
void LoadTranDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer = "");
void LoadBDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, variant_t varServer = "");
void LoadDBConnectionInfo(DBConnectionInfo& db_connect, const ICRDBInfoPtr& pDBInfo, const std::string& stDbName, variant_t varServer = "");
void LoadRemoteDbInfo(const std::string& stMachineName, ICRDBInfoPtr& pDBInfo);

#endif                          
