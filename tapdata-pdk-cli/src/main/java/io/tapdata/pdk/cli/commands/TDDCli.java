package io.tapdata.pdk.cli.commands;

import io.tapdata.pdk.cli.CommonCli;
import io.tapdata.pdk.core.connector.TapConnector;
import io.tapdata.pdk.core.tapnode.TapNodeInfo;
import io.tapdata.pdk.core.utils.CommonUtils;
import io.tapdata.pdk.tdd.core.PDKTestBase;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.PackageSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

@CommandLine.Command(
        description = "Push PDK jar file into Tapdata",
        subcommands = MainCli.class
)
public class TDDCli extends CommonCli {
    private static final String TAG = TDDCli.class.getSimpleName();
    @CommandLine.Parameters(paramLabel = "FILE", description = "One ore more pdk jar files")
    File file;
//    @CommandLine.Option(names = { "-t", "--testCase" }, required = false, description = "Specify the test class simple name to test")
//    private String testClass;
    @CommandLine.Option(names = { "-l", "--connectorLevel" }, required = false, description = "Connector has three levels, beginner, intermediate and expert, more functions connector implemented, the level is higher. ")
    private String level = LEVEL_BEGINNER;
    @CommandLine.Option(names = { "-c", "--testConfig" }, required = true, description = "Specify the test json configuration file")
    private String testConfig;
    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "TapData cli help")
    private boolean helpRequested = false;
    private SummaryGeneratingListener listener = new SummaryGeneratingListener();
    public void runOne(String testClass) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass("io.tapdata.pdk.tdd.tests." + testClass))
                .build();
        runTests(request);
    }

    public void runLevel() {
        List<PackageSelector> selectors = new ArrayList<>();
        switch (level) {
            case LEVEL_EXPERT:
                selectors.add(DiscoverySelectors.selectPackage(SOURCE_PREFIX + LEVEL_EXPERT));
                selectors.add(DiscoverySelectors.selectPackage(TARGET_PREFIX + LEVEL_EXPERT));
            case LEVEL_INTERMEDIATE:
                selectors.add(DiscoverySelectors.selectPackage(SOURCE_PREFIX + LEVEL_INTERMEDIATE));
                selectors.add(DiscoverySelectors.selectPackage(TARGET_PREFIX + LEVEL_INTERMEDIATE));
            case LEVEL_BEGINNER:
                selectors.add(DiscoverySelectors.selectPackage("io.tapdata.pdk.tdd.tests.basic"));
                selectors.add(DiscoverySelectors.selectPackage(SOURCE_PREFIX + LEVEL_BEGINNER));
                selectors.add(DiscoverySelectors.selectPackage(TARGET_PREFIX + LEVEL_BEGINNER));
                break;

        }
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
//                .selectors(selectPackage("io.tapdata.pdk.tdd.tests.basic"),
//                        selectPackage("io.tapdata.pdk.tdd.tests.source." + level),
//                        selectPackage("io.tapdata.pdk.tdd.tests.target." + level))
                .selectors(selectors)
                .filters(includeClassNamePatterns(".*Test"))
                .build();
        runTests(request);
    }

    public static final String LEVEL_BEGINNER = "beginner";
    public static final String LEVEL_INTERMEDIATE = "intermediate";
    public static final String LEVEL_EXPERT = "expert";

    public static final String SOURCE_PREFIX = "io.tapdata.pdk.tdd.tests.source.";
    public static final String TARGET_PREFIX = "io.tapdata.pdk.tdd.tests.target.";

    private void runTests(LauncherDiscoveryRequest request) {
        Launcher launcher = LauncherFactory.create();
//        TestPlan testPlan = launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
        summary.printFailuresTo(new PrintWriter(System.out));
    }

    public Integer execute() throws Exception {
        testPDKJar(file, testConfig);
        return 0;
    }

    private void testPDKJar(File file, String testConfig) {
        CommonUtils.setProperty("pdk_test_jar_file", file.getAbsolutePath());
        CommonUtils.setProperty("pdk_test_config_file", testConfig);

        PDKTestBase testBase = new PDKTestBase();
        TapConnector testConnector = testBase.getTestConnector();
        Collection<TapNodeInfo> tapNodeInfoCollection = testConnector.getTapNodeClassFactory().getConnectorTapNodeInfos();

        switch (level) {
            case LEVEL_BEGINNER:
            case LEVEL_EXPERT:
            case LEVEL_INTERMEDIATE:
                break;
            default:
                throw new IllegalArgumentException("Level is illegal, need to be " + LEVEL_BEGINNER + ", " + LEVEL_INTERMEDIATE + ", or " + LEVEL_EXPERT);
        }
        runLevel();
    }

}
