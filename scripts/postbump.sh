#!/bin/bash

TEMPLATE="push_commit_desc.txt"
HMI_FUNCTION="$(cat package.json | jq -r .hmi.function)"
HMI_MODULE="$(cat package.json | jq -r .hmi.module)"
VERSION="$(cat package.json | jq -r .version)"

echo "[${HMI_FUNCTION}] ${HMI_MODULE} release v${VERSION}" > ${TEMPLATE}
echo  >> ${TEMPLATE}
echo "[ISSUE Tracker] &JIRA_NAME&" >> ${TEMPLATE}
echo "[ISSUETYPE] OEM_REQ" >> ${TEMPLATE}
echo "[RS_ID] NA" >> ${TEMPLATE}
echo "[DESIGN_ID] NA" >> ${TEMPLATE}
echo "[EPIC] NA" >> ${TEMPLATE}
echo "[DESC] see changes in CHANGELOG.md" >> ${TEMPLATE}
echo  >> ${TEMPLATE}
git add ${TEMPLATE}

