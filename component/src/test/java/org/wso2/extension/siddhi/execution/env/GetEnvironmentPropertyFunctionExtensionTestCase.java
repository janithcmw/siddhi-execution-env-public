/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.extension.siddhi.execution.env;

import io.siddhi.core.SiddhiAppRuntime;
import io.siddhi.core.SiddhiManager;
import io.siddhi.core.event.Event;
import io.siddhi.core.exception.SiddhiAppCreationException;
import io.siddhi.core.query.output.callback.QueryCallback;
import io.siddhi.core.stream.input.InputHandler;
import io.siddhi.core.util.EventPrinter;
import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Test case for GetEnvironmentPropertyFunction function extension.
 */

public class GetEnvironmentPropertyFunctionExtensionTestCase {

    private static Logger logger = Logger.getLogger(GetEnvironmentPropertyFunctionExtensionTestCase.class);

    @Test
    public void testDefaultBehaviour() throws Exception {
        logger.info("GetEnvironmentPropertyFunctionExtensionTestDefaultBehaviour TestCase");

        SiddhiManager siddhiManager = new SiddhiManager();

        String stream = "define stream inputStream (key string);\n";

        String query = ("@info(name = 'query1') from inputStream "
                + "select env:getEnvironmentProperty(key) as propertyValue "
                + "insert into outputStream;");

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(stream + query);
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents,
                                Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                String result;
                for (Event event : inEvents) {
                    result = (String) event.getData(0);
                    AssertJUnit.assertEquals(System.getProperty("os.name"), result);
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime
                .getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new String[]{"os.name"});
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void testDefaultBehaviourWithWithDefaultValueProvided() throws Exception {
        logger.info("GetEnvironmentPropertyFunctionExtensionTestDefaultBehaviourWithWithDefaultValueProvided TestCase");

        SiddhiManager siddhiManager = new SiddhiManager();

        String stream = "define stream inputStream (key string);\n";

        String query = ("@info(name = 'query1') from inputStream "
                + "select env:getEnvironmentProperty(key,'defaultValue') as propertyValue "
                + "insert into outputStream;");

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(stream + query);
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents,
                                Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                String result;
                for (Event event : inEvents) {
                    result = (String) event.getData(0);
                    AssertJUnit.assertEquals(System.getProperty("os.name"), result);
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new String[]{"os.name"});
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void testDefaultBehaviourWithWithDefaultValueProvided2() throws Exception {
        logger.info("GetEnvironmentPropertyFunctionExtensionTestDefaultBehaviourWithWithDefaultValueProvided " +
                "TestCase2");

        SiddhiManager siddhiManager = new SiddhiManager();

        String stream = "define stream inputStream (key string);\n";

        String query = ("@info(name = 'query1') from inputStream "
                + "select env:getEnvironmentProperty(key,'defaultValue') as propertyValue "
                + "insert into outputStream;");

        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(stream + query);
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents,
                                Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                String result;
                for (Event event : inEvents) {
                    result = (String) event.getData(0);
                    AssertJUnit.assertEquals("defaultValue", result);
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new String[]{"not.os.name"});
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void exceptionTestCaseNonStringKey() {
        logger.info("GetEnvironmentPropertyFunctionExtension exceptionTestCaseNonStringKey");

        SiddhiManager siddhiManager = new SiddhiManager();

        String stream = "define stream inputStream (key string);\n";

        String query = ("@info(name = 'query1') from inputStream "
                + "select env:getEnvironmentProperty(5) as functionOutput "
                + "insert into outputStream;");

        siddhiManager.createSiddhiAppRuntime(stream + query);
    }


    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void exceptionTestCaseNullKey() {
        logger.info("GetEnvironmentPropertyFunctionExtension exceptionTestCaseNullKey");

        SiddhiManager siddhiManager = new SiddhiManager();

        String stream = "define stream inputStream (key string);\n";

        String query = ("@info(name = 'query1') from inputStream "
                + "select env:getEnvironmentProperty() as functionOutput "
                + "insert into outputStream;");

        siddhiManager.createSiddhiAppRuntime(stream + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void exceptionTestCaseNonStringDefault() {
        logger.info("GetEnvironmentPropertyFunctionExtension exceptionTestCaseNonStringDefault");

        SiddhiManager siddhiManager = new SiddhiManager();

        String stream = "define stream inputStream (key string);\n";

        String query = ("@info(name = 'query1') from inputStream "
                + "select env:getEnvironmentProperty(key , 5) as functionOutput "
                + "insert into outputStream;");

        siddhiManager.createSiddhiAppRuntime(stream + query);
    }
}
