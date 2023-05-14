package org.hupidou.action;

import javassist.*;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Map;

/*
在Java Agent 中，我们可以通过 Java 字节码处理工具，如 Javassist 来修改类的字节码。而对于需要对字节码进行修改的 Java 类，我们可以通过 YAML 格式的配置文件来指定需要修改的类。这时候就需要用到 snakeyaml 这个库来解析读取 YAML 文件。
在 Java Agent 中，我们可以定义一个 AgentArgs 类，其中包含需要修改的类的类名、修改该类的方法名以及需要注入的代码等信息，然后将这些信息以 YAML 格式写入配置文件中，Java Agent 运行时读取该 YAML 文件，然后将其中的配置信息读取到 AgentArgs 对象中，然后在 Java Agent 运行时使用 Javassist 工具，对目标类的字节码进行修改。
因此，snakeyaml 是 Java 中常用的一个用于解析和生成 YAML 数据的库，通过它我们可以在 Java 程序中读写 YAML 数据，而在 Java Agent 中，就是将 snakeyaml 应用于解析读取 YAML 配置文件中的内容，从而完成对目标类字节码的修改。
 */
public class AgentBytecodeTest {

    @Test
    void test() throws NotFoundException, CannotCompileException, IOException {
        // 获取 YAML 配置文件中的内容
        // 以后有用的时候再深挖
        String configContent = "className: org.hupidou.action.MyClass\n" +
                "methodName: run\n" +
                "injectCode: |\n" +
                "  System.out.println(\"Before running method\");\n" +
                "  System.out.println(\"After running method\");";
        // 使用 snakeyaml 库解析 YAML 格式的配置文件
        Yaml yaml = new Yaml();
        Map<String, String> config = yaml.load(configContent);

        // 根据 YAML 配置文件中的配置信息生成 AgentArgs
        AgentArgs agentArgs = new AgentArgs();
        agentArgs.setClassName(config.get("className"));
        agentArgs.setMethodName(config.get("methodName"));
        agentArgs.setInjectCode(config.get("injectCode"));
        // 然后，我们就可以使用 Javassist 工具，加载 com.example.MyClass 类的字节码，并在指定的方法中插入注入代码：
        ClassPool pool = ClassPool.getDefault();
        CtClass targetClass = pool.get(agentArgs.getClassName());

        // 获取指定方法
        CtMethod targetMethod = targetClass.getDeclaredMethod(agentArgs.getMethodName());

        // 在方法前后插入代码
        targetMethod.insertBefore(agentArgs.getInjectCode());
        targetMethod.insertAfter(agentArgs.getInjectCode());

        // 将修改后的字节码保存至文件
        targetClass.writeFile();


    }

    public class AgentArgs {
        private String className;
        private String methodName;
        private String injectCode;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getInjectCode() {
            return injectCode;
        }

        public void setInjectCode(String injectCode) {
            this.injectCode = injectCode;
        }
    }
}
