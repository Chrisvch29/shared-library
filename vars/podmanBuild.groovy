def call(String project, String ImageTag, String hubUser){
    
    sh """
     podman image build -t ${hubUser}/${project} . 
     podman image tag ${hubUser}/${project} ${hubUser}/${project}:${ImageTag}
     podman image tag ${hubUser}/${project} ${hubUser}/${project}:latest
    """
}
