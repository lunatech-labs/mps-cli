/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package org.mps_cli.gradle.plugin

class ModuleDependenciesBuildingTest extends TestBase {

    def "module dependencies extraction test"() {
        given:
        projectName = proj
        settingsFile << ""
        buildFile << "${buildPreamble4ModuleDependenciesExtraction()}" + '''

task extractModuleDependencies {
    dependsOn buildModuleDependencies
    doLast {
        def solution2AllDownstreamDependencies = buildModuleDependencies.solution2AllDownstreamDependencies
        def solution2AllUpstreamDependencies = buildModuleDependencies.solution2AllUpstreamDependencies
        
        for (Object sol : solution2AllDownstreamDependencies.keySet()) {
            println "downstream dependencies for ${sol.name} are: ${solution2AllDownstreamDependencies[sol].collect { it.name }}"
        }

        for (Object sol : solution2AllUpstreamDependencies.keySet()) {
            println "upstream dependencies for ${sol.name} are: ${solution2AllUpstreamDependencies[sol].collect { it.name }}"
        }
    }
}

'''

        when:
        runTask("extractModuleDependencies")

        then:
        // check downstream dependencies
        result.output.contains("downstream dependencies for mps.cli.lanuse.library_second are: []") ||
                result.output.contains("downstream dependencies for mps.cli.lanuse.library_second.default_persistency are: []")
        result.output.contains("downstream dependencies for mps.cli.lanuse.library_top are: [mps.cli.lanuse.library_second]") ||
            result.output.contains("downstream dependencies for mps.cli.lanuse.library_top.default_persistency are: [mps.cli.lanuse.library_second.default_persistency]")

        // check upstream dependencies
        result.output.contains("upstream dependencies for mps.cli.lanuse.library_second are: [mps.cli.lanuse.library_top]") ||
                result.output.contains("upstream dependencies for mps.cli.lanuse.library_second.default_persistency are: [mps.cli.lanuse.library_top.default_persistency]")
        result.output.contains("upstream dependencies for mps.cli.lanuse.library_top are: []") ||
                result.output.contains("upstream dependencies for mps.cli.lanuse.library_top.default_persistency are: []")

        where:
        proj                                 | library_top_dot_library_top                                  | library_top_dot_authors_top                                  | library_second_dot_library_top
        "mps_cli_lanuse_file_per_root"       | "mps.cli.lanuse.library_top.library_top"                     | "mps.cli.lanuse.library_top.authors_top"                     | "mps.cli.lanuse.library_second.library_top"
        "mps_cli_lanuse_default_persistency" | "mps.cli.lanuse.library_top.default_persistency.library_top" | "mps.cli.lanuse.library_top.default_persistency.authors_top" | "mps.cli.lanuse.library_second.default_persistency.library_top"
        "mps_cli_lanuse_binary"              | "mps.cli.lanuse.library_top.library_top"                     | "mps.cli.lanuse.library_top.authors_top"                     | "mps.cli.lanuse.library_second.library_top"
    }

    def buildPreamble4ModuleDependenciesExtraction()
    {
        """ 
plugins {
    id('org.mps_cli.gradle.plugin')
}

buildModuleDependencies {
   sourcesDir = ['../../../../../../../../../mps_test_projects/$projectName']   
}"""
    }
}
