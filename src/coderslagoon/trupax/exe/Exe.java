/*
Copyright 2010-2013 CODERSLAGOON

This file is part of TruPax.

TruPax is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

TruPax is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
TruPax. If not, see http://www.gnu.org/licenses/.
*/

package coderslagoon.trupax.exe;

import coderslagoon.baselib.util.CmdLnParser;
import coderslagoon.baselib.util.MiscUtils;
import coderslagoon.trupax.lib.prg.Prg;


public abstract class Exe {
    final static String DEF_PROP_FILE_NAME = "trupax"; 
    public static String _propFileName = DEF_PROP_FILE_NAME; 
    
    public final static int COPYRIGHT_START_YEAR = 2010;
    
    public final static String PRODUCT_NAME = "TruPax";                 
    public final static String PRODUCT_SITE = "https://github.com/coderslagoon/TruPax";    
    
    protected final static String[][] LANGS = new String[][] {
        { "de"                                         , "Deutsch" },
        { coderslagoon.baselib.util.NLS.DEFAULT_LANG_ID, "English" }
    };
    
    ///////////////////////////////////////////////////////////////////////////

    static class ExitError extends Exception {
        private static final long serialVersionUID = 5054107294731808046L;
        public ExitError(Prg.Result res) {
            super();
            this.result = res;
        }
        public final Prg.Result result;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    protected CmdLnParser clp;
    
    protected abstract void addCmdLnOptions();
    
    protected String[] processArgs(String[] args) throws ExitError {
        if (null != MiscUtils.__TEST_uncaught_now) {
            throw new Error("uncaught_test");  
        }
        this.clp = new CmdLnParser(); 
        addCmdLnOptions();
        try {
            return this.clp.parse(args, true, false);
        }
        catch (CmdLnParser.Error clpe) {
            throw new ExitError(new Prg.Result(Prg.Result.Code.INVALID_CMDLN_ARG, 
                                clpe.getMessage(), null)); 
        }
    }
}
