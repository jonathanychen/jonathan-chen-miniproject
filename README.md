# jonathan-chen-miniproject

## PMD - Static Code Analyzer

To install PMD, my chosen tool for static code anaylsis, perform the steps as outlined by the [quickstart page](https://pmd.github.io/):

(This is for MacOS)

1. Download the .zip containing the tool: `curl -OL https://github.com/pmd/pmd/releases/download/pmd_releases%2F7.16.0/pmd-dist-7.16.0-bin.zip`
2. Unzip it: `unzip pmd-dist-7.16.0-bin.zip`
3. Create an alias for the tool: `alias pmd="$HOME/pmd-bin-7.16.0/bin/pmd"`
4. Run it against the path of the directory containing the source code: `pmd check -d path/to/src -R rulesets/java/quickstart.xml -f text`
