def call(String project, String imageTag, String gitUser){
    
    sh """
     podman build -t registry.demo.com/${gitUser}/${project}:${imageTag} .
    """
}
