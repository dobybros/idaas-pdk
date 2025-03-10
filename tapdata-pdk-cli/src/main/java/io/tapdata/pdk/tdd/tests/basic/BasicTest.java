package io.tapdata.pdk.tdd.tests.basic;

import io.tapdata.entity.schema.TapTable;
import io.tapdata.entity.utils.DataMap;
import io.tapdata.pdk.apis.entity.TestItem;
import io.tapdata.pdk.apis.logger.PDKLogger;
import io.tapdata.pdk.core.tapnode.TapNodeInfo;
import io.tapdata.pdk.tdd.core.PDKTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

@DisplayName("Tests for basic test")
public class BasicTest extends PDKTestBase {
    private static final String TAG = BasicTest.class.getSimpleName();

    @Test
    @DisplayName("Test method connectionTest")
    void connectionTest() {
        consumeQualifiedTapNodeInfo(nodeInfo -> {
            Assertions.assertNotNull(connectionOptions, "Missing \"connection\" key in test config json file. The value of \"connection\" key is the user input values of json form items. ");

            DataMap configOptions = nodeInfo.getTapNodeSpecification().getConfigOptions();
            Assertions.assertNotNull(configOptions, "The key \"configOptions\" doesn't be found in spec json file. ");

            verifyConnection(configOptions, connectionOptions);

            prepareConnectionNode(nodeInfo, connectionOptions, connectionNode -> {
                LinkedHashMap<String, TestItem> testItemMap = new LinkedHashMap<>();
                connectionNode.connectionTest(testItem -> {
                    Assertions.assertNotNull(testItem, "TestItem is null");
                    TestItem old = testItemMap.put(testItem.getItem(), testItem);
                    Assertions.assertNull(old, "TestItem has duplicated item " + testItem.getItem());
                });
                Assertions.assertFalse(testItemMap.isEmpty(), "TestItem is needed to return at least one from connectionTest method");

                connectionNode.getConnectorNode().destroy();
            });
        });
    }

    @Test
    @DisplayName("Test method discoverSchema")
    void discoverSchemaTest() {
        consumeQualifiedTapNodeInfo(nodeInfo -> {
            Assertions.assertNotNull(connectionOptions, "Missing \"connection\" key in test config json file. The value of \"connection\" key is the user input values of json form items. ");

            prepareConnectionNode(nodeInfo, connectionOptions, connectionNode -> {
                List<TapTable> allTables = new ArrayList<>();
                connectionNode.discoverSchema(tables -> allTables.addAll(tables));
                Assertions.assertFalse(allTables.isEmpty(), "At least one table can be discovered from discoverSchema method.");
                for(TapTable table : allTables) {
                    Assertions.assertNotNull(table, "Discovered table can not be null");
                    Assertions.assertNotNull(table.getName(), "Discovered table name can not be null");
                    Assertions.assertNotNull(table.getId(), "Discovered table id can not be null");
                }
                connectionNode.getConnectorNode().destroy();
            });
        });
    }

    private void verifyConnection(DataMap configOptions, DataMap connectionInputs) {
        Map<String, Object> connectionMap = (Map<String, Object>) configOptions.get("connection");
        Assertions.assertNotNull(connectionMap, "The key \"configOptions.connection\" doesn't be found in spec json file.");

        Set<String> missingInConnection = new HashSet<>();

        //Check the format of connectionMap
        Object typeObj = connectionMap.get("type");
        Assertions.assertEquals("object", typeObj, "The spec json file's \"configOptions.connection.type\" has to be \"object\"");
        Object propertiesObj = connectionMap.get("properties");
        Assertions.assertTrue(propertiesObj instanceof Map, "The spec json file's \"configOptions.connection.properties\" has to be Map type");
        Map<String, Object> propertiesMap = (Map<String, Object>) propertiesObj;
        for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
            if (!connectionInputs.containsKey(entry.getKey()))
                missingInConnection.add(entry.getKey());
            Map<String, Object> map = (Map<String, Object>) entry.getValue();
            Object theType = map.get("type");
            Assertions.assertTrue(theType instanceof String, "In \"properties\", key " + entry.getKey() + "'s value missing \"type\"");
            Object title = map.get("title");
            Assertions.assertTrue(title instanceof String, "In \"properties\", key " + entry.getKey() + "'s value missing \"title\"");
        }

        for (String key : connectionInputs.keySet()) {
            Assertions.assertTrue(propertiesMap.containsKey(key), "Key \"" + key + "\" in test config json file is not defined in \"optionConfig.connection.properties\"");
        }

        if (!missingInConnection.isEmpty())
            PDKLogger.warn(TAG, "Missing keys " + Arrays.toString(missingInConnection.toArray()) + " in connection test config json file which is defined in spec json file, \"configOptions.connection.properties\"");
    }

}
