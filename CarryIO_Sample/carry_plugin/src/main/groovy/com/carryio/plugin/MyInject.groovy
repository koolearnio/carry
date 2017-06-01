package com.carryio.plugin

import javassist.*
import javassist.bytecode.CodeAttribute
import javassist.bytecode.LocalVariableAttribute
import javassist.bytecode.MethodInfo
import org.gradle.tooling.model.internal.Exceptions

class MyInject {

    private static ClassPool pool = ClassPool.getDefault()
    private static String injectStr = "Log.d(\"leixun\",\"leixun I Love you\" );"
    private static String hh = "System.out.println(\"leixunlei\");"
    public static void injectDir(String path, String packageName) {
        pool.appendClassPath(path)
        pool.insertClassPath("/Users/leixun/software/android-sdk-macosx/platforms/android-24/android.jar")
        pool.insertClassPath("/Users/leixun/software/android-sdk-macosx/extras/android/support/v7/appcompat/libs/android-support-v4.jar")
        pool.insertClassPath("/Users/leixun/myandroid/KoolearnIO/KoolearnIO_SDK_Sample/koolearnio_sdk/mylibs/libs/koolearnio_sdk.jar")
        pool.insertClassPath("/Users/leixun/myandroid/KoolearnIO/KoolearnIO_SDK_Sample/koolearnio_sdk/mylibs/libs/fastjson-1.2.9.jar")
        File dir = new File(path)
        System.out.println("path:"+path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath
                System.out.println(filePath)
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    // 判断当前目录是否是在我们的应用包里面
                    int index = filePath.indexOf(packageName);
                    if (index!=-1) {
                        System.out.println("isMyPackage:----")
                        int end = filePath.length() - 6 // .class = 6
                        String className = filePath.substring(index, end).replace('\\', '.').replace('/', '.')
                        System.out.println("className:----"+className)
                        //开始修改class文件
                        CtClass c = pool.getCtClass(className)

                        if (c.isFrozen()) {
                            c.defrost()
                        }

//                        CtConstructor[] cts = c.getDeclaredConstructors()
                        CtMethod[] methods = c.getMethods();
                        pool.importPackage("android.util.Log")
                        pool.importPackage("com.koolearnio.sdk.view.Monitor")
                        pool.importPackage("com.koolearnio.sdk.view.KDView")
                        if(methods !=null && methods.length > 0){
                            int size = methods.size();
                            for(int i = 0 ; i < size ; i++ ){
                                if("onClick".equals(methods[i].getName())){
                                    String[] paramNames = getMethodParamNames(methods[i])
                                    CtClass[] paramClass = methods[i].getParameterTypes();
                                    if(paramClass==null || paramClass.length != 1){
                                        return
                                    }
                                    System.out.println("parames:"+paramClass[0].getName())
                                    if("android.view.View".equals(paramClass[0].getName()) && paramNames !=null && paramNames.length == 1){

                                        System.out.println("methodsName:"+methods[i].getLongName() +"----"+ paramNames[0])
                                        methods[i].insertAt(0,"Monitor.onViewClick("+paramNames[0]+");")
                                    }
                                }
                            }
                        }
                        c.writeFile(path)
                        c.detach()
                    }
                }
            }
        }
    }

    protected static String[] getMethodParamNames(CtMethod cm) {
        CtClass cc = cm.getDeclaringClass();
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);
        codeAttribute.getAttribute()
        if (attr == null) {
            return null;
        }

        String[] paramNames = null;

        try {
            paramNames = new String[cm.getParameterTypes().length];
        } catch (NotFoundException e) {
            Exceptions.uncheck(e);
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
            if("this".equals(paramNames[0])){
                for (int j = 0 ; j< 10;j++){
                    System.out.println("---222+"+paramNames.length+"----"+attr.variableName(j));
                }
            }
        }
        return paramNames;
    }

}