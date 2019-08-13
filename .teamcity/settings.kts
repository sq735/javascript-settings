import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.NUnitStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.nuGetInstaller
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.nuGetPack
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.nunit
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.1"

project {

    vcsRoot(HttpsGithubComG0t4teamcityCourseAspnetIdentityMongoRefsHeadsMaster)

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    artifactRules = """src\AspNet.Identity.MongoDB\obj\Release => release.zip"""

    vcs {
        root(HttpsGithubComG0t4teamcityCourseAspnetIdentityMongoRefsHeadsMaster)
    }

    steps {
        nuGetInstaller {
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            projects = "src/AspNet.Identity.MongoDB.sln"
        }
        visualStudio {
            path = "src/AspNet.Identity.MongoDB.sln"
            version = VisualStudioStep.VisualStudioVersion.vs2019
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V16_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V16_0
            configuration = "Release"
        }
        nunit {
            nunitVersion = NUnitStep.NUnitVersion.NUnit_2_6_4
            runtimeVersion = NUnitStep.RuntimeVersion.v4_0
            includeTests = """build\tests\Tests.dll"""
            coverage = dotcover {
            }
        }
        nuGetPack {
            toolPath = "%teamcity.tool.NuGet.CommandLine.DEFAULT%"
            paths = "src/AspNet.Identity.MongoDB/AspNet.Identity.MongoDB.csproj"
            outputDir = "build"
            cleanOutputDir = false
            publishPackages = true
        }
    }

    triggers {
        vcs {
        }
    }
})

object HttpsGithubComG0t4teamcityCourseAspnetIdentityMongoRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/sq735/teamcity-course-aspnet-identity-mongo"
    url = "https://github.com/sq735/teamcity-course-aspnet-identity-mongo"
    branchSpec = "+:refs/heads/*"
})
