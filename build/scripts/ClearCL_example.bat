@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  ClearCL_example startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and CLEAR_CL_EXAMPLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\ClearCL_example.jar;%APP_HOME%\lib\clearcl-0.5.2.jar;%APP_HOME%\lib\bio-formats-tools-5.5.3.jar;%APP_HOME%\lib\formats-gpl-5.5.3.jar;%APP_HOME%\lib\formats-bsd-5.5.3.jar;%APP_HOME%\lib\formats-api-5.5.3.jar;%APP_HOME%\lib\specification-5.5.4.jar;%APP_HOME%\lib\ome-xml-5.5.4.jar;%APP_HOME%\lib\metakit-5.3.0.jar;%APP_HOME%\lib\ome-poi-5.3.1.jar;%APP_HOME%\lib\ome-common-5.3.1.jar;%APP_HOME%\lib\guava-23.0.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\jsr305-1.3.9.jar;%APP_HOME%\lib\error_prone_annotations-2.0.18.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.14.jar;%APP_HOME%\lib\coremem-0.4.4.jar;%APP_HOME%\lib\javacl-1.0.0-RC4.jar;%APP_HOME%\lib\jocl-2.0.0.jar;%APP_HOME%\lib\vecmath-1.5.2.jar;%APP_HOME%\lib\junit-4.12.jar;%APP_HOME%\lib\logback-classic-1.1.1.jar;%APP_HOME%\lib\jxrlib-all-0.2.1.jar;%APP_HOME%\lib\native-lib-loader-2.1.4.jar;%APP_HOME%\lib\netcdf-4.3.22.jar;%APP_HOME%\lib\slf4j-api-1.7.6.jar;%APP_HOME%\lib\logback-core-1.1.1.jar;%APP_HOME%\lib\xalan-2.7.2.jar;%APP_HOME%\lib\serializer-2.7.2.jar;%APP_HOME%\lib\javacl-core-1.0.0-RC4.jar;%APP_HOME%\lib\opencl4java-1.0.0-RC4.jar;%APP_HOME%\lib\bridj-0.7.0.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\joda-time-2.2.jar;%APP_HOME%\lib\jai_imageio-5.5.3.jar;%APP_HOME%\lib\turbojpeg-5.5.3.jar;%APP_HOME%\lib\jgoodies-forms-1.7.2.jar;%APP_HOME%\lib\kryo-2.24.0.jar;%APP_HOME%\lib\perf4j-0.9.13.jar;%APP_HOME%\lib\metadata-extractor-2.6.2.jar;%APP_HOME%\lib\ome-mdbtools-5.3.0.jar;%APP_HOME%\lib\JWlz-1.4.0.jar;%APP_HOME%\lib\jhdf5-14.12.0.jar;%APP_HOME%\lib\xercesImpl-2.8.1.jar;%APP_HOME%\lib\xml-apis-1.3.04.jar;%APP_HOME%\lib\dx-1.7.jar;%APP_HOME%\lib\nativelibs4java-utils-1.6.jar;%APP_HOME%\lib\jgoodies-common-1.7.0.jar;%APP_HOME%\lib\minlog-1.2.jar;%APP_HOME%\lib\objenesis-2.1.jar;%APP_HOME%\lib\xmpcore-5.1.2.jar;%APP_HOME%\lib\commons-logging-1.1.1.jar

@rem Execute ClearCL_example
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %CLEAR_CL_EXAMPLE_OPTS%  -classpath "%CLASSPATH%" imageUI.ImageUI %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable CLEAR_CL_EXAMPLE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%CLEAR_CL_EXAMPLE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
