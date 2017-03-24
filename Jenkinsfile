#!groovy
podTemplate(label: 'pipe2scala', containers: [
        containerTemplate(name: 'jnlp', image: 'henryrao/jnlp-slave', args: '${computer.jnlpmac} ${computer.name}', alwaysPullImage: true),
        containerTemplate(name: 'kubectl', image: 'henryrao/kubectl:1.5.2', ttyEnabled: true, command: 'cat'),
        containerTemplate(name: 'sbt', image: 'henryrao/sbt:211', ttyEnabled: true, command: 'cat', alwaysPullImage: true),
        containerTemplate(name: 'docker', image: 'docker:1.12.6', ttyEnabled: true, command: 'cat')
],
        volumes: [
                hostPathVolume(mountPath: '/root/.kube/config', hostPath: '/root/.kube/config'),
                persistentVolumeClaim(claimName: 'jenkins-ivy2', mountPath: '/home/jenkins/.ivy2', readOnly: false)
        ],
        workspaceVolume: emptyDirWorkspaceVolume(false)
) {
    node('pipe2scala') {
        ansiColor('xterm') {
            checkout scm
            stage('compile') {
                container('sbt') {
                    sh 'sbt cpJarsForDocker'
                }
            }
            stage('build image'){
                dir('target/docker') {
                    container('docker') {
                        def mainClass = sh(returnStdout: true, script: 'cat main').trim()
                        echo "${mainClass}"
                    }
                }
            }
            step([$class: 'LogParserPublisher', failBuildOnError: true, unstableOnWarning: true, showGraphs: true,
                  projectRulePath: 'jenkins-rule-logparser', useProjectRule: true])
        }

    }

}