def call(String project, String imageTag, String gitUser){
    
    sh """
     podman push --tls-verify=false registry.demo.com/${gitUser}/${project}:${imageTag}
    """
}
