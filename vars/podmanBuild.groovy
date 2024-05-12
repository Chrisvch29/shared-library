def call(String project, String ImageTag, String gitUser){
    
    sh """
     podman login --tls-verify=false  -u ${gitUser} -p redhat11 registry.demo.com
     podman build -t registry.demo.com/${gitUser}/${project}:${ImageTag} .
     podman push --tls-verify=false registry.demo.com/${gitUser}/${project}:${ImageTag}
    """
}
