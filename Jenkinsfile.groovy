GITLAB_VERSION = '11.0'

def setUnstableOnShellResult =
{
  resultShell, resultUnstable ->
  if(resultShell == resultUnstable)
  {
    currentBuild.result = 'UNSTABLE'
  }
}

def saveArtifacts =
{
  sh '''
    ARTIFACTS_DIR=artifacts
    ARTIFACTS_STAGE_DIR=$ARTIFACTS_DIR/$STAGE_NAME

    mkdir -p "$ARTIFACTS_DIR"
    rm -rf "$ARTIFACTS_STAGE_DIR"
    mkdir -p "$ARTIFACTS_STAGE_DIR"
    cd workdir
    git ls-files -o --directory | xargs -n 1 -I{} cp -a --parents {} \
      "../$ARTIFACTS_STAGE_DIR"
  '''
}

def doStage =
{
  stageName, stageBody ->
  stage (stageName)
  {
    gitlabCommitStatus(name: stageName)
    {
      stageBody()
    }

    if (currentBuild.result == 'UNSTABLE')
    {
      updateGitlabCommitStatus(name: stageName, state: 'failed')
    }
  }
}

def cleanUp =
{
  sh '''
    cd workdir
    git clean -x -d -f
  '''
}

def stageCppcheck =
{
  cleanUp()

  dir('workdir')
  {
    def shellReturnStatus = sh returnStatus: true, script: '''
      ../bin/run-cppcheck -J --suppress=unusedFunction .
    '''

    setUnstableOnShellResult(shellReturnStatus, 1)

    publishCppcheck displayAllErrors: false,
                    displayErrorSeverity: true,
                    displayNoCategorySeverity: true,
                    displayPerformanceSeverity: true,
                    displayPortabilitySeverity: true,
                    displayStyleSeverity: true,
                    displayWarningSeverity: true,
                    pattern: 'cppcheck-result.xml',
                    severityNoCategory: false
  }

  saveArtifacts()
}

def stageBuildDebug =
{
  cleanUp()

  sh '''
    cd workdir
    ../bin/run-cmake --debug .
    make -B unittests
  '''

  saveArtifacts()
}

def stageBuildRelease =
{
  cleanUp()

  sh '''
    cd workdir
    ../bin/run-cmake --release .
    make -B unittests
  '''

  saveArtifacts()
}

def stageDetectGCCWarnings =
{
  warnings canComputeNew: false,
           canResolveRelativePaths: false,
           categoriesPattern: '',
           consoleParsers: [[parserName: 'GNU Make + GNU C Compiler (gcc)']]
}


def stageDetectClangWarnings =
{
  warnings canComputeNew: false,
           canResolveRelativePaths: false,
           categoriesPattern: '',
           consoleParsers: [[parserName: 'Clang (LLVM based)']],
           defaultEncoding: '',
           excludePattern: '',
           healthy: '',
           includePattern: '',
           messagesPattern: '',
           unHealthy: ''
}

def stageClangStaticAnalysis =
{
  cleanUp()

  sh '''
    cd workdir
    scan-build ../bin/run-cmake --debug .
    scan-build -o clangScanBuildReports -v -v --use-cc clang \
      --use-analyzer=/usr/bin/clang make -B
  '''

  saveArtifacts()
}
