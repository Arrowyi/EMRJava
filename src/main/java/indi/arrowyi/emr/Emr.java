/*
 *  Copyright 2022 - 2022 [arrowyi] All rights reserved.
 *   email : arrowyi@gmail.com
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package indi.arrowyi.emr;

/**
 * The type Emr.
 */
public class Emr {
    private static LogInterface log = null;

    /**
     * Init log interface.
     *
     * @param logInterface the log interface
     */
    public static void initLogInterface(LogInterface logInterface) {
        log = logInterface;
    }


    /**
     * check if the parameter is null.
     *
     * @param o the object you want to check
     * @return the boolean , if o is not null return true , else false with outputting the log
     */
    public static boolean notNull(Object o) {

        if (o == null) {
            getRecordString("should not be null");
            return false;
        }

        return true;
    }


    /**
     * check if the parameter is null
     *
     * @param msg the msg, is expectation missed, the msg will be attached to the log
     * @param o   the object you want to check
     * @return the boolean,if o is not null return true , else false with outputting the log
     */
    public static boolean notNull(String msg, Object o) {
        if (o == null) {
            getRecordString(msg + " should not be null");
            return false;
        }

        return true;
    }


    /**
     * check the parameter is true
     *
     * @param check value to be check
     * @return the boolean, true if the value is true, or false with outputting the log
     */
    public static boolean beTrue(boolean check) {

        if (!check) {
            getRecordString("should be true");
            return false;
        }

        return true;
    }


    /**
     * check the parameter is true
     *
     * @param msg the msg, is expectation missed, the msg will be attached to the log
     * @param check value to be check
     * @return the boolean, true if the value is true, or false with outputting the log
     */
    public static boolean beTrue(String msg, boolean check) {
        if (!check) {
            getRecordString(msg + " should be true");
            return false;
        }

        return true;
    }


    /**
     * check the parameter is false
     *
     * @param check value to be check
     * @return the boolean, true if the value is false, or false with outputting the log
     */
    public static boolean beFalse(boolean check) {

        if (check) {
            getRecordString("should be false");
            return false;
        }

        return true;
    }


    /**
     * check the parameter is false
     *
     * @param msg the msg, is expectation missed, the msg will be attached to the log
     * @param check value to be check
     * @return the boolean, true if the value is false, or false with outputting the log
     */
    public static boolean beFalse(String msg, boolean check) {
        if (check) {
            getRecordString(msg + " should be false");
            return false;
        }

        return true;
    }


    /**
     * check the value is null
     *
     * @param check the value to be check
     * @return the boolean, true if the value is null, or false with outputting the log
     */
    public static boolean beNull(Object check) {

        if (check != null) {
            getRecordString("should be null");
            return false;
        }

        return true;
    }


    /**
     * check the value is null
     *
     * @param msg the msg, is expectation missed, the msg will be attached to the log
     * @param check the value to be check
     * @return the boolean, true if the value is null, or false with outputting the log
     */
    public static boolean beNull(String msg, Object check) {
        if (check != null) {
            getRecordString(msg + " should be null");
            return false;
        }

        return true;
    }



    private static void getRecordString(String msg) {
        Throwable t = new Throwable();
        StackTraceElement element = t.getStackTrace()[2];
        log.printMsg(String.format(msg + System.lineSeparator()
                        + "Expectation miss record : at %s"
                        + System.lineSeparator() + "%s  "
                        + System.lineSeparator() + "%s  %d "
                , element.getFileName() == null ? "file name is null" : element.getFileName()
                , element.getClassName() == null ? "class name is null" : element.getClassName()
                , element.getMethodName() == null ? "method name is null" : element.getMethodName()
                , element.getLineNumber()));
    }

}
