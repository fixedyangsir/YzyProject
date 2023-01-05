package com.viewmodel.compiler

/**
 * Created by yzy
 * viewModel 注解处理
 *
 */

import java.io.File
import java.io.FileWriter
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import kotlin.collections.HashMap
import kotlin.reflect.KClass


@Target(
    AnnotationTarget.FUNCTION
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class InjectViewModel(
    val value: KClass<*>,
    val isNeedRefresh: Boolean = false,
    val isOnlyIO: Boolean = false,
    val returnCanNull: Boolean = false,
)

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(TestAnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class TestAnnotationProcessor : AbstractProcessor() {

    lateinit var mOutputDirectory: String

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private lateinit var elementUtils: Elements


    override fun init(p0: ProcessingEnvironment) {
        super.init(p0)
        elementUtils = p0.getElementUtils()
        p0.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.run {
            mOutputDirectory = this.replace("kaptKotlin", "kapt")
        }
    }

    /**
     * 支持的注解类型
     */
    override fun getSupportedAnnotationTypes(): Set<String>? {
        val types: MutableSet<String> =
            LinkedHashSet()
        types.add(InjectViewModel::class.java.canonicalName)
        return types
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(InjectViewModel::class.java)
        if (elements.isEmpty()) return false


        //class：TypeElement
        //fun：ExecutableElement
        //attribute：VariableElement
        val map: MutableMap<String, MutableList<ExecutableElement>> =
            HashMap()

        for (element in elements) {
            val variableElement = element as ExecutableElement

            //get modelName
            val moudleName: String =
                getMyValue(variableElement, InjectViewModel::class.java, "value").toString()
                    .split(".").last()


            var variableElements = map[moudleName]
            if (variableElements == null) {
                variableElements = ArrayList()
                map[moudleName] = variableElements
            }
            variableElements.add(variableElement)
        }




        if (map.size > 0) {
            val iterator: Iterator<String> = map.keys.iterator()

            while (iterator.hasNext()) {

                val key = iterator.next()
                val variableElements: List<ExecutableElement>? =
                    map[key]

                //moule name
                val moudleName = key + "Help"
                //To get the package name
                val typeElement = variableElements!![0]
                val packageName: String =
                    elementUtils.getPackageOf(typeElement).getQualifiedName().toString()


                //Attribute data set
                val filedMap = HashMap<String, String>()

                //Determine if there is a refresh
                var isNeedRefresh = false

                var ioPackage = mutableMapOf<String, String>()

                variableElements.forEach {
                    val refresh = it.getAnnotation(InjectViewModel::class.java).isNeedRefresh
                    if (refresh) {
                        isNeedRefresh = true
                    }
                    //Determine if it is just a time consuming operation
                    val isOnlyIO =
                        it.getAnnotation(InjectViewModel::class.java).isOnlyIO


                    val httpName = it.getEnclosingElement().getSimpleName().toString()
                    var filedType: String = ""

                    if (isOnlyIO) {
                        filedType = filterFiledType(it.returnType.toString())
                        val ioImportKey = elementUtils.getPackageOf(it).getQualifiedName()
                            .toString() + "." + httpName
                        if (!ioPackage.containsKey(ioImportKey)) {
                            ioPackage.put(
                                ioImportKey,
                                "import " + elementUtils.getPackageOf(it).getQualifiedName()
                                    .toString() + "." + httpName + "\n"
                            )
                        }


                    } else {

                        if (it.getParameters().size > 0) {
                            val parameter: VariableElement = it.getParameters()
                                .get(it.getParameters().size - 1)
                            filedType = getFiledType(parameter.asType().toString())
                        }
                    }



                    if (!filedMap.containsKey(httpName + "_" + it.simpleName)) {
                        val canNull = it.getAnnotation(InjectViewModel::class.java).returnCanNull
                        val can = if (canNull) "?" else ""
                        val key =
                            if (it.simpleName.endsWith("_")) it.simpleName else (it.simpleName.toString() + "_")

                        filedMap.put(
                            httpName + "_" + key,
                            "val ${key}LiveData: EventLiveData<ResultState<${
                                filedType
                            }$can>> = EventLiveData()\n"
                        )
                    }
                }


                var writer: FileWriter? = null

                try {
                    writer = FileWriter(
                        File(
                            "$mOutputDirectory",
                            packageName.replace(".", "/") + "/$moudleName.kt"
                        ).apply { parentFile.mkdirs() }, true
                    )

                    writer.write("package $packageName\n")
                    if (isNeedRefresh) {
                        writer.write("import com.yzy.lib_common.base.viewmodel.BaseRefreshViewModel\n")
                    } else {
                        writer.write("import com.yzy.lib_common.base.viewmodel.BaseViewModel\n")
                    }


                    writer.write("import com.yzy.lib_common.ext.request\n")
                    writer.write("import com.yzy.lib_common.ext.launch\n")
                    writer.write("import com.yzy.lib_common.network.state.ResultState\n")
                    writer.write("import github.leavesczy.eventlivedata.EventLiveData\n")

                    ioPackage.forEach {
                        writer.write(it.value)
                    }


                    if (isNeedRefresh) {
                        writer.write("public abstract class $moudleName: BaseRefreshViewModel(){\n")
                    } else {
                        writer.write("open public class $moudleName: BaseViewModel(){\n")
                    }


                    //Generated Properties
                    filedMap.forEach {
                        writer.write(it.value)
                    }


                    //generation method
                    variableElements.forEach { variableElement ->
                        //Determine if it is just a time consuming operation
                        val isOnlyIO =
                            variableElement.getAnnotation(InjectViewModel::class.java).isOnlyIO


                        writer.write("open fun ")
                        writer.write("${variableElement.simpleName}(")


                        val parameterSize =
                            if (isOnlyIO) variableElement.getParameters().size else variableElement.getParameters().size - 1
                        if (variableElement.getParameters().size > 0) {
                            for (i in 0 until parameterSize) {
                                val parameter: VariableElement =
                                    variableElement.getParameters().get(i)

                                writer.write(
                                    "$parameter:" + filterFiledType(
                                        parameter.asType().toString()
                                    )
                                )
                                //Determine whether it is nullable
                                if (parameter.toString().startsWith("_")) {
                                    writer.write("?")
                                }

                                if (i != parameterSize - 1
                                ) {
                                    writer.write(",")
                                }
                            }
                            if (parameterSize>0)
                                writer.write(",")
                        }

                        //Whether to display a popover
                        writer.write("isShowDialog: Boolean?=false")
                        //hint
                        writer.write(",loadingMessage: String?=null")


                        writer.write("){\n")


                        //Generate request method
                        val httpName =
                            variableElement.getEnclosingElement().getSimpleName().toString()

                        if (isOnlyIO) {
                            writer.write("launch({ ${httpName}.${variableElement.simpleName}(")
                        } else {
                            writer.write("request({ ${getServiceName(httpName)}.${variableElement.simpleName}(")
                        }




                        if (variableElement.getParameters().size > 0) {
                            for (i in 0 until parameterSize) {
                                val parameter: VariableElement =
                                    variableElement.getParameters().get(i)
                                writer.write(parameter.toString())
                                if (i != parameterSize - 1
                                ) {
                                    writer.write(",")
                                }
                            }
                        }
                        val simpleName =
                            if (variableElement.simpleName.endsWith("_")) variableElement.simpleName else (variableElement.simpleName.toString() + "_")

                        writer.write(") }, ${simpleName}LiveData,isShowDialog,loadingMessage)")

                        writer.write(" }\n")


                    }
                    writer.write("}")

                } catch (e: Exception) {
                } finally {
                    writer?.close()
                }


            }
        }



        return true
    }


/*
    fun getParmeterType(parameter: String) =

        if (parameter.contains("Integer") || parameter.toLowerCase().contains("int")) {
            "Int"
        } else if (parameter.contains("String")) {
            "String"
        } else if (parameter.contains("Float") || parameter.toLowerCase().contains("float")) {
            "Float"
        } else if (parameter.contains("Double") || parameter.toLowerCase().contains("double")) {
            "Double"
        } else if (parameter.contains("Long") || parameter.toLowerCase().contains("long")) {
            "Long"
        } else {
            parameter
        }
*/

    fun getServiceName(service: String): String {
        return service.replace("Service", "_Service").toUpperCase()
    }


    fun getFiledType(type: String): String {
        if (type.contains("ApiResponse<")) {
            val index = type.indexOf("ApiResponse<")
            return filterFiledType(type.substring(index + "ApiResponse<".length, type.length - 2))
        }
        return type
    }

    fun filterFiledType(parameter: String): String =
        parameter.replace("java.lang.Integer", "Int").replace("java.lang.String", "String")
            .replace("java.lang.Double", "Double").replace("java.lang.Float", "Float")
            .replace("java.lang.Long", "Long")
            .replace("java.lang.Boolean", "Boolean")
            .replace("java.util.ArrayList", "ArrayList").replace("java.util.List", "MutableList")
            .replace("java.util.Map", "Map")
            .replace("int","Int")
            .replace("double","Double")
            .replace("float","Float")
            .replace("boolean","Boolean")
            .replace("long","Long")
            .replace("String[]","Array<String>")
            .replace("int[]","Array<Int>")
            .replace("java.lang.Object","Any")


    private fun getEventTypeAnnotationMirror(
        typeElement: ExecutableElement,
        clazz: Class<*>
    ): AnnotationMirror? {
        val clazzName = clazz.name
        for (m in typeElement.annotationMirrors) {
            if (m.annotationType.toString() == clazzName) {
                return m
            }
        }
        return null
    }

    private fun getAnnotationValue(
        annotationMirror: AnnotationMirror,
        key: String
    ): AnnotationValue? {
        for ((key1, value) in annotationMirror.elementValues) {
            if (key1!!.simpleName.toString() == key) {
                return value
            }
        }
        return null
    }

    private fun getMyValue(foo: ExecutableElement, clazz: Class<*>, key: String): TypeMirror? {
        val am = getEventTypeAnnotationMirror(foo, clazz) ?: return null
        val av = getAnnotationValue(am, key)
        return if (av == null) {
            null
        } else {
            av.value as TypeMirror
        }

    }

}