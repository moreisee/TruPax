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

package coderslagoon.trupax.lib;

public class NLS extends coderslagoon.baselib.util.NLS {
    static {
        coderslagoon.baselib.util.NLS.Reg.instance().register(NLS.class); 
    }

    public static Str PRG_OPERATION_ABORTED;
    public static Str PRGPROPS_ERR_INVALID_ARGUMENT_1;
    public static Str PRGPROPS_ERR_UNKNOWN_ARGUMENT_1;
    public static Str PRGIMPL_ERR_CANNOT_LOAD_PROPFILE;
    public static Str PRGIMPL_ERR_CANNOT_SAVE_PROPFILE;
    public static Str PRGIMPL_PROPS_COMMENT;
    public static Str PRGIMPL_NOT_ENOUGH_PARAMS;
    public static Str PRGIMPL_ERR_OBJ_REG_1;
    public static Str PRGIMPL_NO_SUCH_OBJ_REG_1;
    public static Str PRGIMPL_REG_SEARCH_FAILED_1;
    public static Str PRGIMPL_INTERNAL_ERROR_1;
    public static Str PRGIMPL_RESOLVE_ERROR_1;
    public static Str PRGIMPL_CREATE_VOLUME_ERROR;
    public static Str PRGIMPL_INIT_TC_BLOCKDEV_ERROR_1;
    public static Str PRGIMPL_RESOLVE_ERROR;
    public static Str PRGIMPL_MAKE_ERROR_1;
    public static Str PRGIMPL_MAKE_REJECT_NOVOL;
    public static Str PRGIMPL_UNKNOWN_PROPERTY_1;
    public static Str PRGIMPL_INVALID_PROPERTY_1;
    public static Str PRGIMPL_INVALID_PROPERTY_VALUE_2;
    public static Str PRGIMPL_ALGORITHM_TEST_ERROR;
    public static Str PRGIMPL_FILE_COLLISION;
    public static Str PRGIMPL_VOL_EXISTS;
    public static Str PRGIMPL_WIPE_REASON_VANISHED;
    public static Str PRGIMPL_WIPE_REASON_CANNOTOPEN;
    public static Str PRGIMPL_WIPE_REASON_IOERROR;
    public static Str PRGIMPL_WIPE_REASON_RENFAILED;
    public static Str PRGIMPL_WIPE_REASON_REN2FAILED;
    public static Str PRGIMPL_WIPE_REASON_DELFAILED;
    public static Str PRGIMPL_WIPE_REASON_UNKNOWN_1;
    public static Str PRGIMPL_CONCERN_SKIP;
    public static Str PRGIMPL_CONCERN_WARNING;
    public static Str PRGIMPL_CONCERN_ERROR;
    public static Str PRGIMPL_CONCERN_EXISTS;
    public static Str PRGIMPL_CANNOT_OPEN_VOLUME_1;
    public static Str PRGIMPL_CANNOT_UNLOCK_VOLUME;
    public static Str PRGIMPL_EXTERR_DEV;
    public static Str PRGIMPL_EXTERR_OBJ_2;
    public static Str PRGIMPL_EXTERR_FAIL;
    public static Str PRGIMPL_EXTRACT_FILE_EXISTS_3;
    public static Str PRGIMPL_INVALIDATE_ERR_1;
    public static Str PRGIMPL_INVALIDATE_ERRDEL;
    public static Str PRGIMPL_FILE_NOT_FOUND_1;
}
