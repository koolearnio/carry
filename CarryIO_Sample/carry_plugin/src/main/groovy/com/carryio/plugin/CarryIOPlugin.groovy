package com.carryio.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CarryIOPlugin implements Plugin<Project> {

    void apply(Project project) {
//        project.task('testTask') << {
//            println "Hello gradle plugin"
//        }
        project.extensions.create('carry', CarryExtension)
//        project.carry.extensions.create('jarPath',JarExtension)
        System.out.println(carry.transforPackage+"111111111111")
//        System.out.println(jarPath.supportV4JarPath+"22222222222")
//        System.out.println(jarPath.androidJarPath+"333333333333")
//        project.afterEvaluate {
//            def hc = project.extensions.findByName('hc') as HCExtension
//            System.out.println(hc.myName+"----------------------------4--------------------------------")
//
//        }
        def android = project.extensions.findByType(AppExtension)
        project.getDependencies().println();
        android.registerTransform(new MyTransform(project))

//        project.task('readExtension') << {
//            def address=project['address']
//
//            println project['hc'].myName
//            println address.province+" "+address.city
//

    }
}