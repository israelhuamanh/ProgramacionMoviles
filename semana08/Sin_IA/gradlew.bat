@if "%DEBUG%" == "" @echo off
@if "%CALL_USAGE%" == "" set CALL_USAGE=0

set JAVA_HOME=C:\Users\USER\.gradle\jdks\eclipse_adoptium-21-amd64-windows.2
set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

set JAVA_EXE=%JAVA_HOME%\bin\java.exe

:execute
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
"%JAVA_EXE%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

:fail
exit /b 1
