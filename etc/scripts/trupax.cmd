@echo off
IF EXIST %SystemRoot%\SysWOW64\java.exe (
    %SystemRoot%\SysWOW64\java -cp "%~d0%~p0\trupax.jar" coderslagoon.trupax.exe.CmdLn %*
) ELSE (
    java -cp "%~d0%~p0\trupax.jar" coderslagoon.trupax.exe.CmdLn %*
)
