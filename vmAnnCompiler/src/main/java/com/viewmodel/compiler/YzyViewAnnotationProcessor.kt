package com.viewmodel.compiler

/**
 * Created by yzy
 *
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
    AnnotationTarget.CONSTRUCTOR
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class InjectView(
    val value: KClass<*>,

    )

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(YzyViewAnnotationProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class YzyViewAnnotationProcessor : AbstractProcessor() {

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
        types.add(InjectView::class.java.canonicalName)
        return types
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment,
    ): Boolean {
        val elements = roundEnv.getElementsAnnotatedWith(InjectView::class.java)
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
                getMyValue(variableElement, InjectView::class.java, "value").toString()
                    .split(".").last()


            var variableElements = map[moudleName]
            if (variableElements == null) {
                variableElements = ArrayList()
                map[moudleName] = variableElements
            }
            variableElements.add(variableElement)
        }




        if (map.isNotEmpty()) {
            val iterator: Iterator<String> = map.keys.iterator()

            while (iterator.hasNext()) {

                val key = iterator.next()
                val variableElements: List<ExecutableElement>? =
                    map[key]

                //moule name
                val moudleName = key + "Provider"
                //To get the package name
                val typeElement = variableElements!![0]
                val packageName: String =
                    elementUtils.getPackageOf(typeElement).getQualifiedName().toString()


                var writer: FileWriter? = null

                try {
                    writer = FileWriter(
                        File(
                            "$mOutputDirectory",
                            packageName.replace(".", "/") + "/$moudleName.kt"
                        ).apply { parentFile.mkdirs() }, true
                    )

                    writer.write("package $packageName\n")


                    writer.write("import com.yzy.lib_protocal.base.IYzyViewProvider\n")


                    writer.write("open public class $moudleName: IYzyViewProvider{\n")

                    writer.write(" override fun loadInto(providers: MutableMap<String, String>) {\n")


                    writer.write("providers.put(")
                    writer.write("""
                    "$key","$packageName.$key")
                    """)


                    writer.write("}\n")

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
            .replace("int", "Int")
            .replace("double", "Double")
            .replace("float", "Float")
            .replace("boolean", "Boolean")
            .replace("long", "Long")
            .replace("String[]", "Array<String>")
            .replace("int[]", "Array<Int>")
            .replace("java.lang.Object", "Any")


    private fun getEventTypeAnnotationMirror(
        typeElement: ExecutableElement,
        clazz: Class<*>,
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
        key: String,
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