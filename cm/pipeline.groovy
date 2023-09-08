import org._____.appeng.f3.builders.*
import org._____.appeng.f3.common.*

class ProvisionComponent extends CommonImpl {

    private static String AGS                        = "DIVER"
    private static String COMPONENT                  = "Performance"
    private static String DIRECTORY                  = "Testing/Performance"
    private static String GIT_REPOSITORY             = "ssh://git@bitbucket._____.org:7999/diver/test-api-diver.git"
    private static List<String> EMAIL                = ["DL-DIVER@_____.org"]

    private static String BUILD_LABEL                = "amznlinux2_ci:latest"
    private static String CHECK_LABEL                = "amznlinux2_cd:latest"

    private static String JOBS_FOLDER                = "${AGS}/${DIRECTORY}"
    private static String COMPONENT_UPPER            = "${COMPONENT}".toUpperCase()
    private static String BUILD_FOLDER               = "${JOBS_FOLDER}/${COMPONENT_UPPER}"
    private static String EXECUTE_FOLDER             = "${JOBS_FOLDER}/EXECUTE/${COMPONENT_UPPER}"
    private static String ROOT_JOB                   = "${JOBS_FOLDER}/${COMPONENT_UPPER}"
    private static String INITIAL_JOB                = "${COMPONENT_UPPER}"
    private static String JENKINS_THEME              = "https://bitbucket._____.org/users/khmarskv/repos/jenkins-style/raw/css/jenkins-muted.css"
    private static String CM_BUILD_FILE              = "build.sh"
    private static String SCRIPT_PATH                = ". release/${CM_BUILD_FILE}"
    private static String CM_FOLDER                  = "test-performance-diver/cm"
    private static String START_SHELL_JOB            = "checkout/${CM_FOLDER}/${CM_BUILD_FILE} app dev"
    private static String CRON_SCHEDULE              = "H 18 * * 5"

    private static List<String> DEV                  = ["DEV"]
    private static List<String> QA                   = ["QA"]
    private static List<String> PRODY                = ["PRODY"]

    def suffix = [DEV:".diver.dev._____.org", QA:".diver.qa._____.org", PRODY: ".diver.pa._____.org"]

    ECSLabel generateEcsLabel(String image, String cluster, Integer memory) {
        return ECSLabel.newInstance()
                .withImage(image)
                .withCluster(cluster.toUpperCase())
                .withRole("JENKINS_${AGS}")
                .withMemory("${memory.toString()}.0")
    }

    BppView buildPipeline() {
        Logger.log("FJDSL : Creating pipeline view " + ROOT_JOB)
        BppView.newInstance()
                .withSelectedJob(INITIAL_JOB)
                .withDisplayedBuilds(20)
                .withViewFolderLocation(JOBS_FOLDER)
                .withCustomCssUrl(JENKINS_THEME)
                .withStartJob(start())
    }

    JenkinsJob start() {
        def buildJob = JenkinsJob.newInstance()
                .withName(ROOT_JOB)
                .withEcsLabel(generateEcsLabel(BUILD_LABEL, "DEV", 4))
                .withBuildName("#\$BUILD_NUMBER ~ \$GIT_BRANCH")
                .withTemplate(Template.BUILD)
                .withComponent(COMPONENT)
                .withAgs(AGS)
                .withJobShell(START_SHELL_JOB)
                .withConcurrentBuild(true)
                .withGitTagging(false)
                .withDiscardOldBuilds(
                        daysToKeep: 90,
                        artifactDaysToKeep: 90,
                        numToKeep: 200,
                        artifactNumToKeep: 200
                )
                .withRepos([
                        [
                                url: GIT_REPOSITORY,
                                sub_directory: "checkout",
                                description: "Code for the service will be checked out from this branch",
                                branchVariable: "GIT_BRANCH",
                                defaultValue: "develop",
                                withShallow: "true"
                        ]
                ])
                .withInputStringParam([
                        [
                                key        : "STACK_NAME",
                                value      : "sbint",
                                description: "ECS stack to run the api test against with (relqa, sbint, relct)."
                        ],
                        [
                                key        : "USERS_COUNT",
                                value      : "2",
                                description: "Count of simultaneously triggers users"
                        ],
                        [
                                key        : "THRESHOLD",
                                value      : "90",
                                description: "Percentage Of successful API calls"
                        ],
                        [
                                key        : "RAMPUP_DURATION",
                                value      : "60",
                                description: "Rampup duration in seconds"
                        ],
                        [
                                key        : "THROUGH_PUT",
                                value      : "100",
                                description: "Count of Throughput"
                        ],
                        [
                                key        : "USER",
                                value      : "tst_mat_user1",
                                description: "Name of test user"
                        ]
                ])
                .withInputChoiceParam([
                        [
                                key        : "AUTODEPLOYENV",
                                value      : ["DEV", "QA", "PRODY"],
                                description: "Select one or more environments for automatic deployment"
                        ]
                ])
                .withAdditionalEnvVars([
                        [
                                key  : "JAVA_HOME",
                                value: "/usr/lib/jvm/java"
                        ],
                        [
                                key  :"M2_HOME",
                                value: "/apps/maven/apache-maven-3.3.9"
                        ],
                        [
                                key  :"PATH",
                                value: "\${M2_HOME}/bin:\${JAVA_HOME}/bin:\${PATH}"
                        ]
                ])
                .withCronSchedule(CRON_SCHEDULE)
                .withDryRun(false)

        DEV.each {
            buildJob.withAutoTriggerDownstream(this.autoCheck(it))
        }
        QA.each {
            buildJob.withAutoTriggerDownstream(this.autoCheck(it))
        }
        PRODY.each {
            buildJob.withAutoTriggerDownstream(this.autoCheck(it))
        }

        return buildJob
    }

    private JenkinsJob autoCheck(String version){
        def checkJob = JenkinsJob.newInstance()
                .withName(EXECUTE_FOLDER + "/CHECK_ENV_" + version)
                .withBuildName("#\$BUILD_NUMBER ~ diver-ecs-\$STACK_NAME${suffix[version]} ~ \$GIT_BRANCH")
                .withEcsLabel(generateEcsLabel(CHECK_LABEL, "${version}",2))
                .withTemplate(Template.CHECK)
                .withConcurrentBuild(true)
                .withAutoDeployEnv(version)

        checkJob.withAutoTriggerDownstream(deploy(version))

        return checkJob
    }

    // Provision component stack and associate ECS service to deployed image (in ECR)
    private JenkinsJob deploy(String version) {
        def deployJob = JenkinsJob.newInstance()
                .withName(EXECUTE_FOLDER + "/RUN_PERFORMANCE_" + version)
                .withBuildName("#\$BUILD_NUMBER ~ diver-ecs-\$STACK_NAME${suffix[version]} ~ \$GIT_BRANCH")
                .withEcsLabel(generateEcsLabel(BUILD_LABEL, "${version}",4))
                .withShell(SCRIPT_PATH + " deploy " + version + " \$STACK_NAME" + " \$USERS_COUNT" + " \$THRESHOLD" + " \$RAMPUP_DURATION" + " \$THROUGH_PUT" + " \$USER")
                .withConcurrentBuild(true)
                .withS3CopyJob(BUILD_FOLDER)
                .withAdditionalEnvVars([
                        [
                                key  : "JAVA_HOME",
                                value: "/usr/lib/jvm/java"
                        ],
                        [
                                key  :"M2_HOME",
                                value: "/apps/maven/apache-maven-3.3.9"
                        ],
                        [
                                key  :"PATH",
                                value: "\${M2_HOME}/bin:\${JAVA_HOME}/bin:\${PATH}"
                        ]
                ])
                .withGatling(Gatling.newInstance().
                        withTrackGatlingLoadSimulation(true)
                )
                .withEmails(Email.newInstance()
                        .withAttachmentPatterns(["release/buildinfo"])
                        .withAttachBuildLog(true)
                        .withSubject("[\${BUILD_STATUS}] ${AGS} >> ${COMPONENT_UPPER} >> \$BUILD_NUMBER (diver-ecs-\$STACK_NAME${suffix[version]} ~ \$GIT_BRANCH)\n")
                        .withContent("${AGS} >> ${DIRECTORY} >> ${COMPONENT_UPPER} >> \$BUILD_NUMBER (diver-ecs-\$STACK_NAME${suffix[version]} ~ \$GIT_BRANCH)\n" +
                                "BUILD_NUMBER:\${BUILD_NUMBER}\n"+
                                "RUN_TYPE: ${COMPONENT_UPPER}\n" +
                                "BRANCH NAME: \$GIT_BRANCH\n" +
                                "ENV: ${version}\n" +
                                "STACK NAME: \$STACK_NAME\n" +
                                "USER=\$USER\n")
                        .withTriggerList(["failure", "unstable", "fixed", "success"])
                        .withRecipientList(EMAIL))
                .withTemplate(Template.DEPLOY)
    }
}

ProvisionComponent.newInstance().createPipeline(this, ProvisionComponent.newInstance().buildPipeline())
