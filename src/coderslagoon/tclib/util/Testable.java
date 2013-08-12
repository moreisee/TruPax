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

package coderslagoon.tclib.util;

/**
 * To run a self-test on a throw-away instance. 
 */
public interface Testable {
    /**
     * Run the test. The instance can be invalid afterwards and must be
     * discarded by the caller. 
     * @throws Throwable If any error happened during the test. In such a case
     * the test must be treated as a failure and the class of the instance in
     * general being prevented from further usage since it might produce
     * invalid data or worse!
     */
    void test() throws Throwable;
}
