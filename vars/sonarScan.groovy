// def call(credentialsId){

//     withSonarQubeEnv(credentialsId: credentialsId) {
//          sh 'mvn clean package sonar:sonar -s settings.xml'
//     }
// }

def call(String project){
    
    sh """
     mvn clean package -DskipTests sonar:sonar -s settings.xml
    """
}
