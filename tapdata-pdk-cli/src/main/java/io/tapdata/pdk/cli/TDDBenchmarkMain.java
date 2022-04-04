package io.tapdata.pdk.cli;

import picocli.CommandLine;

/**
 * Picocli aims to be the easiest way to create rich command line applications that can run on and off the JVM. Considering picocli? Check what happy users say about picocli.
 * https://picocli.info/
 *
 * @author aplomb
 */
public class TDDBenchmarkMain {
    //
    public static void main(String... args) {
        args = new String[]{
                "tdd", "-c", "B:\\code\\tapdata\\idaas-pdk\\tapdata-pdk-cli\\src\\main\\resources\\config\\aerospike.json",
//                "tdd", "-c", "B:\\code\\tapdata\\idaas-pdk\\tapdata-pdk-cli\\src\\main\\resources\\config\\emptyBenchmark.json",
                "-t", "io.tapdata.pdk.tdd.tests.target.benchmark.BenchmarkTest",
                "B:\\code\\tapdata\\idaas-pdk\\dist\\aerospike-connector-v1.0-SNAPSHOT.jar",
//                "B:\\code\\tapdata\\idaas-pdk\\dist\\doris-connector-v1.0-SNAPSHOT.jar",
//                "B:\\code\\tapdata\\idaas-pdk\\dist\\empty-connector-v1.1-SNAPSHOT.jar",

        };

        Main.registerCommands().parseWithHandler(new CommandLine.RunLast(), args);
    }
}
