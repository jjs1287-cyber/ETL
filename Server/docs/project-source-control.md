# GitHub project source control

Studio supports one ETLForge project per GitHub repository. The repository root must contain exactly one .jip file and the complete project directory, including Data, settings and filestore assets.

## Opening and publishing

The Open Project screen includes **Open from Source Control**. Enter an HTTPS GitHub URL or git@github.com:owner/repository.git, an optional branch, and a new local checkout directory. The complete repository is cloned before Studio loads the project. A failed clone/open retains any created checkout for diagnosis. Existing local directories are never overwritten.

For an existing local project, choose **Source Control > Connect to GitHub**, enter the URL of an existing empty repository, and configure **Git Identity** with a name and email. Choose **Commit and Push**, review the changed-file list and enter a required comment. This also appears under **Actions**. Source Control additionally provides Push, Changed Files and Commit History. No repository is created on GitHub automatically.

Authenticate in advance using Git Credential Manager (HTTPS) or an SSH agent and verified GitHub host key (SSH). For the supplied portable Git, Git Credential Manager is available through `C:\ETL\Tools\Git\cmd\git.exe credential-manager github login`. Complete its sign-in yourself. Studio does not store GitHub tokens or passwords in projects, command arguments, or configuration screens. Background operations disable interactive credential prompts and time out after 120 seconds. Existing credentials must already be usable without prompting. HTTPS certificate validation and SSH host-key checking remain enabled.

A Source Control menu is beside Actions. Clone supports branch selection; this version does not switch branches, pull, merge or resolve conflicts inside an open project. When a push is rejected, retain the local commit, close the project, reconcile changes with a Git client, then reopen it. No force pushes or automatic destructive resets are performed.

## Deployment rule

For a local project with Git metadata at its root, every Studio deployment requires a nonblank comment, a successful commit and a successful push to origin on the current branch. An unchanged deployment creates an empty checkpoint commit with that comment. Canceling or a Git failure prevents entry into server deployment processing. If push fails after commit, the local commit remains available for retry. Ordinary projects without Git metadata keep their existing deployment behavior.

The check runs in the common Studio DeployJob before DeployManager. Full, selected-item and deploy-before-test paths using that job share the check. Separate command-line deployment tools and direct server APIs do not implement this Studio UI policy. This is a client workflow rule, not a server authorization boundary. Removing Git metadata also removes Git detection.

The preview lists saved files. If their Git status or SHA-256 content fingerprint changes before confirmation completes, Studio requires another review. This is not a filesystem transaction: another external process can still modify files during a Git operation. Use one Studio instance and avoid external project edits while committing/deploying. Local commits represent the saved project; deployment still uses the selected deployment objects and their required dependencies.

## Files and secrets

Studio creates/appends a .gitignore excluding .project.lock and root cache/log/logs/temp/tmp directories and the persist runtime-history directory, *.tmp and *.lck. Already tracked local cache/log/lock files block commits until removed from the index. All other project definitions, resources and binary assets are included. UTF-8 text is preserved; malformed UTF-8 Git output/ignore text is rejected. Symlinks, junctions, nested repositories and submodules are rejected. Native Git hooks are disabled for these Studio operations; signing is not enabled by this workflow.

Saved source/target credentials and Project Variable values may be present in project files. The commit screen warns that these files will be included. Masking a password in Studio does not remove it from Git history. Use a private repository and external secret references where possible. This feature does not rewrite existing project credential storage or scrub Git history.

## Implementation and packaging

Native Git performs clone, staging, commit, push, status and history. Java is limited to process orchestration and the existing Java Studio UI/project loader; duplicating the Studio object model or implementing Git in new C++ code would reduce compatibility. No Tomcat or server runtime calls are added.

Git 2.53.0.windows.3 is supplied separately at C:\ETL\Tools\Git (approximately 124 MB uncompressed), including Git Credential Manager, SSH and upstream licenses. The resolver accepts -Detlforge.git.executable=<absolute git.exe path>, otherwise uses the supplied path when present, then Git on PATH. A relocated installer should set that property or configure PATH. An optional Git component or detection of an existing compatible Git installation can keep the installer smaller. GitHub account access, branch protection, signed-commit policies and repository-specific requirements need verification against the user's actual repository.

Source: Studio/src/main/java/org/etlforge/integration/client/project/sourcecontrol and Studio/src/main/java/org/etlforge/integration/client/ui/project/sourcecontrol. Regression fixtures are in Server/tests/integration/GitProjectRepositoryTest.java, SourceControlUiTest.java and git_fixture_http.py. Recovery originals: C:\ETL\Archives\source-control-20261029.

## Verification

Disposable loopback Git smart-HTTP fixtures exercise the real native Git transport, clone, commit and push. A test-only global configuration rewrites fixture GitHub URLs to that local server; it is isolated from user Git configuration and is not installed in Studio. Tests verify UTF-8/binary preservation, ignored files, deletions, comment validation, checkpoint commits, divergent/missing-remote failures, menu wiring, cancellation/failure deployment blocking, successful deployment continuation, and the actual Clone and Open button reopening a complete Studio project.

Real GitHub authentication and repository permissions remain to be tested after the user supplies the new private repository URL.

Initial remote supplied: https://github.com/JacobStantonEndowanceCom/ETLForge-Project1.git. An unauthenticated read required credentials. Git Credential Manager browser sign-in was launched once; the user reported an account/sign-in disabled message. Pending sign-in processes were stopped and no retries or uploads were performed. The exact error and whether it concerns GitHub or Credential Manager remain unconfirmed.

Final verification: 57 checks passed against installed JARs (39 Git/repository and 18 Studio UI/deployment/clone checks). All eight services remained running. Test fixtures were closed.

Subsequent real GitHub verification completed using jjs1287-cyber and private repository https://github.com/jjs1287-cyber/FTLForge-Project1.git. Test Operations was published on main (e849aff1590339fdac5d3037cccd67d6e789b6c2). All 47 tracked files matched a fresh checkout, which opened in Studio with 5 operations and 3 scripts. These three real checks supplement the 57 installed fixture checks. Earlier authentication failure is superseded by this successful account sign-in and publication.
