#!/usr/bin/env groovy
pipeline{
    agent any

    //Define stages for the build process
    stages{
        //Define the deploy stage
        stage('Deploy'){
            steps{
                script{
                    docker.withRegistry('https://gt-build.hdap.gatech.edu') {
                        //Build and push the database image
                        def pacerIndexImage = docker.build("pacerindex:1.0", "-f ./Dockerfile .")
                        pacerIndexImage.push('latest')
                    }
                }
            }
        }

        //Define stage to notify rancher
        stage('Notify'){
            steps{
                script{
                    rancher confirm: true, credentialId: 'gt-rancher-server', endpoint: 'https://gt-rancher.hdap.gatech.edu/v2-beta', environmentId: '1a7', environments: '', image: 'gt-build.hdap.gatech.edu/pacerindex:latest', ports: '', service: 'GPHD/pacer-index-api', timeout: 60
                }
            }
        }
    }
}