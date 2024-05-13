def call(String gitUser, String project, String imageTag){
    
    sh """
     podman push --tls-verify=false registry.demo.com/${gitUser}/${project}:${imageTag}
    """
}
