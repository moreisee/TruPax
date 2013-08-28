@echo off
IF EXIST %SystemRoot%\SysWOW64\java.exe (
    %SystemRoot%\SysWOW64\java -Xmx1024m -cp "%~d0%~p0\trupax.jar" coderslagoon.trupax.exe.CmdLn %*
) ELSE (
    java -Xmx1024m -cp "%~d0%~p0\trupax.jar" coderslagoon.trupax.exe.CmdLn %*
)
