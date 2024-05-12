def call(String gitPass, String gitUser){
    
    sh """
     podman login --tls-verify=false  -u ${gitUser} -p ${gitPass} registry.demo.com
    """
}
