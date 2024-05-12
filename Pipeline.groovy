pipeline {
    agent any
    
    stages {
        stage('Build and Deploy') {
            steps {
                script {
                    podTemplate(label: 'mypod', containers: [
                        containerTemplate(
                            name: 'git',
                            image: 'alpine/git',
                            ttyEnabled: true,
                            command: 'cat'
                        ),
                        containerTemplate(
                            name: 'maven',
                            image: 'maven:3.3.9-jdk-8-alpine',
                            command: 'cat',
                            ttyEnabled: true
                        ),
                        containerTemplate(
                            name: 'podman',
                            image: 'quay.io/podman/stable',
                            command: 'cat',
                            ttyEnabled: true,
                            privileged: true, // Añadido para ejecutar el contenedor con privilegios
                            alwaysPullImage: true, // Siempre se obtiene la última imagen
                            volumes: [
                                hostPathVolume(
                                    mountPath: '/dev/fuse',
                                    hostPath: '/dev/fuse',
                                    readOnly: false
                                ),
                                hostPathVolume(
                                    mountPath: '/var/lib/containers',
                                    hostPath: '/opt/container',
                                    readOnly: false
                                )
                            ]
                        )
                    ]) {
                        node('mypod') {
                            stage('Check running containers') {
                                container('podman') {
                                    // example to show you can run podman commands when you mount the socket
                                    sh 'cat /etc/hosts'
                                    sh 'echo "192.168.1.9 gitlab.demo.com" >> /etc/hosts'
                                    sh 'echo "192.168.1.9 registry.demo.com" >> /etc/hosts'
                                    sh 'echo "192.168.1.9 sonarqube.demo.com" >> /etc/hosts'
                                    sh 'cat /etc/hosts'
                                    sh 'podman ps'
                                }
                            }
                            
                            stage('Clone repository') {
                                container('podman') {
                                    sh 'dnf install git -y'
                                    sh 'whoami'
                                    sh 'git -c http.sslVerify=false  clone -b main https://gitlab.demo.com/demo/crud.git'
                                }
                            }

                            stage('Maven Build') {
                                container('podman') {
                                    dir('crud/') {
                                        sh 'export SHORT_NAME_RESOLUTION=never'
                                        sh 'podman login --tls-verify=false  -u demo -p redhat11 registry.demo.com'
                                        sh 'podman build -t registry.demo.com/demo/crud:v2 .'
                                        sh 'podman push --tls-verify=false registry.demo.com/demo/crud:v2'
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
