package com.company.nifi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.client.CleanupProcessClientImpl;
import org.apache.commons.io.IOUtils;
import org.apache.nifi.processors.script.ExecuteScript;
import org.apache.nifi.script.ScriptingComponentUtils;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CleanupHandlerTest {
    private TestRunner runner;
    private ObjectMapper MAPPER = new ObjectMapper();

    @Before
    public void setup() throws IOException {
        runner = TestRunners.newTestRunner(ExecuteScript.class);
        runner.setProperty("Script Engine", "Groovy");
        runner.setProperty(ScriptingComponentUtils.SCRIPT_BODY,
                IOUtils.toString(this.getClass().getResourceAsStream("/script_body.groovy"), StandardCharsets.UTF_8));
        runner.setProperty(ScriptingComponentUtils.MODULES, "../nifi-fatjar/target/demo-fat-jar.jar");
        runner.assertValid();
    }

    @Test
    public void testWithGoodData() throws JsonProcessingException {
        runTest("11/25/2020 12:17:31", 0, 1);
    }

    @Test
    public void testWithBadData() throws JsonProcessingException {
        runTest("Lorem ipsum", 1, 0);
    }

    void runTest(String inputString, int failure, int success) throws JsonProcessingException {
        Map<String, Object> input = new HashMap<>();
        input.put(CleanupProcessClientImpl.DATE_KEY, inputString);
        String record = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        runner.enqueue(record);
        runner.run();

        runner.assertTransferCount(ExecuteScript.REL_FAILURE, failure);
        runner.assertTransferCount(ExecuteScript.REL_SUCCESS, success);
    }
}
