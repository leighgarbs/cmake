GITLAB_VERSION = '11.0'

def setUnstableOnShellResult =
{
  resultShell, resultUnstable ->
  if(resultShell == resultUnstable)
  {
    currentBuild.result = 'UNSTABLE'
  }
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

def stageCppcheck =
{
  runCppcheckArgs = '' ->
  def shellReturnStatus = sh returnStatus: true, script: """
    $TEMP_BIN/run-cppcheck -J $runCppcheckArgs .
  """

  setUnstableOnShellResult(shellReturnStatus, 1)
}

def stageWarnings =
{
  warningParser = 'GNU Make + GNU C Compiler (gcc)' ->
    warnings canComputeNew: false,
           canResolveRelativePaths: false,
           categoriesPattern: '',
           consoleParsers: [[parserName: warningParser]]
}

def stageCMakeBuild =
{
  runCMakeArgs = '', target = '' ->
  sh """
    $TEMP_BIN/run-cmake $runCMakeArgs .
    make $target
  """
}

def stageClangStaticAnalysis =
{
  sh """
    scan-build $TEMP_BIN/run-cmake --debug .
    scan-build -o clangScanBuildReports -v -v --use-cc clang \
      --use-analyzer=/usr/bin/clang make
  """
}
