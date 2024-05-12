// def call(String project, String ImageTag = 'latest', String hubUser = 'defaultUser') {
//     def imageName = "${hubUser}/${project}"
//     def customContainers = [
//         [name: 'podman',
//          image: 'quay.io/podman/stable',
//          command: 'cat',
//          ttyEnabled: true,
//          privileged: true,
//          alwaysPullImage: true,
//          volumes: [
//              [hostPathVolume(
//                  mountPath: '/dev/fuse',
//                  hostPath: '/dev/fuse',
//                  readOnly: false
//              )],
//              [hostPathVolume(
//                  mountPath: '/var/lib/containers',
//                  hostPath: '/opt/container',
//                  readOnly: false
//              )]
//          ]
//         ]
//     ]

//     try {
//         // Construir la imagen principal
//         sh "podman image build -t ${imageName} ."

//         // Etiquetar la imagen principal con el tag proporcionado
//         if (ImageTag) {
//             sh "podman image tag ${imageName} ${imageName}:${ImageTag}"
//         }

//         // Etiquetar la imagen principal como 'latest'
//         sh "podman image tag ${imageName} ${imageName}:latest"

//         // Construir y etiquetar contenedores personalizados
//         customContainers.each { container ->
//             String containerName = container.name
//             String containerImage = container.image
//             String containerCommand = container.command ?: 'cat'
//             boolean ttyEnabled = container.ttyEnabled ?: true
//             boolean privileged = container.privileged ?: false
//             boolean alwaysPullImage = container.alwaysPullImage ?: false
//             List volumes = container.volumes ?: []

//             sh """
//             podman image build -t ${containerImage} .
//             podman image tag ${containerImage} ${containerImage}:${ImageTag}
//             podman image tag ${containerImage} ${containerImage}:latest
//             """

//             containerTemplate(
//                 name: containerName,
//                 image: containerImage,
//                 command: containerCommand,
//                 ttyEnabled: ttyEnabled,
//                 privileged: privileged,
//                 alwaysPullImage: alwaysPullImage,
//                 volumes: volumes.flatten() // Flatten volumes list
//             )
//         }

//         echo "Imagen construida y etiquetada exitosamente."
//     } catch (Exception e) {
//         echo "Error: ${e.message}"
//         currentBuild.result = 'FAILURE'
//     }
// }


def call(String project, String ImageTag = 'latest', String hubUser = 'defaultUser') {
    def imageName = "${hubUser}/${project}"
    
    try {
        // Construir la imagen
        containerTemplate(
            name: 'podman',
            image: 'quay.io/podman/stable', // Reemplaza con la ruta a tu imagen personalizada
            ttyEnabled: true,
            command: 'cat'
        ) {
            // Ejecutar comandos dentro del contenedor
            sh "podman image build -t ${imageName} ."
            // Resto del código...
        }
    } catch (Exception e) {
        echo "Error: ${e.message}"
        currentBuild.result = 'FAILURE'
    }
}
