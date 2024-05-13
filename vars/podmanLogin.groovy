def call(String gitUser, String gitPass){
    
    sh """
     podman login --tls-verify=false  -u ${gitUser} -p ${gitPass} registry.demo.com
    """
}
