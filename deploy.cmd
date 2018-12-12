
 @echo off

:: ----------------------
:: B00766612 MyAzure Deployment Script
:: Version:4
:: ----------------------

del D:\home\site\wwwroot\webapps\*.war
:: ----------------------
:: need to edit app folder
cd D:\home\site\repository\B00766612examsample\target\
:: ----------------------
copy *.war D:\home\site\wwwroot\webapps\*.war
cd D:\home\site\wwwroot\webapps\
rename *.war ROOT.war

