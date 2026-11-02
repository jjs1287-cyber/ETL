@echo off
cd /d C:\ETL\Studio
"C:\ETL\Tools\jdk8u504-b01\bin\java.exe" -Xmx2g -DRunUpgradeScripts=false -Duser.home=C:/ETL/Studio/run-user-home-etlforge -cp "configuration;dist/lib/*;lib/*" org.etlforge.integration.client.main.IntegrationMain -d C:/ETL/Studio/run-user-home-etlforge/application %*

