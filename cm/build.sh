#!/bin/sh
set -x # echo all commands before execution
set -e # exit if any command returns non-zero

# by default, a pipeline's exit status is equal to its last command
# however, we want the pipeline to fail if any of its commands fail
#set -o pipefail

readonly PROGNAME=$(basename "$0")

readonly COMMAND="$1"
export SDLC="$2"
export sdlc=${SDLC,,}
export STACK_NAME="$3"
export USERS_COUNT="$4"
export THRESHOLD="$5"
export RAMPUP_DURATION="$6"
export THROUGH_PUT="$7"
export USER="$8"

echo "-------------------------------------------"
echo "Pipeline"
echo "$RUNTYPE"
export ARTIFACTS="$WORKSPACE/release"
export COMPONENT="$COMPONENT"
export AGS=DIVER

usage() {
    cat <<-EOF
Execute a stage in a Jenkins pipeline.

    usage: $PROGNAME <command> <sdlc> <runtype>

where <command> can be any one of
    app         - builds the maven package
    deploy      - deploys the image to ecs

and <sdlc> is one of: dev*, qa*, prody
EOF
    exit 1
}

credSDLC=$SDLC
if [[ "$SDLC" == "PRODY" ]]
then
  	credSDLC="PROD-ANLYS"
fi

############################################################
# utility functions
############################################################
aws_login() {
    case "$SDLC" in
      dev)  eval "$(aws ecr get-login --no-include-email --region us-east-1 --registry-ids $VPC)";;
      *) eval "sudo $(aws ecr get-login --no-include-email --region us-east-1 --registry-ids $VPC)";;
    esac
}

fip_access_token() {
  # get FIP access token to pass to all API calls
  case "$SDLC" in
      DEV*)     FIP_SDLC="devint"
                ;;
      QA*)      FIP_SDLC="qaint"
                ;;
      PRODY)    FIP_SDLC="prod"
                ;;
      *)        exit_error "unknown SDLC" ;;
  esac

  BASE_64_AUTH=$(echo -n $1:$2 | base64)
  FIP_SERVER_URL=$(aws ssm get-parameters --names "/share/FIP/isso/${FIP_SDLC}/fipServerUrl" --region us-east-1 --output text --query "Parameters[0].Value")
  echo $(curl --location --request POST ${FIP_SERVER_URL}'/rest/isso/oauth2/access_token?grant_type=client_credentials' --header 'Authorization: Basic '${BASE_64_AUTH} | jq -r '.access_token') # dHN0X21hdF91c2VyMTpWU3Bic0tYbXlBVGozeHo0TzR6SWE4bFE=
  # curl --location --request POST 'https://isso-devint.fip.dev._____.org/fip/rest/isso/oauth2/access_token?grant_type=client_credentials' --header 'Authorization: Basic dHN0X21hdF91c2VyMTpWU3Bic0tYbXlBVGozeHo0TzR6SWE4bFE='
}

exit_error() {
    echo "$PROGNAME: $@"
    exit 1
}

############################################################
# pipeline functions
############################################################
build_app() {
	  echo "-------------------------------------------"
    echo "Create Workspace $WORKSPACE/checkout"
    echo "-------------------------------------------"
    cd $WORKSPACE/checkout

	  echo "-------------------------------------------"
    echo "Compress framework"
    echo "-------------------------------------------"
    zip -r "test-performance-diver.zip" test-performance-diver

	  echo "-------------------------------------------"
    echo "Create Artifacts folder"
    echo "-------------------------------------------"
	  mkdir -p "$ARTIFACTS"

	  echo "-----------------------------------"
    echo "FJDSL : Creating $WORKSPACE/release/buildinfo..."
    echo "-----------------------------------"
    echo "@(#) Project: ${COMPONENT}" > $ARTIFACTS/buildinfo
    echo "@(#) Release: ${RELEASE}" >> $ARTIFACTS/buildinfo
    echo "@(#) Timestamp: ${BUILD_TIMESTAMP}" >> $ARTIFACTS/buildinfo
    echo "@(#) Application: ${AGS}" >> $ARTIFACTS/buildinfo
    echo "@(#) Component: ${COMPONENT}" >> $ARTIFACTS/buildinfo
    echo "@(#) BUILD_URL: ${BUILD_URL}" >> $ARTIFACTS/buildinfo
    echo "@(#) GIT BRANCH: ${GIT_BRANCH}" >> $ARTIFACTS/buildinfo

	  echo "-------------------------------------------"
    echo "Copy compressed framework to Artifacts folder"
    echo "-------------------------------------------"
    cp -R ./test-performance-diver/cm/build.sh $ARTIFACTS
    cp -R ./test-performance-diver.zip $ARTIFACTS
    cp -R $M3_SETTINGS $ARTIFACTS

	  echo "-------------------------------------------"
    echo "Change directory to Artifacts folder"
    echo "-------------------------------------------"
    cd $ARTIFACTS
    echo "Build script is done"
}

deploy_job(){
  cd "$ARTIFACTS"
  python -m zipfile -e test-performance-diver.zip .
  cd test-performance-diver

  echo "Stack name is: ${STACK_NAME}"
  echo "Branch name is ${GIT_BRANCH}"

  case "$SDLC" in
      PRODY)  echo "URL: https://diver-ecs-${STACK_NAME}.diver.pa._____.org";;
      *) echo "URL: https://diver-ecs-${STACK_NAME}.diver.${sdlc}._____.org";;
  esac

  echo "-------------------------------------------"
  echo "Extracting ${USER} user credentials from credstashfx and token"
  echo "-------------------------------------------"

  USER_PASSWORD=`credstashfx get ${USER} -T AGS=${AGS} SDLC=${credSDLC}`
  USER_TOKEN=$(fip_access_token ${USER} ${USER_PASSWORD})

  echo "-------------------------------------------"
  echo "Build - Run maven"
  echo "-------------------------------------------"
  MAVEN=/apps/maven/apache-maven-3.3.9/bin/mvn
  $MAVEN -X clean install -s ${WORKSPACE}/release/settings.xml

  echo "-------------------------------------------"
  echo "Executing Gatling test"
  echo "-------------------------------------------"
  case "$SDLC" in
      PRODY)  $MAVEN -s ${WORKSPACE}/release/settings.xml gatling:test -DbaseUrl="https://diver-ecs-${STACK_NAME}.diver.pa._____.org" -Dusers=${USERS_COUNT} -Dthreshold=${THRESHOLD} -Drampup=${RAMPUP_DURATION} -Dthroughput=${THROUGH_PUT} -DuserName=${USER} -Dtoken=${USER_TOKEN} -Dags=DIVER -Dsdlc=${credSDLC};;
      *) $MAVEN -s ${WORKSPACE}/release/settings.xml gatling:test -DbaseUrl="https://diver-ecs-${STACK_NAME}.diver.${sdlc}._____.org" -Dusers=${USERS_COUNT} -Dthreshold=${THRESHOLD} -Drampup=${RAMPUP_DURATION} -Dthroughput=${THROUGH_PUT} -DuserName=${USER} -Dtoken=${USER_TOKEN} -Dags=DIVER -Dsdlc=${credSDLC};;
  esac
}

############################################################
# command execution logic
############################################################
case "$COMMAND" in
    app)    build_app ;;
    deploy) deploy_job ;;
    *)
esac
