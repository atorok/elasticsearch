#!/bin/bash 

if [ -z "$BRANCH" ] ; then 
    echo "BRANCH is unset"
    exit 1
fi 

rm -Rf .ci/jobs
cp -r .ci/jobs.t .ci/jobs

.ci/build.sh updateCIBwcVersions

sed -i "s/%BRANCH%/${BRANCH}/g" .ci/jobs/*.yml
