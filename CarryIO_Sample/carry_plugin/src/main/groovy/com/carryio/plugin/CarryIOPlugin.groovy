package com.carryio.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CarryExtension {
    /**
     * classes in which package will hook and insert carry_sdk code
     */
    def transforPackage = ""
}

/**
 * 需要导入的jar包 路径或者目录
 */
class JarExtension {
    def androidJarPath = "/Users/leixun/software/android-sdk-macosx/platforms/android-24/android.jar"
    def supportV4JarPath = "/Users/leixun/software/android-sdk-macosx/extras/android/support/v7/appcompat/libs/android-support-v4.jar"
    def jarDir = ""
}

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