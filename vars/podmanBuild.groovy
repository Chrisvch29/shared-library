def call(String gitUser, String project, String imageTag){
    
    sh """
     podman build -t registry.demo.com/${gitUser}/${project}:${imageTag} .
    """
}
