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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static indi.arrowyi.emr.Emr.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmrTest {
    @BeforeEach
    void setUp()
    {
        Emr.initLogInterface(new LogInterface() {
            @Override
            public void printMsg(String msg) {
                System.out.println(msg);
            }
        });
    }

    class TestData {
        public boolean bValue = false;
    }

    @Test
    void testNotNull()
    {
        TestData data = null;
        assertFalse(notNull(data));
        data = new TestData();
        assertTrue(notNull(data));
    }

    @Test
    void testBeTrue(){
        TestData data = null;
        if(notNull(data))
        {
            assertNotNull(data);
        }

        data = new TestData();
        if(beTrue(data.bValue))
        {
            assertTrue(data.bValue);
        }

        data.bValue = true;

        if(beFalse(data.bValue))
        {
            assertFalse(data.bValue);
        }
    }
}
